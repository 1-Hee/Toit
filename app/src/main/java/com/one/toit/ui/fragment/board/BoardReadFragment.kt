package com.one.toit.ui.fragment.board

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.one.toit.BR
import com.one.toit.R
import com.one.toit.base.bind.DataBindingConfig
import com.one.toit.base.fatory.ApplicationFactory
import com.one.toit.base.listener.ViewClickListener
import com.one.toit.base.ui.BaseFragment
import com.one.toit.data.dto.TaskDTO
import com.one.toit.data.dto.WarningDTO
import com.one.toit.data.model.TaskInfo
import com.one.toit.data.model.TaskPoint
import com.one.toit.data.viewmodel.TaskInfoViewModel
import com.one.toit.data.viewmodel.TaskPointViewModel
import com.one.toit.data.viewmodel.TaskRegisterViewModel
import com.one.toit.data.viewmodel.TaskViewModel
import com.one.toit.databinding.FragmentBoardReadBinding
import com.one.toit.ui.dialog.WarningDialog
import com.one.toit.util.AppUtil
import com.one.toit.util.AppUtil.Time
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

class BoardReadFragment : BaseFragment<FragmentBoardReadBinding>() {
    // bundle
    private lateinit var mBundle:Bundle
    // bundle로 파싱할 dto 객체
    private var mTaskDTO: TaskDTO? = null
    // 다른 액티비티 이동후 결과 값을 받아 핸들링할 런쳐
     private lateinit var launcher: ActivityResultLauncher<Intent>
    // vm
    private lateinit var taskRegisterViewModel: TaskRegisterViewModel
    private lateinit var taskInfoViewModel: TaskInfoViewModel
    private lateinit var taskViewModel: TaskViewModel
    private lateinit var taskPointViewModel: TaskPointViewModel

