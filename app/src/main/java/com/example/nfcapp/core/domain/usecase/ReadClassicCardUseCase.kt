package com.example.nfcapp.core.domain.usecase

import android.nfc.Tag
import com.example.nfcapp.core.domain.model.CardData
import com.example.nfcapp.core.domain.repository.NfcReadRepository

class ReadClassicCardUseCase(
    private val repository: NfcReadRepository
) {
    suspend fun call(tag: Tag): CardData {
        return repository.readClassicCard(tag)
    }
}