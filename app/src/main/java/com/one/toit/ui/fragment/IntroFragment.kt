package com.one.toit.ui.fragment

import com.one.toit.R
import com.one.toit.base.bind.DataBindingConfig
import com.one.toit.base.ui.BaseFragment
import com.one.toit.databinding.FragmentIntroBinding

class IntroFragment : BaseFragment<FragmentIntroBinding>() {
    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.fragment_intro)
    }

    override fun initViewModel() {
    }

    override fun initView() {
    }
}