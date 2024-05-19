package com.one.toit.ui.compose.ui.unit.profile

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.one.toit.R
import com.one.toit.ui.compose.style.mono300
import com.one.toit.ui.compose.style.purple200
import com.one.toit.ui.compose.style.white
import com.one.toit.util.AppUtil
import com.skydoves.landscapist.CircularReveal
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun ProfilePreviewDialog(
    profilePath:String,
    onDismiss: () -> Unit
){

    // 환경 변수
    val context = LocalContext.current

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
            var iconModifier = Modifier
                .fillMaxSize()
                .padding(4.dp)
            // parsing uri!
            val uri = remember(profilePath) { Uri.parse(profilePath) }
            val painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(context)
                    .data(uri)
                    .placeholder(R.drawable.ic_person)
                    .error(R.drawable.ic_person)
                    .build()
            )
            // 프로필 사진 비었을 경우 스케일 타입, 패딩 재조정
            val emptyProfile = profilePath.isBlank()
            if(emptyProfile){ iconModifier = Modifier.size(128.dp) }
            Box(modifier = Modifier.size(256.dp)){
                Image(
                    painter = painter,
                    contentDescription = null,
                    contentScale = if(emptyProfile) {
                        ContentScale.Fit
                    } else {
                        ContentScale.FillWidth
                    },
                    modifier = iconModifier
                        .align(Alignment.Center)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}