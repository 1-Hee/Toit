package com.one.toit.ui.compose.ui.unit.graph

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import com.one.toit.data.dto.ChartEntry
import com.one.toit.ui.compose.style.mono100
import com.one.toit.ui.compose.style.mono300
import com.one.toit.ui.compose.style.mono600
import com.one.toit.ui.compose.style.mono900
import com.one.toit.ui.compose.style.orange200
import com.one.toit.ui.compose.style.orange300
import com.one.toit.ui.compose.style.orange500
import com.one.toit.ui.compose.style.purple300
import com.one.toit.ui.compose.style.red200
import com.one.toit.ui.compose.style.red300
import com.one.toit.ui.compose.style.white
import timber.log.Timber
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

// TODO List로 바꾸기!
@Composable
fun LineGraphChart(
    modifier: Modifier = Modifier
        .fillMaxWidth(),
    data: MutableMap<String, Number>,
    durationMillis:Int = 1000,
    graphColor: Color = orange300,
    dotColor:Color = orange500,
    textColor:Color = mono900,
    dashColor:Color = red200,
    isDaily:Boolean = false
) {

    val dailyCof = 4;
    val keyList = mutableListOf<String>()
    val dataList = mutableListOf<Int>()
    data.values.forEach { item ->
        dataList.add(item.toInt())
    }
    data.keys.forEach { item ->
        keyList.add(item)
    }

    // 애니메이션
    val lineAnimatable = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        lineAnimatable.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = durationMillis,
                delayMillis = 0,
                easing = LinearOutSlowInEasing
            )
        )
    }

    Column(
        modifier = modifier
            .height(110.dp)
    ) {
        if(dataList.isNotEmpty()){
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                val xInterval = size.width / (dataList.size - 1)
                // 젤 앞쪽 선도 그리기
                val firstY = size.height - if(isDaily) {
                    (dataList[0]*dailyCof).dp.toPx()
                } else {
                    dataList[0].dp.toPx()
                }

                // 대쉬 선 갭
                val dashGap = 16f
                drawLine(
                    color = dashColor,
                    start = Offset(0f, (size.height - dashGap) * lineAnimatable.value),
                    end = Offset(0f, firstY * lineAnimatable.value),
                    strokeWidth = 1.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                )

                for (i in 0 until dataList.size - 1) {
                    val startX = i * xInterval
                    val startY = size.height - if(isDaily) {
                        (dataList[i] * dailyCof).dp.toPx()
                    }else{
                        dataList[i].dp.toPx()
                    }
                    val endX = (i + 1) * xInterval
                    val endY = size.height - if(isDaily) {
                        (dataList[i + 1] * dailyCof).dp.toPx()
                    }else{
                        dataList[i + 1].dp.toPx()
                    }
                    // 값 그래프
                    drawLine(
                        color = graphColor,
                        start = Offset(startX * lineAnimatable.value, startY * lineAnimatable.value),
                        end = Offset(endX * lineAnimatable.value, endY * lineAnimatable.value),
                        strokeWidth = 6.dp.toPx()
                    )

                    // 점선
                    drawLine(
                        color = dashColor,
                        start = Offset(endX, (size.height - dashGap) * lineAnimatable.value),
                        end = Offset(endX, endY * lineAnimatable.value),
                        strokeWidth = 1.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                    )
                }


                // 각 데이터 포인트에 점을 그립니다.
                for (i in dataList.indices) {
                    val x = i * xInterval
                    val itemCofY = if(isDaily) {
                        (dataList[i] * dailyCof).dp.toPx()
                    }else{
                        dataList[i].dp.toPx()
                    }
                    val y = size.height - itemCofY
                    drawCircle(
                        color = dotColor,
                        radius = 4.dp.toPx(),
                        center = Offset(x * lineAnimatable.value, y * lineAnimatable.value)
                    )

                    // 점 아래에 텍스트를 그립니다.
                    drawContext.canvas.nativeCanvas.apply {
                        // 값 글자 그리기
                        if(dataList[i] > 0){
                            drawText(
                                dataList[i].toString(),
                                x,
                                y - 5.dp.toPx(), // 점의 아래에 텍스트를 그리기 위해 y 값을 증가시킵니다.
                                androidx.compose.ui.graphics.Paint().asFrameworkPaint().apply {
                                    isAntiAlias = true
                                    textSize = 8.sp.toPx() * lineAnimatable.value
                                    color = textColor.toArgb()
                                    textAlign = android.graphics.Paint.Align.CENTER
                                }
                            )
                        }
                        // 날짜 글자 그리기
                        drawText(
                            keyList[i],
                            x,
                            size.height,
                            androidx.compose.ui.graphics.Paint().asFrameworkPaint().apply {
                                isAntiAlias = true
                                textSize = 8.sp.toPx() * lineAnimatable.value
                                color = textColor.toArgb()
                                textAlign = android.graphics.Paint.Align.CENTER
                            }
                        )
                    }
                }
            }
        }
    }
}
