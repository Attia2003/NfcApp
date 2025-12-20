package com.example.nfcapp.core.domain.repository

import android.nfc.Tag
import com.example.nfcapp.core.domain.model.CardData

interface NfcReadRepository {
    suspend fun readClassicCard(tag: Tag): CardData
}