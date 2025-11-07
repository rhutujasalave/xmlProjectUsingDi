package com.example.xmlprojectUsingDi.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.xmlprojectUsingDi.data.model.response.CardListResponse
import com.example.xmlprojectUsingDi.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardListViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _cardList = MutableLiveData<List<CardListResponse>>()
    val cardList: LiveData<List<CardListResponse>> get() = _cardList

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    fun fetchCardList(token: String) {
        if (token.isEmpty()) {
            _error.value = "Token missing"
            return
        }

        _loading.value = true

        viewModelScope.launch {
            try {
                val response = repository.getCardList(token)

                if (response.isSuccessful && response.body() != null) {
                    _cardList.value = response.body()
                } else {
                    _error.value = "Failed: ${response.code()} ${response.message()}"
                }

            } catch (e: Exception) {
                _error.value = "Error: ${e.localizedMessage}"
            } finally {
                _loading.value = false
            }
        }
    }
}
