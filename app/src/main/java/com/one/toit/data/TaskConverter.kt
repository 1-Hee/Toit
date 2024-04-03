package com.one.toit.data

import android.annotation.SuppressLint
import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TaskConverter {

    @SuppressLint("SimpleDateFormat")
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    @TypeConverter
    fun dateToString(date: Date?): String? {
        return date?.let { dateFormat.format(it) }
    }

    @TypeConverter
    fun stringToDate(value: String?): Date? {
        return value?.let { dateFormat.parse(it) }
    }

}

