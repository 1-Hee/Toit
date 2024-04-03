package com.one.toit.ui.fragment.board

import android.app.Activity
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.one.toit.BR
import com.one.toit.R
import com.one.toit.base.bind.DataBindingConfig
import com.one.toit.base.fatory.ApplicationFactory
import com.one.toit.base.listener.ViewClickListener
import com.one.toit.base.ui.BaseFragment
import com.one.toit.data.model.TaskInfo
import com.one.toit.data.model.TaskRegister
import com.one.toit.databinding.FragmentBoardWriteBinding
import com.one.toit.ui.dialog.CustomTimeDialog
import com.one.toit.data.viewmodel.TaskInfoViewModel
import com.one.toit.data.viewmodel.TaskRegisterViewModel
import com.one.toit.util.AppUtil
import com.one.toit.util.AppUtil.Time
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Calendar
import java.util.Date


class BoardWriteFragment : BaseFragment<FragmentBoardWriteBinding>(){

    // viewModel
    // 부모 엔티티
    private lateinit var taskRegisterViewModel: TaskRegisterViewModel
    // 자식 엔티티
    private lateinit var taskInfoViewModel: TaskInfoViewModel

    private lateinit var guideArray:Array<String>
    private var mTaskLimit:Date? = null

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
            .addBindingParam(BR.title, "To It! 목표 작성하기")
            .addBindingParam(BR.actionName, "")
            .addBindingParam(BR.limitDescription, "")
            .addBindingParam(BR.isLimit, false)
            .addBindingParam(BR.click, viewClickListener)
            .addBindingParam(BR.onKeyListener, onKeyListener)
            .addBindingParam(BR.textListener, textListener)
            .addBindingParam(BR.textWatcher, textWatcher)
            .addBindingParam(BR.limitText, "")
    }
    override fun initViewModel() {
        val factory = ApplicationFactory(requireActivity().application)
        taskRegisterViewModel = getFragmentScopeViewModel(TaskRegisterViewModel::class.java, factory)
        taskInfoViewModel = getFragmentScopeViewModel(TaskInfoViewModel::class.java, factory)
    }
    override fun initView() {
        val context = requireContext()
        mBinding.limitText = context.getString(R.string.txt_no_limit)
        guideArray = context.resources.getStringArray(R.array.arr_limit_guide) // 메뉴 명
        setGuideDesc(mBinding.isLimit == true)

        // 등록버튼 스타일 세팅
        val btnName = "등록 하기"
        val bgColor = ContextCompat.getColor(context, R.color.purple200)
        val strokeColor = ContextCompat.getColor(context, R.color.purple200)
        val textColor = ContextCompat.getColor(context, R.color.white)
        val rippleColor = ContextCompat.getColor(context, R.color.purple300)

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
                R.id.iv_back -> {
                    val activity = requireActivity()
                    activity.setResult(Activity.RESULT_CANCELED)
                    activity.finish()
                }
                // 현재 페이지 배경
                R.id.cl_write_content -> {
                    // 일단 포커즈 제거
                    AppUtil.UIManager.hideKeyPad(requireActivity())
                }
                // 토글 스위치
                R.id.sw_todo_limit -> {
                    val flag = mBinding.isLimit?:false
                    mBinding.setVariable(BR.isLimit, !flag)
                    setGuideDesc(!flag)
                    if(flag){
                        mTaskLimit = null;
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
                    val timeArr:Array<Int> = Time.getTimeLimit()
                    Timber.i("array : %s", timeArr.contentToString())
                    val mFlag = Time.isEnoughTimeDiff(timeArr[0], timeArr[1])
                    if(mFlag){
                        val dialog = CustomTimeDialog(
                            hour = timeArr[0],
                            min = timeArr[1],
                            listener = dialogListener,
                        )
                        dialog.show(requireActivity().supportFragmentManager, null)
                    }else {
                        AppUtil.toast(context,
                            context.getString(R.string.msg_invalid_limit)
                        )
                    }

                }
                // 등록 버튼!
                R.id.l_btn_write_todo -> {
                    // 제목이 비었는지 확인하는 플래그 변수
                    val context = requireContext()
                    val titleCheckMsg = context.getString(R.string.msg_input_title)
                    val flagTitle = mBinding.etTitleTodo.text?.isBlank() == true
                    if(flagTitle){ // 타이틀 제목이 비었다면 알림 메세지...
                        AppUtil.toast(context, titleCheckMsg)
                    }else { // DB save task...
                        val message = context.getString(R.string.msg_success_todo)

                        lifecycleScope.launch(Dispatchers.IO){
                            val taskRegister = TaskRegister()
                            val taskId = taskRegisterViewModel.addTaskRegister(taskRegister)
                            Timber.i("taskId : %s", taskId)
                            val taskInfo = createTaskInfo(taskId)
                            val infoId = taskInfoViewModel.addTaskInfo(taskInfo)
                            Timber.i("info : %s", infoId)
                            requireActivity().setResult(Activity.RESULT_OK)
                            requireActivity().finish()
                            showToast(message)
                        }
                    }
                }
            }
        }
    }

    // 확장함수를 통한 코루틴 토스트 메세지 호출
    private fun BoardWriteFragment.showToast(message: String) {
        // 토스트 메시지를 노출합니다.
        lifecycleScope.launch {
            val context = requireContext();
            AppUtil.toast(context, message)
        }
    }

    /**
     * 임시로 할일 정보(Task Info) 엔티티를 만드는 메서드
     */
    private fun createTaskInfo(fkTaskId:Long): TaskInfo {
        val titleFlag = mBinding.etTitleTodo.text?.isNotBlank() == true
        val memoFlag = mBinding.etMemoTodo.text?.isNotBlank() == true
        var taskTitle: String = if (titleFlag) mBinding.etTitleTodo.text.toString() else ""
        var taskMemo: String = if (memoFlag) mBinding.etMemoTodo.text.toString() else ""
        var taskLimit: Date? = mTaskLimit
        return TaskInfo(
            fkTaskId = fkTaskId,
            taskTitle = taskTitle,
            taskMemo = taskMemo,
            taskLimit = taskLimit,
        )
    }


    // 다이얼로그 리스너
    private val dialogListener = object : CustomTimeDialog.OnDialogClickListener {
        override fun onSelectTime(hour: Int, min: Int) {
            val flag = Time.isEnoughTimeDiff(hour, min)
            val context = requireContext()
            if(flag){
                mTaskLimit = Time.getLimitDate(hour, min)
                val calendar = Calendar.getInstance()
                calendar.time = mTaskLimit
                var mHour = calendar.get(Calendar.HOUR_OF_DAY)
                var mMinute = calendar.get(Calendar.MINUTE)
                val isBurstTime = Time.isBurstTime(hour, min)
                if(isBurstTime){ // 혹시 시간이 오버됐다면,
                    mHour = 23
                    mMinute = 59 // 23:59로 강제로 고정
                    mTaskLimit = Time.getDate(mHour, mMinute) // 마감기한도 변경
                }
                val strHour = String.format("%02d", mHour)
                val strMin = String.format("%02d", mMinute)
                val timeStr = "$strHour:$strMin"
                mBinding.setVariable(BR.limitText, timeStr)
                mBinding.notifyChange()
            }else {
                mTaskLimit = null
                val strNoLimit = context.getString(R.string.txt_no_limit)
                mBinding.setVariable(BR.limitText, strNoLimit)
                mBinding.notifyChange()
                AppUtil.toast(context, context.getString(R.string.msg_invailid_time_diff))
            }
            Timber.d("hour : $hour , min : $min, flag => $flag")

        }
        override fun onCancel() {
        }
    }
}