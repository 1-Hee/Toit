package com.one.toit.ui.compose.ui.unit.todo

//import com.one.toit.data.dto.TaskDTO
import android.app.Application
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
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
import com.one.toit.ui.compose.style.mono600
import com.one.toit.ui.compose.style.purple200
import com.one.toit.ui.compose.ui.unit.WarningDialog
import com.one.toit.util.AppUtil
import com.one.toit.util.AppUtil.Time

@Composable
fun ItemTodo(
    taskDTO: TaskDTO,
    launcher: ActivityResultLauncher<Intent>? = null,
    onRefresh : () -> Unit
){
    // 환경 변수
    val context = LocalContext.current
    val intent = Intent(context, BoardActivity::class.java)
    val application = context.applicationContext as Application
    val taskRegisterViewModel = TaskRegisterViewModel(application)
    var showPreViewDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    val isComplete: Boolean = taskDTO.taskComplete != null

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
                onRefresh()
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
                if (isComplete) {
                    showPreViewDialog = true
                } else {
                    // 현재 날찌와 비교를 한다.
                    val createAt = taskDTO.createAt
                    val isOverDay = Time.checkOverDay(createAt);
                    if (isOverDay) {
                        showPreViewDialog = true
                    } else {
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

           val modifier = Modifier
               .size(24.dp)
               .align(Alignment.TopEnd)

            //case 1. 제한 ok 성공 ok
            // case 1. 제한시간 있고, 성공한 경우
            // 아이콘
            val isSuccess = if(taskDTO.taskLimit != null && taskDTO.taskComplete != null){
                val tLimit = taskDTO.taskLimit!!
                val tComplete = taskDTO.taskComplete!!
                tComplete.before(tLimit)
            }else {
                false
            }

            val isCheck = (isComplete && taskDTO.taskLimit == null) || isSuccess
            val isOverIcon = (!isComplete && Time.checkOverDay(taskDTO.createAt)) || (isComplete && !isSuccess)
            if(isCheck) { // 시간 제한 있고, 성공한 경우
                val vector = ImageVector.vectorResource(id = R.drawable.ic_check)
                Icon(
                    vector,
                    contentDescription = "",
                    modifier = modifier,
                    green600
                )
            }else if(isOverIcon){
                Image(
                    painter = painterResource(id = R.drawable.ic_time_over),
                    contentDescription = "",
                    modifier = modifier
                )
            }
//            }else if(isSuccess && !isSuccess){
//                Image(
//                    painter = painterResource(id = R.drawable.ic_time_over),
//                    contentDescription = "",
//                    modifier = modifier
//                )
//            //case 3. 제한시간 없고, 완료한 경우
//            }else if(taskDTO.taskLimit == null && isComplete){
//                Image(
//                    painter = painterResource(id = R.drawable.ic_check),
//                    contentDescription = "",
//                    modifier = modifier
//                )
//            // case 4. 작성일 기준 시간이 지났는데 완료하지 않은 경우
//            }

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