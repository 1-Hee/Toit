package com.one.toit.ui.compose.ui.unit.graph

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.one.toit.data.dto.ChartEntry
import com.one.toit.ui.compose.style.mono600
import com.one.toit.ui.compose.style.purple300
import com.one.toit.ui.compose.style.red300

// TODO List로 바꾸기!
@Composable
fun LineGraphChart(
    data: MutableMap<String, ChartEntry>,
    durationMillis:Int = 1000,
    maxValue: Int = 128,
    graphColor: Color = purple300,
    dotColor:Color = red300,
    textColor:Color = mono600
) {

    val keyList = mutableListOf<String>()
    data.keys.forEach{ key ->
        keyList.add(key)
    }

    // 애니메이션
    val animatedProgress = remember { Animatable(0.001f) }
    LaunchedEffect(animatedProgress) {
        animatedProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = durationMillis,
                delayMillis = 0,
                easing = LinearOutSlowInEasing
            )
        )
    }
    // val spacing = 48f
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ){
        val textMeasurer = rememberTextMeasurer()
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(maxValue.dp)
        ) {
            val unit = (size.width / data.values.size)
            val valueList = data.values.toList()
            val center = (size.height/2)
            for (idx in valueList.indices){
                if(idx == valueList.size -1 ) return@Canvas
                val index = idx + 1
                val chartEntry = valueList[index]
                val startX = unit*(idx)
                val startY = center - valueList[index-1].volume.toFloat()
                val endX = unit*(idx+1)
                val endY = center - valueList[index].volume.toFloat()
                val startingPoint = Offset(startX, startY)
                val endingPoint = Offset(endX, endY)
                drawLine(
                    graphColor,
                    strokeWidth = 4.dp.toPx(),
                    start = startingPoint * animatedProgress.value,
                    end = endingPoint * animatedProgress.value,
                    cap = StrokeCap.Round
                )
                // 점
                drawCircle(
                    dotColor,
                    radius = 4.dp.toPx(),
                    center = Offset(startingPoint.x, startingPoint.y) * animatedProgress.value
                )
                drawCircle(
                    dotColor,
                    radius = 4.dp.toPx(),
                    center = Offset(endingPoint.x, endingPoint.y) * animatedProgress.value
                )
                // 값
                if(idx == 0){
                    val startValue = valueList[index-1].volume.toFloat()
                    drawText(
                        textMeasurer = textMeasurer,
                        text = startValue.toString(),
                        topLeft = startingPoint * animatedProgress.value,
                        style = TextStyle.Default.copy(
                            color = textColor,
                            fontSize = 8.sp
                        )
                    )
                    // 날짜
                    val mStartPoint = Offset(startingPoint.x, size.height - 64f)
                    drawText(
                        textMeasurer = textMeasurer,
                        text = keyList[index-1],
                        topLeft = mStartPoint,
                        style = TextStyle.Default.copy(
                            color = textColor,
                            fontSize = 8.sp
                        )
                    )
                }
                drawText(
                    textMeasurer = textMeasurer,
                    text = chartEntry.volume.toString(),
                    topLeft = endingPoint * animatedProgress.value,
                    style = TextStyle.Default.copy(
                        color = textColor,
                        fontSize = 8.sp
                    )
                )
                // 날짜
                val mEndPoint = Offset(endingPoint.x, size.height - 64f)
                drawText(
                    textMeasurer = textMeasurer,
                    text = keyList[index],
                    topLeft = mEndPoint,
                    style = TextStyle.Default.copy(
                        color = textColor,
                        fontSize = 8.sp
                    )
                )
            }
        }
    }
}
