package com.idtech.airpollution.model.data

import com.google.gson.annotations.SerializedName


data class AirPollutionData(
    val records: List<Record>
)

data class Record(
    @SerializedName("County")
    val county: String,
    @SerializedName("PM2.5")
    val pm2_5: String,
    @SerializedName("SiteId")
    val siteId: String,
    @SerializedName("SiteName")
    val siteName: String,
    @SerializedName("Status")
    val status: String
) {
    fun haveWord(word: String): Boolean {
        return county.contains(word) || pm2_5.contains(word) || siteId.contains(word) || siteName.contains(word) || status.contains(word)
    }
}