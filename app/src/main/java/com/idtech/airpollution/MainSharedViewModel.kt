package com.idtech.airpollution

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainSharedViewModel: ViewModel() {
    val searchWord = MutableLiveData<String>()
    val isSearch = MutableLiveData<Boolean>()
}