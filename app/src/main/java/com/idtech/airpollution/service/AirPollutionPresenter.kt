package com.idtech.airpollution.service

import androidx.lifecycle.lifecycleScope
import com.idtech.airpollution.model.AirPollutionRepository
import com.idtech.airpollution.model.data.Record
import com.idtech.airpollution.utils.SingleLiveEvent
import com.orhanobut.logger.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.util.*

class AirPollutionPresenter(val repository: AirPollutionRepository) {

    private var timer: Timer = Timer()
    private val interval = 600 * 1000L
    val setAllData = SingleLiveEvent<List<Record>>()

    fun startTimer(){
        timer.schedule(object : TimerTask() {
            override fun run() {
                Logger.d("Start timer")
                GlobalScope.launch {
                    repository.getAirPollution()
                        .catch { error->
                            Logger.d("Service api error: $error")
                        }.flowOn(Dispatchers.IO)
                        .collect{
                            setAllData.postValue(it.records)
                        }
                }

            }

        },interval,interval)
    }

    fun stopTimer(){
        timer.cancel()
    }
}