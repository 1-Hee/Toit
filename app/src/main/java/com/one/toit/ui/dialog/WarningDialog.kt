package com.one.toit.ui.dialog

import android.view.View
import com.one.toit.BR
import com.one.toit.R
import com.one.toit.base.bind.DataBindingConfig
import com.one.toit.base.listener.ViewClickListener
import com.one.toit.base.ui.BaseDialog
import com.one.toit.data.dto.WarningDTO
import com.one.toit.databinding.DialogWarningBinding

class WarningDialog(
    private val warningDto:WarningDTO,
    private val listener:OnDialogClickListener
) : BaseDialog<DialogWarningBinding>() {
    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.dialog_warning)
            .addBindingParam(BR.warning, warningDto)
            .addBindingParam(BR.click, viewClickListener)
    }

    override fun initViewModel() {

    }

    override fun initView() {
    }

    // 클릭 리스너
    private val viewClickListener = object : ViewClickListener {
        override fun onViewClick(view: View) {
            when(view.id){
                // 취소
                R.id.tv_cancel -> {
                    listener.onCancelClick(view)
                    dismiss()
                }
                // 삭제
                R.id.tv_action -> {
                    listener.onActionClick(view)
                    dismiss()
                }
            }
        }
    }

    interface OnDialogClickListener{
        // 액션(삭제, 거절, 확인 등) 버튼 클릭
        fun onActionClick(view: View)
        // 취소
        // default 메서드로 구현을 강제 X
        fun onCancelClick(view:View){}
    }
}