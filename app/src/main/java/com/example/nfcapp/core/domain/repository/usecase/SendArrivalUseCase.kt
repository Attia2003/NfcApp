package com.example.nfcapp.core.domain.repository.usecase

import com.example.nfcapp.core.domain.repository.ArrivalRepository

class SendArrivalUseCase(
    private val repository: ArrivalRepository
) {
    suspend operator fun invoke(name: String, phoneNumber: String): Result<String> {
        return repository.sendArrival(name, phoneNumber)
    }
}