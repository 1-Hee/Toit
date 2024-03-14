package com.one.toit.data

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TaskConverter {
    companion object {
//        val dateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)
//        val date = dateFormat.parse(dateString)
        private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        @TypeConverter
        @JvmStatic
        fun fromDate(date: Date?): String? {
            return date?.let { dateFormat.format(it) }
        }

        @TypeConverter
        @JvmStatic
        fun toDate(dateString: String?): Date? {
            return dateString?.let { dateFormat.parse(it) }
        }
    }
}

