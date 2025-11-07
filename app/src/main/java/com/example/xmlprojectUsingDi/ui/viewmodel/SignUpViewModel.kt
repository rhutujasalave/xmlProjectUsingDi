package com.example.xmlprojectUsingDi.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.xmlprojectUsingDi.data.model.request.SignUpRequest
import com.example.xmlprojectUsingDi.data.model.response.SignUpResponse
import com.example.xmlprojectUsingDi.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _signUpResponse = MutableLiveData<Response<SignUpResponse>>()
    val signUpResponse: LiveData<Response<SignUpResponse>> get() = _signUpResponse

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    fun signUp(request: SignUpRequest) {
        _loading.value = true

        viewModelScope.launch {
            try {
                val response = repository.signUpUser(request)
                _signUpResponse.value = response

            } catch (e: Exception) {
                _error.value = e.localizedMessage ?: "Something went wrong"
            } finally {
                _loading.value = false
            }
        }
    }
}
