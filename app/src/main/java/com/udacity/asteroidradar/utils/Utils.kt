package com.udacity.asteroidradar.utils

import java.text.SimpleDateFormat
import java.util.*

object Utils {
    fun getToday(): String {
        return SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault()).format(Calendar.getInstance().time)
    }

    fun getDateStringFormat(date: Date): String {
        return SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault()).format(date)
    }
}