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
import androidx.compose.runtime.getValue
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

    var mTaskDTOList = remember { mutableStateListOf<TaskDTO>() }
    val lazyListState: LazyListState = rememberLazyListState()
    var searchedKeyword by remember { mutableStateOf("") }

    LaunchedEffect(Unit){
        withContext(Dispatchers.Main) {
            val mTaskList = taskViewModel.readTaskList();
            val mDtoList = mutableListOf<TaskDTO>()
            mTaskList.forEach { task ->
                val mTaskDTO = parseTaskDTO(task);
                mDtoList.add(mTaskDTO);
            }
            mTaskDTOList.clear();
            mTaskDTOList.addAll(mDtoList);
        }
    }

    LaunchedEffect(searchedKeyword) {
        withContext(Dispatchers.IO){
            if(searchedKeyword.isNotBlank()){
                val mTaskList = taskViewModel.readTaskListByQuery(searchedKeyword)
                val mDtoList = mutableListOf<TaskDTO>()
                mTaskList.forEach { task ->
                    val mTaskDTO = parseTaskDTO(task);
                    mDtoList.add(mTaskDTO);
                }
                mTaskDTOList.clear();
                mTaskDTOList.addAll(mDtoList);
            }else {
                val mTaskList = taskViewModel.readTaskList()
                val mDtoList = mutableListOf<TaskDTO>()
                mTaskList.forEach { task ->
                    val mTaskDTO = parseTaskDTO(task);
                    mDtoList.add(mTaskDTO);
                }
                mTaskDTOList.clear();
                mTaskDTOList.addAll(mDtoList);
            }
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
