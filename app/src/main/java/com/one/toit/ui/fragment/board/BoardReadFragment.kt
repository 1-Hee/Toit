package com.one.toit.ui.fragment.board

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.one.toit.BR
import com.one.toit.R
import com.one.toit.base.bind.DataBindingConfig
import com.one.toit.base.listener.ViewClickListener
import com.one.toit.base.ui.BaseFragment
import com.one.toit.data.dto.TaskDTO
import com.one.toit.data.dto.WarningDTO
import com.one.toit.databinding.FragmentBoardReadBinding
import com.one.toit.ui.dialog.WarningDialog
import com.one.toit.util.AppUtil
import timber.log.Timber

class BoardReadFragment : BaseFragment<FragmentBoardReadBinding>() {
    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.fragment_board_read)
            .addBindingParam(BR.click, viewClickListener)
            .addBindingParam(BR.taskDTO, TaskDTO())
            .addBindingParam(BR.deadLineString, "제한 없음")
    }

    override fun initViewModel() {
    }

    override fun initView() {
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

        // 번들 참조
        arguments?.let { bundle ->
            Timber.i("[READ FRAGMENT] %s", bundle)
            @Suppress("DEPRECATION")
            val mTaskDTO = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getParcelable("taskDTO", TaskDTO::class.java)
            } else {
                bundle.getParcelable("taskDTO")
            }
            Timber.e("[READ FRAGMENT DTO] %s", mTaskDTO)
            mBinding.setVariable(BR.taskDTO, mTaskDTO)
            val deadLineString = if(mTaskDTO?.taskLimit?.isNotBlank()==true){
                mTaskDTO.taskLimit
            }else {
                "제한 없음"
            }
            mBinding.setVariable(BR.deadLineString, deadLineString)
        }

        mBinding.notifyChange()

    }
    // 다른 액티비티 이동후 결과 값을 받아 핸들링할 런쳐
    private val launcher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { // 액티비티 종료시 결과릴 리턴받기 위한 콜백 함수
        if (it.resultCode == Activity.RESULT_OK) {

            Timber.i("data : %s", it.data)
            val photo: Bitmap? = it.data?.extras?.get("data") as Bitmap?
            Timber.i("photo .. %s %s %s", photo?.width, photo?.height, photo?.density)
            // TODO 어떻게 할지..?
            // step 1. 이 사진 자체를 파일로 저장해야 한다.
            // step 2. 찍은 사진의 주소를 저장해두어야 한다...!
            /*
            val intent = result.data
            val resultState = intent?.getStringExtra("newAlbumName")
            Timber.i("resultState : %s", resultState)
             */
        }
    }

    private val viewClickListener = object : ViewClickListener {
        override fun onViewClick(view: View) {
            when(view.id){
                R.id.cv_photo -> {
                    // Toast.makeText(requireContext(), "카메라...", Toast.LENGTH_SHORT).show()
                    // 카메라 권한
                    val cameraFlag = ContextCompat.checkSelfPermission(
                        requireContext(), android.Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_GRANTED
                    if(cameraFlag){
                        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        launcher.launch(cameraIntent)
                    }else {
                        AppUtil.toast(requireContext(), "카메라를 열 수 없습니다. [권한]을 허용해주세요.")
                    }
                }
                R.id.l_btn_delete -> {
                    val warningDto = WarningDTO(
                        title = "목표 삭제하기",
                        content = "정말로  등록하신 목표를 삭제 하시겠어요?\n[삭제]를 누르시면 회원님의 목표가 삭제됩니다.",
                        textCancel = "취소",
                        textAction = "삭제"
                    )
                    val dialog = WarningDialog(warningDto = warningDto, listener = dialogListener)
                    dialog.show(requireActivity().supportFragmentManager, null)
                }
                R.id.l_btn_modify -> {
                    // 수정 액티비티 붑~
                    val modifyFragment = BoardModifyFragment()
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.board_fragment_host, modifyFragment)
                        .addToBackStack(null) // 백 스택에 추가하여 뒤로 가기를 지원
                        .commit()
                }
            }
        }
    }

    // 다이얼로그 리스너
    private val dialogListener = object : WarningDialog.OnDialogClickListener {
        override fun onActionClick(view: View) {
            Toast.makeText(requireContext(), "목표가 삭제되었습니다.", Toast.LENGTH_SHORT).show()
            requireActivity().setResult(Activity.RESULT_OK)
            requireActivity().finish()
        }
    }
}