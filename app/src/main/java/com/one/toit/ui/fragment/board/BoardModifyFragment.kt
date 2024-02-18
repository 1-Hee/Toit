package com.one.toit.ui.fragment.board

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.one.toit.BR
import com.one.toit.R
import com.one.toit.base.bind.DataBindingConfig
import com.one.toit.base.fatory.ApplicationFactory
import com.one.toit.base.listener.ViewClickListener
import com.one.toit.base.ui.BaseFragment
import com.one.toit.data.dto.TaskDTO
import com.one.toit.data.model.TaskInfo
import com.one.toit.databinding.FragmentBoardModifyBinding
import com.one.toit.ui.dialog.CustomTimeDialog
import com.one.toit.ui.viewmodel.TaskInfoViewModel
import com.one.toit.util.AppUtil
import com.patrykandpatrick.vico.core.extension.setFieldValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import timber.log.Timber


class BoardModifyFragment : BaseFragment<FragmentBoardModifyBinding>() {
    // bundle
    private lateinit var mBundle:Bundle
    // vm
    private lateinit var taskInfoViewModel: TaskInfoViewModel

    private lateinit var guideArray:Array<String>
    private var mTaskDTO: TaskDTO? = null
    override fun getDataBindingConfig(): DataBindingConfig {
        mBundle = Bundle() // bundle init
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

        return DataBindingConfig(R.layout.fragment_board_modify)
            .addBindingParam(BR.title, "Modify It! 목표 수정하기")
            .addBindingParam(BR.actionName, "수정")
            .addBindingParam(BR.limitDescription, "")
            .addBindingParam(BR.isLimit, false)
            .addBindingParam(BR.click, viewClickListener)
            .addBindingParam(BR.onKeyListener, onKeyListener)
            .addBindingParam(BR.textListener, textListener)
            .addBindingParam(BR.textWatcher, textWatcher)
            .addBindingParam(BR.taskDTO, TaskDTO())
    }

    override fun initViewModel() {
        val factory = ApplicationFactory(requireActivity().application)
        taskInfoViewModel = getFragmentScopeViewModel(TaskInfoViewModel::class.java, factory)
    }

    override fun initView() {
        guideArray = requireContext().resources.getStringArray(R.array.arr_limit_guide) // 메뉴 명
        setGuideDesc(mBinding.isLimit == true)
        parseArguments()
    }
    private fun parseArguments(){
        // bundle data parsing!
        arguments?.let { bundle ->
            mBundle = bundle
            Timber.i("[MODIFY FRAGMENT] %s", bundle)
            @Suppress("DEPRECATION")
            mTaskDTO = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getParcelable("taskDTO", TaskDTO::class.java)
            } else {
                bundle.getParcelable("taskDTO")
            }
            Timber.e("[READ FRAGMENT DTO] %s", mTaskDTO)
            //
            val limitFlag = mTaskDTO?.taskLimit?.isNotBlank()==true
            mBinding.setVariable(BR.isLimit, limitFlag)
            mBinding.setVariable(BR.taskDTO, mTaskDTO)
            setGuideDesc(limitFlag)
        }
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
                R.id.iv_back -> {
                    val activity = requireActivity()
                    activity.setResult(Activity.RESULT_CANCELED)
                    activity.supportFragmentManager.popBackStack()
                }
                // 액션 버튼
                R.id.tv_action -> {
                    if(mTaskDTO != null){
                        val todoTitle = mBinding.etTitleTodo.text.toString()
                        val todoMemo = mBinding.etMemoTodo.text.toString()
                        val todoLimit = "" // TODO 시간 선택자 받아서 세팅해주기...
                        mTaskDTO?.taskTitle = todoTitle
                        mTaskDTO?.taskMemo = todoMemo
                        mTaskDTO?.taskLimit = todoLimit
                        val dto = mTaskDTO!!
                        val taskInfo = getTaskInfo(dto)
                        val updateResult = lifecycleScope.async(Dispatchers.IO){
                             taskInfoViewModel.modifyTaskInfo(taskInfo)
                        }
                        lifecycleScope.launch {
                            updateResult.await()
                            val context = requireContext()
                            val msg = getString(R.string.msg_modify_ok)
                            AppUtil.toast(context, msg)
                            val activity = requireActivity()
                            activity.setResult(Activity.RESULT_OK)
                            activity.finish()
                        }
                    }

                }
                // 현재 페이지 배경
                R.id.cl_modify_content -> {
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
            }
        }
    }

    private fun getTaskInfo(dto:TaskDTO): TaskInfo {
        return TaskInfo(
            infoId = dto.taskInfoId,
            fkTaskId = dto.taskId,
            taskTitle = dto.taskTitle,
            taskMemo = dto.taskMemo,
            taskLimit = dto.taskLimit,
            taskComplete = dto.taskComplete,
            taskCertification = dto.taskCertification
        )
    }

    // 다이얼로그 리스너
    private val dialogListener = object : CustomTimeDialog.OnDialogClickListener {
        override fun onSelectTime(hour: Int, min: Int) {
        }
        override fun onCancel() {
        }
    }
}