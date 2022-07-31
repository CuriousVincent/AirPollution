package com.idtech.airpollution.model

import com.idtech.airpollution.R
import com.idtech.airpollution.model.data.AirPollutionData
import com.idtech.airpollution.model.data.Record
import com.idtech.airpollution.utils.ContextUtils
import com.orhanobut.logger.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AirPollutionRepository(private val services: AirPollutionService, private val contextUtils: ContextUtils) {

    fun getAirPollution(): Flow<AirPollutionData> {
        return flow {
            val data = services.getAirPollution()
            emit(data)
        }
    }

    fun getHeaderAndCenterList(data:List<Record>):Pair<ArrayList<Record>,ArrayList<Record>>{
        val greaterTenResult = arrayListOf<Record>()
        val lessTenResult = arrayListOf<Record>()
        data.forEach {
            Logger.d("PM 2.5: ${it.pm2_5} siteName: ${it.siteName}")
            if(it.pm2_5.isNotBlank()){
                if (it.pm2_5.toInt() > 10) {
                    greaterTenResult.add(it)
                }else{
                    lessTenResult.add(it)
                }
            }
        }
        return greaterTenResult to lessTenResult
    }

    fun searchWordInRecord(data: List<Record>, word: String): ArrayList<Record> {
        val result = arrayListOf<Record>()
        data.forEach {
            if (it.haveWord(word) || statusHaveWord(it, word)) {
                result.add(it)
            }
        }
        return result
    }

    private fun statusHaveWord(record: Record, word: String) =
        (record.status == contextUtils.getString(R.string.status_good_name) && contextUtils.getString(R.string.status_instead_name).contains(word)) || record.status.contains(word)

}