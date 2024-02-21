package com.one.toit.data

import android.annotation.SuppressLint
import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.Date

class TaskConverter {
    @SuppressLint("SimpleDateFormat")
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    @TypeConverter
    fun dateToString(date: Date): String {
        return dateFormat.format(date)
    }
    @TypeConverter
    fun stringToDate(value: String): Date {
        return dateFormat.parse(value)!!
    }
}

