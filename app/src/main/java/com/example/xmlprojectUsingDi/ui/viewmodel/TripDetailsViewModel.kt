package com.example.xmlprojectUsingDi.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.xmlprojectUsingDi.data.model.response.TripDetailsResponse
import com.example.xmlprojectUsingDi.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TripDetailsViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    val tripDetail = MutableLiveData<TripDetailsResponse>()
    val error = MutableLiveData<String>()

    fun getTripDetails(token: String, tripId: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getTripDetails(token, tripId)
                if (response.isSuccessful) {
                    tripDetail.postValue(response.body())
                } else {
                    error.postValue("Failed to load trip details")
                }
            } catch (e: Exception) {
                error.postValue("Error: ${e.message}")
            }
        }
    }
}
