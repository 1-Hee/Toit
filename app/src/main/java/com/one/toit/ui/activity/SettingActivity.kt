package com.one.toit.ui.activity

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountBox
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.one.toit.BuildConfig
import com.one.toit.R
import com.one.toit.base.fatory.ApplicationFactory
import com.one.toit.base.ui.BaseComposeActivity
import com.one.toit.ui.compose.nav.SettingRoute
import com.one.toit.ui.compose.style.MyApplicationTheme
import com.one.toit.ui.compose.style.black
import com.one.toit.ui.compose.style.mono200
import com.one.toit.ui.compose.style.mono400
import com.one.toit.ui.compose.style.mono700
import com.one.toit.ui.compose.style.mono800
import com.one.toit.ui.compose.style.purple200
import com.one.toit.ui.compose.style.purple400
import com.one.toit.ui.compose.style.red400
import com.one.toit.ui.compose.style.white
import com.one.toit.ui.compose.ui.unit.SimpleTopBar
import com.one.toit.ui.compose.ui.unit.WarningDialog
import com.one.toit.data.viewmodel.TaskRegisterViewModel
import com.one.toit.ui.compose.style.mono900
import com.one.toit.util.AppUtil
import com.one.toit.util.PreferenceUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class SettingActivity : BaseComposeActivity() {
    // vm
    private lateinit var taskRegisterViewModel: TaskRegisterViewModel
    // 다른 액티비티 이동후 결과 값을 받아 핸들링할 런쳐
    private lateinit var launcher: ActivityResultLauncher<Intent>

    private val permissionFlag:Array<Boolean> = Array(4){false}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            checkPermission()
            SettingScreenView(this, permissionFlag,
                openSetting = { openAppPermissions() },
                openLicenses = { openOSSActivity() },
                showBackupDialogs = {},
                clearAllAppData = { clearAppAppData(this) },
                removeAdsAction = {}
            )
        }
        // launcer init
        launcher = registerForActivityResult(
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
        // vm init
        val factory = ApplicationFactory(this.application)
        taskRegisterViewModel = getApplicationScopeViewModel(TaskRegisterViewModel::class.java, factory)
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

    }

    // 앱 설정 이동시키는 메서드
    private fun openAppPermissions(){
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", this.packageName, null)
        intent.data = uri
        AppUtil.toast(this, "애플리케이션 정보 > 권한 에서 설정을 변경해주세요")
        launcher.launch(intent)
    }
    // 오픈소스 라이선스 팝업
    private fun openOSSActivity(){
        val intent = Intent(this, OssLicensesMenuActivity::class.java)
        launcher.launch(intent)
    }

    // 앱 설정 초기화
    // 데이터베이스 & prefs 초기화
    private fun clearAppAppData(context:Context){
        // prefs 초기화
        val prefs = PreferenceUtil.getInstance(context)
        prefs.clearAll()
        // db 초기화
        val deleteResult = lifecycleScope.async(Dispatchers.IO){
            taskRegisterViewModel.deleteAll()
        }
        lifecycleScope.launch {
            deleteResult.await()
            // 토스트 메세지
            val clearText = getString(R.string.msg_app_data_clear)
            Toast.makeText(context, clearText, Toast.LENGTH_SHORT).show()
            val intent = Intent(context, StartActivity::class.java)
            launcher.launch(intent)
            setResult(RESULT_CANCELED)
            finish()
        }
    }

}

@Composable
fun SettingScreenView(
    activity: SettingActivity,
    permissionFlag: Array<Boolean>,
    openSetting: () -> Unit,
    openLicenses: () -> Unit,
    showBackupDialogs: () -> Unit,
    clearAllAppData: () -> Unit,
    removeAdsAction:() -> Unit
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
                openSetting = openSetting,
                openLicenses = openLicenses,
                showBackupDialogs = showBackupDialogs,
                clearAllAppData = clearAllAppData,
                removeAdsAction = removeAdsAction
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
    openSetting: () -> Unit,
    openLicenses: () -> Unit,
    showBackupDialogs: () -> Unit,
    clearAllAppData: () -> Unit,
    removeAdsAction:() -> Unit
){
    NavHost(navController = navController, startDestination = SettingRoute.AppSetting.route){
        composable(SettingRoute.AppSetting.route){
            AppSettingPage(
                permissionFlag = permissionFlag,
                openSetting = openSetting,
                openLicenses = openLicenses,
                showBackupDialogs = showBackupDialogs,
                clearAllAppData = clearAllAppData,
                removeAdsAction = removeAdsAction
            )
        }
    }
}

