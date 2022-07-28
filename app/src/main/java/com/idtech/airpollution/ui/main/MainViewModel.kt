package com.idtech.airpollution.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idtech.airpollution.R
import com.idtech.airpollution.model.AirPollutionRepository
import com.idtech.airpollution.model.data.Record
import com.idtech.airpollution.utils.ContextUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class MainViewModel(private val repo:AirPollutionRepository,private val contextUtils: ContextUtils) : ViewModel() {
    val setHeaderData = MutableLiveData<ArrayList<Record>>()
    val setCenterData = MutableLiveData<ArrayList<Record>>()
    val showHint = MutableLiveData(false)
    val showHeaderList = MutableLiveData(true)
    val noDataHint = MutableLiveData(contextUtils.getString(R.string.please_input_search))
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
    fun searchWord(prefix:String){
        if(prefix.isNullOrEmpty()){
            startSearch()
        }else{
            repo.searchWordInRecord(allData,prefix).apply {
                if(isEmpty()){
                    noDataView(prefix)
                }else{
                    showData(this)
                }
            }
        }
    }

    fun searchFlow(isSearch:Boolean){
        if(isSearch){
            startSearch()
        }else{
            closeSearch()
        }
    }
    private fun noDataView(prefix:String){
        showHint.value = true
        noDataHint.value = contextUtils.getString(R.string.no_data).format(prefix)
    }

    private fun startSearch(){
        showHeaderList.value = false
        showHint.value = true
        noDataHint.value = contextUtils.getString(R.string.please_input_search)
    }

    private fun showData(record: ArrayList<Record>){
        showHint.value = false
        setCenterData.postValue(record)
    }
    private fun closeSearch(){
        showHeaderList.value = true
        showHint.value = false
        setHeaderData.postValue(repo.getPMLessTen(allData))
        setCenterData.postValue(repo.getPMGreaterTen(allData))
    }

}