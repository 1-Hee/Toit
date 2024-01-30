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
    val scrollstate = rememberScrollState()
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

            var prevX = 0f
            var prevY = 0f
            // Timber.i("start : $prevX | $prevY")
            data.values.forEachIndexed { index, chartEntry ->
                val startingPoint = Offset(size.width - prevX, prevY)
                val nextX = prevX + (size.width/data.values.size.toFloat())
                val endingPoint = Offset(size.width - nextX, chartEntry.volume.toFloat())
                // Timber.i("idx $index : (${startingPoint.x} , ${startingPoint.y}) | (${endingPoint.x}, ${endingPoint.y})")
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
                drawText(
                    textMeasurer = textMeasurer,
                    text = chartEntry.volume.toString(),
                    topLeft = startingPoint * animatedProgress.value,
                    style = TextStyle.Default.copy(
                        color = textColor,
                        fontSize = 8.sp
                    )
                )
                val mStartPoint = Offset(startingPoint.x, size.height - 64f)
                drawText(
                    textMeasurer = textMeasurer,
                    text = keyList[index],
                    topLeft = mStartPoint * animatedProgress.value,
                    style = TextStyle.Default.copy(
                        color = textColor,
                        fontSize = 8.sp
                    )
                )
                prevX = nextX
                prevY = endingPoint.y

            }

//            val spacePerHour = (size.width - spacing) / data.values.size
//            val normX = mutableListOf<Float>()
//            val normY = mutableListOf<Float>()
//            val strokePath = Path().apply {
//                for (i in data.values.indices) {
//                    val currentX = spacing + i * spacePerHour
//                    if (i == 0) {
//                        moveTo(currentX, yPoints[i])
//                    } else {
//                        val previousX = spacing + (i - 1) * spacePerHour
//                        val conX1 = (previousX + currentX) / 2f
//                        val conX2 = (previousX + currentX) / 2f
//                        val conY1 = yPoints[i - 1]
//                        val conY2 = yPoints[i]
//                        cubicTo(
//                            x1 = conX1,
//                            y1 = conY1,
//                            x2 = conX2,
//                            y2 = conY2,
//                            x3 = currentX,
//                            y3 = yPoints[i]
//                        )
//                    }
//                    // Circle dot points
//                    normX.add(currentX)
//                    normY.add(yPoints[i])
//                }
//            }
//            drawPath(
//                path = strokePath,
//                color = graphColor,
//                style = Stroke(
//                    width = 4.dp.toPx(),
//                    cap = StrokeCap.Round
//                )
//            )
//            (normX.indices).forEach {
//                drawCircle(
//                    mono100,
//                    radius = 4.dp.toPx(),
//                    center = Offset(normX[it], normY[it])
//                )
//            }
        }
    }
}
