package com.one.toit.compose.ui.page

import android.app.TimePickerDialog
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.one.toit.compose.style.mono50
import com.one.toit.compose.style.purple100
import com.one.toit.compose.style.purple200
import com.one.toit.compose.style.white

@Preview(showBackground = true)
@Composable
fun BoardWritePage(){


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(white)
    ) {
        val context = LocalContext.current

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "TODO",
                style = MaterialTheme.typography.subtitle1
                    .copy(
                        fontSize = 16.sp,
                        textAlign = TextAlign.Start
                    ),
                modifier = Modifier
                    .wrapContentSize()
                    .padding(vertical = 8.dp)
            )
            var todoText by remember { mutableStateOf("") }
            TextField(
                value = todoText,
                onValueChange = { todoText = it },
                label = { Text("todo") }
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ){
                Text(
                    text = "메모",
                    style = MaterialTheme.typography.subtitle1
                        .copy(
                            fontSize = 16.sp,
                            textAlign = TextAlign.Start
                        ),
                    modifier = Modifier
                        .wrapContentSize()
                )
                Text(
                    text = "memo",
                    style = MaterialTheme.typography.subtitle1
                        .copy(
                            fontSize = 12.sp,
                            textAlign = TextAlign.Start
                        ),
                    modifier = Modifier
                        .wrapContentSize()
                )
            }

            var memoText by remember { mutableStateOf("") }
            TextField(
                value = memoText,
                onValueChange = { memoText = it },
                label = { Text("memo") }
            )

            var checked by remember { mutableStateOf(false) }
            var toggleDescription by remember {
                mutableStateOf("기한을 설정하지 않은 목표는 달성 시간 통계에 반영 되지 않아요!")
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ){
                var toggleState by remember { mutableStateOf("기한 없음") }
                Text(
                    text = toggleState,
                    style = MaterialTheme.typography.subtitle1
                        .copy(
                            fontSize = 12.sp,
                            textAlign = TextAlign.Start
                        ),
                    modifier = Modifier
                        .wrapContentSize()
                )
                Switch(
                    checked = checked ,
                    onCheckedChange = {
                        if(it){
                            toggleState = "기한 설정"
                            toggleDescription = "목표를 달성할 시간을 설정합니다."
                        }else {
                            toggleState = "기한 없음"
                            toggleDescription = "기한을 설정하지 않은 목표는 달성 시간 통계에 반영 되지 않아요!"
                        }
                        checked = it
                    }
                )
            }
            Text(
                text = toggleDescription,
                style = MaterialTheme.typography.subtitle1
                    .copy(
                        fontSize = 8.sp,
                        textAlign = TextAlign.End
                    ),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            )
            if(checked){
                Row(
                    Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.End
                ) {
                    Row(modifier = Modifier
                        .wrapContentSize()
                        .clickable {
                           Toast.makeText(context, "다이얼로그 띄우기...", Toast.LENGTH_SHORT).show()
                        },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ){
                        Text(
                            text = "마감 기한",
                            style = MaterialTheme.typography.subtitle1
                                .copy(
                                    fontSize = 14.sp,
                                ),
                            modifier = Modifier
                                .wrapContentSize()
                        )
                        Row(
                            modifier = Modifier
                                .wrapContentSize(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "0:00",
                                style = MaterialTheme.typography.subtitle1
                                    .copy(
                                        fontSize = 16.sp,
                                    ),
                                modifier = Modifier
                                    .wrapContentSize()
                            )
                            Icon(Icons.Rounded.DateRange,
                                contentDescription = "",
                                modifier = Modifier.size(16.dp)
                            )
                        }

                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = { /*TODO*/ },
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = purple200,
                        contentColor = white,
                        disabledBackgroundColor = purple100,
                        disabledContentColor = mono50
                    ),
                    modifier = Modifier
                        .width(156.dp)
                        .wrapContentHeight()
                ) {
                    Text(
                        text = "등록 하기",
                        style = MaterialTheme.typography.subtitle1
                            .copy(
                                fontSize = 14.sp,
                                textAlign = TextAlign.Start,
                                color = white
                            ),
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(vertical = 4.dp)
                    )
                }

            }

        }

    }

}