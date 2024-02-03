package com.one.toit.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.room.Ignore
import com.one.toit.data.dto.MediaDTO
import timber.log.Timber

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
}