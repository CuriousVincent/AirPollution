package com.idtech.airpollution.model

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.idtech.airpollution.model.data.AirPollutionData
import com.idtech.airpollution.model.data.Record
import com.orhanobut.logger.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AirPollutionRepository(private val gson: Gson, private val services: AirPollutionService) {

    fun getAirPollution(): Flow<AirPollutionData> {
        return flow {
            val data = services.getAirPollution()
            emit(data)
        }
    }

    fun getPMGreaterTen(data:List<Record>):ArrayList<Record>{
        val result = arrayListOf<Record>()
        data.forEach {
            Logger.d("PM 2.5: ${it.pm2_5} siteName: ${it.siteName}")
            if(it.pm2_5.isNotEmpty() && it.pm2_5.toInt()>10){
                result.add(it)
            }
        }
        return result
    }
    fun getPMLessTen(data:List<Record>):ArrayList<Record>{
        val result = arrayListOf<Record>()
        data.forEach {
            if(it.pm2_5.isNotEmpty() && it.pm2_5.toInt()<=10){
                result.add(it)
            }
        }
        return result
    }

}