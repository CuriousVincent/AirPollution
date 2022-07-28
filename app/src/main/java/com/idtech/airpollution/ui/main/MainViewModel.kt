package com.idtech.airpollution.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idtech.airpollution.model.AirPollutionRepository
import com.idtech.airpollution.model.data.Record
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class MainViewModel(private val repo:AirPollutionRepository) : ViewModel() {
    val setHeaderData = MutableLiveData<ArrayList<Record>>()
    val setCenterData = MutableLiveData<ArrayList<Record>>()
    var allData = listOf<Record>()

    fun getApi(){
        viewModelScope.launch {
            repo.getAirPollution()
                .flowOn(Dispatchers.IO)
//                .catch {
//                    isLoading.postValue(false) }
                .collect {
                    allData = it.records
                    setHeaderData.postValue(repo.getPMLessTen(allData))
                    setCenterData.postValue(repo.getPMGreaterTen(allData))
                }
        }
    }

}