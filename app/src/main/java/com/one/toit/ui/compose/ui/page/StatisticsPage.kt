package com.one.toit.ui.compose.ui.page

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
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.one.toit.R
import com.one.toit.data.dto.ChartEntry
import com.one.toit.ui.compose.nav.MainRoute
import com.one.toit.ui.compose.style.black
import com.one.toit.ui.compose.style.mono100
import com.one.toit.ui.compose.style.purple200
import com.one.toit.ui.compose.style.purple400
import com.one.toit.ui.compose.style.white
import com.one.toit.ui.compose.ui.unit.graph.PerforatedPieChart

// @Preview(showBackground = true)
@Composable
fun StatisticsPage(
    navController : NavHostController
){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(white)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Preview with sample data
            val data = mapOf<String, ChartEntry>(
                Pair(
                    "완성", ChartEntry(
                        volume = 7,
                        color = colorResource(id = R.color.purple200)
                    )
                ),
                Pair(
                    "미완성", ChartEntry(
                        volume = 3,
                        color = colorResource(id = R.color.mono200)
                    )
                )
            )

            // 차트
            PerforatedPieChart(
                data = data,
                radiusOuter = 64.dp,
                chartBarWidth = 16.dp,
                animDuration = 700,
            )
            // 격언
            val sentence = stringResource(id = R.string.sample_guide)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(vertical = 48.dp),
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

            // 버튼
            Button(onClick = {
                navController.navigate(MainRoute.Graph.route)
            },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = purple200,
                    contentColor = contentColorFor(purple200),
                    disabledBackgroundColor = purple400,
                    disabledContentColor = mono100
                ),
                elevation = null
            ) {
                Text(
                    text = "자세한 통계 보기",
                    style = MaterialTheme.typography.caption.copy(
                        fontSize = 16.sp,
                        color = white,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(horizontal = 4.dp)
                )
            }
        }
    }
}