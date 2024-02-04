package com.one.toit.ui.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
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
import com.one.toit.util.AppUtil

class SettingActivity : BaseComposeActivity() {
    private val permissionFlag:Array<Boolean> = Array(4){false}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            checkPermission()
            SettingScreenView(this, permissionFlag) {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", this.packageName, null)
                intent.data = uri
                AppUtil.toast(this, "애플리케이션 정보 > 권한 에서 설정을 변경해주세요")
                launcher.launch(intent)
            }
        }
    }
    private fun checkPermission(){
        // 알림 권한
        val notifyFlag = ContextCompat.checkSelfPermission(
            this, Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
        // Timber.i("알림 권한 : %s", notifyFlag)
        permissionFlag[0] = notifyFlag

        // 저장소 권한
        val storagePermission = arrayOf(
            Manifest.permission.READ_MEDIA_VIDEO,
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        storagePermission.forEach { it ->
            val flag = ContextCompat.checkSelfPermission(
                this, it
            ) == PackageManager.PERMISSION_GRANTED
            // Timber.i("저장소 권한 : %s", flag)
            permissionFlag[1] = permissionFlag[1] || flag
            // Timber.i("flag : %s / status : %s ", it, flag)
        }
        // 카메라 권한
        val cameraFlag = ContextCompat.checkSelfPermission(
            this, Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
        permissionFlag[2] = cameraFlag

        // 위치 권한
        val locationFlag = ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        permissionFlag[3] = locationFlag
    }

    // 다른 액티비티 이동후 결과 값을 받아 핸들링할 런쳐
    private val launcher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { // 액티비티 종료시 결과릴 리턴받기 위한 콜백 함수
        if (it.resultCode == Activity.RESULT_OK) {
            // 데이터 찍어볼경우!
            /*
            val intent = result.data
            val resultState = intent?.getStringExtra("newAlbumName")
            Timber.i("resultState : %s", resultState)
             */
        }
    }
}

@Composable
fun SettingScreenView(
    activity: SettingActivity,
    permissionFlag: Array<Boolean>,
    launcher: () -> Unit
){
    val navController = rememberNavController()
    Scaffold(
        topBar = { SettingTopBarComponent(activity) },
        bottomBar = {  }
    ) {
        Box(Modifier.padding(it)) {
            SettingNavGraph(
                navController = navController,
                permissionFlag = permissionFlag,
                launcher = launcher
            )
        }
    }
}

@Composable
fun SettingTopBarComponent(activity: SettingActivity){
    val settingTabName = stringResource(id = R.string.p_app_setting)
    SimpleTopBar(activity = activity, title = settingTabName)
}

@Composable
fun SettingNavGraph(
    navController: NavHostController,
    permissionFlag: Array<Boolean>,
    launcher: () -> Unit
){
    NavHost(navController = navController, startDestination = SettingRoute.AppSetting.route){
        composable(SettingRoute.AppSetting.route){
            AppSettingPage(permissionFlag, launcher)
        }
    }
}

@Composable
fun AppSettingPage(
    permissionFlag:Array<Boolean>,
    launcher: () -> Unit
){
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
                description = "앱 사용에 필요한 정보와 알림을 수신합니다.\n앱 OS 설정에서 권한을 수정할 수 있습니다.",
                status = permissionFlag[0],
                launcher = launcher
            )
            SettingUnit(
                imgVector = Icons.Rounded.AccountBox,
                settingName = "사진 및 동영상",
                description = "앱에서 사용할 사진 및 동영상 데이터의 열람을 허용합니다.",
                status = permissionFlag[1],
                launcher = launcher
            )
            SettingUnit(
                imgVector = Icons.Rounded.AddCircle, // change
                settingName = "카메라",
                description = "앱에서 휴대전화의 카메라에 접근하는 것을 허용합니다.",
                status = permissionFlag[2],
                launcher = launcher
            )
            SettingUnit(
                imgVector = Icons.Rounded.LocationOn,
                settingName = "위치",
                description = "사용자의 위치 정보를 조회합니다.",
                status = permissionFlag[3],
                launcher = launcher
            )
        }
    }
}

@Composable
fun SettingUnit(
    imgVector: ImageVector,
    settingName:String,
    description:String,
    status:Boolean = false,
    launcher: () -> Unit
){
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
                checked = status,
                onCheckedChange = {
                    launcher()
                }
            )
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
        SettingScreenView(SettingActivity(), Array(4) { false }) {}
    }
}

