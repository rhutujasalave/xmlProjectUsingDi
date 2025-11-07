//package com.example.xmlprojectUsingDi.viewmodel
//
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.xmlprojectUsingDi.data.model.response.TripItem
//import com.example.xmlprojectUsingDi.repository.AuthRepository
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//@HiltViewModel
//class TripListViewModel @Inject constructor(
//    private val repository: AuthRepository
//) : ViewModel() {
//
//    val upcomingTrips = MutableLiveData<List<TripItem>>()
//    val pastTrips = MutableLiveData<List<TripItem>>()
//    val error = MutableLiveData<String>()
//    val isLoading = MutableLiveData<Boolean>()
//
//    fun getTripList(token: String, isUpcoming: Boolean) {
//        if (token.isEmpty()) {
//            error.value = "Token not found. Please login again."
//            return
//        }
//
//        isLoading.value = true
//        viewModelScope.launch {
//            try {
//                val response = repository.getTripList(token, isUpcoming = isUpcoming)
//                if (response.isSuccessful) {
//                    val tripsList = response.body()?.data ?: emptyList()
//
//                    if (isUpcoming) {
//                        val sorted = tripsList.sortedByDescending { it.createdAt ?: "" }
//                        upcomingTrips.value = sorted
//                    } else {
//                        pastTrips.value = tripsList
//                    }
//                } else {
//                    error.value = "Error: ${response.code()}"
//                }
//            } catch (e: Exception) {
//                error.value = "Exception: ${e.message}"
//            } finally {
//                isLoading.value = false
//            }
//        }
//    }
//}



package com.example.xmlprojectUsingDi.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.xmlprojectUsingDi.data.model.response.TripItem
import com.example.xmlprojectUsingDi.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TripListViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _upcomingTrips = MutableLiveData<List<TripItem>>()
    val upcomingTrips: LiveData<List<TripItem>> get() = _upcomingTrips

    private val _pastTrips = MutableLiveData<List<TripItem>>()
    val pastTrips: LiveData<List<TripItem>> get() = _pastTrips

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    fun getTripList(token: String, isUpcoming: Boolean) {
        if (token.isEmpty()) {
            _error.value = "Token not found. Please login again."
            return
        }

        _loading.value = true

        viewModelScope.launch {
            try {
                val response = repository.getTripList(token, isUpcoming = isUpcoming)

                if (response.isSuccessful && response.body() != null) {
                    val tripsList = response.body()?.data ?: emptyList()

                    if (isUpcoming) {
                        _upcomingTrips.value = tripsList.sortedByDescending { it.createdAt ?: "" }
                    } else {
                        _pastTrips.value = tripsList
                    }

                } else {
                    _error.value = "Error: ${response.code()}"
                }

            } catch (e: Exception) {
                _error.value = "Exception: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }
}
