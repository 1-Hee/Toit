package com.one.toit.compose.ui.page

import android.content.Intent
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.one.toit.R
import com.one.toit.compose.style.black
import com.one.toit.compose.style.mono200
import com.one.toit.compose.style.mono300
import com.one.toit.compose.style.mono600
import com.one.toit.compose.style.purple100
import com.one.toit.compose.style.purple200
import com.one.toit.compose.style.white
import com.one.toit.compose.ui.unit.ItemNoContent
import com.one.toit.compose.ui.unit.ItemTodo
import com.one.toit.ui.activity.BoardActivity


@Preview(showBackground = true)
@Composable
fun TodoPage(){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(white)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {

        val context = LocalContext.current
        val intent = Intent(context, BoardActivity::class.java)
        var checked by remember { mutableStateOf(false) }
        val optionText by remember { mutableStateOf("달성한 목표 숨기기") }
        var hasContent by remember { mutableStateOf(false) }
        val scrollState = rememberScrollState()

        // content list
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            //  상단 정렬 옵션 토글
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(32.dp)){

                Row(modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.CenterEnd)
                    .padding(end = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ){
                    Text(
                        text = optionText,
                        style = MaterialTheme.typography.subtitle1.copy(
                            fontSize = 14.sp,
                            color = black
                        ),
                        modifier = Modifier
                            .wrapContentSize()
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Switch(
                        checked = checked,
                        onCheckedChange = {
                            checked = it
                            hasContent = it
                        },
                        modifier = Modifier
                            .wrapContentWidth()
                            .height(24.dp),
                        colors  = SwitchDefaults.colors(
                            checkedThumbColor = purple200,
                            checkedTrackColor = mono300,
                            uncheckedThumbColor = mono600,
                            uncheckedTrackColor= mono200,
                        ),
                    )

                }
            }
            // content
            // 등록한 List가 있을 경우
            if(hasContent){
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Spacer(modifier = Modifier.height(4.dp))
                    repeat(12){
                        ItemTodo()
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }else {
                ItemNoContent()
            }
        }

        // 플로팅 버튼
        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            shape = MaterialTheme.shapes.small.copy(CornerSize(percent = 50)),
            backgroundColor = purple100,
            contentColor = contentColorFor(white),
            elevation = FloatingActionButtonDefaults.elevation(4.dp),
            onClick = {
                context.startActivity(intent)
            }
        ){
            Icon(
                painter = painterResource(id = R.drawable.ic_create_todo),
                contentDescription = "버튼 아이콘",
                modifier = Modifier
                    .width(26.dp)
                    .height(26.dp),
                tint = white
            )
        }
    }
}

