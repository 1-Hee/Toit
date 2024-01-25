package com.one.toit.ui.dialog

import android.view.View
import com.one.toit.BR
import com.one.toit.R
import com.one.toit.base.bind.DataBindingConfig
import com.one.toit.base.listener.ViewClickListener
import com.one.toit.base.ui.BaseDialog
import com.one.toit.databinding.DialogCustomTimeBinding

class CustomTimeDialog(
    private val hour: Int = 0,
    private val min:Int = 1,
    private val listener:OnDialogClickListener
) : BaseDialog<DialogCustomTimeBinding>(){
    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.dialog_custom_time)
            .addBindingParam(BR.click, viewClickListener)
    }
    override fun initViewModel() {}
    override fun initView() {
        // 시간 설정
        mBinding.npHour.minValue = 0
        mBinding.npHour.maxValue = 23
        mBinding.npHour.value = this.hour

        // 분 설정
        mBinding.npMinute.minValue = 0
        mBinding.npMinute.maxValue = 59
        mBinding.npMinute.value = this.min
    }

    private val viewClickListener = object : ViewClickListener {
        override fun onViewClick(view: View) {
            when(view.id) {
                // 시간 선택
                R.id.tv_select_time -> {
                    val hour = mBinding.npHour.value
                    val min = mBinding.npMinute.value
                    listener.onSelectTime(hour, min)
                    dismiss()
                }
                // 취소
                R.id.tv_cancel -> {
                    listener.onCancel()
                    dismiss()
                }
            }
        }
    }

    interface OnDialogClickListener {
        fun onSelectTime(hour:Int, min:Int)
        fun onCancel()
    }


}