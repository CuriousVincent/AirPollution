package com.idtech.airpollution.service

import com.idtech.airpollution.model.AirPollutionRepository
import com.idtech.airpollution.model.data.Record
import com.idtech.airpollution.utils.SingleLiveEvent
import com.orhanobut.logger.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.util.*
import kotlin.coroutines.CoroutineContext

class AirPollutionPresenter(val repository: AirPollutionRepository, private val uiContext: CoroutineContext = Dispatchers.Main) : CoroutineScope {

    private var timer: Timer = Timer()
    private val interval = 60 * 1000L
    val setAllData = SingleLiveEvent<List<Record>>()

    override val coroutineContext: CoroutineContext
        get() = uiContext

    fun startTimer() {
        timer.schedule(object : TimerTask() {
            override fun run() {
                Logger.d("Start timer")
                launch {
                    repository.getAirPollution()
                        .catch { error ->
                            Logger.d("Service api error: $error")
                        }.flowOn(Dispatchers.IO)
                        .collect {
                            setAllData.postValue(it.records)
                        }
                }
            }
        }, interval, interval)
    }

    fun stopTimer() {
        timer.cancel()
    }
}