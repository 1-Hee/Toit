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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import com.one.toit.ui.compose.style.black
import com.one.toit.ui.compose.style.mono400
import com.one.toit.ui.compose.style.mono600
import com.one.toit.ui.compose.style.navy400
import com.one.toit.ui.compose.style.orange300
import com.one.toit.ui.compose.style.purple300
import com.one.toit.ui.compose.style.red300
import com.one.toit.ui.compose.style.white
import com.one.toit.ui.compose.ui.unit.graph.BarGraphChart
import com.one.toit.ui.compose.ui.unit.graph.LineGraphChart
import com.one.toit.ui.compose.ui.unit.graph.TodayAchieveUnit
import com.one.toit.ui.compose.ui.unit.todo.ItemTodo
import com.one.toit.data.viewmodel.TaskViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import kotlin.random.Random

// @Preview(showBackground = true)
@Composable
fun WeeklyPage(
    navController: NavHostController,
    taskViewModel: TaskViewModel,
    launcher: ActivityResultLauncher<Intent>? = null
){
    val context = LocalContext.current
    // MutableState를 사용하여 taskDTOList를 감싸기
    val taskDTOListState = remember { mutableStateOf<List<TaskDTO>>(emptyList()) }
    LaunchedEffect(Unit) {
        withContext(Dispatchers.Main) {
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
    val test = listOf(
        "9/1","9/2","9/3","9/4","9/5",
        "9/6","9/7","9/8","9/9","9/10",
        "9/10","9/11","9/12","9/13","9/14",
    )
    val colorList = listOf(
        black, red300, orange300, navy400, purple300
    )
    val testData by remember { mutableStateOf(mutableMapOf<String, ChartEntry>()) }
    val testList = mutableListOf<Float>()
    test.forEach { date ->
        /*
         val randomNumberInRange = Random.nextInt(1, 100)
         */
        val randIdx = Random.nextInt(0, 4)
        val volume = Random.nextInt(32,  128)
        testList.add(volume.toFloat())
        testData[date] = ChartEntry(volume, colorList[randIdx])
    }
    // dummy!
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(white)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(outerScrollState)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            var graphMode by remember { mutableStateOf("막대 그래프") }
            var isCheck by remember { mutableStateOf(false) }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ){
                Text(
                    text = graphMode,
                    style = MaterialTheme.typography.caption.copy(
                        color = mono600,
                        fontSize = 8.sp
                    ),
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.width(16.dp))
                Switch(
                    checked = isCheck,
                    onCheckedChange = {
                        isCheck = it
                        graphMode = if(isCheck) "꺾은선 그래프" else "막대 그래프"
                    }
                )
            }
            if(!isCheck){
                LaunchedEffect(testData){}
                BarGraphChart(
                    data = testData,
                    durationMillis = 700,
                    maxValue = 172,
                )
            }else {
                LineGraphChart(
                    data = testData,
                    durationMillis = 700,
                    maxValue = 172
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(id = R.string.txt_guide_graph),
                style = MaterialTheme.typography.caption
                    .copy(
                        fontSize = 8.sp,
                        color = mono400,
                        textAlign = TextAlign.End
                    ),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            )
            Spacer(modifier = Modifier.height(24.dp))
            TodayAchieveUnit(5, 10)
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
                        .height((deviceHeight - 128).dp)
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
}