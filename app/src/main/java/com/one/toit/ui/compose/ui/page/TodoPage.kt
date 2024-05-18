package com.one.toit.ui.compose.ui.page

//import com.one.toit.data.dto.TaskDTO
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Create
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import com.one.toit.data.dto.TaskDTO
import com.one.toit.data.viewmodel.TaskViewModel
import com.one.toit.ui.activity.BoardActivity
import com.one.toit.ui.compose.style.black
import com.one.toit.ui.compose.style.mono200
import com.one.toit.ui.compose.style.mono300
import com.one.toit.ui.compose.style.mono600
import com.one.toit.ui.compose.style.purple200
import com.one.toit.ui.compose.style.white
import com.one.toit.ui.compose.ui.unit.todo.ItemNoContent
import com.one.toit.ui.compose.ui.unit.todo.ItemTodo
import com.one.toit.util.PreferenceUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.Date
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/**
 *  TODO DB쪽과의 시간 차이로 인해서 렌더링 이슈가 살짝 있음
 */

@Composable
fun TodoPage(
    navController: NavHostController,
    taskViewModel: TaskViewModel,
    launcher: ActivityResultLauncher<Intent>? = null
){
    // 환경 변수들
    val context = LocalContext.current
    val intent = Intent(context, BoardActivity::class.java)
    // prefs 로 체크 상태 영속관리
    val prefs = PreferenceUtil.getInstance(context)
    val savedFlag = if(prefs.getValue("isShowAll").isNotBlank()){
        prefs.getValue("isShowAll").toBoolean()
    }else false;
    // for 토글 버튼
    var checked by remember { mutableStateOf(savedFlag) }
    val optionText by remember { mutableStateOf("달성한 목표 숨기기") }

    // 페이징 변수
    var pgNo by remember { mutableIntStateOf(1) }

    // 상태 리프레쉬 + 초기 여부 판단
    var refreshing by remember { mutableStateOf(false) }
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = refreshing)
    val scope = rememberCoroutineScope()

    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleState by lifecycleOwner.lifecycle.currentStateFlow.collectAsState()
    // 컴포저블의 라이프 사이클 관리를 위한 메서드
    LaunchedEffect(lifecycleState) {
        // Do something with your state
        // You may want to use DisposableEffect or other alternatives
        // instead of LaunchedEffect
        when (lifecycleState) {
            Lifecycle.State.DESTROYED -> {}
            Lifecycle.State.STARTED,
            Lifecycle.State.CREATED -> {
                refreshing = true
            }
            Lifecycle.State.RESUMED -> {
                refreshing = true
            }
            else -> {}
        }
    }

    // 할일 목록 리스트를 위한 State
    val lazyListState = rememberLazyListState()
    // 일일 taskList
    var dailyTaskList = remember { mutableStateListOf<TaskDTO>() }
    val baseDate = Date()
    // 초기 리스트 렌더링
    LaunchedEffect(checked, refreshing){
        refreshing = false
        
        withContext(Dispatchers.IO) {
            pgNo = 1
            val mDTOList = loadTaskDTOList(
                true, checked, pgNo, baseDate, taskViewModel
            )
            dailyTaskList.clear()
            dailyTaskList.addAll(mDTOList)
        }
    }


    // 페이징을 위한 비동기 리캄포지션
    LaunchedEffect(pgNo) {
        if(pgNo>1){
            withContext(Dispatchers.IO) {
                val mDailyList  = if(checked){ // 초기화 되고, 체크가 되었을 경우에만!
                    taskViewModel.readRemainTaskListByDate(pgNo, baseDate)
                }else {
                    taskViewModel.readTaskListByDate(pgNo, baseDate)
                }
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
                            prefs.setValue("isShowAll", it.toString());
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
                    val hasNext = taskViewModel.hasNextItem(pgNo+1, baseDate, !checked)
                    if (reachedBottom && hasNext) {
                        pgNo++
                    }
                }
            }
            if(dailyTaskList.isEmpty()){
                ItemNoContent()
            }else {
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
                        items(dailyTaskList) { item ->
                            // Main items content
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
                Icons.Rounded.Create,
                contentDescription = "버튼 아이콘",
                modifier = Modifier
                    .width(26.dp)
                    .height(26.dp),
                tint = white
            )
        }
    }
}
private suspend fun loadTaskDTOList(
    isInitState:Boolean, checked:Boolean,
    pgNo:Int, baseDate: Date, taskViewModel: TaskViewModel
):List<TaskDTO>{
    val mDailyList  = if(isInitState && checked){ // 초기화 되고, 체크가 되었을 경우에만!
        taskViewModel.readRemainTaskListByDate(pgNo, baseDate)
    }else {
        taskViewModel.readTaskListByDate(pgNo, baseDate)
    }
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
    return mDTOList
}
