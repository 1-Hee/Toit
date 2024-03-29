package com.one.toit.ui.compose.ui.unit.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.one.toit.R
import com.one.toit.ui.compose.style.purple200
import com.one.toit.ui.compose.style.white
import com.skydoves.landscapist.CircularReveal
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun ProfilePreviewDialog(
    profilePath:String,
    onDismiss: () -> Unit
){
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Column(
            Modifier
                .width(256.dp)
                .wrapContentHeight()
                .background(color = white, shape = RoundedCornerShape(12.dp)),
            verticalArrangement = Arrangement.Top
        ) {
            // 상단 탑바
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp)
                    .background(
                        color = purple200,
                        shape = RoundedCornerShape(
                            topStart = 12.dp,
                            topEnd = 12.dp
                        )
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Icon(
                      Icons.Rounded.Close,
                      contentDescription = "icon",
                      modifier = Modifier
                          .size(16.dp)
                          .clickable { onDismiss() },
                    tint = white
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
            GlideImage(
                imageModel = profilePath,
                // contentScale 종류 : Crop, Fit, Inside, FillHeight, FillWidth, None
                contentScale = ContentScale.Crop,
                circularReveal = CircularReveal(duration = 0),
                // 이미지 로딩 전 표시할 place holder 이미지
                placeHolder = painterResource(id = R.drawable.ic_profile),
                // 에러 발생 시 표시할 이미지
                error = painterResource(id = R.drawable.ic_profile),
                modifier = Modifier
                    .size(256.dp)
                    .padding(4.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}