package com.example.nfcapp.core.presentation

import android.nfc.Tag
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nfcapp.core.domain.repository.usecase.ReadClassicCardUseCase
import com.example.nfcapp.core.domain.repository.usecase.SendArrivalUseCase
import com.example.nfcapp.core.domain.model.CardData
import com.example.nfcapp.core.presentation.state.BackendStatus
import com.example.nfcapp.core.presentation.state.NfcUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.update
import javax.inject.Inject

private const val TAG = "NfcViewModel"

@HiltViewModel
class NfcViewModel @Inject constructor(
    private val readClassicCard: ReadClassicCardUseCase,
    private val sendArrival: SendArrivalUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<NfcUiState>(NfcUiState.Idle)
    val state = _state.asStateFlow()

    fun onTagScanned(tag: Tag) {
        viewModelScope.launch {
            _state.update { NfcUiState.Loading }
            try {
                val cardData = readClassicCard(tag)
                _state.update { NfcUiState.Success(cardData, BackendStatus.Sending) }

                sendDataToBackend(cardData)
            } catch (e: Exception) {
                _state.update { 
                    NfcUiState.Error(
                        message = e.message ?: "Failed to read NFC card"
                    )
                }
            }
        }
    }

    fun onExternalRead(data: String) {
        viewModelScope.launch {
            _state.update { NfcUiState.Loading }
            try {

                val cardData = CardData(
                    uid = "EXT_$data",
                    fullName = "External Reader Input",
                    id = data,
                    section = data,
                    department = "External",
                    office = "N/A",
                    title = "N/A",
                    joinDate = "N/A"
                )
                _state.update { NfcUiState.Success(cardData, BackendStatus.Sending) }

                sendDataToBackend(cardData)
            } catch (e: Exception) {
                _state.update { 
                    NfcUiState.Error(
                        message = e.message ?: "Failed to process external read"
                    )
                }
            }
        }
    }

    private fun sendDataToBackend(cardData: CardData) {
        viewModelScope.launch {
            try {
                val result = sendArrival(
                    name = cardData.fullName,
                    phoneNumber = cardData.section
                )
                
                result.onSuccess { message ->
                    _state.update { 
                        if (it is NfcUiState.Success) {
                            it.copy(backendStatus = BackendStatus.Success(message))
                        } else {
                            it
                        }
                    }
                }.onFailure { error ->
                   Log.e("NfcViewModel", "Backend error: ${error.message}", error)
                    _state.update { 
                        if (it is NfcUiState.Success) {
                            it.copy(backendStatus = BackendStatus.Failed(
                                error.message ?: "Failed to send data to backend"
                            ))
                        } else {
                            it
                        }
                    }
                }
            } catch (e: Exception) {
               Log.e("NfcViewModel", "Unexpected error in sendDataToBackend", e)
                _state.update { 
                    if (it is NfcUiState.Success) {
                        it.copy(backendStatus = BackendStatus.Failed(
                            "Network error: ${e.message}"
                        ))
                    } else {
                        it
                    }
                }
            }
        }
    }

    fun resetState() {
        _state.update { NfcUiState.Idle }
    }
}