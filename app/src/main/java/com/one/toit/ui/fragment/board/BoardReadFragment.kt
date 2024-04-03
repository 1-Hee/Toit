package com.one.toit.ui.fragment.board

import android.app.Activity
import android.content.ContentValues
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
import androidx.camera.core.impl.Timebase
import androidx.compose.material3.TimeInput
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.one.toit.BR
import com.one.toit.R
import com.one.toit.base.bind.DataBindingConfig
import com.one.toit.base.fatory.ApplicationFactory
import com.one.toit.base.listener.ViewClickListener
import com.one.toit.base.ui.BaseFragment
//import com.one.toit.data.dto.TaskDTO
import com.one.toit.data.dto.WarningDTO
import com.one.toit.data.model.TaskInfo
import com.one.toit.databinding.FragmentBoardReadBinding
import com.one.toit.ui.dialog.WarningDialog
import com.one.toit.data.viewmodel.TaskInfoViewModel
import com.one.toit.data.viewmodel.TaskRegisterViewModel
import com.one.toit.data.viewmodel.TaskViewModel
import com.one.toit.util.AppUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import java.lang.Exception
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

class BoardReadFragment : BaseFragment<FragmentBoardReadBinding>() {
    // bundle
    private lateinit var mBundle:Bundle
    // bundle로 파싱할 dto 객체
//    private var mTaskDTO: TaskDTO? = null
    // 다른 액티비티 이동후 결과 값을 받아 핸들링할 런쳐
     private lateinit var launcher: ActivityResultLauncher<Intent>
    // vm
    private lateinit var taskRegisterViewModel: TaskRegisterViewModel
    private lateinit var taskInfoViewModel: TaskInfoViewModel
    private lateinit var taskViewModel: TaskViewModel
    // 저장된 파일 이름
    private var mFileName:String = ""
    // 카메라로부터 받은 비트맵
    private var acceptedBitmap:Bitmap? = null
    private var mImageUri:Uri? = null
    override fun getDataBindingConfig(): DataBindingConfig {
        mBundle = Bundle() // bundle init
        return DataBindingConfig(R.layout.fragment_board_read)
            .addBindingParam(BR.title, "To It! 목표 완료하기")
            .addBindingParam(BR.actionName, "완료")
            .addBindingParam(BR.click, viewClickListener)
//            .addBindingParam(BR.taskDTO, TaskDTO())
            .addBindingParam(BR.deadLineString, "제한 없음")
            .addBindingParam(BR.isCertified, false)
            .addBindingParam(BR.hasLimit, false)
    }

    override fun initViewModel() {
        val factory = ApplicationFactory(requireActivity().application)
        taskRegisterViewModel = getFragmentScopeViewModel(TaskRegisterViewModel::class.java, factory)
        taskInfoViewModel = getFragmentScopeViewModel(TaskInfoViewModel::class.java, factory)
        taskViewModel = getFragmentScopeViewModel(TaskViewModel::class.java, factory)
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
                val resolver = requireActivity().contentResolver
                mBinding.ivCapture.setImageBitmap(bitmap)
                mBinding.isCertified = true
                mBinding.notifyChange()
                // 파일명 세팅...
                val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                mFileName = "${requireContext().packageName}_${timeStamp}.jpg"
            }
        }
    }

