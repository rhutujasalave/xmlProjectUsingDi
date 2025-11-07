package com.example.xmlprojectUsingDi.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.xmlprojectUsingDi.data.model.request.LoginRequest
import com.example.xmlprojectUsingDi.repository.AuthRepository
import kotlinx.coroutines.launch
import com.example.xmlprojectUsingDi.data.model.response.LoginResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    sealed class LoginState {
        object Idle : LoginState()
        object Loading : LoginState()
        data class Success(val response: LoginResponse) : LoginState()
        data class Error(val message: String) : LoginState()
    }

    private val _loginState = MutableLiveData<LoginState>(LoginState.Idle)
    val loginState: LiveData<LoginState> get() = _loginState

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading


    fun loginUser(diaCode: String, phone: String, password: String) {
        _loading.value = true
        _loginState.value = LoginState.Loading

        viewModelScope.launch {
            try {
                val request = LoginRequest(diaCode, phone, password)
                val response = repository.loginUser(request)

                if (response.isSuccessful && response.body() != null) {
                    _loginState.value = LoginState.Success(response.body()!!)
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Login failed"
                    _loginState.value = LoginState.Error(errorMsg)
                    _error.value = errorMsg
                }

            } catch (e: Exception) {
                val msg = e.localizedMessage ?: "Unknown error"
                _loginState.value = LoginState.Error(msg)
                _error.value = msg
            } finally {
                _loading.value = false
            }
        }
    }
}
