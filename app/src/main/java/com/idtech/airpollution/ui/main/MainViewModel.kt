package com.idtech.airpollution.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idtech.airpollution.model.AirPollutionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class MainViewModel(private val repo:AirPollutionRepository) : ViewModel() {
    val text = MutableLiveData<String>("")

    fun getApi(){
        viewModelScope.launch {
            repo.getAirPollution()
                .flowOn(Dispatchers.IO)
//                .catch {
//                    isLoading.postValue(false) }
                .collect {
                    text.postValue(it.toString())
                }
        }
    }

}