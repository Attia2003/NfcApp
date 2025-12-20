package com.example.nfcapp.core.presentation

import com.example.nfcapp.core.domain.ReadClassicCardUseCase

//@HiltViewModel
//class NfcViewModel @Inject constructor(
//    private val readClassicCard: ReadClassicCardUseCase
//) : ViewModel() {
//
//    private val _state = MutableStateFlow<CardData?>(null)
//    val state = _state.asStateFlow()
//
//    fun onTagScanned(tag: Tag) {
//        viewModelScope.launch {
//            _state.value = readClassicCard(tag)
//        }
//    }
//}