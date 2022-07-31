package com.idtech.airpollution.di

import android.content.Context
import android.net.ConnectivityManager
import com.idtech.airpollution.MainSharedViewModel
import com.idtech.airpollution.model.AirPollutionRepository
import com.idtech.airpollution.model.AirPollutionService
import com.idtech.airpollution.service.AirPollutionPresenter
import com.idtech.airpollution.ui.main.MainViewModel
import com.idtech.airpollution.utils.ContextUtils
import com.idtech.airpollution.utils.LoggerInterceptor
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


val airPollutionModule = module{
    single { AirPollutionRepository(get(),get()) }
    single { createOkHttpClient() }
    single { createMockWebService<AirPollutionService>(get()) }
    viewModel { MainViewModel(get(),get(),get()) }
    viewModel {MainSharedViewModel()}
    single {ContextUtils(androidApplication())}
    single {androidApplication().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager }
    factory{ AirPollutionPresenter(get())}
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