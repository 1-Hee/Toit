package com.one.toit.util

import android.content.Context
import android.content.SharedPreferences
import com.one.toit.R

class PreferenceUtil private constructor(private val context: Context) {
    private val prefsTag = this.context.getString(R.string.app_name)
    private val prefs:SharedPreferences = this.context.getSharedPreferences(prefsTag, Context.MODE_PRIVATE)
    private var defaultValue:String = ""
    // prefs에서 값을 입출력하는 메서드
    // getter
    fun getValue(key:String):String{
        return prefs.getString(key, this.defaultValue).toString()
    }
    // setter
    fun setValue(key:String, value:String){
        prefs.edit().putString(key, value).apply()
    }
    fun clearAll(){
        prefs.edit().clear().commit();
    }
    companion object {
        private var INSTANCE: PreferenceUtil? = null
        fun getInstance(context: Context): PreferenceUtil {
            if (INSTANCE == null) {
                INSTANCE = PreferenceUtil(context)
            }
            return INSTANCE as PreferenceUtil
        }
    }
}