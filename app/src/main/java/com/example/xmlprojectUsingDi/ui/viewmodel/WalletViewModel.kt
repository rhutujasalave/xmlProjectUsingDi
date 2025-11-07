package com.example.xmlprojectUsingDi.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.xmlprojectUsingDi.data.model.response.WalletBalanceResponse
import com.example.xmlprojectUsingDi.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WalletViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _walletBalance = MutableLiveData<WalletBalanceResponse>()
    val walletBalance: LiveData<WalletBalanceResponse> get() = _walletBalance

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    fun fetchWalletBalance(token: String) {
        _loading.value = true

        viewModelScope.launch {
            try {
                val response = repository.getWalletBalance(token)

                if (response.isSuccessful && response.body() != null) {
                    _walletBalance.value = response.body()
                } else {
                    _error.value = "Failed to fetch balance: ${response.code()}"
                }

            } catch (e: Exception) {
                _error.value = e.localizedMessage ?: "Unknown error"
            } finally {
                _loading.value = false
            }
        }
    }
}
