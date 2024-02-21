package com.one.toit.ui.compose.ui.page

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.one.toit.R
import com.one.toit.data.dto.TaskDTO
import com.one.toit.ui.activity.BoardActivity
import com.one.toit.ui.compose.style.black
import com.one.toit.ui.compose.style.mono200
import com.one.toit.ui.compose.style.mono300
import com.one.toit.ui.compose.style.mono600
import com.one.toit.ui.compose.style.purple200
import com.one.toit.ui.compose.style.white
import com.one.toit.ui.compose.ui.unit.todo.ItemNoContent
import com.one.toit.ui.compose.ui.unit.todo.ItemTodo
import com.one.toit.data.viewmodel.TaskViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 *  TODO DB쪽과의 시간 차이로 인해서 렌더링 이슈가 살짝 있음
 */

@Composable
fun TodoPage(
    navController: NavHostController,
    taskViewModel: TaskViewModel,
    launcher: ActivityResultLauncher<Intent>? = null
){
    // 다른 액티비티 이동후 결과 값을 받아 핸들링할 런쳐
    val context = LocalContext.current
    val intent = Intent(context, BoardActivity::class.java)
    var checked by remember { mutableStateOf(false) }
    val optionText by remember { mutableStateOf("달성한 목표 숨기기") }
    val scrollState = rememberScrollState()

    // MutableState를 사용하여 taskDTOList를 감싸기
    val taskDTOListState = remember { mutableStateOf<List<TaskDTO>>(emptyList()) }
    LaunchedEffect(checked) {
        withContext(Dispatchers.Main) {
            Timber.i("토글 상태 : %s", checked)
            if(checked){
                // TODO dao 수정해서 오늘 날짜 기준의 todo만 불러오도록...
                val taskList = taskViewModel.readNotCompleteTaskList()
                Timber.i("[할일 목록 (필터) ] : %s", taskList)
                // 데이터 변화를 감지하기 위해 MutableState를 업데이트
                taskDTOListState.value = taskList.map { task ->
                    TaskDTO(
                        task.register.taskId,
                        task.register.createAt.toString(),
                        task.info.infoId,
                        task.info.taskTitle,
                        task.info.taskMemo,
                        task.info.taskLimit,
                        task.info.taskComplete,
                        task.info.taskCertification
                    )
                }
            }else {
                val taskList = taskViewModel.readTaskList()
                Timber.i("[할일 목록] : %s", taskList)
                // 데이터 변화를 감지하기 위해 MutableState를 업데이트
                taskDTOListState.value = taskList.map { task ->
                    TaskDTO(
                        task.register.taskId,
                        task.register.createAt.toString(),
                        task.info.infoId,
                        task.info.taskTitle,
                        task.info.taskMemo,
                        task.info.taskLimit,
                        task.info.taskComplete,
                        task.info.taskCertification
                    )
                }
            }
        }
    }
    // taskDTOListState를 사용하여 UI 업데이트
    val taskDTOList = taskDTOListState.value
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(white)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        // content list
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            //  상단 정렬 옵션 토글
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(32.dp)){
                Row(modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.CenterEnd)
                    .padding(end = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ){
                    Text(
                        text = optionText,
                        style = MaterialTheme.typography.subtitle1.copy(
                            fontSize = 14.sp,
                            color = black
                        ),
                        modifier = Modifier
                            .wrapContentSize()
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Switch(
                        checked = checked,
                        onCheckedChange = {
                            checked = it
                        },
                        modifier = Modifier
                            .wrapContentWidth()
                            .height(24.dp),
                        colors  = SwitchDefaults.colors(
                            checkedThumbColor = purple200,
                            checkedTrackColor = mono300,
                            uncheckedThumbColor = mono600,
                            uncheckedTrackColor= mono200,
                        ),
                    )
                }
            }
            // content
            // 등록한 List가 있을 경우
            if(taskDTOList.isNotEmpty()){
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Spacer(modifier = Modifier.height(4.dp))
                    repeat(taskDTOList.size){
                        ItemTodo(taskDTO = taskDTOList[it], launcher = launcher)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }else {
                ItemNoContent()
            }
        }
        // 플로팅 버튼
        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            shape = MaterialTheme.shapes.small.copy(CornerSize(percent = 50)),
            backgroundColor = purple200,
            contentColor = contentColorFor(white),
            elevation = FloatingActionButtonDefaults.elevation(4.dp),
            onClick = {
                // TODO 이쪽에 콜백으로 바꿔서, 리컴포지션 일어나게 하기
                intent.putExtra("pageIndex", 0)
                launcher?.launch(intent)
            }
        ){
            Icon(
                painter = painterResource(id = R.drawable.ic_create_todo),
                contentDescription = "버튼 아이콘",
                modifier = Modifier
                    .width(26.dp)
                    .height(26.dp),
                tint = white
            )
        }
    }
}