package com.one.toit.ui.fragment.board

import android.app.Activity
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import android.widget.Toast
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
import com.one.toit.ui.viewmodel.TaskInfoViewModel
import com.one.toit.ui.viewmodel.TaskRegisterViewModel
import com.one.toit.util.AppUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Arrays
import java.util.Date


class BoardWriteFragment : BaseFragment<FragmentBoardWriteBinding>(){

    // viewModel
    // 부모 엔티티
    private lateinit var taskRegisterViewModel: TaskRegisterViewModel
    // 자식 엔티티
    private lateinit var taskInfoViewModel: TaskInfoViewModel

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
            .addBindingParam(BR.limitText, "00:00")
    }
    override fun initViewModel() {
        val factory = ApplicationFactory(requireActivity().application)
        taskRegisterViewModel = getFragmentScopeViewModel(TaskRegisterViewModel::class.java, factory)
        taskInfoViewModel = getFragmentScopeViewModel(TaskInfoViewModel::class.java, factory)
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

        lifecycleScope.launch {
            taskRegisterViewModel.readTaskRegisterList().observe(viewLifecycleOwner){
                Timber.i("register list : %s", it)
            }
            taskInfoViewModel.readTaskInfoList().observe(viewLifecycleOwner){
                Timber.i("info list : %s", it)
            }
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
                    val timeArr:Array<Int> = AppUtil.Time.getTimeArray()
                    Timber.i("array : %s", timeArr.contentToString())
                    val dialog = CustomTimeDialog(
                        hour = timeArr[0],
                        min = timeArr[1],
                        listener = dialogListener,
                    )
                    dialog.show(requireActivity().supportFragmentManager, null)
                }
                // 등록 버튼!
                R.id.l_btn_write_todo -> {
                    val titleString = mBinding.etTitleTodo.text.toString()
                    val context = requireContext()
                    val msg:String = if(titleString.isNotBlank()){
                        lifecycleScope.launch(Dispatchers.IO){
                            val taskRegister = TaskRegister()
                            val taskId = taskRegisterViewModel.addTaskRegister(taskRegister)
                            Timber.i("taskId : %s", taskId)
                            val taskInfo = getTaskInfo(taskId, titleString)
                            val infoId = taskInfoViewModel.addTaskInfo(taskInfo)
                            Timber.i("info : %s", infoId)
                            requireActivity().setResult(Activity.RESULT_OK)
                            requireActivity().finish()
                        }
                        context.getString(R.string.msg_success_todo)
                    }else {
                        context.getString(R.string.msg_input_title)
                    }
                    AppUtil.toast(context, msg)
                }
            }
        }
    }

    private fun getTaskInfo(taskId:Long, titleString:String):TaskInfo{
        val memo = mBinding.etMemoTodo.text.toString()
        return TaskInfo(
            taskTitle = titleString,
            taskMemo = memo,
            fkTaskId = taskId
        )
    }

    // 다이얼로그 리스너
    private val dialogListener = object : CustomTimeDialog.OnDialogClickListener {
        override fun onSelectTime(hour: Int, min: Int) {
            val mLimitString = AppUtil.Time.parseToLimitString(hour, min)
            mBinding.limitText = mLimitString
            mBinding.notifyChange()
            // TODO 여기서 받은 시간을, 현재 시간 +해서 DateString으로 TaskInfo에 추가해야함!
        }

        override fun onCancel() {
        }
    }
}