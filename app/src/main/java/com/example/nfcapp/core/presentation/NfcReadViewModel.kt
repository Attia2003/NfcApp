package com.example.nfcapp.core.presentation

import android.nfc.Tag
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nfcapp.core.domain.usecase.ReadClassicCardUseCase
import com.example.nfcapp.core.domain.usecase.SendArrivalUseCase
import com.example.nfcapp.core.domain.model.CardData
import com.example.nfcapp.core.presentation.state.BackendStatus
import com.example.nfcapp.core.presentation.state.NfcUiState
import com.example.nfcapp.core.presentation.util.toUiError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class NfcViewModel @Inject constructor(
    private val readClassicCard: ReadClassicCardUseCase,
    private val sendArrival: SendArrivalUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<NfcUiState>(NfcUiState.Idle)
    val state = _state.asStateFlow()

    fun onTagScanned(tag: Tag) {
        viewModelScope.launch {
            _state.value = NfcUiState.Loading
            try {
                val cardData = readClassicCard.call(tag)
                _state.value = NfcUiState.Success(cardData, BackendStatus.Sending)
                sendDataToBackend(cardData)
            } catch (e: Exception) {
                _state.value = NfcUiState.Error(e.toUiError())
            }
        }
    }

    fun onExternalRead(data: String) {
        viewModelScope.launch {
            _state.value = NfcUiState.Loading
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
                _state.value = NfcUiState.Success(cardData, BackendStatus.Sending)
                sendDataToBackend(cardData)
            } catch (e: Exception) {
                _state.value = NfcUiState.Error(e.toUiError())
            }
        }
    }

    private fun sendDataToBackend(cardData: CardData) {
        viewModelScope.launch {
            try {
                val message = sendArrival.call(
                    name = cardData.fullName,
                    phoneNumber = cardData.section
                )
                _state.update {
                    if (it is NfcUiState.Success) {
                        it.copy(backendStatus = BackendStatus.Success(message))
                    } else {
                        it
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    if (it is NfcUiState.Success) {
                        it.copy(backendStatus = BackendStatus.Failed(e.toUiError()))
                    } else {
                        it
                    }
                }
            }
        }
    }

    fun resetState() {
        _state.value = NfcUiState.Idle
    }
}