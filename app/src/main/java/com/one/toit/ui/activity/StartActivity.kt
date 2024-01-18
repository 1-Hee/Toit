package com.one.toit.ui.activity

import android.os.Bundle
import com.one.toit.R
import com.one.toit.base.bind.DataBindingConfig
import com.one.toit.base.ui.BaseActivity
import com.one.toit.databinding.ActivityStartBinding

class StartActivity : BaseActivity<ActivityStartBinding>() {
    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_start)
    }
    override fun init(savedInstanceState: Bundle?) {
    }
}