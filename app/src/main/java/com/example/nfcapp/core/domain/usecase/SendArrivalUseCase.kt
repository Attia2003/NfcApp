package com.example.nfcapp.core.domain.usecase

import com.example.nfcapp.core.domain.repository.ArrivalRepository

class SendArrivalUseCase(
    private val repository: ArrivalRepository
) {
    suspend fun call(name: String, phoneNumber: String): String {
        return repository.sendArrival(name, phoneNumber)
    }
}