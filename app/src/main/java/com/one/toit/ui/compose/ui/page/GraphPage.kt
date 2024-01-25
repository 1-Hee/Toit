package com.one.toit.ui.compose.ui.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.one.toit.data.dto.ChartEntry
import com.one.toit.ui.compose.style.black
import com.one.toit.ui.compose.style.mono600
import com.one.toit.ui.compose.style.navy400
import com.one.toit.ui.compose.style.orange300
import com.one.toit.ui.compose.style.purple300
import com.one.toit.ui.compose.style.red300
import com.one.toit.ui.compose.style.white
import com.one.toit.ui.compose.ui.unit.BarGraphChart
import kotlin.random.Random

// @Preview(showBackground = true)
@Composable
fun GraphPage(
    navController: NavHostController
){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(white)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            var graphMode by remember { mutableStateOf("막대 그래프") }
            var isCheck by remember { mutableStateOf(false) }
            // dummy
            val test = listOf(
                "9/1","9/2","9/3","9/4","9/5",
                "9/6","9/7","9/8","9/9","9/10",
                "9/10","9/11","9/12","9/13","9/14",
            )
            val colorList = listOf(
                black, red300, orange300, navy400, purple300
            )
            val testData = mutableMapOf<String, ChartEntry>()
            test.forEach { date ->
                /*
                 val randomNumberInRange = Random.nextInt(1, 100)
                 */
                val randIdx = Random.nextInt(0, 4)
                val volume = Random.nextInt(32,  128)
                testData[date] = ChartEntry(volume, colorList[randIdx])
            }
            // dummy!
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
                BarGraphChart(
                    data = testData,
                    durationMillis = 700,
                    maxValue = 172,
                )
            }else {
                // TODO 꺾은선 그래프!
            }

        }
    }
}