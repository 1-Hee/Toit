package com.one.toit.ui.compose.ui.unit.graph

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.one.toit.ui.compose.style.black
import com.one.toit.ui.compose.style.mono100
import com.one.toit.ui.compose.style.mono200
import com.one.toit.ui.compose.style.mono300
import com.one.toit.ui.compose.style.mono600
import com.one.toit.ui.compose.style.purple300

@Composable
fun TodayAchieveUnit(
    current:Int,
    total:Int,
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

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "2023년 4월 13일", // TODO date로 변환
            style = MaterialTheme.typography.caption.copy(
                color = mono600,
                fontSize = 16.sp
            ),
            textAlign = TextAlign.Center,
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = current.toString(),
                style = MaterialTheme.typography.caption.copy(
                    color = black,
                    fontSize = 20.sp
                ),
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.width(4.dp))
            Canvas(modifier = Modifier
                .width(4.dp)
                .height(16.dp)
                .padding(bottom = 4.dp)
            ) {
                val width = size.width
                val height = size.height
                val startingPoint = Offset(0f, height)
                val endingPoint = Offset(width, 0f)
                drawLine(
                    mono300,
                    strokeWidth = 2.dp.toPx(),
                    start = startingPoint,
                    end = endingPoint,
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = total.toString(),
                style = MaterialTheme.typography.caption.copy(
                    color = mono600,
                    fontSize = 14.sp
                ),
                textAlign = TextAlign.Center,
            )
        }

        // 그래프
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(RoundedCornerShape(8.dp))
        ) {
            Canvas(modifier = Modifier
                .fillMaxSize()
                .background(mono200)
            ) {
                val ratio = current.toFloat()/total.toFloat()
                val width:Float = (size.width * ratio) * animatedProgress.value
                val height = size.height
                val startingPoint = Offset(0f, height/2)
                val endingPoint = Offset(width, height/2)
                drawLine(
                    purple300,
                    strokeWidth = 48.dp.toPx(),
                    start = startingPoint,
                    end = endingPoint,
                )
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "성공은 작은 노력의 쌓임입니다. \n" +
                        "오늘 하루도 조금 더 나아가세요",
                style = MaterialTheme.typography.caption.copy(
                    color = mono600,
                    fontSize = 14.sp
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .wrapContentSize()
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}