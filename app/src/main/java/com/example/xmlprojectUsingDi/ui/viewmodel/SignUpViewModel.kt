package com.example.xmlprojectUsingDi.ui.viewmodel

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

    val signUpResponse = MutableLiveData<Response<SignUpResponse>>()
    val errorMessage = MutableLiveData<String>()

    fun signUp(request: SignUpRequest) {
        viewModelScope.launch {
            try {
                val response = repository.signUpUser(request)
                signUpResponse.postValue(response)
            } catch (e: Exception) {
                errorMessage.postValue(e.message ?: "Something went wrong")
            }
        }
    }
}
