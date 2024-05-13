package com.one.toit.ui.compose.ui.unit.statistics

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.one.toit.data.dto.TaskDTO
import com.one.toit.data.model.Task
import com.one.toit.data.viewmodel.TaskViewModel
import com.one.toit.ui.compose.ui.unit.todo.ItemTodo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.Date

@Composable
fun AllToitListUnit(
    context: Context,
    taskViewModel: TaskViewModel,
    launcher: ActivityResultLauncher<Intent>? = null
){

    val mTaskDTOList = remember { mutableStateListOf<TaskDTO>() }
    val lazyListState: LazyListState = rememberLazyListState()
    var searchedKeyword by remember { mutableStateOf("") }
    // 페이징 변수
    var pgNo by remember { mutableIntStateOf(1) }

    // 초기화
    LaunchedEffect(Unit){
        withContext(Dispatchers.Main) {
            val mTaskList = taskViewModel.readTaskList(pgNo);
            val mDtoList = mutableListOf<TaskDTO>()
            mTaskList.forEach { task ->
                val mTaskDTO = parseTaskDTO(task);
                mDtoList.add(mTaskDTO);
            }
            mTaskDTOList.clear();
            mTaskDTOList.addAll(mDtoList);
        }
    }

    // 검색어 감지
    LaunchedEffect(searchedKeyword) {
        withContext(Dispatchers.IO){
            pgNo = 1
            val mTaskList =  if(searchedKeyword.isNotBlank()){
                taskViewModel.readTaskListByQuery(pgNo, searchedKeyword)
            }else {
              taskViewModel.readTaskList(pgNo)
            }
            val mDtoList = mutableListOf<TaskDTO>()
            mTaskList.forEach { task ->
                val mTaskDTO = parseTaskDTO(task);
                mDtoList.add(mTaskDTO);
            }
            mTaskDTOList.clear();
            mTaskDTOList.addAll(mDtoList);
        }
    }

    // 페이징 감지
    LaunchedEffect(pgNo > 1) {
        withContext(Dispatchers.IO){
            val mTaskList =  if(searchedKeyword.isNotBlank()){
                taskViewModel.readTaskListByQuery(pgNo, searchedKeyword)
            }else {
                taskViewModel.readTaskList(pgNo)
            }
            val mDtoList = mutableListOf<TaskDTO>()
            mTaskList.forEach { task ->
                val mTaskDTO = parseTaskDTO(task);
                mDtoList.add(mTaskDTO);
            }
            mTaskDTOList.addAll(mDtoList);
        }
    }

    SearchUnit(
        onSearch = {
            keyword -> searchedKeyword = keyword
            Timber.d("search keyword is >> %s", keyword)
       },
        onDelete = {
            searchedKeyword = ""
        }
    );

    if(mTaskDTOList.isNotEmpty()){
        SortUnit(context, mTaskDTOList){
            if(it.isNotEmpty()){
                mTaskDTOList.clear()
                mTaskDTOList.addAll(it.toList());
                Timber.i("첫번째 요소 >>> %s", it[0])
            }
        }
    }

    // observe list scrolling
    val reachedBottom: Boolean by remember {
        derivedStateOf {
            val lastVisibleItem = lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()
            lastVisibleItem?.index == lazyListState.layoutInfo.totalItemsCount - 1
        }
    }
//
    // load more if scrolled to bottom
    LaunchedEffect(reachedBottom) {
        withContext(Dispatchers.Main){
            val hasNext = taskViewModel.hasNextData(pgNo+1, searchedKeyword)
            if (reachedBottom && hasNext) {
                pgNo++
            }
        }
    }

    LazyColumn(state = lazyListState) {
        items(mTaskDTOList) { item ->
            Spacer(modifier = Modifier.height(4.dp))
            ItemTodo(taskDTO = item, launcher = launcher)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

private fun parseTaskDTO(task: Task):TaskDTO{
    val taskId: Long = task.register.taskId
    val taskInfoId:Long = task.info.infoId
    val createAt: Date = task.register.createAt
    var taskTitle: String = task.info.taskTitle
    var taskMemo: String = task.info.taskMemo
    var taskLimit: Date? = task.info.taskLimit
    var taskComplete: Date? = task.info.taskComplete
    var taskCertification: String? = task.info.taskCertification
    return  TaskDTO(
        taskId, taskInfoId, createAt, taskTitle,
        taskMemo, taskLimit, taskComplete,
        taskCertification
    )
}
