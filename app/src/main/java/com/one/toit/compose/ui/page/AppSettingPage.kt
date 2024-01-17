package com.one.toit.compose.ui.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountBox
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.one.toit.compose.style.black
import com.one.toit.compose.style.mono500
import com.one.toit.compose.style.mono700
import com.one.toit.compose.style.mono800
import com.one.toit.compose.style.white

@Preview(showBackground = true)
@Composable
fun AppSettingPage(){
    Box(
        modifier = androidx.compose.ui.Modifier
            .fillMaxSize()
            .background(white)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text ="앱 사용에 필요한 권한을 허용합니다.",
                style = MaterialTheme.typography.caption
                    .copy(
                        fontSize = 14.sp,
                        color = mono800

                    ),
                modifier = Modifier.padding(vertical = 8.dp)
            )
            SettingUnit(
                imgVector = Icons.Rounded.Notifications,
                settingName = "앱 푸시 알림",
                description = "앱 사용에 필요한 정보와 알림을 수신합니다.\n앱 OS 설정에서 권한을 수정할 수 있습니다."
            )
            SettingUnit(
                imgVector = Icons.Rounded.AccountBox,
                settingName = "사진 및 동영상",
                description = "앱에서 사용할 사진 및 동영상 데이터의 열람을 허용합니다."
            )
            SettingUnit(
                imgVector = Icons.Rounded.AddCircle, // change
                settingName = "카메라",
                description = "앱에서 휴대전화의 카메라에 접근하는 것을 허용합니다."
            )
            SettingUnit(
                imgVector = Icons.Rounded.LocationOn,
                settingName = "위치",
                description = "사용자의 위치 정보를 조회합니다."
            )
        }
    }
}

@Composable
fun SettingUnit(
    imgVector: ImageVector,
    settingName:String,
    description:String
){
    var checked by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Row(
                modifier = Modifier
                    .wrapContentSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = imgVector,
                    contentDescription = "icon",
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = settingName,
                    style = MaterialTheme.typography.caption
                        .copy(
                            color = black,
                            fontSize = 16.sp
                        )
                )
            }
            Switch(
                checked = checked,
                onCheckedChange = {
                    checked = it
            })
        }
        Text(
            text = description,
            style = MaterialTheme.typography.caption
                .copy(
                    color = mono700,
                    fontSize = 10.sp
                )
        )
    }
}