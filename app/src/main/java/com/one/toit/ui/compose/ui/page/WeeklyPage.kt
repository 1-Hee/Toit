package com.one.toit.ui.compose.ui.page

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
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
import com.one.toit.data.dto.TaskCounter
import com.one.toit.data.viewmodel.TaskViewModel
import com.one.toit.ui.compose.style.black
import com.one.toit.ui.compose.style.blue50
import com.one.toit.ui.compose.style.blue500
import com.one.toit.ui.compose.style.green100
import com.one.toit.ui.compose.style.green700
import com.one.toit.ui.compose.style.mono300
import com.one.toit.ui.compose.style.mono400
import com.one.toit.ui.compose.style.mono500
import com.one.toit.ui.compose.style.mono600
import com.one.toit.ui.compose.style.purple200
import com.one.toit.ui.compose.style.purple400
import com.one.toit.ui.compose.style.white
import com.one.toit.ui.compose.ui.unit.graph.RippleLoadingAnimation
import com.one.toit.ui.compose.ui.unit.graph.BarGraphChart
import com.one.toit.ui.compose.ui.unit.graph.LineGraphChart
import com.one.toit.util.AppUtil.Statistics
import com.one.toit.util.AppUtil.Time
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Date
import kotlin.math.max

@Composable
fun WeeklyPage(
    navController: NavHostController,
    taskViewModel: TaskViewModel,
    launcher: ActivityResultLauncher<Intent>? = null
){

    val mTaskCountList = remember { mutableStateOf<List<TaskCounter>>(listOf()) } // 주간 통계 데이터
    var mMaxValue by remember { mutableStateOf(100) }
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO){
            val mDate = Date()
            val mList = taskViewModel.getWeeklyCounterList(mDate);
            mTaskCountList.value  = mList
        }
    }
    val weeklyData by remember { mutableStateOf(mutableMapOf<String, Number>()) }
    mTaskCountList.value.forEach { item ->
        val volume = item.completeTask;
        val mDate = Time.getSimpleDateLog(item.date)
        weeklyData[mDate] = volume;
        mMaxValue = max(mMaxValue, item.completeTask)
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
            BarGraphChart(
                data = weeklyData,
                durationMillis = 700
            )
        }else {
            LineGraphChart(
                data = weeklyData,
                durationMillis = 700,
                isDaily = true
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
        Spacer(modifier = Modifier.height(48.dp))
        // 주간 목표 달성율
        val titleWeekRatio = stringResource(R.string.title_weekly_ratio)
        val txtGuideWeeklyRatio = stringResource(R.string.txt_guide_weekly_ratio)
        val weekAvgRatio = Statistics.getWeeklyRatio(mTaskCountList.value)// 주간 값으로 계산!
        BubblePieUnit(
            titleString = titleWeekRatio,
            guideText = txtGuideWeeklyRatio,
            weekRatio = weekAvgRatio,
        )

        // 일일 평균 목표 달성율
        val titleAvgDailyRatio = stringResource(R.string.title_average_daily_ratio)
        val txtGuideAvgDailyRatio = stringResource(R.string.txt_guide_average_daily_ratio)
        // 그래프
        val weekAvgDailyRatio = Statistics.getWeeklyDailyAvg(mTaskCountList.value)
        BubblePieUnit(
            titleString = titleAvgDailyRatio,
            guideText = txtGuideAvgDailyRatio,
            weekRatio = weekAvgDailyRatio,
            circleColor = green700,
            bgColor = green100
        )
        // 하단 여백
        Spacer(modifier = Modifier.height(32.dp))
    }
}


@Composable
fun BubblePieUnit(
    titleString:String = "",
    guideText:String = "",
    weekRatio:Float = 1f,
    circleColor: Color = blue500,
    bgColor:Color = blue50,
    animationDelay: Int = 1500,
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 타이틀
        Text(
            text = titleString,
            modifier = Modifier
                //.fillMaxWidth()
                .wrapContentHeight()
                .padding(vertical = 8.dp),
            style = MaterialTheme.typography.caption
                .copy(
                    fontSize = 16.sp,
                    color = black,
                ),
        )

        // 설명
        Text(
            text = guideText,
            modifier = Modifier
                //.fillMaxWidth()
                .wrapContentHeight(),
            style = MaterialTheme.typography.caption
                .copy(
                    fontSize = 12.sp,
                    color = mono600,
                ),
        )
        val displayPercentage = "%.2f%%".format(weekRatio * 100)
        Spacer(modifier = Modifier.height(16.dp))
        RippleLoadingAnimation(
            modifier = Modifier.size(156.dp),
            circleColor = circleColor,
            bgColor = bgColor,
            animationDelay = animationDelay,
            value = displayPercentage
        )
        Spacer(modifier = Modifier.height(32.dp))
    }
}