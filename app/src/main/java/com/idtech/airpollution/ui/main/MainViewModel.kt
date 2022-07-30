package com.idtech.airpollution.ui.main

import android.net.ConnectivityManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idtech.airpollution.R
import com.idtech.airpollution.model.AirPollutionRepository
import com.idtech.airpollution.model.data.Record
import com.idtech.airpollution.utils.ContextUtils
import com.idtech.airpollution.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(private val repo: AirPollutionRepository, private val contextUtils: ContextUtils, private val connectivityManager: ConnectivityManager) : ViewModel() {
    val setHeaderData = MutableLiveData<ArrayList<Record>>()
    val setCenterData = MutableLiveData<ArrayList<Record>>()
    val showHint = MutableLiveData(false)
    val showHeaderList = MutableLiveData(true)
    val isLoading = SingleLiveEvent<Boolean>()
    val noDataHint = MutableLiveData(contextUtils.getString(R.string.please_input_search))
    val showNetworkErrorDialog = SingleLiveEvent<Unit>()
    val showErrorDialog = SingleLiveEvent<String>()
    val setAllData = SingleLiveEvent<List<Record>>()


    fun getApi() {
        viewModelScope.launch {
            flow {
                isLoading.value = true
                emit(isNetworkConnected())
            }.filter {
                if (!it) {
                    isLoading.value = false
                    showNetworkErrorDialog.value = Unit
                }
                it
            }.flowOn(Dispatchers.Main)
                .flatMapConcat {
                    return@flatMapConcat repo.getAirPollution()
                }.map {
                    setAllData.postValue(it.records)
                    return@map repo.getHeaderAndCenterList(it.records)
                }
                .flowOn(Dispatchers.IO)
                .catch { error->
                    isLoading.value = false
                    showErrorDialog.value = error.message
                }.collect { (headerData, centerData) ->
                    setHeaderData.value = headerData
                    setCenterData.value = centerData
                    isLoading.value = false
                }
        }
    }

    fun searchWord(prefix: String,allData:List<Record>) {
        if (prefix.isEmpty()) {
            startSearch()
        } else {
            repo.searchWordInRecord(allData, prefix).apply {
                if (isEmpty()) {
                    noDataView(prefix)
                } else {
                    showData(this)
                }
            }
        }
    }

    fun searchFlow(isSearch: Boolean,allData:List<Record>) {
        if (isSearch) {
            startSearch()
        } else {
            closeSearch(allData)
        }
    }

    private fun noDataView(prefix: String) {
        showHint.value = true
        noDataHint.value = contextUtils.getString(R.string.no_data).format(prefix)
        setCenterData.value = arrayListOf()
    }

    private fun startSearch() {
        setCenterData.value = arrayListOf()
        showHeaderList.value = false
        showHint.value = true
        noDataHint.value = contextUtils.getString(R.string.please_input_search)
    }

    private fun showData(record: ArrayList<Record>) {
        setCenterData.value = record
        showHint.value = false
    }

    private fun closeSearch(allData:List<Record>) {
        viewModelScope.launch {
            isLoading.value = true
            flow {
                emit(repo.getHeaderAndCenterList(allData))
            }
                .flowOn(Dispatchers.IO)
                .catch {error->
                    isLoading.value = false
                    showErrorDialog.value = error.message
                }
                .collect { (headerData, centerData) ->
                    setHeaderData.value = headerData
                    setCenterData.value = centerData
                    showHeaderList.value = true
                    showHint.value = false
                    isLoading.value = false
                }
        }
    }

    private fun isNetworkConnected(): Boolean {
        val mNetworkInfo = connectivityManager.activeNetworkInfo
        return mNetworkInfo != null && mNetworkInfo.isAvailable
    }
}