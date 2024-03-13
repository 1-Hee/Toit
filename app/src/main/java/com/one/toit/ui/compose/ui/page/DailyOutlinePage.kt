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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.one.toit.R
import com.one.toit.data.dto.ChartEntry
import com.one.toit.data.dto.TaskDTO
import com.one.toit.data.viewmodel.TaskViewModel
import com.one.toit.ui.compose.style.black
import com.one.toit.ui.compose.style.mono600
import com.one.toit.ui.compose.style.white
import com.one.toit.ui.compose.ui.unit.graph.LineGraphChart
import com.one.toit.ui.compose.ui.unit.graph.TodayAchieveUnit
import com.one.toit.ui.compose.ui.unit.todo.ItemTodo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.Calendar
import java.util.Date

// @Preview(showBackground = true)
@Composable
fun DailyOutlinePage(
    navController: NavHostController,
    taskViewModel: TaskViewModel,
    launcher: ActivityResultLauncher<Intent>? = null
){
    val currentDate = Date()
    val context = LocalContext.current
    // MutableState를 사용하여 taskDTOList를 감싸기
    val taskDTOListState = remember { mutableStateOf<List<TaskDTO>>(emptyList()) }
    // 오늘 taskValue 값!
    var totalCnt by remember { mutableIntStateOf(0) }
    var completeCnt by remember { mutableIntStateOf(0) }
    LaunchedEffect(Unit) {
        withContext(Dispatchers.Main) {
            val taskList = taskViewModel.readTaskListByDate(currentDate)
            Timber.i("[할일 목록] : %s", taskList)
            // 데이터 변화를 감지하기 위해 MutableState를 업데이트
            taskDTOListState.value = taskList.map { task ->
                TaskDTO(
                    task.register.taskId,
                    task.register.createAt.toString(),
                    task.info.infoId,
                    task.info.taskTitle,
                    task.info.taskMemo,
//                    task.info.taskLimit,
//                    task.info.taskComplete,
                    task.info.taskCertification
                )
            }
            val date = Date()
            totalCnt = taskViewModel.getAllTodayTaskCount(date)
            completeCnt = taskViewModel.getCompleteTodayTaskCount(date)
            Timber.i("total : %s | complete : %s", totalCnt, completeCnt)
        }
    }
    // taskDTOListState를 사용하여 UI 업데이트
    val taskDTOList = taskDTOListState.value
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
    // dummy
    val test = mutableListOf<String>()
    val date = Date()
    val calendar = Calendar.getInstance()
    calendar.time = date
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    for(i in 0..hour step 2){
        val hourUnit = if(i > 9){
            "$i:00"
        }else {
            "0$i:00"
        }
        test.add(hourUnit)
    }

    val testData by remember { mutableStateOf(mutableMapOf<String, ChartEntry>()) }
    val testList = mutableListOf<Float>()
    var volume = 0f
    val step = (100/(test.size-1))
    test.forEach { date ->
        testList.add(volume)
        testData[date] = ChartEntry(volume.toInt())
        volume += step
    }
    // dummy!
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
            data = testData,
            durationMillis = 700,
            maxValue = 100
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
        if(taskDTOList.isNotEmpty()){
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
                    .height((deviceHeight * 0.7).dp)
                    .verticalScroll(innerScrollState)
                ,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Spacer(modifier = Modifier.height(4.dp))
                repeat(taskDTOList.size){
                    ItemTodo(taskDTO = taskDTOList[it], launcher = launcher)
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}