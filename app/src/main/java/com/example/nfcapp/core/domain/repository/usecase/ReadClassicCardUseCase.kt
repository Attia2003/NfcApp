package com.example.nfcapp.core.domain.repository.usecase

import android.nfc.Tag
import com.example.nfcapp.core.domain.model.CardData
import com.example.nfcapp.core.domain.repository.NfcReadRepository

class ReadClassicCardUseCase(
    private val repository: NfcReadRepository
) {
    suspend operator fun invoke(tag: Tag): CardData {
        return repository.readClassicCard(tag)
    }
}