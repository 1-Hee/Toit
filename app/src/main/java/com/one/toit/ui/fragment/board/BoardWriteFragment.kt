package com.one.toit.ui.fragment.board

import android.app.Activity
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.one.toit.BR
import com.one.toit.R
import com.one.toit.base.bind.DataBindingConfig
import com.one.toit.base.listener.ViewClickListener
import com.one.toit.base.ui.BaseFragment
import com.one.toit.databinding.FragmentBoardWriteBinding
import com.one.toit.ui.dialog.CustomTimeDialog
import com.one.toit.util.AppUtil
import timber.log.Timber


class BoardWriteFragment : BaseFragment<FragmentBoardWriteBinding>(){

    private lateinit var guideArray:Array<String>
    override fun getDataBindingConfig(): DataBindingConfig {
        // 키보드 엔터 이벤트 핸들링을 위한 리스너
        val onKeyListener: View.OnKeyListener = View.OnKeyListener { view, keyCode, keyEvent ->
            if(keyCode == KeyEvent.KEYCODE_ENTER
                && keyEvent.action == KeyEvent.ACTION_DOWN){
                // hide keypad
                AppUtil.UIManager.hideKeyPad(requireActivity())
                // action...
                Timber.d("enter...")
                true
            }else {
                false
            }
        }
        // 가상 키패드 엔터 이벤트 핸들링을 위한 리스너
        val textListener: TextView.OnEditorActionListener = TextView.OnEditorActionListener {
                textView, keyCode, keyEvent ->
            if(keyCode == 5 || keyCode == 6){
                // hide keypad
                AppUtil.UIManager.hideKeyPad(requireActivity())

                Timber.d("enter...?")
                true
            }else {
                false
            }
        }
        // 검색어의 유무에 따라 검색어 지우기 옵션을 활성화해줄 리스너
        val textWatcher: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(editable: Editable) {}
        }

        return DataBindingConfig(R.layout.fragment_board_write)
            .addBindingParam(BR.limitDescription, "")
            .addBindingParam(BR.isLimit, false)
            .addBindingParam(BR.click, viewClickListener)
            .addBindingParam(BR.onKeyListener, onKeyListener)
            .addBindingParam(BR.textListener, textListener)
            .addBindingParam(BR.textWatcher, textWatcher)
    }
    override fun initViewModel() {
    }
    override fun initView() {
        guideArray = requireContext().resources.getStringArray(R.array.arr_limit_guide) // 메뉴 명
        setGuideDesc(mBinding.isLimit == true)

        // 등록버튼 스타일 세팅
        val btnName = "등록 하기"
        val bgColor = ContextCompat.getColor(requireContext(), R.color.purple200)
        val strokeColor = ContextCompat.getColor(requireContext(), R.color.purple200)
        val textColor = ContextCompat.getColor(requireContext(), R.color.white)
        val rippleColor = ContextCompat.getColor(requireContext(), R.color.purple300)

        mBinding.setVariable(BR.btnName, btnName)
        mBinding.setVariable(BR.bgColor, bgColor)
        mBinding.setVariable(BR.strokeColor, strokeColor)
        mBinding.setVariable(BR.textColor, textColor)
        mBinding.setVariable(BR.rippleColor, rippleColor)
        mBinding.notifyChange()

    }
    private fun setGuideDesc(flag:Boolean){
        val guideText = if(flag) guideArray[0] else guideArray[1]
        mBinding.setVariable(BR.limitDescription, guideText)
        mBinding.notifyChange()
    }

    // 클릭 리스너
    private val viewClickListener = object : ViewClickListener {
        override fun onViewClick(view: View) {
            when(view.id){
                // 현재 페이지 배경
                R.id.cl_write_board -> {
                    // 일단 포커즈 제거
                    AppUtil.UIManager.hideKeyPad(requireActivity())
                }
                // 토글 스위치
                R.id.sw_todo_limit -> {
                    val flag = mBinding.isLimit?:false
                    mBinding.setVariable(BR.isLimit, !flag)
                    setGuideDesc(!flag)
                }
                // 기한 설정
                R.id.et_limit_input -> {
                    val dialog = CustomTimeDialog(listener = dialogListener)
                    dialog.show(requireActivity().supportFragmentManager, null)
                }
                // 버튼
                R.id.l_btn_write_todo -> {
                    requireActivity().setResult(Activity.RESULT_OK)
                    requireActivity().finish()
                }
            }
        }
    }

    // 다이얼로그 리스너
    private val dialogListener = object : CustomTimeDialog.OnDialogClickListener {
        override fun onSelectTime(hour: Int, min: Int) {
        }

        override fun onCancel() {
        }
    }
}