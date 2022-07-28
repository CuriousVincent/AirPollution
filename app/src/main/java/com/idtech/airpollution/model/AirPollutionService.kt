package com.idtech.airpollution.model

import com.google.gson.JsonObject
import com.idtech.airpollution.model.data.AirPollutionData
import retrofit2.http.GET

interface AirPollutionService {

    @GET("aqx_p_432?limit=1000&api_key=9be7b239-557b-4c10-9775-78cadfc555e9&format=json")
    suspend fun getAirPollution(): AirPollutionData
}