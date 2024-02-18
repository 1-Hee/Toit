package com.one.toit.util

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatEditText
import com.one.toit.data.dto.MediaDTO
import timber.log.Timber
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Suppress("UNREACHABLE_CODE")
class AppUtil {
    // 토스트 메세지
    companion object {
        // 토소트 메세지
        fun toast(context: Context, msg:String){
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }
    object UIManager {
        // 키보드 숨김 메서드
        fun hideKeyPad(activity: Activity) {
            val view = activity.currentFocus ?: return
            val isEditText = view is EditText || view is AppCompatEditText
            if (!isEditText) return
            if (view.hasFocus()) {
                view.clearFocus()
            }
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    object Image {
        @Suppress("DEPRECATION")
        fun getBitmap(uri:Uri, contentResolver: ContentResolver):Bitmap?{
            val bitmap = if(Build.VERSION.SDK_INT < Build.VERSION_CODES.P){
                MediaStore.Images.Media.getBitmap(contentResolver, uri)
            }else {
                val source = ImageDecoder.createSource(contentResolver, uri)
                ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
                    decoder.setTargetSampleSize(1) // shrinking by
                    decoder.isMutableRequired = true // this resolve the hardware type of bitmap problem
                }
            }
            return bitmap
        }

        /**
         * 이미지 로드
         */
        fun loadImage(
            resolver: ContentResolver,
            selection: String? = null,
            selectionArgs: Array<String>? = null,
            sortOrder: String? = null
        ):MutableList<MediaDTO>{
            val mediaDTOList = mutableListOf<MediaDTO>()
            val projection = arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.MIME_TYPE,
                MediaStore.Images.Media.DATE_MODIFIED,
                MediaStore.MediaColumns.SIZE
            )

            val cursor: Cursor = resolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, selection, selectionArgs, sortOrder)
                ?: return mutableListOf();
            val idColum:Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val dpNameColumn:Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            val mimeColumn:Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE)
            // option : DATE_MODIFIED, DATE_ADDED, DATE_EXPIRES, DATE_TAKEN
            val dateColumn:Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_MODIFIED)
            val sizeColumn:Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)


            while(cursor.moveToNext()){
                val id:Long = cursor.getLong(idColum)
                val dpName:String = cursor.getString(dpNameColumn)
                val mimeType:String = cursor.getString(mimeColumn)
                val contentUri:Uri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id.toString())
                val date:Long = cursor.getLong(dateColumn)
                val fileSize:Long = cursor.getLong(sizeColumn)
                val mediaDTO = MediaDTO(
                    fileName = dpName,
                    mimeType =  mimeType,
                    savedAddress = contentUri.toString(),
                    lastUpdate = date,
                    fileSize = fileSize
                )
                mediaDTOList.add(mediaDTO)
            }
            return mediaDTOList
        }
    }

    // 시간 파싱 작업에 사용할 유틸리티
    object Time {
        fun getTimeArray(): Array<Int> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val now = LocalDateTime.now()
                val midnight = LocalDateTime.of(now.toLocalDate(), LocalTime.MAX)
                val duration = Duration.between(now, midnight)
                val hours = duration.toHours().toInt()
                val minutes = (duration.toMinutes() % 60).toInt()
                val seconds = (duration.seconds % 60).toInt()
                return arrayOf(hours, minutes, seconds)
            } else {
                val now = Calendar.getInstance()
                val midnight = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, 23)
                    set(Calendar.MINUTE, 59)
                    set(Calendar.SECOND, 59)
                    set(Calendar.MILLISECOND, 999)
                }
                val remainingTime = midnight.timeInMillis - now.timeInMillis
                val hours = (remainingTime / (1000 * 60 * 60)).toInt()
                val minutes = ((remainingTime % (1000 * 60 * 60)) / (1000 * 60)).toInt()
                val seconds = ((remainingTime % (1000 * 60)) / 1000).toInt()
                return arrayOf(hours, minutes, seconds)
            }
        }
        fun parseToLimitString(hour:Int, min:Int):String{
            val hourString = if(hour > 9){
                hour.toString()
            }else {
                "0$hour"
            }
            val minString = if(min > 9){
                min.toString()
            }else {
                "0$min"
            }
            return "$hourString:$minString"
        }

        /**
         * 코틀린 문법을 사용해 getter setter 를 설정해줌
         */
        private var _dateFormat = "yyyy년 MM월 dd일 HH:mm:ss" // date format
        var dateFormat: String
            get() = _dateFormat
            set(value) {
                _dateFormat = value
            }

        private var _timeFormat = "HH:mm:ss" // time format
        var timeFormat
            get() = _timeFormat
            set(value) {
                _timeFormat = value
            }

        // 날짜
        private var _dateString:String = ""
        val dateString:String
            get() {
                _dateString = getCurrentTimeString(isDate = true)
                return _dateString
            }

        // 시간
        private var _timeString:String = ""
        val timeString:String
            get() {
                _timeString = getCurrentTimeString(isDate = false)
                return _timeString
            }

        /**
         * @param isDate : true (일자) : false (시간)
         */
        private fun getCurrentTimeString(isDate: Boolean):String{
            // 오레오 이상일 경우에는 LocalDateTime을 사용하도록
            return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                val date = LocalDateTime.now()
                parseDateString(date, isDate)
            }else {
                val date = Date()
                parseDateString(date, isDate)
            }
        }

        // simple date parser
        fun parseDateString(date: Date, isDate:Boolean):String{
            val format = if(isDate) this.dateFormat else this.timeFormat
            val sdf = SimpleDateFormat(format)
            return sdf.format(date)
        }

        // local date parser | 오레오(O) 이상
        @RequiresApi(Build.VERSION_CODES.O)
        fun parseDateString(date: LocalDateTime, isDate:Boolean):String{
            val format = if(isDate) this.dateFormat else this.timeFormat
            val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(format)
            return date.format(formatter)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        private fun calculateDeadline(baseTime: LocalDateTime, hoursToAdd: Int, minutesToAdd: Int): LocalDateTime {
            return baseTime.plusHours(hoursToAdd.toLong()).plusMinutes(minutesToAdd.toLong())
        }

        private fun calculateDeadline(baseTime: Date, hoursToAdd: Int, minutesToAdd: Int): Date {
            val baseTimeInMillis = baseTime.time
            val deadlineInMillis = baseTimeInMillis + (hoursToAdd * 60L * 60L * 1000L) + (minutesToAdd * 60L * 1000L)
            return Date(deadlineInMillis)
        }

        fun getDeadLineString(hours:Int, minutes:Int, isDate: Boolean):String{
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val currentDateTime = LocalDateTime.now()
                val deadline = calculateDeadline(currentDateTime, hours, minutes)
                parseDateString(deadline, isDate)
            } else {
                val currentDate = Date()
                val deadline = calculateDeadline(currentDate, hours, minutes)
                parseDateString(deadline, isDate)
            }
        }
        @RequiresApi(Build.VERSION_CODES.O)
        fun getDeadLineLocalDate(hours: Int, minutes: Int):LocalDateTime{
            val currentDateTime = LocalDateTime.now()
            return calculateDeadline(currentDateTime, hours, minutes)
        }
        fun getDeadLineDate(hours: Int, minutes: Int):Date{
            val currentDate = Date()
            return calculateDeadline(currentDate, hours, minutes)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun parseToLocalDateTime(dateString: String): LocalDateTime {
            val formatter = DateTimeFormatter.ofPattern(dateFormat)
            return LocalDateTime.parse(dateString, formatter)
        }

        fun parseToDate(dateString: String?): Date? {
            val dateFormat = SimpleDateFormat(dateFormat, Locale.getDefault())
            return try{
                dateFormat.parse(dateString)
            }catch (pe:ParseException){
                Timber.e("[ERROR] : %s", pe.message)
                null
            }
        }
    }
}