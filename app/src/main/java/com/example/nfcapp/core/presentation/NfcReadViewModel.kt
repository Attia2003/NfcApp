package com.example.nfcapp.core.presentation

import android.nfc.Tag
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nfcapp.core.domain.ReadClassicCardUseCase
import com.example.nfcapp.core.domain.model.CardData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.update
import javax.inject.Inject

sealed class NfcUiState {
    data object Idle : NfcUiState()
    data object Loading : NfcUiState()
    data class Success(val cardData: CardData) : NfcUiState()
    data class Error(val message: String) : NfcUiState()
}

@HiltViewModel
class NfcViewModel @Inject constructor(
    private val readClassicCard: ReadClassicCardUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<NfcUiState>(NfcUiState.Idle)
    val state = _state.asStateFlow()

    fun onTagScanned(tag: Tag) {
        viewModelScope.launch {
            _state.update { NfcUiState.Loading }
            try {
                val cardData = readClassicCard(tag)
                _state.update { NfcUiState.Success(cardData) }
            } catch (e: Exception) {
                _state.update { 
                    NfcUiState.Error(
                        message = e.message ?: "Failed to read NFC card"
                    )
                }
            }
        }
    }

    fun resetState() {
        _state.update { NfcUiState.Idle }
    }
}