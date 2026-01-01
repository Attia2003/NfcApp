package com.example.nfcapp.core.data.source

import android.nfc.Tag
import android.nfc.tech.MifareClassic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import java.io.IOException

class ReadDataSource {

    suspend fun readAscii(
        tag: Tag,
        sector: Int,
        blockOffset: Int,
        timeoutMillis: Long = 5000L
    ): String = withContext(Dispatchers.IO) {
        val mifare = MifareClassic.get(tag) ?: return@withContext "Not Classic"

        try {
            withTimeout(timeoutMillis) {
                mifare.connect()
                val block = mifare.sectorToBlock(sector) + blockOffset

                val auth = mifare.authenticateSectorWithKeyA(
                    sector,
                    MifareClassic.KEY_DEFAULT
                )
                if (!auth) return@withTimeout "Auth failed"

                val raw = mifare.readBlock(block)
                raw.map {
                    val c = it.toInt() and 0xFF
                    if (c in 32..126) c.toChar() else ' '
                }.joinToString("").trim()
            }
        } catch (e: IOException) {
            "Read error: ${e.message}"
        } catch (e: Exception) {
            "Error: ${e.message}"
        } finally {
            try {
                if (mifare.isConnected) {
                    mifare.close()
                }
            } catch (_: Exception) {

            }
        }
    }
}