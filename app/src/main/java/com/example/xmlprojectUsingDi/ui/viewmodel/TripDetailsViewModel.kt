//package com.example.xmlprojectUsingDi.viewmodel
//
//
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.xmlprojectUsingDi.data.model.response.TripDetailsResponse
//import com.example.xmlprojectUsingDi.repository.AuthRepository
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//@HiltViewModel
//class TripDetailsViewModel @Inject constructor(
//    private val repository: AuthRepository
//) : ViewModel() {
//
//    val tripDetail = MutableLiveData<TripDetailsResponse>()
//    val error = MutableLiveData<String>()
//
//    fun getTripDetails(token: String, tripId: Int) {
//        viewModelScope.launch {
//            try {
//                val response = repository.getTripDetails(token, tripId)
//                if (response.isSuccessful) {
//                    tripDetail.postValue(response.body())
//                } else {
//                    error.postValue("Failed to load trip details")
//                }
//            } catch (e: Exception) {
//                error.postValue("Error: ${e.message}")
//            }
//        }
//    }
//}


package com.example.xmlprojectUsingDi.viewmodel

import androidx.lifecycle.LiveData
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

    private val _tripDetail = MutableLiveData<TripDetailsResponse>()
    val tripDetail: LiveData<TripDetailsResponse> get() = _tripDetail

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    fun getTripDetails(token: String, tripId: Int) {
        _loading.value = true

        viewModelScope.launch {
            try {
                val response = repository.getTripDetails(token, tripId)

                if (response.isSuccessful && response.body() != null) {
                    _tripDetail.value = response.body()
                } else {
                    _error.value = "Failed to load trip details"
                }

            } catch (e: Exception) {
                _error.value = "Error: ${e.localizedMessage}"
            } finally {
                _loading.value = false
            }
        }
    }
}
