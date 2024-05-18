package com.one.toit.ui.compose.ui.page

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.one.toit.R
import com.one.toit.data.dto.ChartEntry
import com.one.toit.data.dto.TaskDTO
//import com.one.toit.data.dto.TaskDTO
import com.one.toit.data.viewmodel.TaskViewModel
import com.one.toit.ui.compose.style.black
import com.one.toit.ui.compose.style.mono600
import com.one.toit.ui.compose.style.white
import com.one.toit.ui.compose.ui.unit.graph.LineGraphChart
import com.one.toit.ui.compose.ui.unit.graph.TodayAchieveUnit
import com.one.toit.ui.compose.ui.unit.todo.ItemTodo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.Date

// @Preview(showBackground = true)
@Composable
fun DailyPage(
    navController: NavHostController,
    taskViewModel: TaskViewModel,
    launcher: ActivityResultLauncher<Intent>? = null
){
    val baseDate by remember { mutableStateOf(Date()) }
    val context = LocalContext.current
    // 할일 목록 리스트를 위한 State
    val lazyListState = rememberLazyListState()
    // 일일 taskList
    var dailyTaskList = remember { mutableStateListOf<TaskDTO>() }
    // 페이징 변수
    var pgNo by remember { mutableIntStateOf(1) }
    // 오늘 taskValue 값!
    var totalCnt by remember { mutableIntStateOf(0) }
    var completeCnt by remember { mutableIntStateOf(0) }

    var refreshing by remember { mutableStateOf(false) }

    LaunchedEffect(refreshing){
        withContext(Dispatchers.IO) {
            val mDailyList = taskViewModel.readRemainTaskListByDate(pgNo, baseDate)
            val mDTOList = mutableListOf<TaskDTO>()
            mDailyList.forEach { taskItem ->
                val mTaskDTO = TaskDTO(
                    taskId = taskItem.register.taskId,
                    taskInfoId = taskItem.info.infoId,
                    createAt = taskItem.register.createAt,
                    taskTitle = taskItem.info.taskTitle,
                    taskMemo = taskItem.info.taskMemo,
                    taskLimit = taskItem.info.taskLimit,
                    taskComplete = taskItem.info.taskComplete,
                    taskCertification = taskItem.info.taskCertification
                )
                //Timber.i("[item] : $mTaskDTO")
                mDTOList.add(mTaskDTO)
            }
            dailyTaskList.clear()
            dailyTaskList.addAll(mDTOList.toList())
            // total cnt !
            totalCnt = taskViewModel.getTaskCntByDate(baseDate);
            completeCnt = taskViewModel.getCompleteTaskCnt(baseDate);
        }
    }
    // 페이징을 위한 비동기 리캄포지션
    LaunchedEffect(pgNo) {
        if(pgNo>1){
            withContext(Dispatchers.IO) {
                val mDailyList = taskViewModel.readRemainTaskListByDate(pgNo, baseDate)
                val mDTOList = mutableListOf<TaskDTO>()
                mDailyList.forEach { taskItem ->
                    val mTaskDTO = TaskDTO(
                        taskId = taskItem.register.taskId,
                        taskInfoId = taskItem.info.infoId,
                        createAt = taskItem.register.createAt,
                        taskTitle = taskItem.info.taskTitle,
                        taskMemo = taskItem.info.taskMemo,
                        taskLimit = taskItem.info.taskLimit,
                        taskComplete = taskItem.info.taskComplete,
                        taskCertification = taskItem.info.taskCertification
                    )
                    //Timber.i("[item] : $mTaskDTO")
                    mDTOList.add(mTaskDTO)
                }
                dailyTaskList.addAll(mDTOList.toList());
            }
        }
    }

    // taskDTOListState를 사용하여 UI 업데이트
//    val taskDTOList = taskDTOListState.value
    // 현재 높이를 저장하기 위한 상태 변수
    var deviceHeight by remember { mutableStateOf(0) }
    // LocalDensity를 사용하여 현재 디바이스의 화면 밀도를 가져옴
    val density = LocalDensity.current.density
    // 현재 높이를 구하는 코드
    LaunchedEffect(Unit) {
        val displayMetrics = context.resources.displayMetrics
        deviceHeight = (displayMetrics.heightPixels / density).toInt()
    }
    val outerScrollState = rememberScrollState()
    val innerScrollState = rememberScrollState()

    /**
     * 시간대에 따른 추이 계싼해서 렌더링하는 메서드
     */
    // 차트 맵 변수
    var achieveMap by remember { mutableStateOf(mutableMapOf<String, Number>()) }
    // step1. 오늘의 할일 개수 전체를 셈함.
    LaunchedEffect(Unit) {
        withContext(Dispatchers.Main){
            val totalCnt = taskViewModel.getTaskCntByDate(baseDate);
            val mAchieveMap = taskViewModel.getAchievementMap(context, baseDate, totalCnt)
            achieveMap = mAchieveMap
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(outerScrollState)
            .background(white)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringResource(R.string.title_daily_todo_graph),
            style = MaterialTheme.typography.subtitle1
                .copy(
                    fontSize = 16.sp,
                    color = black
                ),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        )
        LineGraphChart(
            data = achieveMap,
            durationMillis = 700
        )
        Text(
            text = stringResource(R.string.txt_guide_daily_todo_graph),
            style = MaterialTheme.typography.subtitle1
                .copy(
                    fontSize = 11.sp,
                    color = mono600,
                    textAlign = TextAlign.End
                ),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(vertical = 4.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        TodayAchieveUnit(completeCnt, totalCnt)
        Spacer(modifier = Modifier.height(12.dp))
        // content
        // 등록한 List가 있을 경우
        if(dailyTaskList.isNotEmpty()){
            Text(
                text = stringResource(id = R.string.header_todo_list),
                style = MaterialTheme.typography.subtitle1
                    .copy(
                        fontSize = 16.sp,
                        color = black
                    ),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            )
            Spacer(modifier = Modifier.height(24.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height((deviceHeight * 0.7).dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Spacer(modifier = Modifier.height(4.dp))
                // content
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
                        val hasNext = taskViewModel.hasNextItem(pgNo+1, baseDate)
                        if (reachedBottom && hasNext) {
                            pgNo++
                        }
                    }
                }
                LazyColumn(state = lazyListState) {
                    items(dailyTaskList) { item ->
                        // Main items content
                        Spacer(modifier = Modifier.height(4.dp))
                        ItemTodo(taskDTO = item, launcher = launcher){
                            refreshing = true
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}