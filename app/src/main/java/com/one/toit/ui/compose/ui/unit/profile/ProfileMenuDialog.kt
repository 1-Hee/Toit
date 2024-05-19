package com.one.toit.ui.compose.ui.unit.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.one.toit.R
import com.one.toit.ui.compose.style.black
import com.one.toit.ui.compose.style.mono200
import com.one.toit.ui.compose.style.purple200
import com.one.toit.ui.compose.style.white

@Composable
fun ProfileMenuDialog(
    onDismiss: () -> Unit,
    onNickNameEdit:() -> Unit,
    onEditProfile:() -> Unit,
    onInitProfile:() -> Unit,
) {
    val context = LocalContext.current
    val menuList = stringArrayResource(id = R.array.menu_profile_edit)
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Column(
            Modifier
                .width(256.dp)
                .wrapContentHeight()
                .background(color = white, shape = RoundedCornerShape(12.dp))
            ,
            verticalArrangement = Arrangement.Top
        ) {
            // 상단 탑바
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(
                        color = purple200,
                        shape = RoundedCornerShape(
                            topStart = 12.dp,
                            topEnd = 12.dp
                        )
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = menuList[0],
                    fontSize = 14.sp,
                    style = MaterialTheme.typography.caption.copy(
                        color = white
                    )
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                for(i in 1..4){
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clickable {
                            when(i){
                                1 -> { // 닉네임 변경
                                    onNickNameEdit()
                                }
                                2 -> { // 프로필 변경
                                    onEditProfile()
                                }
                                3 -> { // 프로필 초기화
                                    onInitProfile()
                                }
                                else ->{
                                    onDismiss()
                                }
                            }
                        }
                    ){
                        Text(
                            text = menuList[i],
                            fontSize = 14.sp,
                            style = MaterialTheme.typography.caption.copy(
                                color = black,
                                textAlign = TextAlign.Center
                            ),
                            modifier = Modifier.align(Alignment.Center)
                        )
                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .align(Alignment.TopCenter),
                            color = mono200,
                            thickness = 1.dp
                        )
                    }
                }
            }
        }
    }
}