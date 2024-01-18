package com.one.toit.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class MainMenuViewModel : ViewModel() {
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