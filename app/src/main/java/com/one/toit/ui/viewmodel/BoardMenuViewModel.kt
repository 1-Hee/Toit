package com.one.toit.ui.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel

class BoardMenuViewModel : ViewModel() {
    val pageName = mutableStateOf<String>("")
    fun init(){
        pageName.value = ""
    }

    fun getPageName():String{
        return this.pageName.value

    }

    fun setPageName(name:String){
        this.pageName.value = name
    }
}