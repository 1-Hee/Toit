package com.one.toit.ui.compose.ui.unit.statistics

import android.content.Context
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.one.toit.R
//import com.one.toit.data.dto.TaskDTO
import com.one.toit.ui.compose.style.black
import com.one.toit.ui.compose.style.mono300
import com.one.toit.ui.compose.style.mono900
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * 정렬 선택상자 컴포저블
 */
//@Composable
//fun sortOption(
//    context: Context,
//    taskDTOList:List<TaskDTO>
//):List<TaskDTO>{
//    var expanded by remember { mutableStateOf(false) }
//    val options = context.resources.getStringArray(R.array.option_todo_sort)
//    var selectedOption by remember { mutableStateOf(options[0]) }
//    var mTaskDTOList by remember { mutableStateOf(taskDTOList) }
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .wrapContentHeight()
//            .padding(vertical = 8.dp),
//        horizontalAlignment = Alignment.End,
//    ){
//        Row(
//            modifier = Modifier
//                .wrapContentSize(),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.spacedBy(8.dp)
//        ){
//            Text(
//                text = stringResource(R.string.txt_sort_standard),
//                style = MaterialTheme.typography.caption.copy(
//                    color = black,
//                    fontSize = 14.sp,
//                ),
//            )
//            Row(
//                modifier = Modifier
//                    .width(128.dp)
//                    .height(32.dp)
//                    .padding(horizontal = 12.dp)
//                    .border(
//                        width = 1.dp,
//                        color = mono300,
//                        shape = getBottomLineShape(1.dp)
//                    )
//                    .clickable {
//                        expanded = true
//                    },
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.SpaceBetween
//            ){
//                Text(
//                    text = selectedOption,
//                    style = MaterialTheme.typography.caption.copy(
//                        color = mono900,
//                        fontSize = 14.sp,
//                    ),
//                )
//                Icon(
//                    imageVector = Icons.Default.ArrowDropDown,
//                    contentDescription = null,
//                )
//            }
//        }
//
//        DropdownMenu(
//            expanded = expanded,
//            onDismissRequest = {
//                expanded = false
//            },
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            options.forEachIndexed { index, option ->
//                DropdownMenuItem(
//                    onClick = {
//                        selectedOption = option
//                        expanded = false
//                        mTaskDTOList = sortList(index, mTaskDTOList)
//                    },
//                ) {
//                    Text(
//                        text = option,
//                    )
//                }
//            }
//        }
//    }
//    return mTaskDTOList
//}
//
//// 정렬 함수
//private fun sortList(sIndex:Int, list: List<TaskDTO>): List<TaskDTO> {
//    // todo..  완료 문자열도 포멧 통일하기...
//    // SimpleDateFormat을 사용하여 문자열을 Date로 파싱
//    val inputFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss 'GMT'Z yyyy", Locale.ENGLISH)
//    val taskList:List<TaskDTO> = when(sIndex){
//        1 -> {  // 이름순
//            list.sortedBy { it.taskTitle }
//        }
//        2 -> { // 최신순
//            list.sortedByDescending {
//                val createAt = inputFormat.parse(it.createAt)
//                createAt?.time
//            }
//        }
//        3 -> { // 과거 순
//            list.sortedBy {
//                val createAt = inputFormat.parse(it.createAt)
//                createAt?.time
//            }
//        }
//        4 -> { // 목표 상태
//            list.sortedByDescending { it.taskComplete }
//        }
//        5 -> { // 시간 짧은 순
//            list.sortedBy {
//                val mValue = if(it.taskComplete == null){
//                    Long.MAX_VALUE
//                }else {
//                    val createAt = inputFormat.parse(it.createAt)
//                    val mInputFormat = SimpleDateFormat("yyyy년 MM월 dd일 HH:mm:ss", Locale.getDefault())
//                    val completeAt = mInputFormat.parse(it.taskComplete)
//                    (completeAt?.time ?: 0) - (createAt?.time ?: 0)
//                }
//                mValue
//            }
//        }
//        6 -> { // 시간 긴 순
//            list.sortedBy {
//                val mValue = if(it.taskComplete == null){
//                    Long.MAX_VALUE
//                }else {
//                    val createAt = inputFormat.parse(it.createAt)
//                    val mInputFormat = SimpleDateFormat("yyyy년 MM월 dd일 HH:mm:ss", Locale.getDefault())
//                    val completeAt = mInputFormat.parse(it.taskComplete)
//                    (createAt?.time ?: 0) - (completeAt?.time ?: 0)
//                }
//                mValue
//            }
//        }
//        else -> list
//    }
//    return taskList
//}


// 바닥 선만 그려주는 메서드
@Composable
private fun getBottomLineShape(lineThicknessDp: Dp) : Shape {
    val lineThicknessPx = with(LocalDensity.current) {lineThicknessDp.toPx()}
    return GenericShape { size, _ ->
        // 1) Bottom-left corner
        moveTo(0f, size.height)
        // 2) Bottom-right corner
        lineTo(size.width, size.height)
        // 3) Top-right corner
        lineTo(size.width, size.height - lineThicknessPx)
        // 4) Top-left corner
        lineTo(0f, size.height - lineThicknessPx)
    }
}