package com.one.toit.ui.fragment.board

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
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
import com.one.toit.data.viewmodel.TaskInfoViewModel
import com.one.toit.databinding.FragmentBoardModifyBinding
import com.one.toit.ui.dialog.CustomTimeDialog
import com.one.toit.util.AppUtil
import com.one.toit.util.AppUtil.Time
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Calendar
import java.util.Date
import kotlin.math.abs


class BoardModifyFragment : BaseFragment<FragmentBoardModifyBinding>() {
    // bundle
    private lateinit var mBundle:Bundle
    // vm
    private lateinit var taskInfoViewModel: TaskInfoViewModel

    private lateinit var guideArray:Array<String>
    private var mTaskDTO: TaskDTO? = null
    private var mTaskLimit: Date? = null
    override fun getDataBindingConfig(): DataBindingConfig {
        mBundle = Bundle() // bundle init
        // 키보드 엔터 이벤트 핸들링을 위한 리스너
        val onKeyListener: View.OnKeyListener = View.OnKeyListener { _, keyCode, keyEvent ->
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
                _, keyCode, _ ->
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
            .addBindingParam(BR.click, viewClickListener)
            .addBindingParam(BR.onKeyListener, onKeyListener)
            .addBindingParam(BR.textListener, textListener)
            .addBindingParam(BR.textWatcher, textWatcher)
            .addBindingParam(BR.taskDTO, TaskDTO())
            .addBindingParam(BR.limitText, "")
            .addBindingParam(BR.limitDesc, "")
            .addBindingParam(BR.hasLimit, false)
            .addBindingParam(BR.isComplete, false)
    }

    override fun initViewModel() {
        val factory = ApplicationFactory(requireActivity().application)
        taskInfoViewModel = getFragmentScopeViewModel(TaskInfoViewModel::class.java, factory)
    }

    override fun initView() {
        mBinding.limitText = requireContext().getString(R.string.txt_no_limit)
        guideArray = requireContext().resources.getStringArray(R.array.arr_limit_guide) // 메뉴 명
        setGuideDesc(mBinding.hasLimit == true)
        mBinding.notifyChange()
        parseArguments()
    }
    private fun parseArguments(){
        // bundle data parsing!
        arguments?.let { bundle ->
            val context = requireContext()
            mBundle = bundle
            Timber.i("[MODIFY FRAGMENT] %s", bundle)
            @Suppress("DEPRECATION")
            mTaskDTO = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getParcelable("taskDTO", TaskDTO::class.java)
            } else {
                bundle.getParcelable("taskDTO")
            }
            Timber.d("MODIFY dto >> $mTaskDTO")
            mBinding.setVariable(BR.taskDTO, mTaskDTO)
            val mHasLimit = mTaskDTO?.taskLimit != null
            mBinding.setVariable(BR.hasLimit, mHasLimit)
            val mLimitText = if(mHasLimit){
                Time.getTimeLog(mTaskDTO?.taskLimit)
            }else {
                context.resources.getString(R.string.txt_no_limit)
            }
            mBinding.setVariable(BR.limitText, mLimitText)
            mTaskLimit = mTaskDTO?.taskLimit

            val createAt:Date? = mTaskDTO?.createAt;
            // 완료 여부 판정
            val isOverDay = Time.checkOverDay(createAt);
            // 하루 이상 차이가 나는지 확인 +
            // 완료 여부 판정
            val isComplete = mTaskDTO?.taskComplete != null;

            if(isComplete) { // 성공시
                val timeLog = Time.getFullString(mTaskDTO?.taskComplete)
                val suffixStr = context.resources.getString(R.string.suffix_complete)
                mBinding.setVariable(BR.limitDesc, "$timeLog $suffixStr")
                mBinding.setVariable(BR.isComplete, true)
            }else if(isOverDay) { // 시간 지났을 경우
                mBinding.setVariable(BR.isComplete, true)
            } else {
                mBinding.setVariable(BR.isComplete, false)
                setGuideDesc(mHasLimit)
            }
            mBinding.notifyChange()
        }
    }