@Composable
fun AppSettingPage(
    permissionFlag:Array<Boolean>,
    openSetting: () -> Unit,
    openLicenses: () -> Unit,
    showBackupDialogs: () -> Unit,
    clearAllAppData: () -> Unit,
    removeAdsAction:() -> Unit,
){
    val context = LocalContext.current
    // 앱 버전
    val appVersionHeader by remember { mutableStateOf(context.getString(R.string.txt_h_app_version)) }
    val appVersion by remember { mutableStateOf(BuildConfig.VERSION_NAME) }
    // 오픈 소스 라이선스
    val ossText by remember { mutableStateOf(context.getString(R.string.txt_oss)) }
    // 앱 데이터 초기화
    val appDataClearText by remember { mutableStateOf(context.getString(R.string.txt_app_data_clear)) }
    // 광고 제거하기
    val removeAdsText by remember { mutableStateOf(context.getString(R.string.txt_remove_ads)) }
    // 설정 복원
    val restoreText by remember { mutableStateOf(context.getString(R.string.txt_app_restore)) }
    // 스크롤
    val scrollState = rememberScrollState()
    // 다이얼로그 창
    var showDeleteDialog by remember { mutableStateOf(false) }
    // 삭제 다이얼로그
    if(showDeleteDialog){
        WarningDialog(
            onDismiss = { showDeleteDialog = false },
            onCancel = { showDeleteDialog = false },
            onAction = {
                showDeleteDialog = false
                clearAllAppData()
            },
            title = stringResource(id = R.string.txt_app_data_clear),
            content = stringResource(R.string.txt_guide_app_data_clear),
            textAction = "초기화"
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(white)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
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
                openSetting = openSetting
            )
            val iconGallery = ImageVector.vectorResource(id = R.drawable.ic_gallery)
            SettingUnit(
                imgVector = iconGallery,
                settingName = "사진 및 동영상",
                description = "앱에서 사용할 사진 및 동영상 데이터의 열람을 허용합니다.",
                status = permissionFlag[1],
                openSetting = openSetting
            )
            val iconProfile = ImageVector.vectorResource(id = R.drawable.ic_camera)
            SettingUnit(
                imgVector = iconProfile, // change
                settingName = "카메라",
                description = "앱에서 휴대전화의 카메라에 접근하는 것을 허용합니다.",
                status = permissionFlag[2],
                openSetting = openSetting
            )
            Spacer(modifier = Modifier.height(24.dp))
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(0.5.dp),
                thickness = 0.5.dp,
                color = mono200
            )
            Spacer(modifier = Modifier.height(24.dp))
            // 앱 버전
            Row (modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ){
                Text(
                    text = appVersionHeader,
                    style = MaterialTheme.typography.caption
                        .copy(
                            color = black,
                            fontSize = 16.sp
                        )
                )
                Text(
                    text = appVersion,
                    style = MaterialTheme.typography.caption
                        .copy(
                            color = mono700,
                            fontSize = 16.sp
                        )
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            // 오픈 소스 라이선스
            Text(
                text = ossText,
                style = MaterialTheme.typography.caption
                    .copy(
                        color = black,
                        fontSize = 16.sp
                ),
                modifier = Modifier
                    .wrapContentSize()
                    .padding(vertical = 8.dp)
                    .clickable { openLicenses() }
            )
            // TODO 개인 정보 처리 방침
            Spacer(modifier = Modifier.height(16.dp))
            // 앱 설정 복원하기
            Text(
                text = restoreText,
                style = MaterialTheme.typography.caption
                    .copy(
                        color = black,
                        fontSize = 16.sp
                    ),
                modifier = Modifier
                    .wrapContentSize()
                    .padding(vertical = 8.dp)
                    .clickable { showBackupDialogs() }
            )
            Spacer(modifier = Modifier.height(16.dp))
            // 앱 데이터 초기화
            Text(
                text = appDataClearText,
                style = MaterialTheme.typography.caption
                    .copy(
                        color = red400,
                        fontSize = 16.sp
                    ),
                modifier = Modifier
                    .wrapContentSize()
                    .padding(vertical = 8.dp)
                    .clickable {
                        showDeleteDialog = true
                    }
            )
            // TODO 표시 문구 추가하기..?
            Spacer(modifier = Modifier.height(16.dp))
            // 광고 제거 하기
            Text(
                text = removeAdsText,
                style = MaterialTheme.typography.caption
                    .copy(
                        color = black,
                        fontSize = 16.sp
                    ),
                modifier = Modifier
                    .wrapContentSize()
                    .padding(vertical = 8.dp)
                    .clickable { removeAdsAction() }
            )
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun SettingUnit(
    imgVector: ImageVector,
    settingName:String,
    description:String,
    status:Boolean = false,
    openSetting: () -> Unit
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
                    modifier = Modifier.size(20.dp),
                    tint = mono900
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
                    openSetting()
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = purple400,
                    checkedTrackColor = purple200,
                    uncheckedThumbColor = mono400,
                    uncheckedTrackColor = mono200
                )
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
        SettingScreenView(SettingActivity(), Array(4) { false },{},{},{},{},{})
    }
}

