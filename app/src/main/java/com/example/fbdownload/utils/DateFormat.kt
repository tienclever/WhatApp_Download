package com.example.fbdownload.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

enum class DateType(val type: String){
    OnlyDate("dd/MM/yyyy"),
    OnlyDate2("dd-MM-yyyy"),
}

object DateFormat {
    @SuppressLint("SimpleDateFormat")
    fun convertDate(date: Date = Date(), type: DateType): String{
        val sdf= SimpleDateFormat(type.type)
        return sdf.format(date)
    }
    @SuppressLint("SimpleDateFormat")
    fun convertDate(timeStamp: Long, type: DateType): String{
        val sdf= SimpleDateFormat(type.type)
        return sdf.format(timeStamp)
    }
    @SuppressLint("SimpleDateFormat")
    fun convertDate(timeStamp: String, type: DateType): String{
        val sdf= SimpleDateFormat(type.type)
        return sdf.format(timeStamp)
    }
    fun getCurrentTime(): Long{
        return System.currentTimeMillis()
    }
    @SuppressLint("SimpleDateFormat")
    fun convertDateToLong(date: String): Long {
        val df = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
        return df.parse(date).time
    }
}