    private fun setGuideDesc(flag:Boolean){
        val guideText = if(flag) guideArray[0] else guideArray[1]
        mBinding.setVariable(BR.limitDesc, guideText)
        mBinding.notifyChange()
    }

    // 클릭 리스너
    private val viewClickListener = object : ViewClickListener {
        override fun onViewClick(view: View) {
            when(view.id){
                R.id.iv_back -> {
                    val activity = requireActivity()
                    activity.setResult(Activity.RESULT_CANCELED)
                    activity.onBackPressedDispatcher.onBackPressed()
                }
                // 수정 버튼
                R.id.tv_action -> {
//                    // TODO 기한 설정 부분 로직 이상해짐..db 반영 안됨
                    if(mTaskDTO != null){
                        val mInfo = createTaskInfo(mTaskDTO!!)
                        val context = requireContext()
                        val msg = context.getString(R.string.msg_modify_ok)
                        val activity = requireActivity()
                        lifecycleScope.launch {
                            taskInfoViewModel.modifyTaskInfo(mInfo)
                            activity.setResult(Activity.RESULT_OK)
                            activity.finish()
                            showToast(msg)
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
                    val flag = mBinding.hasLimit?:false
                    mBinding.setVariable(BR.hasLimit, !flag)
                    setGuideDesc(!flag)
                    if(flag){
                        mTaskLimit = null
                        val context = requireContext()
                        mBinding.limitText = context.getString(R.string.txt_no_limit)
                        mBinding.notifyChange()
                        val message = context.getString(R.string.msg_no_limit)
                        AppUtil.toast(context, message)
                    }
                }
                // 기한 설정
                R.id.et_limit_input -> {
                    val context = requireContext()
                    // 현재 시간 기준 Date 객체 생성하여 비교!
                    val mDate = Date();
                    val calendar = Calendar.getInstance()
                    calendar.time = mDate;
                    calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE)+10)
                    val hour = calendar.get(Calendar.HOUR_OF_DAY)
                    val min = calendar.get(Calendar.MINUTE)
                    val mFlag = Time.isEnoughTimeDiff(calendar.time);
                    if(mFlag){
                        val dialog = CustomTimeDialog(
                            hour = hour,
                            min = min,
                            listener = dialogListener,
                        )
                        dialog.show(requireActivity().supportFragmentManager, null)
                    }else {
                        AppUtil.toast(context,
                            context.getString(R.string.msg_invalid_limit)
                        )
                    }
                }
            }
        }
    }

    // 확장함수를 통한 토스트 메시지 호출
    private fun BoardModifyFragment.showToast(message: String) {
        // 토스트 메시지를 노출합니다.
        lifecycleScope.launch {
            val context = requireContext()
            AppUtil.toast(context, message)
        }
    }

    /**
     * 할일 정보(Task Info) 엔티티를 만드는 메서드
     */
    private fun createTaskInfo(mTaskDTO: TaskDTO): TaskInfo {
        val titleFlag = mBinding.etTitleTodo.text?.isNotBlank() == true
        val memoFlag = mBinding.etMemoTodo.text?.isNotBlank() == true
        val taskTitle: String = if (titleFlag) mBinding.etTitleTodo.text.toString() else ""
        val taskMemo: String = if (memoFlag) mBinding.etMemoTodo.text.toString() else ""
        val taskLimit: Date? = mTaskLimit
        return TaskInfo(
            infoId = mTaskDTO.taskInfoId,
            fkTaskId = mTaskDTO.taskId,
            taskTitle = taskTitle,
            taskMemo = taskMemo,
            taskLimit = taskLimit,
            taskComplete = mTaskDTO.taskComplete,
            taskCertification = mTaskDTO.taskCertification
        )
    }

    // 다이얼로그 리스너
    private val dialogListener = object : CustomTimeDialog.OnDialogClickListener {
        override fun onSelectTime(hour: Int, min: Int) {
            val context = requireContext()
            mTaskLimit = Time.getLimitDate(hour, min)
            val timeStr = Time.getTimeString(context, mTaskLimit)
            mBinding.setVariable(BR.limitText, timeStr)
            mBinding.notifyChange()
        }
        override fun onCancel() {
        }
    }
}