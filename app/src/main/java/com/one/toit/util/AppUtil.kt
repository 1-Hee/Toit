package com.one.toit.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
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
        fun loadBitmapFromUri(uri: Uri, contentResolver: ContentResolver): Bitmap? {
            return try {
                ImageDecoder.decodeBitmap(
                    ImageDecoder.createSource(contentResolver, uri)
                )
            } catch (e: Exception) {
                // Handle any exceptions that may occur during bitmap loading
                e.printStackTrace()
                null
            }
        }

        fun loadDrawableFromUri(uri: Uri, contentResolver: ContentResolver): Drawable? {
            return try {
                contentResolver.openInputStream(uri)?.use { inputStream ->
                    Drawable.createFromStream(inputStream, uri.toString())
                }
            } catch (e: Exception) {
                // Handle any exceptions that may occur during drawable loading
                e.printStackTrace()
                null
            }
        }

        @Suppress("DEPRECATION")
        fun getBitmap(uri:Uri, contentResolver: ContentResolver):Bitmap{
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
    }
}