package com.one.toit.ui.activity

import android.Manifest
import android.os.Bundle
import androidx.activity.compose.setContent
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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.one.toit.R
import com.one.toit.base.ui.BaseComposeActivity
import com.one.toit.ui.compose.nav.SettingRoute
import com.one.toit.ui.compose.style.MyApplicationTheme
import com.one.toit.ui.compose.style.black
import com.one.toit.ui.compose.style.mono700
import com.one.toit.ui.compose.style.mono800
import com.one.toit.ui.compose.style.white
import com.one.toit.ui.compose.ui.unit.SimpleTopBar

class SettingActivity : BaseComposeActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SettingScreenView(this)
        }
    }

    // 권한 요청 결과 처리
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1001 -> {
                // 각 권한에 대한 결과를 확인하고 필요한 처리 수행
                for (i in permissions.indices) {
                    when (permissions[i]) {
                        Manifest.permission.READ_MEDIA_VIDEO -> {
                            // READ_MEDIA_VIDEO에 대한 처리
                        }
                        Manifest.permission.READ_MEDIA_IMAGES -> {
                            // READ_MEDIA_IMAGES에 대한 처리
                        }
                        // ... 다른 권한들에 대한 처리 추가 ...
                    }
                }
            }
            // 다른 요청 코드에 대한 처리 추가
        }
    }
}

@Composable
fun SettingScreenView(activity: SettingActivity){
    val navController = rememberNavController()
    Scaffold(
        topBar = { SettingTopBarComponent(activity) },
        bottomBar = {  }
    ) {
        Box(Modifier.padding(it)) {
            SettingNavGraph(navController = navController)
        }
    }
}

@Composable
fun SettingTopBarComponent(activity: SettingActivity){
    val settingTabName = stringResource(id = R.string.p_app_setting)
    SimpleTopBar(activity = activity, title = settingTabName)
}

@Composable
fun SettingNavGraph(navController: NavHostController){
    NavHost(navController = navController, startDestination = SettingRoute.AppSetting.route){
        composable(SettingRoute.AppSetting.route){
            AppSettingPage()
        }
    }
}

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
            /**
             * 체크할 권한
             * 1) 푸시 알림, 2) 카메라, 3) 미디어, 4) 위치
             */
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
                androidx.compose.material.Icon(
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

@Preview(showBackground = true)
@Composable
fun DefaultPreView(){
    MyApplicationTheme {
        SettingScreenView(SettingActivity())
    }
}