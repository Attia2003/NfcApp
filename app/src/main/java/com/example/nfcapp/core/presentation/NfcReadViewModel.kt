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
import javax.inject.Inject

@HiltViewModel
class NfcViewModel @Inject constructor(
    private val readClassicCard: ReadClassicCardUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<CardData?>(null)
    val state = _state.asStateFlow()

    fun onTagScanned(tag: Tag) {
        viewModelScope.launch {
            _state.value = readClassicCard(tag)
        }
    }
}