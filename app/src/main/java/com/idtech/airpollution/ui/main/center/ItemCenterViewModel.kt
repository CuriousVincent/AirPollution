package com.idtech.airpollution.ui.main.center

import com.idtech.airpollution.model.data.Record

data class ItemCenterViewModel(val data: Record) {
    val status = if(data.status == "良好") "The status is good, we want to go out to have fun" else data.status
    val showArrow = data.status != "良好"
}