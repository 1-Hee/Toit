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
import com.one.toit.R
import com.one.toit.data.dto.MediaDTO
import com.one.toit.data.dto.TaskCounter
import timber.log.Timber
import java.lang.StringBuilder
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
                    path = contentUri.toString(),
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
        // 시간이 10분 이상 차이가 나는지 점검하는 메서드
        fun isEnoughTimeDiff(mDate:Date):Boolean{
            val cDate = Date() // 비교할 날짜!
            val cCalendar = Calendar.getInstance() // date1
            cCalendar.time = cDate
            cCalendar.set(Calendar.HOUR_OF_DAY, 23)
            cCalendar.set(Calendar.MINUTE, 59)
            val mCalendar = Calendar.getInstance() // date2
            mCalendar.time = mDate
            mCalendar.set(Calendar.MINUTE, (mCalendar.get(Calendar.MINUTE)+10));
            val comparison = cCalendar.time.compareTo(mCalendar.time)
            return comparison > 0;
        }

        // 시간과 분 정보를 받아 Date 객체 리턴하는 메서드
        fun getLimitDate(hour: Int, min: Int):Date {
            val mDate = Date();
            val calendar = Calendar.getInstance()
            calendar.time = mDate
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, min)
            return calendar.time
        }

        fun getTimeString(context: Context, mDate: Date?):String{
            val txtNoLimit = context.resources.getString(R.string.txt_no_limit)
            if(mDate == null) return txtNoLimit
            val calendar = Calendar.getInstance()
            calendar.time = mDate
            val mHour = calendar.get(Calendar.HOUR_OF_DAY)
            val mMinute = calendar.get(Calendar.MINUTE)
            val strHour = String.format("%02d", mHour)
            val strMin = String.format("%02d", mMinute)
            return "$strHour:$strMin"
        }
        // 저장된 date 를 기반으로 제한시간 문자열 리턴하는 메서드
        fun getLimitString(mDate:Date):String {
            val calendar = Calendar.getInstance()
            calendar.time = mDate
            val mHour = calendar.get(Calendar.HOUR_OF_DAY)
            val mMinute = calendar.get(Calendar.MINUTE)
            val sb = StringBuilder()
            sb.append(String.format("%02d", mHour)) // Hour
                .append(":").append(String.format("%02d", mMinute)) // min
            return sb.toString()
        }
        // Date 값을 기준으로 시간을 추출하는 메서드
        fun getFullLog(context:Context, mDate:Date):String{
            val suffix = context.getString(R.string.suffix_create)
            val calendar = Calendar.getInstance()
            calendar.time = mDate
            val mYear = calendar.get(Calendar.YEAR)
            val mMonth = calendar.get(Calendar.MONTH)+1
            val mDay = calendar.get(Calendar.DAY_OF_MONTH)
            val mHour = calendar.get(Calendar.HOUR_OF_DAY)
            val mMinute = calendar.get(Calendar.MINUTE)
            val mSecond = calendar.get(Calendar.SECOND)
            val sb = StringBuilder()
            sb.append(String.format("%04d", mYear)) // Year
                .append("-")
                .append(String.format("%02d", mMonth)) // Month
                .append("-")
                .append(String.format("%02d", mDay)) // Day
                .append(" ")
                .append(String.format("%02d", mHour)) // hour
                .append(":")
                .append(String.format("%02d", mMinute)) // minute
                .append(":")
                .append(String.format("%02d", mSecond))
                .append(suffix)
            return sb.toString()
        }

        // Date 값을 기준으로 시간을 추출하는 메서드
        fun getFullString(mDate:Date?):String{
            val sb = StringBuilder()
            if(mDate == null){
                return sb.toString()
            }else {
                val calendar = Calendar.getInstance()
                calendar.time = mDate
                val mYear = calendar.get(Calendar.YEAR)
                val mMonth = calendar.get(Calendar.MONTH)+1
                val mDay = calendar.get(Calendar.DAY_OF_MONTH)
                val mHour = calendar.get(Calendar.HOUR_OF_DAY)
                val mMinute = calendar.get(Calendar.MINUTE)
                val mSecond = calendar.get(Calendar.SECOND)
                sb.append(String.format("%04d", mYear)) // Year
                    .append("-")
                    .append(String.format("%02d", mMonth)) // Month
                    .append("-")
                    .append(String.format("%02d", mDay)) // Day
                    .append(" ")
                    .append(String.format("%02d", mHour)) // hour
                    .append(":")
                    .append(String.format("%02d", mMinute)) // minute
                    .append(":")
                    .append(String.format("%02d", mSecond))
                return sb.toString()
            }
        }
        // 시간 로그
        fun getTimeLog(context:Context, mDate:Date):String{
            val suffx = context.resources.getString(R.string.suffix_create);
            val calendar = Calendar.getInstance();
            calendar.time = mDate
            val mHour = calendar.get(Calendar.HOUR_OF_DAY)
            val mMinute = calendar.get(Calendar.MINUTE)
            val mSecond = calendar.get(Calendar.SECOND)
            val sb = StringBuilder()
            sb.append(String.format("%02d", mHour)) // Hour
                .append(":").append(String.format("%02d", mMinute))
                .append(":").append(String.format("%02d", mSecond))
                .append(" ").append(suffx);
            return sb.toString()
        }

        // Date 값을 기준으로 시간 문자열을 추출하는 메서드
        fun getTimeLog(mDate: Date?):String{
            if(mDate == null) return "empty"
            val calendar = Calendar.getInstance()
            calendar.time = mDate
            val mHour = calendar.get(Calendar.HOUR_OF_DAY)
            val mMinute = calendar.get(Calendar.MINUTE)
            val sb = StringBuilder()
            sb.append(String.format("%02d", mHour)) // hour
                .append(":")
                .append(String.format("%02d", mMinute)) // minute
            return sb.toString()
        }

        // 일일 9/1 이런 문자열 얻기 위한 메서드
        fun getSimpleDateLog(mDate: Date?):String{
            if(mDate == null) return ""
            else {
                val calendar = Calendar.getInstance()
                calendar.time = mDate
                val mMonth = calendar.get(Calendar.MONTH) + 1
                val mDay = calendar.get(Calendar.DAY_OF_MONTH)
                return "$mMonth/$mDay"
            }
        }
    }

    // 통계 관런 계산에 사용할 유틸리티
    object Statistics{
        // 주간 목표 달성율 계산 메서드
        fun getWeeklyRatio(mCounterList:List<TaskCounter>):Float{
            if(mCounterList.size < 7) return 0f;
            else {
                var mAllCnt:Float = 0f;
                var mAllCompleteCnt:Float = 0f;
                mCounterList.forEach { item ->
                    mAllCnt += item.totalTask
                    mAllCompleteCnt += item.completeTask
                }
                return if(mAllCnt <=0) 0f else mAllCompleteCnt/mAllCnt
            }
        }
        // 일일 평균 목표 달성율 계산 메서드
        fun getWeeklyDailyAvg(mCounterList:List<TaskCounter>):Float{
            if(mCounterList.size < 7) return 0f;
            else {
                var mTotalRatio = 0f;
                var size = mCounterList.size ;
                for(item in mCounterList){
                    if(item.totalTask <= 0) {
                        size--;
                        continue
                    };
                    val mDailyRatio:Float = ((item.completeTask.toFloat())/(item.totalTask.toFloat()))
                    mTotalRatio += mDailyRatio
                }
                return if(size <= 0) 0f else mTotalRatio / size
            }
        }
    }
}