//package com.example.xmlproject.viewmodel
//
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.xmlproject.data.model.response.TripItem
//import com.example.xmlproject.repository.AuthRepository
//import kotlinx.coroutines.launch
//
//class TripListViewModel(private val repository: AuthRepository) : ViewModel() {
//
//    val trips = MutableLiveData<List<TripItem>>()
//    val error = MutableLiveData<String>()
//    val isLoading = MutableLiveData<Boolean>()
//
//    fun getTripList(token: String,isUpcoming: Boolean) {
//        if (token.isEmpty()) {
//            error.value = "Token not found. Please login again."
//            return
//        }
//
//        isLoading.value = true
//        viewModelScope.launch {
//            try {
//                val response = repository.getTripList(token,isUpcoming = isUpcoming)
//                if (response.isSuccessful) {
//                    trips.value = response.body()?.data ?: emptyList()
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

    val upcomingTrips = MutableLiveData<List<TripItem>>()
    val pastTrips = MutableLiveData<List<TripItem>>()
    val error = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()

    fun getTripList(token: String, isUpcoming: Boolean) {
        if (token.isEmpty()) {
            error.value = "Token not found. Please login again."
            return
        }

        isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.getTripList(token, isUpcoming = isUpcoming)
                if (response.isSuccessful) {
                    val tripsList = response.body()?.data ?: emptyList()

                    if (isUpcoming) {
                        val sorted = tripsList.sortedByDescending { it.createdAt ?: "" }
                        upcomingTrips.value = sorted
                    } else {
                        pastTrips.value = tripsList
                    }
                } else {
                    error.value = "Error: ${response.code()}"
                }
            } catch (e: Exception) {
                error.value = "Exception: ${e.message}"
            } finally {
                isLoading.value = false
            }
        }
    }
}
