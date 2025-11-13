package com.example.xmlprojectUsingDi.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor() : ViewModel() {

    private val _authToken = MutableLiveData<String?>()
    val authToken: LiveData<String?> get() = _authToken

    fun saveToken(token: String) {
        _authToken.value = token
    }

    fun clearToken() {
        _authToken.value = null
    }
}
