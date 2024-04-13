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
//import com.one.toit.data.dto.TaskDTO
import com.one.toit.data.model.TaskInfo
import com.one.toit.databinding.FragmentBoardModifyBinding
import com.one.toit.ui.dialog.CustomTimeDialog
import com.one.toit.data.viewmodel.TaskInfoViewModel
import com.one.toit.util.AppUtil
import com.one.toit.util.AppUtil.Time
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Calendar
import java.util.Date
import java.util.Locale


class BoardModifyFragment : BaseFragment<FragmentBoardModifyBinding>() {
    // bundle
    private lateinit var mBundle:Bundle
    // vm
    private lateinit var taskInfoViewModel: TaskInfoViewModel

    private lateinit var guideArray:Array<String>
//    private var mTaskDTO: TaskDTO? = null
    private var mDeadLDT: LocalDateTime? = null
    private var mDeadDate: Date? = null
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
//            .addBindingParam(BR.taskDTO, TaskDTO())
            .addBindingParam(BR.limitText, "")
            .addBindingParam(BR.isComplete, false)
    }

    override fun initViewModel() {
        val factory = ApplicationFactory(requireActivity().application)
        taskInfoViewModel = getFragmentScopeViewModel(TaskInfoViewModel::class.java, factory)
    }

    override fun initView() {
        mBinding.limitText = requireContext().getString(R.string.txt_no_limit)

        guideArray = requireContext().resources.getStringArray(R.array.arr_limit_guide) // 메뉴 명
        setGuideDesc(mBinding.isLimit == true)
        mBinding.notifyChange()
        parseArguments()
    }
    private fun parseArguments(){
        // bundle data parsing!
        arguments?.let { bundle ->
            mBundle = bundle
            Timber.i("[MODIFY FRAGMENT] %s", bundle)
//            @Suppress("DEPRECATION")
//            mTaskDTO = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//                bundle.getParcelable("taskDTO", TaskDTO::class.java)
//            } else {
//                bundle.getParcelable("taskDTO")
//            }
//            Timber.e("[READ FRAGMENT DTO] %s", mTaskDTO)
//            mBinding.setVariable(BR.taskDTO, mTaskDTO)
//            val mHasLimit = !(mTaskDTO?.taskLimit?.equals("null")?:false)
//            val limitText = if(mHasLimit) {
//                val timeStr = AppUtil.Time.dateFormat.parse(mTaskDTO?.taskLimit.toString());
//                val calendar = Calendar.getInstance()
//                calendar.time = timeStr
//                val lHour = calendar.get(Calendar.HOUR_OF_DAY)
//                val lMinute = calendar.get(Calendar.MINUTE)
//                calendar.set(Calendar.HOUR, lHour)
//                calendar.set(Calendar.MINUTE, lMinute)
//                mDeadDate = calendar.time
//                String.format("%02d:%02d", lHour, lMinute)
//            } else {
//                requireContext().getString(R.string.txt_no_limit)
//            }

//            // 완료 여부 판정
//            val isComplete = bundle.getBoolean("isComplete")
//            if(isComplete){
//                if(mTaskDTO != null){
//                    val date = AppUtil.Time.dateFormat.parse(mTaskDTO?.taskComplete.toString())
//                    val calendar = Calendar.getInstance()
//                    calendar.time = date
//                    val mDateTimeStr = AppUtil.Time.getFullDateString(calendar)
//                    val suffixStr = requireContext().resources.getString(R.string.suffix_complete)
//                    mBinding.setVariable(BR.limitDescription, "$mDateTimeStr $suffixStr")
//                }
//
//            }else{
//                setGuideDesc(mHasLimit)
//            }
//            Timber.i("FRAGMENT : %s", isComplete)

//            mBinding.setVariable(BR.isComplete, isComplete)
//            mBinding.setVariable(BR.deadLineString, limitText)
//            mBinding.setVariable(BR.isLimit, mHasLimit)
            mBinding.setVariable(BR.isComplete, false)
//            mBinding.setVariable(BR.deadLineString, false)
            mBinding.setVariable(BR.isLimit, false)

        }
        mBinding.notifyChange()
    }

    private fun setGuideDesc(flag:Boolean){
        val guideText = if(flag) guideArray[0] else guideArray[1]
        mBinding.setVariable(BR.limitDescription, guideText)
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

//                    // TODO 기한 설정 부분 로직 이상해짐..db 반영 안됨
//                    if(mTaskDTO != null){
//                        val todoTitle = mBinding.etTitleTodo.text.toString()
//                        val todoMemo = mBinding.etMemoTodo.text.toString()
//                        mTaskDTO?.taskTitle = todoTitle
//                        mTaskDTO?.taskMemo = todoMemo
//                        mTaskDTO?.taskLimit = mDeadDate.toString()
//                        val taskInfo = getTaskInfo(mTaskDTO!!)
//                        val updateResult = lifecycleScope.async(Dispatchers.IO){
//                             taskInfoViewModel.modifyTaskInfo(taskInfo)
//                        }
//                        lifecycleScope.launch {
//                            updateResult.await()
//                            val context = requireContext()
//                            val msg = getString(R.string.msg_modify_ok)
//                            AppUtil.toast(context, msg)
//                            val activity = requireActivity()
//                            activity.setResult(Activity.RESULT_OK)
//                            activity.finish()
//                        }
//                    }

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
                    val timeArr:Array<Int> = Time.getTimeLimit()
                    Timber.i("array : %s", timeArr.contentToString())
                    val dialog = CustomTimeDialog(
                        hour = timeArr[0],
                        min = timeArr[1],
                        listener = dialogListener,
                    )
                    dialog.show(requireActivity().supportFragmentManager, null)
                }
            }
        }
    }
//    private fun getTaskInfo(dto:TaskDTO): TaskInfo {
//        // Date 문자열을 Date 객체로 파싱
//        val dateFormat = AppUtil.Time.dateFormat;
//        val mTaskLimit = if(dto.taskLimit == null || dto.taskLimit == "null"){
//            null
//        } else {
//            dateFormat.parse(dto.taskLimit)
//        }
//        val mTaskComplete = if(dto.taskComplete == null || dto.taskComplete == "null") {
//            null
//        } else {
//            dateFormat.parse(dto.taskComplete)
//        }
//
//        return TaskInfo(
//            infoId = dto.taskInfoId,
//            fkTaskId = dto.taskId,
//            taskTitle = dto.taskTitle,
//            taskMemo = dto.taskMemo,
//            taskLimit = mTaskLimit,
//            taskComplete = mTaskComplete,
//            taskCertification = dto.taskCertification
//        )
//    }

    // 다이얼로그 리스너
    private val dialogListener = object : CustomTimeDialog.OnDialogClickListener {
        override fun onSelectTime(hour: Int, min: Int) {
            val limitString = if(hour > 0 || min > 0){
                // time init...
                val currentDate = Date();
                val calendar = Calendar.getInstance()
                calendar.time = currentDate;
                val lHour = hour + calendar.get(Calendar.HOUR_OF_DAY)
                val lMinute = min + calendar.get(Calendar.MINUTE)
                calendar.set(Calendar.HOUR, lHour)
                calendar.set(Calendar.MINUTE, lMinute)
                mDeadDate = calendar.time
                String.format("%02d:%02d", lHour, lMinute)
            }else {
                requireContext().getString(R.string.txt_no_limit)
            }
            mBinding.limitText = limitString
            mBinding.notifyChange()
        }
        override fun onCancel() {
        }
    }
}