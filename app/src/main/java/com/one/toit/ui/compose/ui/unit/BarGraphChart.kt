package com.one.toit.ui.compose.ui.unit

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.one.toit.data.dto.ChartEntry
import com.one.toit.ui.compose.style.black
import com.one.toit.ui.compose.style.mono600
import com.one.toit.ui.compose.style.navy100
import com.one.toit.ui.compose.style.navy300
import com.one.toit.ui.compose.style.purple200
import com.one.toit.ui.compose.style.purple300
import com.one.toit.ui.compose.style.purple400
import com.one.toit.ui.compose.style.purple500
import com.one.toit.ui.compose.style.red300
import com.one.toit.ui.compose.style.white
import kotlinx.coroutines.delay
import java.lang.Math.abs

@Composable
fun BarGraphChart(
    data:Map<String, ChartEntry> = mapOf(),
    durationMillis:Int = 1000,
    maxValue: Int = 128,
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

    val scrollState = rememberScrollState()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(maxValue.dp)
            .horizontalScroll(scrollState),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = if(data.size > 9) Arrangement.spacedBy(16.dp) else Arrangement.SpaceEvenly
    ) {
        data.forEach { (key, entry) ->
            Column(
                modifier = Modifier
                    .wrapContentSize(),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = entry.volume.toString(),
                    style = MaterialTheme.typography.caption.copy(
                        color = mono600,
                        fontSize = 8.sp
                    ),
                    textAlign = TextAlign.Center,
                )

                Canvas(modifier = Modifier
                    .wrapContentWidth()
                    .height(entry.volume.dp)
                ) {
                    val height = size.height
                    val startingPoint = Offset(size.width, height)
                    val endingPoint = Offset(size.width, height - (height*animatedProgress.value))
                    drawLine(
                        purple300,
                        strokeWidth = 16.dp.toPx(),
                        start = startingPoint,
                        end = endingPoint,
                    )
                }
                Text(
                    text = key,
                    style = MaterialTheme.typography.caption.copy(
                        color = mono600,
                        fontSize = 12.sp
                    ),
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}