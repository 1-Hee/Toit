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
            // 값이 변경될 때 호출되는 콜백
            // newVal에는 새로운 값이 들어 있음
        }

        mBinding.npMinute.setOnValueChangedListener { picker, oldVal, newVal ->
            val isBurstTime = Time.isBurstTime(mBinding.npHour.value, newVal)
            if(isBurstTime) {
                mBinding.npHour.value = 0;
                mBinding.npMinute.value = 10
                val context = requireContext();
                AppUtil.toast(context, context.getString(R.string.msg_invalid_time))
            }
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