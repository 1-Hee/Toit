package com.one.toit.ui.compose.ui.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.one.toit.R
import com.one.toit.ui.compose.style.black
import com.one.toit.ui.compose.style.mono100
import com.one.toit.ui.compose.style.mono300
import com.one.toit.ui.compose.style.mono800
import com.one.toit.ui.compose.style.mono900
import com.one.toit.ui.compose.style.purple200
import com.one.toit.ui.compose.style.purple300
import com.one.toit.ui.compose.style.white

@Preview(showBackground = true)
@Composable
fun ProfilePage() {
    var userNickname by remember { mutableStateOf("닉네임을 입력하세요") }
    var todoState by remember { mutableStateOf(5) }
    var todoGoal by remember { mutableStateOf(10) }
    var toitPoint by remember { mutableStateOf(2024) }
    var totalTodo by remember { mutableStateOf(127) }
    var avgTodo by remember { mutableStateOf(8.7) }
    var minRecord by remember { mutableStateOf(780) } // second
    var maxRecord by remember { mutableStateOf(26580) } // second
    var avgRecord by remember { mutableStateOf(11460) } // second
    var axiomString by remember { mutableStateOf("성공은 작은 노력의 쌓임입니다.\n오늘 하루도 조금 더 나아가세요.") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(white)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // 사용자 프로필 부분
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ){
                // 프로필 이미지
                Box(modifier = Modifier
                    .size(64.dp)
                    .clip(shape = RoundedCornerShape(32.dp))
                ){
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .background(color = mono100)
                    ){
                        Image(
                            painter = painterResource(id = R.drawable.ic_profile),
                            contentDescription = "profileImage",
                            modifier = Modifier
                                .size(24.dp)
                                .align(Alignment.Center)
                        )
                    }
                }

                // 프로필 명
                Text(
                    text = "$userNickname 님",
                    style = MaterialTheme.typography.caption
                        .copy(
                            fontSize = 16.sp,
                            color = mono800

                        )
                )
                // 편집 버튼
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .width(56.dp)
                        .height(24.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = purple200,
                        contentColor = contentColorFor(backgroundColor),
                        disabledBackgroundColor = MaterialTheme
                            .colors.onSurface
                            .copy(alpha = 0.12f)
                            .compositeOver(purple300),
                        disabledContentColor = mono300.copy(alpha = ContentAlpha.disabled)
                    ),
                    contentPadding = PaddingValues(0.dp),
                ) {
                    Row(
                        modifier = Modifier
                            .wrapContentWidth()
                            .wrapContentHeight(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_create_todo),
                            contentDescription = "",
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "편집",
                            fontSize = 10.sp,
                            style = MaterialTheme.typography.caption.copy(
                                color = white
                            )
                        )
                    }

                }
            }

            // 일일 달성 목표
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(vertical = 12.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "일일 분석",
                    style = MaterialTheme.typography.subtitle1.copy(
                        color = black,
                        textAlign = TextAlign.Start
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(horizontal = 4.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Text(
                        text = "목표 달성",
                        style = MaterialTheme.typography.caption
                            .copy(
                                color = black,
                            )
                    )
                    // 목표 달성량
                    Text(
                        text = "$todoState / $todoGoal",
                        style = MaterialTheme.typography.caption
                            .copy(
                                color = black,
                            )
                    )
                }

            }

            // 레코드 보드
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(vertical = 12.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "레코드 보드",
                    style = MaterialTheme.typography.subtitle1
                        .copy(
                            color = black,
                            textAlign = TextAlign.Start
                        ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                )
                // toit, 전체 목표, 평균 목표
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    RecordBox("Toit!", "$toitPoint", true)
                    Box(modifier = Modifier
                        .width(1.dp)
                        .height(24.dp)
                        .background(mono300))
                    RecordBox("전체 목표", "$totalTodo" )
                    Box(modifier = Modifier
                        .width(1.dp)
                        .height(24.dp)
                        .background(mono300))
                    RecordBox("평균 목표", "$avgTodo" )

                }

                // 최단기록, 최장기록, 평균 기록
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    RecordBox("최단 기록", calcTimeString(minRecord.toLong()))
                    Box(modifier = Modifier
                        .width(1.dp)
                        .height(24.dp)
                        .background(mono300))
                    RecordBox("최장 기록", calcTimeString(maxRecord.toLong()))
                    Box(modifier = Modifier
                        .width(1.dp)
                        .height(24.dp)
                        .background(mono300))
                    RecordBox("평균 기록", calcTimeString(avgRecord.toLong()))
                }
            }

            // 격언
            Text(
                text = axiomString,
                style = MaterialTheme.typography.caption
                    .copy(
                        mono900,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    ),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(vertical = 48.dp)
            )
        }
    }
}

@Composable
fun RecordBox(title:String, content:String, hasHelp:Boolean = false){
    Box(modifier = Modifier
        .wrapContentWidth()
        .height(48.dp)){
        Row(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.TopCenter)
            ,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.caption
                    .copy(
                        color = black,
                        fontSize = 16.sp
                    )
            )
            if(hasHelp){
                Icon(
                    Icons.Rounded.Info,
                    contentDescription = "icon",
                    tint = mono900,
                    modifier = Modifier.size(16.dp)
                )

            }
        }
        Text(
            text = content,
            style = MaterialTheme.typography.caption
                .copy(
                    color = mono900
                ),
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.BottomCenter)
        )
    }
}

fun calcTimeString(time:Long):String{
    val hour  = ( time / 60 ) / 60;
    val min = (time / 60) % 60;
    return "$hour:$min"
}
