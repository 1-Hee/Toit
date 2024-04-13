package com.one.toit.ui.dialog

import android.view.View
import com.one.toit.BR
import com.one.toit.R
import com.one.toit.base.bind.DataBindingConfig
import com.one.toit.base.listener.ViewClickListener
import com.one.toit.base.ui.BaseDialog
import com.one.toit.databinding.DialogCustomTimeBinding
import com.one.toit.util.AppUtil
import com.one.toit.util.AppUtil.Time
import kotlin.math.max
import kotlin.math.min

class CustomTimeDialog(
    private val hour: Int = 0,
    private val min:Int = 0,
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
        val maxHour = min(hour, 23)
        mBinding.npHour.maxValue = maxHour
        mBinding.npHour.value = 0

        // 분 설정
        mBinding.npMinute.minValue = 0
        val maxMin = max(min, 59)
        mBinding.npMinute.maxValue = maxMin
        mBinding.npMinute.value = 10

        // 값 감지 및 유효성 체크용 리스너
        mBinding.npHour.setOnValueChangedListener { picker, oldVal, newVal ->
            val mMinute = mBinding.npMinute.value // 현재 선택된 분
            val isBurstTime = Time.isBurstTime(newVal, mMinute) // 시, 분으로 유효성을 점검함
            resetToLegalTimes(isBurstTime) //  유효성을 검사!
        }

        mBinding.npMinute.setOnValueChangedListener { picker, oldVal, newVal ->
            val mHour = mBinding.npHour.value // 현재 선택된 시간
            val isBurstTime = Time.isBurstTime(mHour, newVal) // 시, 분으로 유효성을 점검함
            resetToLegalTimes(isBurstTime) //  유효성을 검사!
         }
    }

    // 유효성 체크 후 강제로 값을 변경하는 메서드
    private fun resetToLegalTimes(isBurstTime : Boolean){
        if(isBurstTime) {
            mBinding.npHour.value = 0;
            mBinding.npMinute.value = 10
            val context = requireContext();
            AppUtil.toast(context, context.getString(R.string.msg_invalid_time))
        }
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