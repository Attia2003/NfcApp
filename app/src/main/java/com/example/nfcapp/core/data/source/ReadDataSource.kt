package com.example.nfcapp.core.data.source

import android.nfc.Tag
import android.nfc.tech.MifareClassic

class ReadDataSource {
    fun readAscii(
        tag: Tag,
        sector: Int,
        blockOffset: Int
    ): String {
        val mifare = MifareClassic.get(tag) ?: return "Not Classic"

        return try {
            mifare.connect()
            val block = mifare.sectorToBlock(sector) + blockOffset

            val auth = mifare.authenticateSectorWithKeyA(
                sector,
                MifareClassic.KEY_DEFAULT
            )
            if (!auth) return "Auth failed"

            val raw = mifare.readBlock(block)
            raw.map {
                val c = it.toInt() and 0xFF
                if (c in 32..126) c.toChar() else ' '
            }.joinToString("").trim()

        } finally {
            try { mifare.close() } catch (_: Exception) {}
        }
    }
}