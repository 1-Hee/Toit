package com.one.toit.ui.compose.ui.unit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.one.toit.ui.compose.style.navy300
import com.one.toit.ui.compose.style.orange200
import com.one.toit.ui.compose.style.purple200
import com.one.toit.ui.compose.style.red100
import com.one.toit.ui.compose.style.white
import java.text.NumberFormat

@Composable
fun ToitPointCard(
    modifier: Modifier = Modifier,
    toitPoint:Int
){
    // TODO 다이얼로그 창을 띄워 수식 알려주는거!
    val numberFormat = NumberFormat.getInstance()
    // 뱃지 배경
    val colorList = listOf(red100, navy300, purple200, orange200)
    val brush = Brush.linearGradient(colorList)
    Card(
        elevation = 1.dp,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
            .width(256.dp)
            .wrapContentHeight(),
//        backgroundColor = none
    ){
        Box(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(brush = brush)
        ){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .align(Alignment.TopStart)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(top = 16.dp, bottom = 4.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Toit Point \uD83D\uDD25",
                        style = MaterialTheme.typography.caption
                            .copy(
                                white,
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center
                            ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                    )
                }
                Row(
                    Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(top = 4.dp, bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = numberFormat.format(toitPoint),
                        style = MaterialTheme.typography.caption
                            .copy(
                                white,
                                fontSize = 20.sp,
                                textAlign = TextAlign.Center
                            ),
                        modifier = Modifier
                            .wrapContentSize()
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "point",
                        style = MaterialTheme.typography.caption
                            .copy(
                                white,
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center
                            ),
                        modifier = Modifier
                            .wrapContentSize()
                    )
                }
            }
        }
    }
}