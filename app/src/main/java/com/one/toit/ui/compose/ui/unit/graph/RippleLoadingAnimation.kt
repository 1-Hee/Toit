package com.one.toit.ui.compose.ui.unit.graph

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInBack
import androidx.compose.animation.core.EaseInBounce
import androidx.compose.animation.core.EaseInCirc
import androidx.compose.animation.core.EaseInElastic
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.EaseOutBounce
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.one.toit.ui.compose.style.blue500
import com.one.toit.ui.compose.style.mono100
import com.one.toit.ui.compose.style.mono300
import com.one.toit.ui.compose.style.orange300
import com.one.toit.ui.compose.style.orange50
import com.one.toit.ui.compose.style.orange500
import com.one.toit.ui.compose.style.purple300
import com.one.toit.ui.compose.style.white
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun RippleLoadingAnimation(
    modifier: Modifier = Modifier.size(156.dp),
    circleColor: Color,
    bgColor:Color,
    animationDelay: Int,
    value : String = ""
) {

    // 글자 크게 증감을 위한 애니메이션
    val textAnimatable = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        textAnimatable.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            )
        )
    }

    // 3개의 원 애니메이션용 변수들
    val circles = listOf(
        remember {
            Animatable(initialValue = 0f)
        },
        remember {
            Animatable(initialValue = 0f)
        },
        remember {
            Animatable(initialValue = 0f)
        }
    )

    // 각 원마다 애니메이션 효과 및 이징 효과를 다르게 함.
    circles.forEachIndexed { index, animatable ->
        LaunchedEffect(Unit) {
            // Use coroutine delay to sync animations
            // divide the animation delay by number of circles
            delay(timeMillis = (animationDelay / 3L) * (index + 1))

            val random = Random.nextInt(0, 6)
            val effect = when(random){
                1 -> LinearOutSlowInEasing
                2 -> FastOutSlowInEasing
                3 -> FastOutLinearInEasing
                4 -> EaseInBounce
                5 -> EaseOutBounce
                else -> LinearEasing
            }

            animatable.animateTo(
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = animationDelay,
                        easing = effect
                    ),
                    repeatMode = RepeatMode.Restart
                )
            )
        }
    }

    /// 외부 반원
    // outer circle
    Box(
        modifier = modifier
            .clip(shape = CircleShape)
            .background(color = bgColor)
    ) {
        // animating circles
        circles.forEachIndexed { index, animatable ->
            Box(
                modifier = modifier
                    .scale(scale = animatable.value)
                    .clip(shape = CircleShape)
                    .background(
                        color = circleColor
                        .copy(alpha = (1 - animatable.value))
                    )
            ) {
            }
        }
        // 데이터 값
        Text(
            text = value,
            modifier = Modifier.align(Alignment.Center),
            style = TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp * textAnimatable.value,
                color = white
            )
        )
    }
}