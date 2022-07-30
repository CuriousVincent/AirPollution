package com.idtech.airpollution.service

import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.lifecycle.LifecycleService
import com.idtech.airpollution.model.data.Record
import org.koin.android.ext.android.inject


class AirPollutionService : LifecycleService() {
    var binder = AirPollutionBinder(this)
    var allData = listOf<Record>()

   private val presenter : AirPollutionPresenter by inject()

    override fun onCreate() {
        super.onCreate()
        presenter.setAllData.observe(this){
            it?.apply {
                allData = this
            }
        }
    }

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)
        presenter.startTimer()
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        presenter.stopTimer()
        return super.onUnbind(intent)
    }

    class AirPollutionBinder(val service: AirPollutionService) : Binder()
}