    // 저장된 파일 이름
    private var mFileName:String = ""
    // 카메라로부터 받은 비트맵
    private var acceptedBitmap:Bitmap? = null
    private var mImageUri:Uri? = null
    override fun getDataBindingConfig(): DataBindingConfig {
        mBundle = Bundle() // bundle init
        return DataBindingConfig(R.layout.fragment_board_read)
            .addBindingParam(BR.title, "")
            .addBindingParam(BR.actionName, "")
            .addBindingParam(BR.click, viewClickListener)
            .addBindingParam(BR.mTaskDTO, TaskDTO())
            .addBindingParam(BR.limitText, "")
            .addBindingParam(BR.isCertified, false)
            .addBindingParam(BR.hasLimit, false)
    }
    override fun initViewModel() {
        val factory = ApplicationFactory(requireActivity().application)
        taskRegisterViewModel = getFragmentScopeViewModel(TaskRegisterViewModel::class.java, factory)
        taskInfoViewModel = getFragmentScopeViewModel(TaskInfoViewModel::class.java, factory)
        taskViewModel = getFragmentScopeViewModel(TaskViewModel::class.java, factory)
        taskPointViewModel = getFragmentScopeViewModel(TaskPointViewModel::class.java, factory)
    }
    override fun initView() {
        // 액티비티 종료시 결과릴 리턴받기 위한 콜백 함수
        launcher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                createCaptureImages(result)
            }
        }
        setButtonStyles() // 버튼 색상 초기화 해주고,
        parseArguments() // 매개변수 받아와서 세팅해준다.
        mBinding.notifyChange()
    }

    // 버튼 색상 초기화 메서드
    private fun setButtonStyles(){
        val deleteBtnName = "삭제 하기"
        val deleteBgColor = ContextCompat.getColor(requireContext(), R.color.none)
        val deleteStrokeColor = ContextCompat.getColor(requireContext(), R.color.red400)
        val deleteTextColor = ContextCompat.getColor(requireContext(), R.color.red400)
        val deleteRippleColor = ContextCompat.getColor(requireContext(), R.color.red100)
        val modifyBtnName = "수정 하기"
        val modifyBgColor = ContextCompat.getColor(requireContext(), R.color.purple200)
        val modifyStrokeColor = ContextCompat.getColor(requireContext(), R.color.none)
        val modifyTextColor = ContextCompat.getColor(requireContext(), R.color.white)
        val modifyRippleColor = ContextCompat.getColor(requireContext(), R.color.purple400)
        mBinding.setVariable(BR.deleteBtnName, deleteBtnName)
        mBinding.setVariable(BR.deleteBgColor, deleteBgColor)
        mBinding.setVariable(BR.deleteStrokeColor, deleteStrokeColor)
        mBinding.setVariable(BR.deleteTextColor, deleteTextColor)
        mBinding.setVariable(BR.deleteRippleColor, deleteRippleColor)
        mBinding.setVariable(BR.modifyBtnName, modifyBtnName)
        mBinding.setVariable(BR.modifyBgColor, modifyBgColor)
        mBinding.setVariable(BR.modifyStrokeColor, modifyStrokeColor)
        mBinding.setVariable(BR.modifyTextColor, modifyTextColor)
        mBinding.setVariable(BR.modifyRippleColor, modifyRippleColor)
    }

    // 매개 변수로 받아온 인자를 파싱하는 함수
    private fun parseArguments(){
        // 번들 참조
        arguments?.let { bundle ->
            mBundle = bundle
            Timber.i("[READ FRAGMENT] %s", bundle)
            @Suppress("DEPRECATION")
            mTaskDTO = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getParcelable("taskDTO", TaskDTO::class.java)
            } else {
                bundle.getParcelable("taskDTO")
            }
            Timber.e("[READ FRAGMENT DTO] %s", mTaskDTO)
            mBinding.setVariable(BR.mTaskDTO, mTaskDTO)
            if(mTaskDTO != null){
                initBundleTaskDTO(requireContext(), mTaskDTO!!)
            }
        }
    }

    // bundle로 받아온 값 파싱하는 메서드
    private fun initBundleTaskDTO(context: Context, taskDTO: TaskDTO){
        val actionStr = context.resources.getString(R.string.txt_complete)
        val isCertified = taskDTO.taskComplete != null // 할일 완료 여부
        val hasLimit = taskDTO.taskLimit != null // 제한 시간이 있는지 여부
        val limitText = if(hasLimit){
            Time.getLimitString(taskDTO.taskLimit!!)
        }else {
            context.getString(R.string.txt_no_limit)
        }
        mBinding.setVariable(BR.title, taskDTO.taskTitle)
        mBinding.setVariable(BR.actionName, actionStr)
        mBinding.setVariable(BR.isCertified, isCertified)
        mBinding.setVariable(BR.hasLimit, hasLimit)
        mBinding.setVariable(BR.limitText, limitText)
        mBinding.notifyChange()
    }

    // 카메라로부터 받아온 이미지 비트맵을 uri로 저장하는 메서드
    // TODO 현재 카메라 기능이 지원중단 되었으므로 향후 카메라X나 카메라2로 기능 이전이 필요...
    private fun createCaptureImages(result:ActivityResult){
        if(result.data?.extras != null){
            Timber.i("result : %s", result)
            Timber.i("data : %s", result.data)
            val bitmap:Bitmap? = try {
                result.data?.extras?.get("data") as Bitmap
            }catch (e:Exception){
                null
            }
            Timber.i("photo .. %s %s %s", bitmap?.width, bitmap?.height, bitmap?.density)
            acceptedBitmap = bitmap
            if (acceptedBitmap != null) {
                // 이미지 뷰에 바인딩
                mBinding.ivCapture.setImageBitmap(bitmap)
                mBinding.isCertified = true
                mBinding.notifyChange()
                // 파일명 세팅...
                // TODO 파일명 세팅 리터칭하기
                val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                mFileName = "${requireContext().packageName}_${timeStamp}.jpg"
            }
        }
    }

    private fun showCamera(){
        // 카메라 권한
        val cameraFlag = ContextCompat.checkSelfPermission(
            requireContext(), android.Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
        if(cameraFlag){
            /**
             * 카메라 촬영 ...
             */
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            launcher.launch(cameraIntent)
            // TODO.. 카메라2나 카메라 X로 기능 이전 필요...!
        }else {
            AppUtil.toast(requireContext(), getString(R.string.msg_request_camera_auth))
        }
    }

    private fun saveCapturedPhoto(bitmap:Bitmap) {
        /**
         * 파일 이름 저장
         */
        if(mTaskDTO != null){
            val resolver = requireActivity().contentResolver
            // 비트맵을 저장할 경로 생성
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, mFileName)
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.WIDTH, bitmap.width)
                put(MediaStore.Images.Media.HEIGHT, bitmap.height)
            }
            // val picturePath = Environment.DIRECTORY_PICTURES
            // val pictureUri = Uri.parse(picturePath) // 이건 안되는듯...
            // MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            this.mImageUri = imageUri
            imageUri?.let {
                resolver.openOutputStream(it)?.use { outputStream ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                    outputStream.flush()
                }
            }

            // 파일 생성 후 미디어 스캐닝 기능
            Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
                imageUri?.path.let { path ->
                    val f = File(path)
                    mediaScanIntent.data = Uri.fromFile(f)
                    requireActivity().sendBroadcast(mediaScanIntent)
                }
            }
        }
   }

    private val viewClickListener = object : ViewClickListener {
        override fun onViewClick(view: View) {
            when(view.id){
                R.id.iv_back -> {
                    val activity = requireActivity()
                    activity.setResult(Activity.RESULT_CANCELED)
                    activity.finish()
                }
                // 액션 버튼
                R.id.tv_action -> { showCompleteDialog() }
                // 촬영
                R.id.cv_photo -> { showCamera() }
                // 재촬영
                R.id.btn_recertification -> { showCamera() }
                R.id.l_btn_delete -> { showDeleteDialog() }
                R.id.l_btn_modify -> {
                    // 수정 액티비티 붑~
                    val modifyFragment = BoardModifyFragment()
                    modifyFragment.arguments = mBundle
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.board_fragment_host, modifyFragment)
                        .addToBackStack(null) // 백 스택에 추가하여 뒤로 가기를 지원
                        .commit()
                }
            }
        }
    }

    // 목표 완료 경고 알림창
    private fun showCompleteDialog(){
        val context = requireContext()
        val warningDto = WarningDTO(
            title = context.resources.getString(R.string.title_complete_todo),
            content = context.resources.getString(R.string.txt_guide_complete_todo),
            textCancel = context.resources.getString(R.string.txt_cancel),
            textAction = context.resources.getString(R.string.txt_complete)
        )
        val dialog = WarningDialog(
            warningDto = warningDto,
            listener = object : WarningDialog.OnDialogClickListener {
                override fun onActionClick(view: View) {
                    Timber.i("dto : %s", mTaskDTO)
                    if(mTaskDTO != null){
                        // 파일 저장
                        if(acceptedBitmap != null){
                            try {
                                val bitmap = acceptedBitmap!!
                                saveCapturedPhoto(bitmap)
                            }catch (e:Exception){
                                Timber.e("[ERROR] : %s", e.message)
                            }
                        }
                        mTaskDTO?.taskComplete = Date()
                        mTaskDTO?.taskCertification = mImageUri?.toString()
                        // 완료 날짜!
                        val mTaskInfo = parseToTaskInfo(mTaskDTO)

                        if(mTaskInfo != null){
                            lifecycleScope.launch(Dispatchers.IO){
                                taskInfoViewModel.modifyTaskInfo(mTaskInfo)
                                /**
                                 *  점수 계산 로직
                                 */
                                val mDate = Date()
                                // mTaskInfo
                                var mPoint = taskViewModel.getTaskPoint(mDate, mTaskInfo.fkTaskId)
                                val isFirst = taskPointViewModel.checkIsFirst(mDate)
                                if(isFirst) mPoint += 300 // 첫 등록시 300 점 추가
                                // 점수 엔티티 생성
                                val mTaskPoint = getTaskPoint(mPoint, mTaskInfo)
                                val pointId = taskPointViewModel.addTaskPoint(mTaskPoint)
                                Timber.d("point id .. %s", pointId)
                                //
                                val msg = context.resources.getString(R.string.msg_todo_complete)
                                mTaskDTO?.taskCertification = mImageUri?.toString()
                                mBinding.setVariable(BR.mTaskDTO, mTaskDTO)
                                mBinding.setVariable(BR.isCertified, true)
                                mBinding.notifyChange()
                                showToast(msg)
                                requireActivity().setResult(Activity.RESULT_OK)
                                requireActivity().finish()
                            }
                        }
                    }
                }
            })
        dialog.show(requireActivity().supportFragmentManager, null)
    }

    // 점수 모델 생성 매서드
    private fun getTaskPoint(mPoint:Int, taskInfo:TaskInfo):TaskPoint {
        return TaskPoint(
            fkTaskId = taskInfo.fkTaskId,
            point = mPoint
        )
    }

    private fun parseToTaskInfo(dto:TaskDTO?): TaskInfo? {
        return if(dto != null) {
            TaskInfo(
                infoId = dto.taskInfoId,
                fkTaskId = dto.taskId,
                taskTitle = dto.taskTitle,
                taskMemo = dto.taskMemo,
                taskLimit = dto.taskLimit,
                taskComplete = dto.taskComplete,
                taskCertification = dto.taskCertification
            )
        }else null
    }

    
    // 삭제 경고 알림창
    private fun showDeleteDialog(){
        val context = requireContext()
        val warningDto = WarningDTO(
            title = context.resources.getString(R.string.title_remove_todo),
            content = context.resources.getString(R.string.txt_guide_remove_todo),
            textCancel = context.resources.getString(R.string.txt_cancel),
            textAction = context.resources.getString(R.string.txt_delete)
        )
        val dialog = WarningDialog(
            warningDto = warningDto,
            listener = object : WarningDialog.OnDialogClickListener {
                override fun onActionClick(view: View) {
                    Timber.i("dto : %s", mTaskDTO)
                    if(mTaskDTO != null){
                        val mTaskId = mTaskDTO?.taskId ?: -1
                        if(mTaskId > 0){
                            val msg = context.resources.getString(R.string.msg_delete_todo)
                            lifecycleScope.launch(Dispatchers.Main) {
                                taskRegisterViewModel.removeTaskRegisterById(mTaskId)
                                showToast(msg)
                                requireActivity().setResult(Activity.RESULT_OK)
                                requireActivity().finish()

                            }
                        }
                    }
                }
            })
        dialog.show(requireActivity().supportFragmentManager, null)
    }

    // 확장함수를 통한 코루틴 토스트 메세지 호출
    private fun BoardReadFragment.showToast(message: String) {
        // 토스트 메시지를 노출합니다.
        lifecycleScope.launch {
            val context = requireContext()
            AppUtil.toast(context, message)
        }
    }
}