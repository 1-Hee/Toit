package com.one.toit.base.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.annotation.NonNull
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import timber.log.Timber

abstract class BaseComposeActivity : ComponentActivity() {

    private var mApplicationProvider:ViewModelProvider? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
    }

    protected fun <T:ViewModel> getApplicationScopeViewModel(@NonNull modelClass:Class<T>):T{
        if(mApplicationProvider == null){
            mApplicationProvider = ViewModelProvider(this)
        }
        return mApplicationProvider!![modelClass]
    }

    protected fun <T:ViewModel> getApplicationScopeViewModel(@NonNull modelClass: Class<T>, factory: ViewModelProvider.NewInstanceFactory):T{
        if(mApplicationProvider == null){
            mApplicationProvider = ViewModelProvider(this, factory)
        }
        return mApplicationProvider!![modelClass]
    }

    protected open fun initViewModel(){
        Timber.i("initViewModel....")
    }
}