//    private fun getTaskInfo(dto:TaskDTO):TaskInfo{
//        Timber.d("getTaskInfo ... %s", dto);
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
    private fun saveCapturedPhoto(bitmap:Bitmap) {
        /**
         * 파일 이름 저장
         */
//        if(mTaskDTO != null){
//            val resolver = requireActivity().contentResolver
//            // 비트맵을 저장할 경로 생성
//            val contentValues = ContentValues().apply {
//                put(MediaStore.Images.Media.DISPLAY_NAME, mFileName)
//                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
//                put(MediaStore.Images.Media.WIDTH, bitmap.width)
//                put(MediaStore.Images.Media.HEIGHT, bitmap.height)
//            }
//            // val picturePath = Environment.DIRECTORY_PICTURES
//            // val pictureUri = Uri.parse(picturePath) // 이건 안되는듯...
//            // MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//            val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
//            this.mImageUri = imageUri
//            imageUri?.let {
//                resolver.openOutputStream(it)?.use { outputStream ->
//                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
//                    outputStream.flush()
//                }
//            }
//
//            // 파일 생성 후 미디어 스캐닝 기능
//            Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
//                imageUri?.path.let { path ->
//                    val f = File(path)
//                    mediaScanIntent.data = Uri.fromFile(f)
//                    requireActivity().sendBroadcast(mediaScanIntent)
//                }
//            }
//        }
   }

    // 매개 변수로 받아온 인자를 파싱하는 함수
    private fun parseArguments(){
        // 번들 참조
        arguments?.let { bundle ->
            mBundle = bundle
            Timber.i("[READ FRAGMENT] %s", bundle)
//            @Suppress("DEPRECATION")
//            mTaskDTO = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//                bundle.getParcelable("taskDTO", TaskDTO::class.java)
//            } else {
//                bundle.getParcelable("taskDTO")
//            }
//            Timber.e("[READ FRAGMENT DTO] %s", mTaskDTO)
//            mBinding.setVariable(BR.taskDTO, mTaskDTO)
//            val mHasLimit = mTaskDTO?.taskLimit?.isNotBlank()==true
//            val deadLineString:String

            mBinding.setVariable(BR.deadLineString, "") // todo here
//            mBinding.setVariable(BR.hasLimit, mHasLimit)
            mBinding.setVariable(BR.hasLimit, false)
        }
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
            AppUtil.toast(requireContext(), "카메라를 열 수 없습니다. [권한]을 허용해주세요.")
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
        val warningDto = WarningDTO(
            title = "목표 완료 하기",
            content = "정말로  등록하신 목표를 완료 하시겠어요?\n[완료]를 누르시면 회원님의 목표가 완료 처리됩니다.\n완료 후에는 데이터를 수정할 수 없으니,\n다시 한번 확인해주세요. 😁",
            textCancel = "취소",
            textAction = "완료"
        )
//        val dialog = WarningDialog(
//            warningDto = warningDto,
//            listener = object : WarningDialog.OnDialogClickListener {
//                override fun onActionClick(view: View) {
//                    Timber.i("dto : %s", mTaskDTO)
//                    var msg = "목표 완료 실패..."
//                    val context = requireContext()
//                    if(mTaskDTO != null){
//                        // 파일 저장
//                        if(acceptedBitmap != null){
//                            try {
//                                val bitmap = acceptedBitmap!!
//                                saveCapturedPhoto(bitmap)
//                            }catch (e:Exception){
//                                Timber.e("[ERROR] : %s", e.message)
//                            }
//                        }
//                        mTaskDTO?.taskComplete = Date().toString()
//                        mTaskDTO?.taskCertification = mImageUri?.toString()
//                        val mTaskInfo = getTaskInfo(mTaskDTO!!)
//
//                        val modifyResult = lifecycleScope.async(Dispatchers.IO){
//                            taskInfoViewModel.modifyTaskInfo(mTaskInfo)
//                            msg = "목표가 완료되었습니다."
//                            mTaskDTO?.taskCertification = mImageUri?.toString()
//                            mBinding.setVariable(BR.taskDTO, mTaskDTO)
//                            mBinding.setVariable(BR.isCertified, true)
//                            mBinding.notifyChange()
//                        }
//                        lifecycleScope.launch {
//                            modifyResult.await()
//                            AppUtil.toast(context, msg)
//                            requireActivity().setResult(Activity.RESULT_OK)
//                            requireActivity().finish()
//                        }
//                    }
//                }
//            })
//        dialog.show(requireActivity().supportFragmentManager, null)
    }
    
    // 삭제 경고 알림창
    private fun showDeleteDialog(){
//        val warningDto = WarningDTO(
//            title = "목표 삭제하기",
//            content = "정말로  등록하신 목표를 삭제 하시겠어요?\n[삭제]를 누르시면 회원님의 목표가 삭제됩니다.",
//            textCancel = "취소",
//            textAction = "삭제"
//        )
//        val dialog = WarningDialog(
//            warningDto = warningDto,
//            listener = object : WarningDialog.OnDialogClickListener {
//                override fun onActionClick(view: View) {
//                    Timber.i("dto : %s", mTaskDTO)
//                    var msg = "목표 삭제 실패..."
//                    val context = requireContext()
//                    if(mTaskDTO != null){
//                        val mTaskId = mTaskDTO?.taskId ?: -1
//                        if(mTaskId > 0){
//                            msg = "목표가 삭제되었습니다."
//                            taskRegisterViewModel.removeTaskRegisterById(mTaskId)
//                        }
//                    }
//                    AppUtil.toast(context, msg)
//                    requireActivity().setResult(Activity.RESULT_OK)
//                    requireActivity().finish()
//                }
//            })
//        dialog.show(requireActivity().supportFragmentManager, null)
    }
    
}