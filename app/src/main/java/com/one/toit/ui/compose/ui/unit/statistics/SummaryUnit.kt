package com.one.toit.ui.compose.ui.unit.statistics

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.one.toit.R
import com.one.toit.data.viewmodel.TaskPointViewModel
import com.one.toit.data.viewmodel.TaskViewModel
import com.one.toit.ui.compose.style.black
import com.one.toit.ui.compose.style.mono100
import com.one.toit.ui.compose.style.mono50
import com.one.toit.ui.compose.style.mono600
import com.one.toit.ui.compose.style.orange300
import com.one.toit.ui.compose.style.red300
import com.one.toit.ui.compose.style.white
import com.one.toit.ui.compose.ui.unit.ToitPointCard
import com.one.toit.util.PreferenceUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Date
import kotlin.math.roundToInt

// 전체 통계 대쉬보드
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SummaryUnit(
    taskViewModel: TaskViewModel,
    taskPointViewModel: TaskPointViewModel
){
    // 오늘 날짜 정보 init
    val date = Date();
    val calendar = Calendar.getInstance()
    calendar.time = date;
    // 시간 정보 init
    val mYear = calendar.get(Calendar.YEAR)
    val mMonth = calendar.get(Calendar.MONTH)+1;
    val mDay = calendar.get(Calendar.DAY_OF_MONTH)
    val mHour = calendar.get(Calendar.HOUR)
    val mMin = calendar.get(Calendar.MINUTE)
    val mSec = calendar.get(Calendar.SECOND)

    // 환경 정보
    val context = LocalContext.current
    val prefs = PreferenceUtil.getInstance(context)
    val nickNameKey = stringResource(id = R.string.key_nickname)
    var userNickname by remember { mutableStateOf(prefs.getValue(nickNameKey)) } // 사용자 닉네임

    /**
     * 표시할 통계 정보
     */
    // Toit 점수
    var mToitPoint by remember { mutableStateOf<Long>(0) }
    // 전체 목표 수
    val mTaskTotalCnt = remember { mutableStateOf<Long>(0) }
    // 평균 목표 수
    val mTaskCntAvg = remember { mutableStateOf(0f) }
    // 월간 목표 수
    val mMonthCnt = remember { mutableStateOf(0L) }
    // 최장 기록
    val mMaxTime = remember { mutableStateOf(0L) }
    // 최단 기록
    val mMinTime = remember { mutableStateOf(0L) }
    // 평균 기록
    val mAvgTime = remember { mutableStateOf(0f) }

    // 스크롤 컴포저블 state
    val outerState = rememberScrollState()
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO){
            // toit 점수
            mToitPoint = taskPointViewModel.getToitPoint()
            // 전체 목표 수
            mTaskTotalCnt.value = taskViewModel.getAllTaskCnt()
            // 평균 목표 수
            mTaskCntAvg.value = taskViewModel.getAvgTaskCnt()
            // 월간 목표 수
            mMonthCnt.value = taskViewModel.getMonthTaskCnt(date)
            // 최장 기록
            mMaxTime.value = taskViewModel.getMaxTaskTime()
            // 최단 기록
            mMinTime.value = taskViewModel.getMinTaskTime()
            // 평균 기록
            mAvgTime.value = taskViewModel.getAvgTaskTime()

        }
    }


    // 가장 외곽
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(outerState),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        // 일자
        Spacer(modifier = Modifier.height(36.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ToitPointCard(
                toitPoint = mToitPoint
            )
        }
        Spacer(modifier = Modifier.height(48.dp))

        val dataList = listOf<StatisticsData>(
            // 전체 목표 수
            StatisticsData(
                stringResource(R.string.txt_h_total_todo),
                stringResource(R.string.txt_desc_total_todo, userNickname),
                mTaskTotalCnt.value,
                0 // 개수
            ),
            // 평균 목표 수
            StatisticsData(
                stringResource(R.string.txt_h_avg_todo),
                stringResource(R.string.txt_desc_avg_todo, userNickname),
                mTaskCntAvg.value,
                0 // 개수
            ),
            // n월 누적 할일 목록
            StatisticsData(
                stringResource(R.string.txt_h_cumulative_todo, mMonth),
                stringResource(R.string.txt_desc_cumulative_todo, mMonth),
                mMonthCnt.value,
                0 // 개수
            ),
            // 최장 기록
            StatisticsData(
                stringResource(R.string.txt_h_todo_max),
                stringResource(R.string.txt_desc_todo_max, userNickname),
                mMaxTime.value,
                1 // 시간
            ),
            // 최단 기록
            StatisticsData(
                stringResource(R.string.txt_h_todo_min),
                stringResource(R.string.txt_desc_todo_min, userNickname),
                mMinTime.value,
                1 // 시간
            ),
            // 평균 기록
            StatisticsData(
                stringResource(R.string.txt_h_todo_avg),
                stringResource(R.string.txt_desc_todo_avg, userNickname),
                mAvgTime.value,
                1 // 시간
            )
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            repeat(dataList.size){
                val item = dataList[it]
                StatisticsCard(item)
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}

data class StatisticsData(
    val key: String,
    val desc:String,
    val value:Number,
    val type:Int = 0
)

@Composable
fun StatisticsCard(
    data:StatisticsData,
    date:Date = Date(),
    durationMillis:Int = 2000
){

    var animationPlayed by remember { mutableStateOf(false) }

    val animateSize by animateFloatAsState(
        targetValue = if (animationPlayed) 1f else 0f,
        animationSpec =  spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = ""
    )

    val infiniteTransition = rememberInfiniteTransition(label = "")
    val animationRotate by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation =  tween(
                durationMillis = durationMillis,
                delayMillis = 0,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "alpha"
    )
    val bgColorList = listOf(mono50, white)
    val circleColorList = listOf(mono100, red300, orange300)
    val bgBrush = Brush.linearGradient(bgColorList)
    val circleBrush = Brush.linearGradient(circleColorList)

    LaunchedEffect(key1 = true) {
        animationPlayed = true
    }

    val chartBarWidth = 4.dp

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(96.dp),
        backgroundColor = white,
        elevation = 2.dp,
        contentColor = black,
        shape = RoundedCornerShape(8.dp),
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(bgBrush)
            .padding(horizontal = 12.dp, vertical = 8.dp)
        ){
            // 헤더
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.CenterStart),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = data.key,
                    style = MaterialTheme.typography.caption.copy(
                        color = black,
                        fontSize = 16.sp,
                    ),
                )
                Text(
                    text = data.desc,
                    style = MaterialTheme.typography.caption.copy(
                        color = mono600,
                        fontSize = 8.sp,
                    ),
                )
            }

            Box(modifier = Modifier
                .wrapContentSize()
                .align(Alignment.CenterEnd)
            ){
                Canvas(
                    modifier = Modifier
                        .size(72.dp)
                        .rotate(animationRotate)
                        .align(Alignment.Center)
                ) {
                    drawArc(
                        brush = circleBrush,
                        0f,
                        360f,
                        useCenter = false,
                        style = Stroke(chartBarWidth.toPx(), cap = StrokeCap.Butt)
                    )
                }
                // 값
                Row(
                    modifier = Modifier
                        .wrapContentSize()
                        .align(Alignment.Center)
                        //.padding(12.dp)
                    , horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    var dpStr = ""
                    val icon = when(data.type){
                        1 -> {
                            val sec = data.value.toFloat().roundToInt()
                            val min = (sec / 60) % 60;
                            val hour = (sec / 60) / 60;
                            dpStr = if(hour + min > 0){
                                String.format("%02d", hour) + ":" + String.format("%02d", min);
                            }else {
                                stringResource(R.string.txt_no_record)
                            }
                            painterResource(id = R.drawable.ic_time)
                        }
                        else -> {
                            val dpValue = if(data.value is Int){
                                data.value
                            }else {
                                (data.value.toFloat() * 10).roundToInt() / 10.0
                            }
                            dpStr = "$dpValue "+ stringResource(id = R.string.txt_unit)
                            null
                        }
                    }
                    Text(
                        text = dpStr,
                        style = MaterialTheme.typography.caption.copy(
                            color = mono600,
                            fontSize = 14.sp * animateSize,
                            fontWeight = FontWeight.Medium
                        ),
                        modifier = Modifier
                            .wrapContentSize()
                    )
                    if(icon != null){
                        Icon(
                            painter = icon,
                            contentDescription =  "icon",
                            modifier = Modifier
                                .size(12.dp),
                            tint = mono600
                        )
                    }
                }
            }
        }
    }
}
