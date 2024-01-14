package com.one.toit.compose.ui.unit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.one.toit.R
import com.one.toit.compose.style.black
import com.one.toit.compose.style.mono300
import com.one.toit.compose.style.mono50
import com.one.toit.compose.style.purple200

@Preview(showBackground = true)
@Composable
fun ItemTodo(
    title:String = "Let's To it!",
    timeString:String = "00:00 남음",
    logTimeString:String = "12:34에 기록됨.",
    isSuccess:Boolean = false
){
    Card(
        elevation = 1.dp,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(92.dp)
            .background(mono50)
    ){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ){
            // Title
            Text(
                text = title,
                style = MaterialTheme.typography.subtitle1
                    .copy(
                        fontSize = 16.sp,
                        color = black
                    ),
                modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.TopStart)
            )

            // 달성 여부
            if(isSuccess){
                Icon(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "",
                    modifier = Modifier
                        .width(24.dp)
                        .height(24.dp)
                        .align(Alignment.TopEnd)
                )
            }

            // 남은 시간
            Row(
                modifier = Modifier
                    .wrapContentSize()
                    .align(
                        Alignment.BottomStart
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                Icon(
                    painter = painterResource(id = R.drawable.ic_time),
                    contentDescription = "",
                    modifier = Modifier
                        .width(18.dp)
                        .height(18.dp),
                    tint = purple200
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = timeString,
                    style = MaterialTheme.typography.subtitle1
                        .copy(
                            fontSize = 14.sp,
                            color = black
                        )
                )
            }
            // 로그 시간
            Text(
                text = logTimeString,
                style = MaterialTheme.typography.subtitle1
                    .copy(
                        fontSize = 14.sp,
                        color = mono300
                    ),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp)
            )
        }
    }
}
