package com.one.toit.ui.fragment.start

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.one.toit.R
import com.one.toit.BR
import com.one.toit.base.bind.DataBindingConfig
import com.one.toit.base.listener.ViewClickListener
import com.one.toit.base.ui.BaseFragment
import com.one.toit.databinding.FragmentIntroBinding
import com.one.toit.ui.activity.MainActivity
import timber.log.Timber

class IntroFragment : BaseFragment<FragmentIntroBinding>() {

    // 백버튼 콜백
    private lateinit var callBack: OnBackPressedCallback
    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.fragment_intro)
            .addBindingParam(BR.click, viewClickListener)
    }

    override fun initViewModel() {
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
    }

    val viewClickListener = object : ViewClickListener {
        override fun onClickView(view: View) {
            when(view.id){
                R.id.btn_start -> {
                    val intent = Intent(requireActivity(), MainActivity::class.java)
                    // TODO intent nickname value?
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                    launcher.launch(intent)
                }
            }
        }
    }

    private val launcher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { // 액티비티 종료시 결과릴 리턴받기 위한 콜백 함수
            result -> Timber.d("onActivityResult.......")
        if (result.resultCode == Activity.RESULT_OK) { // 저장 성공
            Timber.d("result Ok...")
        }else if(result.resultCode == Activity.RESULT_CANCELED){ // 저장 실패
            Timber.e("result Cancel...")
        }
    }
}