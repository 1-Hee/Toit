package com.one.toit.ui.activity

import android.os.Bundle
import com.one.toit.R
import com.one.toit.base.bind.DataBindingConfig
import com.one.toit.base.ui.BaseActivity
import com.one.toit.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_main)
    }
    override fun init(savedInstanceState: Bundle?) {
    }
}