package com.one.toit.ui.compose.ui.unit.graph

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.one.toit.R
import com.one.toit.data.dto.ChartEntry
import com.one.toit.ui.compose.style.black
import com.one.toit.ui.compose.style.mono600
import com.one.toit.ui.compose.style.mono700
import com.one.toit.ui.compose.style.mono900

@Composable
fun PerforatedPieChart(
    data: Map<String, ChartEntry>,
    radiusOuter: Dp = 140.dp,
    chartBarWidth: Dp = 35.dp,
    animDuration: Int = 1000,
    total:Int,
    success:Int
) {
    val colors = mutableListOf<Color>()

    /*
    PerformChartEntry
     */
    var totalSum = 0
    for(entry in data.values){
        totalSum += entry.volume
        colors.add(entry.color)
    }
    val floatValue = mutableListOf<Float>()

    // To set the value of each Arc according to
    // the value given in the data, we have used a simple formula.
    // For a detailed explanation check out the Medium Article.
    // The link is in the about section and readme file of this GitHub Repository
    data.values.forEachIndexed { index, entry ->
        floatValue.add(index, 360 * entry.volume.toFloat() / totalSum.toFloat())
    }
    var animationPlayed by remember { mutableStateOf(false) }

    var lastValue = 0f
    // it is the diameter value of the Pie
    val animateSize by animateFloatAsState(
        targetValue = if (animationPlayed) radiusOuter.value * 2f else 0f,
        animationSpec = tween(
            durationMillis = animDuration,
            delayMillis = 0,
            easing = LinearOutSlowInEasing
        ), label = ""
    )

    // if you want to stabilize the Pie Chart you can use value -90f
    // 90f is used to complete 1/4 of the rotation
    val animateRotation by animateFloatAsState(
        targetValue = if (animationPlayed) 90f * 11f else 0f,
        animationSpec = tween(
            durationMillis = animDuration,
            delayMillis = 0,
            easing = LinearOutSlowInEasing
        ),
        label = ""
    )

    // to play the animation only once when the function is Created or Recomposed
    LaunchedEffect(key1 = true) {
        animationPlayed = true
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Pie Chart using Canvas Arc
        Box(
            modifier = Modifier.size(animateSize.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(
                modifier = Modifier
                    .size(radiusOuter * 2f)
                    .rotate(animateRotation)
            ) {
                // draw each Arc for each data entry in Pie Chart
                floatValue.forEachIndexed { index, value ->
                    drawArc(
                        color = colors[index],
                        lastValue,
                        value,
                        useCenter = false,
                        style = Stroke(chartBarWidth.toPx(), cap = StrokeCap.Butt)
                    )
                    lastValue += value
                }
            }
            Column(modifier = Modifier
                .wrapContentSize()
                .align(Alignment.Center),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "$success / $total",
                    style = MaterialTheme.typography.caption.copy(
                        fontSize = 14.sp,
                        color = mono900,
                        fontWeight = FontWeight.Medium
                    ),
                    modifier = Modifier
                        .wrapContentSize()
                )
                Text(
                    text = stringResource(R.string.txt_daily_circle_graph),
                    style = MaterialTheme.typography.caption.copy(
                        fontSize = 12.sp,
                        color = mono600,
                        fontWeight = FontWeight.Medium
                    ),
                    modifier = Modifier
                        .wrapContentSize()
                )
            }
        }

        // To see the data in more structured way
        // Compose Function in which Items are showing data
        DetailsPieChart(
            data = data,
            colors = colors
        )

    }
}
@Composable
fun DetailsPieChart(
    data: Map<String, ChartEntry>,
    colors: List<Color>
) {
    Column(
        modifier = Modifier
            .padding(top = 64.dp)
            .fillMaxWidth()
    ) {
        // create the data items
        data.values.forEachIndexed { index, value ->
            DetailsPieChartItem(
                data = Pair(data.keys.elementAt(index), value),
                color = colors[index]
            )
        }

    }
}
@Composable
fun DetailsPieChartItem(
    data: Pair<String, ChartEntry>,
    height: Dp = 32.dp,
    color: Color
) {
    Surface(
        modifier = Modifier
            .padding(vertical = 10.dp, horizontal = 40.dp),
        color = Color.Transparent
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 아이콘!
            Box(
                modifier = Modifier
                    .background(
                        color = color,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .size(height)
            )

            Row(modifier = Modifier
                .wrapContentSize()
                .padding(start = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = data.first,
                    style = MaterialTheme.typography.caption.copy(
                        fontSize = 16.sp,
                        color = black,
                        fontWeight = FontWeight.Medium
                    ),
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(horizontal = 8.dp)
                )
                Text(
                    text = data.second.volume.toString(),
                    style = MaterialTheme.typography.caption.copy(
                        fontSize = 14.sp,
                        color = mono600,
                        fontWeight = FontWeight.Medium
                    ),
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(horizontal = 8.dp)
                )
            }
        }

    }
}