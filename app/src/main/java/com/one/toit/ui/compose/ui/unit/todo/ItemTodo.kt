package com.one.toit.ui.compose.ui.unit.todo

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.one.toit.ui.activity.BoardActivity
import com.one.toit.ui.compose.style.black
import com.one.toit.ui.compose.style.mono300
import com.one.toit.ui.compose.style.mono50
import com.one.toit.ui.compose.style.purple200
import com.one.toit.ui.compose.ui.unit.WarningDialog
import timber.log.Timber

@Preview(showBackground = true)
@Composable
fun ItemTodo(
    title:String = "Let's To it!",
    timeString:String = "00:00 남음",
    logTimeString:String = "12:34에 기록됨.",
    isSuccess:Boolean = false
){

    val context = LocalContext.current
    val intent = Intent(context, BoardActivity::class.java)

    var showPreViewDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }


    // 다이얼로그 팝업
    if (showPreViewDialog) {
        TodoPreviewDialog(
            onDismiss = {
                showPreViewDialog = false
            },
            onEdit = { id ->
                Timber.i("id : %s", id)
                intent.putExtra("pageIndex", 1)
                context.startActivity(intent)
            },
            onDelete = {
                showPreViewDialog = false
                showDeleteDialog = true
            }
        )
    }

    // 삭제 다이얼로그
    if(showDeleteDialog){
        WarningDialog(
            onDismiss = { showDeleteDialog = false },
            onCancel = { showDeleteDialog = false },
            onAction = { showDeleteDialog = false },
            title = "목표 삭제하기",
            content = "정말로  등록하신 목표를 삭제 하시겠어요?\n[삭제]를 누르시면 회원님의 목표가 삭제됩니다.",
            textAction = "삭제"
        )
    }

    Card(
        elevation = 1.dp,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(92.dp)
            .background(mono50)
            .clickable {
                showPreViewDialog = true
//                intent.putExtra("pageIndex", 2)
//                context.startActivity(intent)
            }
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
