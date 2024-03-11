package com.one.toit.ui.compose.ui.unit.statistics

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.one.toit.R
import com.one.toit.ui.compose.style.black
import com.one.toit.ui.compose.style.mono600
import com.one.toit.ui.compose.style.mono700
import com.one.toit.ui.compose.style.purple300
import com.one.toit.ui.compose.style.white
import com.one.toit.ui.compose.ui.unit.ToitPointCard
import com.one.toit.util.PreferenceUtil
import java.util.Calendar
import java.util.Date

// 전체 통계 대쉬보드
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TotalSummaryUnit(

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

    // 스크롤 컴포저블 state
    val outerState = rememberScrollState()
    val lazyListState: LazyListState = rememberLazyListState()

    // 가장 외곽
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(outerState),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        // 일자
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringResource(R.string.txt_statistics_date, mYear, mMonth),
            style = MaterialTheme.typography.caption.copy(
                color = black,
                fontSize = 16.sp
            ),
        )
        Spacer(modifier = Modifier.height(24.dp))
        ToitPointCard(toitPoint = 123456)
        Spacer(modifier = Modifier.height(24.dp))
        // 일자
        Row(
            modifier = Modifier
                .wrapContentSize(),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ){
            // 이름
            Text(
                text = userNickname,
                style = MaterialTheme.typography.caption.copy(
                    color = purple300,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            // suffix
            Text(
                text = stringResource(R.string.txt_h_monthly_report),
                style = MaterialTheme.typography.caption.copy(
                    color = mono600,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
        // temp
        // todo 실제 데이터로...
        val dataList = listOf<StatisticsData>(
            // 전체 목표 수
            StatisticsData(
                stringResource(R.string.txt_h_total_todo), 
                "${1234} ${stringResource(id = R.string.txt_unit)}",
                0 // 개수
            ),
            // 평균 목표 수
            StatisticsData(
                stringResource(R.string.txt_h_avg_todo), 
                "${13.5f} ${stringResource(id = R.string.txt_unit)}",
                0 // 개수
            ),
            // n월 누적 할일 목록
            StatisticsData(
                stringResource(R.string.txt_h_cumulative_todo, mMonth),
                "${1234} ${stringResource(id = R.string.txt_unit)}",
                0 // 개수
            ),
            // 최장 기록
            // todo 실제 값으로...
            StatisticsData(
                stringResource(R.string.txt_h_todo_max),
                "12:34",
                1 // 시간
            ),
            // 최단 기록
            StatisticsData(
                stringResource(R.string.txt_h_todo_min),
                "12:34",
                1 // 시간
            ),
            // 평균 기록
            StatisticsData(
                stringResource(R.string.txt_h_todo_avg),
                "12:34",
                1 // 시간
            )
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
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
    val value:String,
    val type:Int = 0
)

@Composable
fun StatisticsCard(
    data:StatisticsData,
    date:Date = Date()
){


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(96.dp),
        backgroundColor = white,
        elevation = 4.dp,
        contentColor = black,
        shape = RoundedCornerShape(8.dp),
    ) {
        Box(modifier = Modifier
            .fillMaxSize()){
            // 헤더
            Text(
                text = data.key,
                style = MaterialTheme.typography.caption.copy(
                    color = black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(12.dp)
            )
            // 값
           Row(
               modifier = Modifier
                   .wrapContentSize()
                   .align(Alignment.BottomEnd)
                   .padding(12.dp),
               horizontalArrangement = Arrangement.spacedBy(8.dp),
               verticalAlignment = Alignment.CenterVertically
           ) {
               val icon = when(data.type){
                   0 -> painterResource(id = R.drawable.ic_statistics)
                   1 -> painterResource(id = R.drawable.ic_time)
                   else -> painterResource(id = R.drawable.ic_create_todo)
               }
               Icon(
                   painter = icon,
                   contentDescription =  "icon",
                   modifier = Modifier
                       .size(24.dp),
                   tint = purple300
               )
               Text(
                   text = data.value,
                   style = MaterialTheme.typography.caption.copy(
                       color = mono600,
                       fontSize = 20.sp,
                       fontWeight = FontWeight.Medium
                   ),
               )
           }
        }
    }

}
