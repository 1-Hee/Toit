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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.SwitchColors
import androidx.compose.material.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.one.toit.R
import com.one.toit.data.dto.ChartEntry
import com.one.toit.data.viewmodel.TaskViewModel
import com.one.toit.ui.compose.style.black
import com.one.toit.ui.compose.style.mono100
import com.one.toit.ui.compose.style.mono200
import com.one.toit.ui.compose.style.mono300
import com.one.toit.ui.compose.style.mono400
import com.one.toit.ui.compose.style.mono50
import com.one.toit.ui.compose.style.mono500
import com.one.toit.ui.compose.style.mono600
import com.one.toit.ui.compose.style.navy400
import com.one.toit.ui.compose.style.orange300
import com.one.toit.ui.compose.style.purple200
import com.one.toit.ui.compose.style.purple300
import com.one.toit.ui.compose.style.purple400
import com.one.toit.ui.compose.style.purple50
import com.one.toit.ui.compose.style.red300
import com.one.toit.ui.compose.style.white
import com.one.toit.ui.compose.ui.unit.graph.BarGraphChart
import com.one.toit.ui.compose.ui.unit.graph.LineGraphChart
import com.one.toit.ui.compose.ui.unit.graph.PackedPieChart
import com.one.toit.ui.compose.ui.unit.graph.PackedPieChartEntry
import com.one.toit.ui.compose.ui.unit.graph.PerforatedPieChart
import kotlin.math.ceil
import kotlin.math.round
import kotlin.random.Random

@Composable
fun WeeklyPage(
    navController: NavHostController,
    taskViewModel: TaskViewModel,
    launcher: ActivityResultLauncher<Intent>? = null
){
    // dummy
    val test = listOf(
        "9/1","9/2","9/3","9/4","9/5",
        "9/6","9/7",
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

    /// scroll state
    val outerScrollState = rememberScrollState()
    // dummy!
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(outerScrollState)
            .background(white)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        val txtBarGraph = stringResource(R.string.txt_bar_graph)
        val txtLineGraph = stringResource(R.string.txt_line_graph)
        var graphMode by remember { mutableStateOf(txtBarGraph) }
        var isCheck by remember { mutableStateOf(false) }
        val switchStyle: SwitchColors = SwitchDefaults.colors(
            checkedThumbColor = purple400,
            checkedTrackColor = purple200,
            uncheckedThumbColor = mono500,
            uncheckedTrackColor = mono300
        )
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
                    graphMode = if(isCheck) txtLineGraph else txtBarGraph
                },
                colors = switchStyle
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
        // 주간 목표 달성율
        val titleWeekRatio = stringResource(R.string.title_weekly_ratio)
        val txtGuideWeeklyRatio = stringResource(R.string.txt_guide_weekly_ratio)
        // 그래프
        val weekAvgRatio = 0.854f // TODO 실제 계산한 값으로..
        PackedPieWeekUnit(
            titleString = titleWeekRatio,
            guideText = txtGuideWeeklyRatio,
            weekRatio = weekAvgRatio
        )
        // 일일 평균 목표 달성율
        val titleAvgDailyRatio = stringResource(R.string.title_average_daily_ratio)
        val txtGuideAvgDailyRatio = stringResource(R.string.txt_guide_average_daily_ratio)
        // 그래프
        val weekAvgDailyRatio = 0.754f // TODO 실제 계산한 값으로..
        PackedPieWeekUnit(
            titleString = titleAvgDailyRatio,
            guideText = txtGuideAvgDailyRatio,
            weekRatio = weekAvgDailyRatio
        )
        // 하단 여백
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun PackedPieWeekUnit(
    titleString:String = "",
    guideText:String = "",
    weekRatio:Float = 1f,
    completeColor:Color = purple200,
    noneColor:Color = mono200
){
    // 일일 평균 목표 달성율
    Text(
        text = titleString,
        style = MaterialTheme.typography.caption
            .copy(
                fontSize = 16.sp,
                color = black,
            ),
        modifier = Modifier
            .wrapContentSize()
            .padding(vertical = 8.dp)
    )
    Text(
        text = guideText,
        style = MaterialTheme.typography.caption
            .copy(
                fontSize = 12.sp,
                color = mono600,
            ),
    )

    val entries: List<PackedPieChartEntry> = listOf(
        PackedPieChartEntry(completeColor, weekRatio),
        PackedPieChartEntry(noneColor, 1-weekRatio)
    )
    val displayPercentage = "%.2f%%".format(weekRatio * 100)
    Spacer(modifier = Modifier.height(24.dp))
    Box(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()){
        PackedPieChart(
            modifier = Modifier.align(Alignment.Center),
            entries = entries,
            size = 156.dp,
            text = displayPercentage
        )
    }
    Spacer(modifier = Modifier.height(24.dp))
}