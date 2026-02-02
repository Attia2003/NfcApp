package com.example.nfcapp

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.content.BroadcastReceiver
import android.content.Context
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.nfcapp.core.presentation.NfcViewModel
import com.example.nfcapp.core.presentation.ui.NfcScreen
import com.example.nfcapp.core.presentation.ui.SplashScreen
import com.example.nfcapp.ui.theme.NfcAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var nfcAdapter: NfcAdapter? = null
    private lateinit var pendingIntent: PendingIntent

    private val viewModel: NfcViewModel by viewModels()
    
    private var showSplash by mutableStateOf(true)

    private val externalReaderBuffer = StringBuilder()

    private val usbReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (UsbManager.ACTION_USB_DEVICE_ATTACHED == action) {
                val device: UsbDevice? = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE)
                Log.d("USB", "Device attached: ${device?.deviceName}")
            } else if (UsbManager.ACTION_USB_DEVICE_DETACHED == action) {
                val device: UsbDevice? = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE)
                Log.d("USB", "Device detached: ${device?.deviceName}")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
            PendingIntent.FLAG_MUTABLE
        )

        setContent {
            NfcAppTheme {
                if (showSplash) {
                    SplashScreen(
                        onSplashFinished = { showSplash = false }
                    )
                } else {
                    NfcScreen(viewModel)
                }
            }
        }


        handleNfcIntent(intent)
    }

    override fun onResume() {
        super.onResume()


        if (nfcAdapter?.isEnabled == true) {
            val intentFilters = arrayOf(
                IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED),
                IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED),
                IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)
            )

            val techLists = arrayOf(
                arrayOf("android.nfc.tech.MifareClassic")
            )

            nfcAdapter?.enableForegroundDispatch(
                this,
                pendingIntent,
                intentFilters,
                techLists
            )
        } else {
            Log.w("NFC", "NFC adapter is not available or not enabled")
        }

        val filter = IntentFilter().apply {
            addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED)
            addAction(UsbManager.ACTION_USB_DEVICE_DETACHED)
        }
        registerReceiver(usbReceiver, filter)
    }

    override fun onPause() {
        super.onPause()
        nfcAdapter?.disableForegroundDispatch(this)
        unregisterReceiver(usbReceiver)
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN) {
            val char = event.unicodeChar.toChar()
            if (event.keyCode == KeyEvent.KEYCODE_ENTER) {
                val data = externalReaderBuffer.toString().trim()
                if (data.isNotEmpty()) {
                    Log.d("ExternalReader", "Scanned data: $data")
                    viewModel.onExternalRead(data)
                    externalReaderBuffer.setLength(0)
                }
                return true
            } else if (char.isLetterOrDigit() || char == '-') {
                externalReaderBuffer.append(char)
            }
        }
        return super.dispatchKeyEvent(event)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Log.d("NFC", "onNewIntent fired")

        handleNfcIntent(intent)
    }

    private fun handleNfcIntent(intent: Intent) {
        val action = intent.action
        Log.d("NFC", "Intent action: $action")

        if (
            action == NfcAdapter.ACTION_TAG_DISCOVERED ||
            action == NfcAdapter.ACTION_TECH_DISCOVERED ||
            action == NfcAdapter.ACTION_NDEF_DISCOVERED
        ) {
            val tag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG, Tag::class.java)
            } else {
                @Suppress("DEPRECATION")
                intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
            }

            if (tag == null) {
                Log.e("NFC", "Tag is NULL")
                return
            }

            Log.d(
                "NFC",
                "Tag scanned UID: ${tag.id.joinToString { "%02X".format(it) }}"
            )
            Log.d("NFC", "Tech list: ${tag.techList.joinToString()}")

            viewModel.onTagScanned(tag)
        }
    }
}
