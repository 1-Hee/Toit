package com.one.toit.ui.fragment.start

import android.app.Activity
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.one.toit.R
import com.one.toit.BR
import com.one.toit.base.bind.DataBindingConfig
import com.one.toit.base.listener.ViewClickListener
import com.one.toit.base.ui.BaseFragment
import com.one.toit.databinding.FragmentIntroBinding
import com.one.toit.ui.activity.MainActivity
import com.one.toit.util.AppUtil
import com.one.toit.util.PreferenceUtil
import timber.log.Timber

class IntroFragment : BaseFragment<FragmentIntroBinding>() {

    // 백버튼 콜백
    private lateinit var callBack: OnBackPressedCallback
    private lateinit var prefs:PreferenceUtil
    override fun getDataBindingConfig(): DataBindingConfig {
        // 키보드 엔터 이벤트 핸들링을 위한 리스너
        val onKeyListener: View.OnKeyListener = View.OnKeyListener { view, keyCode, keyEvent ->
            if(keyCode == KeyEvent.KEYCODE_ENTER
                && keyEvent.action == KeyEvent.ACTION_DOWN){
                // hide keypad
                AppUtil.UIManager.hideKeyPad(requireActivity())
                val txtView = view as TextView
                Timber.i("value : %s", txtView.text)
                saveNickName(txtView.text.toString())
                // moveToMain()
                true
            }else {
                false
            }
        }

        "기한을 설정하지 않은 목표는 달성 시간 통계에 반영 되지 않아요!"
        // 가상 키패드 엔터 이벤트 핸들링을 위한 리스너
        val textListener: TextView.OnEditorActionListener = TextView.OnEditorActionListener {
                textView, keyCode, keyEvent ->
            if(keyCode == 5 || keyCode == 6){
                // hide keypad
                AppUtil.UIManager.hideKeyPad(requireActivity())
                Timber.i("value : %s", textView.text)
                saveNickName(textView.text.toString())
                // moveToMain()
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

        return DataBindingConfig(R.layout.fragment_intro)
            .addBindingParam(BR.click, viewClickListener)
            .addBindingParam(BR.onKeyListener, onKeyListener)
            .addBindingParam(BR.textListener, textListener)
            .addBindingParam(BR.textWatcher, textWatcher)
    }

    override fun initViewModel() {
    }

    override fun onResume() {
        super.onResume()
        prefs = PreferenceUtil.getInstance(requireContext())
        val nickNameKey = requireContext().getString(R.string.key_nickname)
        if(prefs.getValue(nickNameKey).isNotBlank()){ moveToMain() }
    }
    override fun initView() {
        // 콜백함수에 intent 추가!
        callBack = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                requireActivity().setResult(AppCompatActivity.RESULT_CANCELED)
                requireActivity().finish()
            }
        }
        // 액티비티에 콜백 함수 추가
        requireActivity().onBackPressedDispatcher.addCallback(callBack)

        // 시작 버튼 스타일 세팅
        val btnName = "시작 하기"
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

    private val viewClickListener = object : ViewClickListener {
        override fun onViewClick(view: View) {
            when(view.id){
                R.id.l_btn_start -> {
                    val text = mBinding.etInputNickname.text.toString()
                    saveNickName(text)
                    Timber.i("value %s", mBinding.etInputNickname.text.toString())
                    // moveToMain()
                }
            }
        }
    }

    private fun saveNickName(text:String){
        if(text.isBlank()){
            val errText = requireContext().getString(R.string.ts_invalid_nickname)
            AppUtil.toast(requireContext(), errText)
            return
        }
        val nickNameKey = requireContext().getString(R.string.key_nickname)
        val successText = requireContext().getString(R.string.ts_save_nickname)
        AppUtil.toast(requireContext(), successText)
        Timber.i("nickName.. %s", text)
        prefs.setValue(nickNameKey, text)
        moveToMain()
    }

    private fun moveToMain(){
        val startText = requireContext().getString(R.string.ts_greet)
        AppUtil.toast(requireContext(), startText)
        val intent = Intent(requireActivity(), MainActivity::class.java)
        requireActivity().onBackPressedDispatcher.onBackPressed()
        launcher.launch(intent)
    }

    private val launcher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { // 액티비티 종료시 결과릴 리턴받기 위한 콜백 함수
            result -> Timber.d("onActivityResult.......")
        if (result.resultCode == Activity.RESULT_OK) { // 저장 성공
        }else if(result.resultCode == Activity.RESULT_CANCELED){ // 저장 실패
        }
    }
}