package com.one.toit.ui.compose.ui.unit

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.one.toit.R
import com.one.toit.ui.compose.style.black
import com.one.toit.ui.compose.style.mono300
import com.one.toit.ui.compose.style.mono50
import com.one.toit.ui.compose.style.mono600
import com.one.toit.ui.compose.style.mono900
import com.one.toit.ui.compose.style.navy300
import com.one.toit.ui.compose.style.orange200
import com.one.toit.ui.compose.style.purple200
import com.one.toit.ui.compose.style.purple300
import com.one.toit.ui.compose.style.purple400
import com.one.toit.ui.compose.style.red100
import com.one.toit.ui.compose.style.red300
import com.one.toit.ui.compose.style.white
import java.text.NumberFormat

@Composable
fun ToitPointCard(
    modifier: Modifier = Modifier,
    toitPoint:Int,
    durationMillis:Int = 2500,
    rotateMillis:Int = durationMillis*24
){

    // TODO 다이얼로그 창을 띄워 수식 알려주는거!
    val numberFormat = NumberFormat.getInstance()
    // 뱃지 배경
//    val colorList = listOf(white, mono50, purple300, purple200, purple400)
     val colorList = listOf(red100, navy300, purple200, orange200)
    val brush = Brush.linearGradient(colorList)

    val radiusOuter = 64.dp
    val chartBarWidth = 16.dp
    var animationPlayed by remember { mutableStateOf(false) }

//    // it is the diameter value of the Pie
//    val animateSize by animateFloatAsState(
//        targetValue = if (animationPlayed) radiusOuter.value * 2f else 0f,
//        animationSpec = tween(
//            durationMillis = durationMillis,
//            delayMillis = 0,
//            easing = LinearOutSlowInEasing
//        ), label = ""
//    )

    // 90f is used to complete 1/4 of the rotation
    val animateRotation by animateFloatAsState(
        targetValue = if (animationPlayed) 360 * 11f else 0f,
        animationSpec = tween(
            durationMillis = rotateMillis,
            delayMillis = 0,
            easing = FastOutSlowInEasing
        ),
        label = ""
    )

    // to play the animation only once when the function is Created or Recomposed
    LaunchedEffect(key1 = true) {
        animationPlayed = true
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(128.dp),
        backgroundColor = mono50,
        contentColor = black,
        elevation = 4.dp,
        shape = RoundedCornerShape(8.dp)
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .background(brush)
                .padding(horizontal = 16.dp)
        ){
            // 타이틀
            Text(
                text = "\uD83D\uDD25 Toit Point",
                style = MaterialTheme.typography.caption.copy(
                    color = white,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 8.dp, bottom = 12.dp)
            )

            // 문구
            Text(
                text = stringResource(R.string.txt_guide_toit_point),
                style = MaterialTheme.typography.caption.copy(
                    color = white,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.ExtraLight
                ),
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(bottom = 8.dp)
            )

            // 점수
            Text(
                text = numberFormat.format(toitPoint),
                style = MaterialTheme.typography.caption
                    .copy(
                        white,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    ),
                modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.CenterEnd)
                    .padding(end = 32.dp)
            )

            // Pie Chart using Canvas Arc
            Box(
                modifier = Modifier
                    .size((radiusOuter.value * 2f).dp)
                    .align(Alignment.BottomEnd),
                contentAlignment = Alignment.Center
            ) {
                Canvas(
                    modifier = Modifier
                        .size(radiusOuter * 2f)
                        .rotate(animateRotation)
                ) {
                    // draw each Arc for each data entry in Pie Chart
                    drawArc(
                        color = white,
                        32f,
                        328f,
                        useCenter = false,
                        style = Stroke(chartBarWidth.toPx(), cap = StrokeCap.Butt)
                    )
                }
            }
        }
    }
}