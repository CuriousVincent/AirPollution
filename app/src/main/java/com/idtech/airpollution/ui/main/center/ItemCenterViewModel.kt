package com.idtech.airpollution.ui.main.center

import com.idtech.airpollution.R
import com.idtech.airpollution.model.data.Record
import com.idtech.airpollution.utils.ContextUtils

data class ItemCenterViewModel(val data: Record,val contextUtils: ContextUtils) {

    val status = if(data.status == contextUtils.getString(R.string.status_good_name)) contextUtils.getString(R.string.status_instead_name) else data.status
    val showArrow = data.status != contextUtils.getString(R.string.status_good_name)
}