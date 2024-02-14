package com.one.toit.ui.compose.ui.page

import android.content.Intent
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController
import com.one.toit.R
import com.one.toit.data.dto.TaskDTO
import com.one.toit.data.model.TaskRegister
import com.one.toit.ui.activity.BoardActivity
import com.one.toit.ui.compose.style.black
import com.one.toit.ui.compose.style.mono200
import com.one.toit.ui.compose.style.mono300
import com.one.toit.ui.compose.style.mono600
import com.one.toit.ui.compose.style.purple200
import com.one.toit.ui.compose.style.white
import com.one.toit.ui.compose.ui.unit.todo.ItemNoContent
import com.one.toit.ui.compose.ui.unit.todo.ItemTodo
import com.one.toit.ui.viewmodel.TaskInfoViewModel
import com.one.toit.ui.viewmodel.TaskRegisterViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

@Composable
fun TodoPage(
    navController: NavHostController,
    taskRegisterViewModel: TaskRegisterViewModel,
    taskInfoViewModel: TaskInfoViewModel
){
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val intent = Intent(context, BoardActivity::class.java)
    var checked by remember { mutableStateOf(false) }
    val optionText by remember { mutableStateOf("달성한 목표 숨기기") }
    var hasContent by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    // MutableState로 선언된 리스트
    var mTaskMap by remember { mutableStateOf<Map<Long, TaskDTO>>(mutableMapOf()) }
    var mTaskDTOList by remember { mutableStateOf<List<TaskDTO>>(mutableListOf()) }
    // LaunchedEffect를 통해 비동기 작업을 수행하고 결과를 MutableState에 반영
    LaunchedEffect(Unit) {
        withContext(Dispatchers.Main) {
            taskRegisterViewModel.readTaskRegisterList().observe(lifecycleOwner){ list ->
                val newTaskMap = mTaskMap.toMutableMap()
                list.forEach { register ->
                    val dto = TaskDTO(
                        taskId = register.taskId,
                        createAt = register.createAt
                    )
                    newTaskMap[dto.taskId] = dto
                    mTaskMap = newTaskMap
                }
            }
        }
    }

    Timber.i("mTaskMap..!!!! %s", mTaskMap)


    if(mTaskMap.keys.isNotEmpty()){
        LaunchedEffect(Unit){
            withContext(Dispatchers.Main){
                Timber.i("keyList.. %s", mTaskMap.keys)
                val updatedTaskDTOMap = mTaskMap.toMutableMap()
                val updateList = mutableListOf<TaskDTO>()
                mTaskMap.keys.forEach { key ->
                    taskInfoViewModel.readTaskInfoListWithTaskId(key).observe(lifecycleOwner){
                        val mTaskDTO = updatedTaskDTOMap[it.fkTaskId]
                        Timber.i("맵 확인!!! %s", mTaskDTO)
                        Timber.i("인포 확인..! %s", it)
                        mTaskDTO?.taskTitle = it.taskTitle
                        mTaskDTO?.taskMemo = it.taskMemo
                        mTaskDTO?.taskLimit = it.taskLimit
                        mTaskDTO?.taskComplete = it.taskComplete
                        mTaskDTO?.taskCertification = it.taskCertification
                        if (mTaskDTO != null) {
                            updatedTaskDTOMap[mTaskDTO.taskId] = mTaskDTO
                            updateList.add(mTaskDTO)
                            Timber.i("updated taskDTO..!!!!! %s", mTaskDTO)
                        }
                    }
                    mTaskMap = updatedTaskDTOMap
                    mTaskDTOList = updateList
                    Timber.i("taskMap..!!!!! %s", mTaskMap)
                }
            }
        }
        Timber.i("if mTaskDTOList!!!! %s", mTaskDTOList)
    }

    Timber.i("mTaskDTOList!!!! %s", mTaskDTOList)



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
                            hasContent = it
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
            if(hasContent){
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Spacer(modifier = Modifier.height(4.dp))
                    repeat(12){
                        ItemTodo()
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
                intent.putExtra("pageIndex", 0)
                context.startActivity(intent)
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