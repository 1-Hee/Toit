package com.one.toit.ui.compose.ui.page

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.one.toit.R
import com.one.toit.data.dto.ChartEntry
import com.one.toit.data.viewmodel.TaskViewModel
import com.one.toit.ui.activity.StatisticsActivity
import com.one.toit.ui.compose.style.black
import com.one.toit.ui.compose.style.mono100
import com.one.toit.ui.compose.style.mono300
import com.one.toit.ui.compose.style.none
import com.one.toit.ui.compose.style.purple200
import com.one.toit.ui.compose.style.purple400
import com.one.toit.ui.compose.style.white
import com.one.toit.ui.compose.ui.unit.graph.PerforatedPieChart
import com.patrykandpatrick.vico.compose.component.shapeComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.Date

// @Preview(showBackground = true)
@Composable
fun NavStatisticsPage(
    navController : NavHostController,
    taskViewModel: TaskViewModel,
    launcher: ActivityResultLauncher<Intent>? = null

){
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val intent = Intent(context, StatisticsActivity::class.java)
    // 오늘 taskValue 값!
    var totalCnt by remember { mutableIntStateOf(0) }
    var completeCnt by remember { mutableIntStateOf(0) }
    var updateState by remember { mutableStateOf(false) }
    LaunchedEffect(updateState) {
        withContext(Dispatchers.Main) {
            val date = Date()
            totalCnt = taskViewModel.getTaskCntByDate(date)
            completeCnt = taskViewModel.getCompleteTaskCnt(date)
            Timber.i("total : %s | complete : %s", totalCnt, completeCnt)
            updateState = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(white)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            // 차트
            val dataList = listOf(
                ChartEntry(
                    volume = completeCnt,
                    color = colorResource(id = R.color.purple200)
                ),
                ChartEntry(
                    volume = totalCnt-completeCnt,
                    color = colorResource(id = R.color.mono200)
                )
            )
            PerforatedPieChart(
                data = dataList,
                radiusOuter = 64.dp,
                chartBarWidth = 16.dp,
                animDuration = 700,
                total = totalCnt,
                success = completeCnt
            )
            Spacer(modifier = Modifier.height(24.dp))
            if(totalCnt > 0){
                // 격언
                val sentence = stringResource(id = R.string.sample_guide)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ){
                    Text(
                        text = "\" $sentence \"",
                        style = MaterialTheme.typography.caption.copy(
                            fontSize = 16.sp,
                            color = black,
                            fontWeight = FontWeight.Medium
                        ),
                        modifier = Modifier
                            .wrapContentSize()
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            // 버튼
            OutlinedButton(
                onClick = { launcher?.launch(intent) },
                border = BorderStroke(1.dp, none),
                shape = CircleShape,
                // or shape = CircleShape
                colors = ButtonDefaults.outlinedButtonColors(
                    backgroundColor = purple200,
                    contentColor = white,
                    disabledContentColor = mono300
                )
            ){
                Text(
                    text = stringResource(R.string.txt_see_detail_statistics),
                    style = MaterialTheme.typography.caption.copy(
                        fontSize = 16.sp,
                        color = white,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(
                            vertical = 4.dp,
                            horizontal = 2.dp
                        ),
                    )
            }
        }
    }
}