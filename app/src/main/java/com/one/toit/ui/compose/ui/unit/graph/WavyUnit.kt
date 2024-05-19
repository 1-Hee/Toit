package com.one.toit.ui.compose.ui.unit.graph

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random


@Composable
fun Dp.toPx(): Float {
    return with(LocalDensity.current) { this@toPx.toPx() }
}
@Composable
fun WavyUnit() {
    val circleDiameter = 200.dp

    val sinAnimatable = remember {
        Animatable(initialValue = 0f)
    }
    val cosAnimatable = remember {
        Animatable(initialValue = 0f)
    }

    var sinAmplitude by remember { mutableStateOf(100f) }
    var cosAmplitude by remember { mutableStateOf(100f) }
    var waveFrequency by remember { mutableStateOf(2f) }
    var durationTimes by remember { mutableStateOf(1000) }

    LaunchedEffect(Unit) {
        repeat(Int.MAX_VALUE){
            val startSin = Random.nextInt(0, 300)
            sinAnimatable.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = durationTimes,
                    delayMillis = startSin,
                    easing = FastOutSlowInEasing
                )
            )
            sinAnimatable.snapTo(0f)
            sinAmplitude = Random.nextInt(30, 100).toFloat()
            waveFrequency = Random.nextInt(2, 5).toFloat()
            durationTimes = Random.nextInt(1000, 2000)
        }
    }

    LaunchedEffect(Unit){
        repeat(Int.MAX_VALUE){
            val startCos = Random.nextInt(0, 300)
            cosAnimatable.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = durationTimes,
                    delayMillis = startCos,
                    easing = FastOutLinearInEasing
                )
            )
            cosAnimatable.snapTo(0f)
            cosAmplitude = Random.nextInt(30, 100).toFloat()
            waveFrequency = Random.nextInt(2, 5).toFloat()
            durationTimes = Random.nextInt(1000, 2000)
        }
    }


    val animationProgress = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()


    LaunchedEffect(Unit) {
        scope.launch {
            animationProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = 1500,
                    easing = LinearEasing,
                ),

            )
        }
    }


    Canvas(
        modifier = Modifier
            .size(circleDiameter)
    ) {
        val sinPath = Path().apply {
            //val amplitude = 30f // 진폭
            val centerY = (size.height/2)

            moveTo(0f, centerY)

            val pathX = size.width
            for (x in 0..pathX.toInt() step 1) {
                val y = centerY + sinAmplitude * sin((x * waveFrequency * 2 * PI / size.width).toFloat())
                lineTo(x.toFloat(), ((size.height) - (y * sinAnimatable.value)))
            }
            // 사인 곡선의 끝 지점에서 바닥까지 선을 그림
            lineTo(pathX, size.height)
            lineTo(0f, size.height)
            close()
        }
        val cosPath = Path().apply {
            // val amplitude = 100f  // 진폭
            val centerY = (size.height/2)

            moveTo(0f, centerY)

            val pathX = size.width
            for (x in 0..pathX.toInt() step 1) {
                val y = centerY + cosAmplitude * cos((x * waveFrequency * 2 * PI / size.width).toFloat())
                lineTo(x.toFloat(), ((size.height) - (y * cosAnimatable.value)))
            }

            // 코사인 곡선의 끝 지점에서 바닥까지 선을 그림
            lineTo(pathX, size.height)
            lineTo(0f, size.height)
            close()
        }

        // 사인 파형
        drawPath(
            path = sinPath,
            color = Color.Blue,
            //style =
        )
        // 코사인 파형
        drawPath(
            path = cosPath,
            color = Color.Red,
        )
    }
}
