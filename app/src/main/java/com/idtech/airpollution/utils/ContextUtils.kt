package com.idtech.airpollution.utils

import android.content.Context

class ContextUtils(private val context: Context) {
    fun getString(id:Int) = context.getString(id)
}