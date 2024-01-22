package com.one.toit.ui.fragment.board

import android.app.Activity
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.one.toit.BR
import com.one.toit.R
import com.one.toit.base.bind.DataBindingConfig
import com.one.toit.base.listener.ViewClickListener
import com.one.toit.base.ui.BaseFragment
import com.one.toit.data.dto.WarningDTO
import com.one.toit.databinding.FragmentBoardReadBinding
import com.one.toit.ui.dialog.WarningDialog

class BoardReadFragment : BaseFragment<FragmentBoardReadBinding>() {
    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.fragment_board_read)
            .addBindingParam(BR.click, viewClickListener)
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
        mBinding.notifyChange()

    }

    private val viewClickListener = object : ViewClickListener {
        override fun onViewClick(view: View) {
            when(view.id){
                R.id.cv_photo -> {
                    Toast.makeText(requireContext(), "카메라...", Toast.LENGTH_SHORT).show()
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