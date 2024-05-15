package com.one.toit.ui.compose.ui.unit.todo

//import com.one.toit.data.dto.TaskDTO
import android.app.Application
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.one.toit.R
import com.one.toit.data.dto.TaskDTO
import com.one.toit.data.viewmodel.TaskRegisterViewModel
import com.one.toit.ui.activity.BoardActivity
import com.one.toit.ui.compose.style.black
import com.one.toit.ui.compose.style.green600
import com.one.toit.ui.compose.style.mono300
import com.one.toit.ui.compose.style.mono50
import com.one.toit.ui.compose.style.purple200
import com.one.toit.ui.compose.ui.unit.WarningDialog
import com.one.toit.util.AppUtil
import com.one.toit.util.AppUtil.Time

@Composable
fun ItemTodo(
    taskDTO: TaskDTO,
    isSuccess:Boolean = taskDTO.taskComplete != null,
    launcher: ActivityResultLauncher<Intent>? = null
){
    val context = LocalContext.current
    val intent = Intent(context, BoardActivity::class.java)
    val application = context.applicationContext as Application
    val taskRegisterViewModel = TaskRegisterViewModel(application)
    var showPreViewDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    // 다이얼로그 팝업
    if (showPreViewDialog) {
        TodoPreviewDialog(
            taskDTO = taskDTO,
            onDismiss = {
                showPreViewDialog = false
            },
            onDelete = {
                showPreViewDialog = false
                showDeleteDialog = true
            },
            launcher = launcher
        )
    }

    // 삭제 다이얼로그
    if(showDeleteDialog){
        WarningDialog(
            onDismiss = { showDeleteDialog = false },
            onCancel = { showDeleteDialog = false },
            onAction = {
                showDeleteDialog = false
                if(taskDTO.taskId > 0){
                    val msg = context.getString(R.string.msg_delete_todo)
                    taskRegisterViewModel.removeTaskRegisterById(taskDTO.taskId)
                    AppUtil.toast(context, msg)
                }
            },
            title = stringResource(R.string.title_remove_todo),
            content = stringResource(R.string.txt_guide_remove_todo),
            textAction = stringResource(R.string.txt_delete)
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
//                // dommy...
                if (isSuccess) {
                    showPreViewDialog = true
                } else {
                    // 현재 날찌와 비교를 한다.
                    val createAt = taskDTO.createAt
                    val isOverDay = Time.compareCreatedDate(createAt);
                    if(isOverDay){
                        showPreViewDialog = true
                    }else {
                        intent.putExtra("pageIndex", 2)
                        intent.putExtra("taskDTO", taskDTO)
                        intent.putExtra("isComplete", false)
                        launcher?.launch(intent)
                    }
                }
            }
    ){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ){
            // Title
            Text(
                text = taskDTO.taskTitle,
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
                    Icons.Rounded.Check,
                    contentDescription = "",
                    modifier = Modifier
                        .width(24.dp)
                        .height(24.dp)
                        .align(Alignment.TopEnd),
                    tint = green600
                )
            }

            // 남은 시간 표시 로직
            if(taskDTO.taskLimit != null){
                val timeStr = Time.getTimeLog(taskDTO.taskLimit!!)
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
                        text = timeStr,
                        style = MaterialTheme.typography.subtitle1
                            .copy(
                                fontSize = 14.sp,
                                color = black
                            )
                    )
                }
            }

            // 작성 일자 표시 텍스트...
            Text(
                text = Time.getTimeLog(context, taskDTO.createAt), // 로그 텍스트
                style = MaterialTheme.typography.subtitle1
                    .copy(
                        fontSize = 14.sp,
                        color = mono300
                    ),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
            )
        }
    }
}

