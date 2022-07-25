package com.idtech.airpollution.model

import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AirPollutionRepository(private val gson: Gson, private val services: AirPollutionService) {

    fun getAirPollution(): Flow<JsonObject> {
        return flow {
            val data = services.getAirPollution()
            emit(data)
        }
    }

}