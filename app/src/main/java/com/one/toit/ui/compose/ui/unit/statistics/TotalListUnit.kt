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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.one.toit.data.dto.TaskDTO
import com.one.toit.data.model.Task
import com.one.toit.data.viewmodel.TaskViewModel
import com.one.toit.ui.compose.ui.unit.todo.ItemNoContent
import com.one.toit.ui.compose.ui.unit.todo.ItemTodo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.Date

@Composable
fun TotalListUnit(
    context: Context,
    taskViewModel: TaskViewModel,
    launcher: ActivityResultLauncher<Intent>? = null
){

    val mTaskDTOList = remember { mutableStateListOf<TaskDTO>() }
    val lazyListState: LazyListState = rememberLazyListState()
    var searchedKeyword by remember { mutableStateOf("") }
    // 페이징 변수
    var pgNo by remember { mutableIntStateOf(1) }

    var refreshing by remember { mutableStateOf(false) }
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = refreshing)
    val scope = rememberCoroutineScope()
    // 정렬 옵션
    var orderIdx by remember { mutableIntStateOf(0) }

    // 초기화
    LaunchedEffect(refreshing, searchedKeyword, orderIdx){
        withContext(Dispatchers.Main) {
            pgNo = 1
            val mTaskList = if(searchedKeyword.isNotBlank()){
                taskViewModel.readSortedListWithQuery(pgNo, searchedKeyword, orderIdx)
            }else {
                taskViewModel.readSortedTaskList(pgNo, orderIdx)
            }
            val mDtoList = mutableListOf<TaskDTO>()
            mTaskList.forEach { task ->
                val mTaskDTO = parseTaskDTO(task);
                mDtoList.add(mTaskDTO);
            }
            mTaskDTOList.clear();
            mTaskDTOList.addAll(mDtoList);
            scope.launch {
                delay(500)
                refreshing = false
            }
        }
    }

//    // 검색어 감지
//    LaunchedEffect(searchedKeyword) {
//        withContext(Dispatchers.IO){
//            pgNo = 1
//            val mTaskList =  if(searchedKeyword.isNotBlank()){
//                taskViewModel.readTaskListByQuery(pgNo, searchedKeyword)
//            }else {
//              taskViewModel.readTaskList(pgNo)
//            }
//            val mDtoList = mutableListOf<TaskDTO>()
//            mTaskList.forEach { task ->
//                val mTaskDTO = parseTaskDTO(task);
//                mDtoList.add(mTaskDTO);
//            }
//            mTaskDTOList.clear();
//            mTaskDTOList.addAll(mDtoList);
//        }
//    }

    // 페이징 감지
    LaunchedEffect(pgNo > 1) {
        withContext(Dispatchers.IO){
            if(pgNo > 1){
                val mTaskList =  if(searchedKeyword.isNotBlank()){
                    taskViewModel.readSortedListWithQuery(pgNo, searchedKeyword, orderIdx)
                }else {
                    taskViewModel.readSortedTaskList(pgNo, orderIdx)
                }
                val mDtoList = mutableListOf<TaskDTO>()
                mTaskList.forEach { task ->
                    val mTaskDTO = parseTaskDTO(task);
                    mDtoList.add(mTaskDTO);
                }
                mTaskDTOList.addAll(mDtoList);
                scope.launch {
                    delay(500)
                    refreshing = false
                }
            }
        }
    }
    SearchUnit(
        onSearch = { keyword -> searchedKeyword = keyword
            Timber.d("search keyword is >> %s", keyword)
        },
        onDelete = {
            searchedKeyword = ""
        }
    )



    SortUnit(context){
        orderIdx = it
        //refreshing = true
    }

    if(mTaskDTOList.isEmpty()){
        ItemNoContent();
    } else {
        // observe list scrolling
        val reachedBottom: Boolean by remember {
            derivedStateOf {
                val lastVisibleItem = lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()
                lastVisibleItem?.index == lazyListState.layoutInfo.totalItemsCount - 1
            }
        }

        // load more if scrolled to bottom
        LaunchedEffect(reachedBottom) {
            withContext(Dispatchers.Main){
                val hasNext = taskViewModel.hasNextData(pgNo+1, searchedKeyword)
                if (reachedBottom && hasNext) {
                    pgNo++
                }
            }
        }
        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = {
                // 새로고침 로직을 구현합니다. 예를 들어, 네트워크 요청을 보냅니다.
                refreshing = true
                // Simulate a network request
                scope.launch {
                    delay(1000) // Simulate network delay
                    // Update your list here
                    refreshing = false // Indicate that the refresh has finished
                }
            },
        ) {
            LazyColumn(state = lazyListState) {
                items(mTaskDTOList) { item ->
                    Spacer(modifier = Modifier.height(4.dp))
                    ItemTodo(taskDTO = item, launcher = launcher){
                        refreshing = true
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

private fun parseTaskDTO(task: Task):TaskDTO{
    val taskId: Long = task.register.taskId
    val taskInfoId:Long = task.info.infoId
    val createAt: Date = task.register.createAt
    val taskTitle: String = task.info.taskTitle
    val taskMemo: String = task.info.taskMemo
    val taskLimit: Date? = task.info.taskLimit
    val taskComplete: Date? = task.info.taskComplete
    val taskCertification: String? = task.info.taskCertification
    return  TaskDTO(
        taskId, taskInfoId, createAt, taskTitle,
        taskMemo, taskLimit, taskComplete,
        taskCertification
    )
}
