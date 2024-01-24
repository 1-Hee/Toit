package com.one.toit.ui.compose.ui.unit

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.one.toit.data.dto.ChartEntry
import com.one.toit.ui.compose.style.black
import com.one.toit.ui.compose.style.navy100
import com.one.toit.ui.compose.style.navy300
import com.one.toit.ui.compose.style.red300
import kotlinx.coroutines.delay
import java.lang.Math.abs


@Composable
fun ChartBar(
    modifier: Modifier = Modifier,
    percentage: Int,
    brush: Brush,
    isHighlighted: Boolean = false
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .height(kotlin.math.abs(percentage).dp)
            .width(8.dp)
            .background(brush)
            .background(color = if (!isHighlighted) Color.Black.copy(alpha = 0.5f) else Color.Transparent)
    )
}

@Composable
fun BarGraphChart(
//    radiusOuter: Dp = 140.dp,
//    chartBarWidth: Dp = 35.dp,
    data:Map<String, ChartEntry> = mapOf(),
    durationMillis:Int = 1000
) {

    //Animation - it will be repeated 2 times
    val animatedProgress = remember { Animatable(0.001f) }
    LaunchedEffect(animatedProgress) {
        animatedProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 1000,
                delayMillis = 0,
                easing = LinearOutSlowInEasing
            )
        )
    }



    val brush = Brush.horizontalGradient(listOf(black, red300, navy300))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        repeat(3){
            Canvas(modifier = Modifier
                .wrapContentWidth()
                .size(64.dp) ) {

                val height = size.height
                val startingPoint = Offset(size.width, height)
                val endingPoint = Offset(size.width, height - (height*animatedProgress.value))

                drawLine(
                    brush = brush,
                    strokeWidth = 8.dp.toPx(),
                    start = startingPoint,
                    end = endingPoint
                )
            }
        }
    }


}