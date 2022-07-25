package com.idtech.airpollution.di

import androidx.lifecycle.viewmodel.viewModelFactory
import com.google.gson.Gson
import com.idtech.airpollution.model.AirPollutionRepository
import com.idtech.airpollution.model.AirPollutionService
import com.idtech.airpollution.ui.main.MainViewModel
import com.idtech.airpollution.utils.LoggerInterceptor
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


val airPollutionModule = module{
    single { Gson() }
    single { AirPollutionRepository(get(),get()) }
    single { createOkHttpClient() }
    single { createMockWebService<AirPollutionService>(get()) }
    viewModel { MainViewModel(get()) }
}

fun createOkHttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
        .retryOnConnectionFailure(true)
        .addNetworkInterceptor(LoggerInterceptor())
        .connectTimeout(60L, TimeUnit.SECONDS)
        .readTimeout(60L, TimeUnit.SECONDS)
        .connectionPool(ConnectionPool(0, 1, TimeUnit.NANOSECONDS))
        .build()
}

inline fun <reified T> createMockWebService(okHttpClient: OkHttpClient): T {
    val url = "https://data.epa.gov.tw/api/v1/"
    val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    return retrofit.create(T::class.java)
}