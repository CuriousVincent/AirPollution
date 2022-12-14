package com.idtech.airpollution

import android.app.Application
import com.idtech.airpollution.di.airPollutionModule
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        Logger.addLogAdapter(AndroidLogAdapter())
        startKoin{
            androidLogger()
            androidContext(this@App)
            modules(airPollutionModule)
        }
    }
}