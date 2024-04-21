package com.one.toit.ui.compose.ui.unit.graph

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.one.toit.ui.compose.style.black
import com.one.toit.ui.compose.style.mono900
import com.one.toit.ui.compose.style.red300
import com.one.toit.ui.compose.style.white

data class PackedPieChartEntry(
    val color: Color, val percentage: Float
)

private fun calculateStartAngles(entries: List<PackedPieChartEntry>): List<Float> {
    var totalPercentage = 0f
    return entries.map { entry ->
        val startAngle = totalPercentage * 360
        totalPercentage += entry.percentage
        startAngle
    }
}

@Composable
fun PackedPieChart(
    modifier: Modifier = Modifier,
    entries: List<PackedPieChartEntry>,
    size: Dp = 300.dp,
    text:String? = null,
    durationMillis:Int = 1000
) {

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

    val textMeasurer = rememberTextMeasurer()
    Canvas(modifier = modifier
        .size(size)
    ) {
        val startAngles = calculateStartAngles(entries)
        entries.forEachIndexed { index, entry ->
            val mSweepAngle = if(index == 0){
                (entry.percentage * 360f) * animatedProgress.value
            }else {
                entry.percentage * 360f
            }
            drawArc(
                color = entry.color,
                startAngle = startAngles[index],
                sweepAngle = mSweepAngle,
                useCenter = true,
                topLeft = Offset.Zero,
                size = this.size
            )
        }
        if(text != null){
            val textSize = 18.sp
            val adjustment = (textSize.value*4f)
            val mWidth = ((this.size.width/2f) - adjustment)
            val mHeight = ((this.size.height/2f) - adjustment) * (animatedProgress.value)
            val offset = Offset(mWidth, mHeight)
            drawText(
                textMeasurer = textMeasurer,
                text = text,
                topLeft = offset,
                style = TextStyle.Default.copy(
                    color = white,
                    fontSize = textSize,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}