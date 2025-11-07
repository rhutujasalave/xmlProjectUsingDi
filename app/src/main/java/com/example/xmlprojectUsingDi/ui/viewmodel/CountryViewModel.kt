//package com.example.xmlprojectUsingDi.ui.viewmodel
//
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.xmlprojectUsingDi.data.model.response.DialCode
//import com.example.xmlprojectUsingDi.repository.AuthRepository
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//@HiltViewModel
//class CountryViewModel @Inject constructor(
//    private val repository: AuthRepository
//) : ViewModel() {
//
//    val countryListLiveData = MutableLiveData<List<DialCode>>()
//    val errorLiveData = MutableLiveData<String>()
//
//    fun fetchCountryList() {
//        viewModelScope.launch {
//            try {
//                val response = repository.getDialCodes()
//                if (response.isSuccessful && response.body() != null) {
//                    val apiList = response.body()?.data ?: emptyList()
//                    countryListLiveData.postValue(apiList)
//                } else {
//                    errorLiveData.postValue("Failed to fetch countries: ${response.code()}")
//                }
//            } catch (e: Exception) {
//                errorLiveData.postValue(e.message ?: "Something went wrong")
//            }
//        }
//    }
//}




package com.example.xmlprojectUsingDi.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.xmlprojectUsingDi.data.model.response.DialCode
import com.example.xmlprojectUsingDi.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountryViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _countryList = MutableLiveData<List<DialCode>>()
    val countryList: LiveData<List<DialCode>> get() = _countryList

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    fun fetchCountryList() {
        _loading.value = true

        viewModelScope.launch {
            try {
                val response = repository.getDialCodes()

                if (response.isSuccessful && response.body() != null) {
                    val list = response.body()?.data ?: emptyList()
                    _countryList.value = list
                } else {
                    _error.value = "Failed : ${response.code()}"
                }

            } catch (e: Exception) {
                _error.value = e.localizedMessage ?: "Something went wrong"
            } finally {
                _loading.value = false
            }
        }
    }
}
