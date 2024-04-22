package com.one.toit.ui.compose.ui.page

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.contentColorFor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.one.toit.R
import com.one.toit.data.viewmodel.TaskViewModel
import com.one.toit.ui.compose.style.black
import com.one.toit.ui.compose.style.mono100
import com.one.toit.ui.compose.style.purple200
import com.one.toit.ui.compose.style.white
import com.one.toit.ui.compose.ui.unit.statistics.AllToitListUnit
import com.one.toit.ui.compose.ui.unit.statistics.TotalSummaryUnit

@Composable
fun AllStatisticsPage(
    navController: NavHostController,
    taskViewModel: TaskViewModel,
    launcher: ActivityResultLauncher<Intent>? = null
){
    val context = LocalContext.current
    // scroll state
    // val outerScrollState = rememberScrollState()
    // tab index
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    // 현재 높이를 저장하기 위한 상태 변수
    var deviceWidth by remember { mutableIntStateOf(0) }
    var deviceHeight by remember { mutableIntStateOf(0) }
    // LocalDensity를 사용하여 현재 디바이스의 화면 밀도를 가져옴
    val density = LocalDensity.current.density
    // 현재 높이를 구하는 코드
    LaunchedEffect(Unit) {
        val displayMetrics = context.resources.displayMetrics
        deviceHeight = (displayMetrics.heightPixels / density).toInt()
        deviceWidth = (displayMetrics.widthPixels / density).toInt()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(white)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        // TabLayoutExample()

        TabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = white,
            contentColor = contentColorFor(backgroundColor),
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier
                        .tabIndicatorOffset(tabPositions[selectedTabIndex])
                        .padding(horizontal = 8.dp),
                    color = purple200
                )
            },
            tabs = {
                Tab(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(8.dp),
                    selected = selectedTabIndex == 0,
                    onClick = { selectedTabIndex = 0 },
                    text = {
                        Text(
                            text = stringResource(R.string.tab_statistics_summary),
                            style = MaterialTheme.typography.caption.copy(
                                color = black,
                                fontSize = 16.sp
                            ),
                        )
                    },
                    selectedContentColor =  mono100,
                    unselectedContentColor = white
                )
                Tab(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(8.dp),
                    selected = selectedTabIndex == 1,
                    onClick = { selectedTabIndex = 1 },
                    text = {
                        Text(
                            text = stringResource(R.string.tab_todo_all),
                            style = MaterialTheme.typography.caption.copy(
                                color = black,
                                fontSize = 16.sp
                            ),
                        )
                    },
                    selectedContentColor =  mono100,
                    unselectedContentColor = white
                )
            }

        )

        Column(
            modifier = Modifier
                .fillMaxSize()
            //.verticalScroll(outerScrollState),
        ) {
            // Content of each tab
            when (selectedTabIndex) {
                0 -> {
                    TotalSummaryUnit(taskViewModel)
                }
                1 -> {
                    AllToitListUnit(context, taskViewModel, launcher)
                }
            }
        }

    }
}
