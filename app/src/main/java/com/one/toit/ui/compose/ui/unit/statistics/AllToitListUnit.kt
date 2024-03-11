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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.one.toit.data.dto.TaskDTO
import com.one.toit.data.viewmodel.TaskViewModel
import com.one.toit.ui.compose.ui.unit.todo.ItemNoContent
import com.one.toit.ui.compose.ui.unit.todo.ItemTodo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

@Composable
fun AllToitListUnit(
    context: Context,
    taskViewModel: TaskViewModel,
    launcher: ActivityResultLauncher<Intent>? = null
){
    // 페이지 계수 관리를 위한 mutable state
    var pageIndex by remember { mutableIntStateOf(1) } // 페이지 계수
    var maxCnt by remember { mutableIntStateOf(0) }
    var mTaskDTOList by remember { mutableStateOf(mutableListOf<TaskDTO>()) }
    val lazyListState: LazyListState = rememberLazyListState()
    var searchedKeyword by remember { mutableStateOf("") }

    LaunchedEffect(Unit){
        withContext(Dispatchers.Main) {
            maxCnt = taskViewModel.getAllTaskCnt()
        }
    }

    LaunchedEffect(pageIndex, searchedKeyword) {
        withContext(Dispatchers.Main) {
            val taskList = if(searchedKeyword.isBlank()){
                taskViewModel.readTaskList(pageIndex)
            }else {
                val searchedList = taskViewModel.readTaskListByQuery(pageIndex, searchedKeyword)
                Timber.i("items :%s", searchedList)
                searchedList
            }
            val parsedTaskDTOList = mutableListOf<TaskDTO>()
            taskList.map { task ->
                val dto = TaskDTO(
                    task.register.taskId,
                    task.register.createAt.toString(),
                    task.info.infoId,
                    task.info.taskTitle,
                    task.info.taskMemo,
                    task.info.taskLimit,
                    task.info.taskComplete,
                    task.info.taskCertification
                )
                parsedTaskDTOList.add(dto)
            }
            mTaskDTOList = if(pageIndex == 1){
                Timber.i("첫 로드...")
                parsedTaskDTOList
            }else {
                val tempList = mTaskDTOList
                tempList.addAll(parsedTaskDTOList)
                tempList
            }

        }
    }
    SearchUnit(){ query ->
        if(query.isNotBlank()){
            pageIndex = 1
        }
        searchedKeyword = query
        Timber.i("searchedKeyword : %s", searchedKeyword)
    }
    if(mTaskDTOList.isNotEmpty()){
        mTaskDTOList = sortOption(context, mTaskDTOList).toMutableList()
    }

    // observe list scrolling
    val reachedBottom: Boolean by remember {
        derivedStateOf {
            val lastVisibleItem = lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()
            lastVisibleItem?.index != 0 && lastVisibleItem?.index == lazyListState.layoutInfo.totalItemsCount - 1
        }
    }
    // load more if scrolled to bottom
    LaunchedEffect(reachedBottom) {
        if (reachedBottom) {
            if(maxCnt > mTaskDTOList.size){
                Timber.i("스크롤 할 수 있음 ....")
                pageIndex++
            }
        }
    }
    if(mTaskDTOList.isNotEmpty()){
        // display our list
        LazyColumn(state = lazyListState) {
            items(mTaskDTOList) { item ->
                // Main items content
                Spacer(modifier = Modifier.height(4.dp))
                ItemTodo(taskDTO = item, launcher = launcher)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }else {
        ItemNoContent()
    }
}
