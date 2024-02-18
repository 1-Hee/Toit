package com.one.toit.ui.activity

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.one.toit.BR
import com.one.toit.R
import com.one.toit.base.bind.DataBindingConfig
import com.one.toit.base.listener.ViewClickListener
import com.one.toit.base.ui.BaseActivity
import com.one.toit.data.dto.TaskDTO
import com.one.toit.databinding.ActivityBoardBinding
import com.one.toit.ui.fragment.board.BoardModifyFragment
import com.one.toit.ui.fragment.board.BoardReadFragment
import com.one.toit.ui.fragment.board.BoardWriteFragment
import timber.log.Timber

class BoardActivity : BaseActivity<ActivityBoardBinding>() {

    private lateinit var titleArr:Array<String>
    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_board)
            .addBindingParam(BR.click, viewClickListener)
            .addBindingParam(BR.title, "")
            .addBindingParam(BR.actionName, "")
    }

    override fun init(savedInstanceState: Bundle?) {
        // TODO read 프래그먼트에서 수정 프래그먼트로 이동할 때 액티비티 라이프 사이클 함수 호출하게 해서 이동하기
        titleArr = this.resources.getStringArray(R.array.arr_board_title)
        Timber.i("extra : %s", intent?.extras)
        Timber.i("extra idx : %s", intent?.extras?.getInt("pageIndex"))
        // intent로부터 인자를 받아 세팅
        val pageIndex = intent?.extras?.getInt("pageIndex")?:0;
        val actionName = getActionName(pageIndex)
        @Suppress("DEPRECATION")
        val mTaskDTO = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent?.extras?.getParcelable("taskDTO", TaskDTO::class.java)
        } else {
            intent?.extras?.getParcelable("taskDTO")
        }
        Timber.i("taskDTO... %s", mTaskDTO)
        val bundle = Bundle()
        if(mTaskDTO!=null){
            bundle.putParcelable("taskDTO", mTaskDTO)
        }
        navFragment(pgIndex = pageIndex, bundle = bundle)
        mBinding.setVariable(BR.title, titleArr[pageIndex])
        mBinding.setVariable(BR.actionName, actionName)
        mBinding.notifyChange()
    }

    private val viewClickListener = object : ViewClickListener {
        override fun onViewClick(view: View) {}
    }

    private fun getActionName(pgIndex: Int):String{
        return when(pgIndex){
            2 -> "완료"
            else -> ""
        }
    }
    private fun navFragment(
        pgIndex:Int = 0,
        allowBackStack:Boolean = false,
        bundle: Bundle? = null
    ) {
        /**
         *  0 : TODO 작성
         *  1 : TODO 수정
         *  2 : TODO 완료
         */
        val fragment = when(pgIndex){
            1 -> { // 수정
                BoardModifyFragment() // 다른 프래그먼트
            }
            2 -> { // 완료
                BoardReadFragment()
            }
            else -> BoardWriteFragment()
        }
        fragment.arguments = bundle // bundle init
        if(allowBackStack) {
            // 다른 프래그먼트로 교체하는 트랜잭션을 수행합니다.
            supportFragmentManager.beginTransaction()
                .replace(R.id.board_fragment_host, fragment)
                .addToBackStack(null) // 백 스택에 추가하여 뒤로 가기를 지원합니다.
                .commit()
        }else {
            // 다른 프래그먼트로 교체하는 트랜잭션을 수행합니다.
            supportFragmentManager.beginTransaction()
                .replace(R.id.board_fragment_host, fragment)
                .commit()
        }
        
    }
}