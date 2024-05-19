package com.one.toit.ui.compose.ui.unit.todo

import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.one.toit.R
import com.one.toit.data.dto.TaskDTO
import com.one.toit.ui.activity.BoardActivity
import com.one.toit.ui.compose.style.black
import com.one.toit.ui.compose.style.green400
import com.one.toit.ui.compose.style.green700
import com.one.toit.ui.compose.style.mono100
import com.one.toit.ui.compose.style.mono200
import com.one.toit.ui.compose.style.mono300
import com.one.toit.ui.compose.style.mono400
import com.one.toit.ui.compose.style.mono700
import com.one.toit.ui.compose.style.purple200
import com.one.toit.ui.compose.style.white
import com.one.toit.util.AppUtil.Time
import com.skydoves.landscapist.CircularReveal
import com.skydoves.landscapist.glide.GlideImage
import timber.log.Timber

@Composable
fun TodoPreviewDialog(
    taskDTO: TaskDTO,
    onDismiss: () -> Unit,
    onDelete: () -> Unit,
    isDisable:Boolean = taskDTO.taskComplete == null,
    launcher: ActivityResultLauncher<Intent>? = null
) {
    val context = LocalContext.current
    val intent = Intent(context, BoardActivity::class.java)
    val sCreateAt  = remember {
        mutableStateOf(
            Time.getFullLog(context, taskDTO.createAt)
        )
    }
    val suffix = " " + stringResource(id = R.string.txt_complete)

    val sCompleteAt = remember { // 완료 일자
        mutableStateOf(
            if(taskDTO.taskComplete == null){
                ""
            }else {
               Time.getTimeLog(context, taskDTO.taskComplete!!, suffix)
            }
        )
    }
    val scrollState = rememberScrollState()

    Dialog(
        onDismissRequest = onDismiss
    ) {
        val corner = 8.dp
        Column(
            Modifier
                .background(color = white, shape = RoundedCornerShape(corner))
                .wrapContentSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // 상단 탑바
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(
                        color = purple200,
                        shape = RoundedCornerShape(
                            topStart = corner,
                            topEnd = corner
                        )
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Icon(
                    imageVector = Icons.Rounded.Edit,
                    contentDescription = "iconEdit",
                    modifier = Modifier
                        .size(20.dp)
                        .clickable {
                            // TODO 이쪽에 콜백으로 바꿔서, 리컴포지션 일어나게 하기
                            intent.putExtra("pageIndex", 1)
                            intent.putExtra("taskDTO", taskDTO)
                            intent.putExtra("isComplete", true)
                            launcher?.launch(intent)
                        },
                    tint = white
                )
                if(!isDisable){
                    Spacer(modifier = Modifier.width(24.dp))
                    Icon(
                        imageVector = Icons.Rounded.Delete,
                        contentDescription = "iconEdit",
                        modifier = Modifier
                            .size(20.dp)
                            .clickable {
                                onDelete()
                                onDismiss()
                            },
                        tint = white
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
            }
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.Start
            ) {
                // 제목 부분
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ){
                    Text(
                        text = "TODO",
                        style = MaterialTheme.typography.subtitle1.copy(
                            fontSize = 16.sp,
                            color = black
                        ),
                        modifier = Modifier
                            .wrapContentSize()
                    )
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(10.dp)
                            .background(color = mono300)
                    )

                    Text(
                        text = taskDTO.taskTitle,
                        style = MaterialTheme.typography.subtitle1.copy(
                            fontSize = 14.sp,
                            color = black
                        ),
                        modifier = Modifier
                            .wrapContentSize()
                    )
                }
                // 메모
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ){
                    Text(
                        text = "메모",
                        style = MaterialTheme.typography.subtitle1.copy(
                            fontSize = 16.sp,
                            color = black
                        ),
                        modifier = Modifier
                            .wrapContentSize()
                    )
                    Text(
                        text = "memo",
                        style = MaterialTheme.typography.subtitle1.copy(
                            fontSize = 10.sp,
                            color = mono700
                        ),
                        modifier = Modifier
                            .wrapContentSize()
                    )
                }
                // 메모 내용
                Text(
                    text = taskDTO.taskMemo,
                    style = MaterialTheme.typography.subtitle1.copy(
                        fontSize = 14.sp,
                        color = mono700,
                        textAlign = TextAlign.Start
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .heightIn(max = 172.dp)
                        .verticalScroll(scrollState)
                )
                // 이미지
                if(taskDTO.taskCertification?.isNotBlank() == true){
                    val uriString = taskDTO.taskCertification?:""
                    val uri = remember(uriString) { Uri.parse(uriString) }

                    val painter = rememberAsyncImagePainter(
                        model = ImageRequest.Builder(context)
                            .data(uri)
                            .placeholder(R.drawable.image_placeholder)
                            .error(R.drawable.image_error)
                            .build()
                    )
                    Image(
                        painter = painter,
                        contentDescription = null,
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(256.dp)
                    )
                }

                // 날짜 & 완성 정보 표시 레이어
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // 완료 일자
                    Text(
                        // 파싱 함수 추가
                        text = sCompleteAt.value,
                        style = MaterialTheme.typography.subtitle1.copy(
                            fontSize = 12.sp,
                            color = green700,
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier
                            .wrapContentSize()
                    )
                    // 생성 시간
                    Text(
                        // 파싱 함수 추가
                        text = sCreateAt.value,
                        style = MaterialTheme.typography.subtitle1.copy(
                            fontSize = 12.sp,
                            color = mono400,
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