package com.one.toit.ui.activity

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.ads.MobileAds
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.one.toit.R
import com.one.toit.base.fatory.ApplicationFactory
import com.one.toit.base.ui.BaseComposeActivity
import com.one.toit.data.viewmodel.TaskPointViewModel
import com.one.toit.data.viewmodel.TaskViewModel
import com.one.toit.ui.compose.nav.MainRoute
import com.one.toit.ui.compose.style.mono300
import com.one.toit.ui.compose.style.mono500
import com.one.toit.ui.compose.style.mono600
import com.one.toit.ui.compose.style.purple200
import com.one.toit.ui.compose.style.white
import com.one.toit.ui.compose.ui.page.ProfilePage
import com.one.toit.ui.compose.ui.page.TodoPage
import com.one.toit.ui.compose.ui.page.TodoStatusPage
import com.one.toit.ui.viewmodel.PageViewModel
import com.one.toit.util.AppUtil
import timber.log.Timber

class MainActivity : BaseComposeActivity(), LifecycleOwner {
    // viewModel
    private lateinit var pageViewModel: PageViewModel
    private lateinit var taskPointViewModel: TaskPointViewModel;
    private lateinit var taskViewModel: TaskViewModel
    private lateinit var launcher: ActivityResultLauncher<Intent>

    // 업데이트 감지
    private fun checkAppUpdate(){
        val appUpdateManager = AppUpdateManagerFactory.create(this)
        appUpdateManager.registerListener(installListener)
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            // This example applies an immediate update. To apply a flexible update
            // instead, pass in AppUpdateType.FLEXIBLE
            val updateFlag = appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
            if (updateFlag && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)){ // 즉시 업데이트
                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    activityResultLauncher,
                    AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build())
            }else if(updateFlag
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)){ // 유연한 업데이트
                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    activityResultLauncher,
                    AppUpdateOptions.newBuilder(AppUpdateType.FLEXIBLE).build())
            }
            /**
             * 사용자가 앱 포그라운드에 진입했을 때,
             * 업데이트 상에 문제가 있으면 다시 업데이트를 요청하는 메서드
             */
            // 업데이트 실패시
            if (appUpdateInfo.updateAvailability()
                == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS
            ) {
                // If an in-app update is already running, resume the update.
                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    activityResultLauncher,
                    AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build())
            }
        }
    }

    /**
     * 업데이트 결과 수신을 위한 콜백 함수
     */
    private val activityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()) {
            result: ActivityResult ->
        // handle callback
        if (result.resultCode != RESULT_OK) {
            AppUtil.toast(this, this.getString(R.string.msg_update_fail_restart))
            // If the update is canceled or fails,
            // you can request to start the update again.
        }
    }

    /**
     *  인앱 업데이트 상황을 감지하기 위한 리스너,
     *  현재 업데이트 진행 상황을 로깅할 수 있음
     */
    // Create a listener to track request state updates.
    private val installListener = InstallStateUpdatedListener { state ->
        // (Optional) Provide a download progress bar.
        if (state.installStatus() == InstallStatus.DOWNLOADING) { // 다운로드 중일 때!
            val bytesDownloaded = state.bytesDownloaded()
            val totalBytesToDownload = state.totalBytesToDownload()
            // Show update progress bar.
        }else if (state.installStatus() == InstallStatus.DOWNLOADED) { // 다운도르 완료 시
            AppUtil.toast(this, this.getString(R.string.msg_complete_update))
        }
        // Log state or install the update.
    }
    override fun onResume() {
        super.onResume()
        checkAppUpdate() // 업데이트 여부 체크
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MobileAds.initialize(this) {} // 광고 init
        requestPermission(this) // 권한 체크
        // 액티비티 호출용 콜백 함수 런처
        launcher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { // 액티비티 종료시 결과릴 리턴받기 위한 콜백 함수
                result -> Timber.d("onActivityResult.......")
            if (result.resultCode == Activity.RESULT_OK) {
                Timber.i("OK......")
            }else if(result.resultCode == Activity.RESULT_CANCELED){
                Timber.i("CANCEL......")
            }
        }
        setContent {
            MainScreenView(pageViewModel, taskViewModel, taskPointViewModel, launcher)
        }
    }
    override fun initViewModel() {
        super.initViewModel()
        pageViewModel = getApplicationScopeViewModel(PageViewModel::class.java)
        pageViewModel.init()
        pageViewModel.setPageName(baseContext.getString(R.string.p_todo))
        val factory = ApplicationFactory(this.application)
        taskViewModel = getApplicationScopeViewModel(TaskViewModel::class.java, factory)
        taskPointViewModel = getApplicationScopeViewModel(TaskPointViewModel::class.java, factory)
    }

    //  권한 요청 함수
    private fun requestPermission(context: Context){
        // API Version < 33
        val ALL_PERMISSION:Array<String> = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.POST_NOTIFICATIONS,
            Manifest.permission.CAMERA
        )
        // API Version >= 33
        val ALL_PERMISSION_33:Array<String> = arrayOf(
            Manifest.permission.READ_MEDIA_VIDEO,
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.POST_NOTIFICATIONS,
            Manifest.permission.CAMERA
        )
        val permissionList = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            ALL_PERMISSION_33
        }else {
            ALL_PERMISSION
        }
        // 권한 요청 함수
        val permissionsToRequest = mutableListOf<String>()
        // 필요한 권한 중에서 아직 허용되지 않은 권한을 확인
        for (permission in permissionList) {
            if (ContextCompat.checkSelfPermission(context, permission)
                != PackageManager.PERMISSION_GRANTED
            ) {
                permissionsToRequest.add(permission)
            }
        }
        // 권한 요청이 필요한 경우 요청
        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                context as Activity,
                permissionsToRequest.toTypedArray(),
                1001
            )
        }
    }
}
@Composable
fun MainScreenView(
    pageViewModel: PageViewModel,
    taskViewModel: TaskViewModel,
    taskPointViewModel: TaskPointViewModel,
    launcher: ActivityResultLauncher<Intent>? = null
) {
    val navController = rememberNavController()
    Scaffold(
        topBar = { MainTopBarComponent(pageViewModel) },
        bottomBar = { MainBottomNavigation(navController, pageViewModel) }
    ) {
        Box(Modifier.padding(it)){
            MainNavGraph(navController, taskViewModel, taskPointViewModel, launcher)
        }
    }
}
@Composable
fun MainTopBarComponent(
    viewModel:PageViewModel,
){
    val context = LocalContext.current
    val intent = Intent(context, SettingActivity::class.java)
    var pageTitle by remember { mutableStateOf("") }
    LaunchedEffect(viewModel.pageName.value){
        pageTitle = viewModel.pageName.value
    }

    Box(modifier = Modifier
        .fillMaxWidth()
        .height(48.dp)
        .background(white)
        .padding(horizontal = 16.dp, vertical = 12.dp)
    ){
        // 타이틀
        Text(
            text = pageTitle,
            style = MaterialTheme.typography.subtitle1
                .copy(
                    fontSize = 16.sp
                ),
            modifier = Modifier
                .align(Alignment.CenterStart)
        )

        Icon(
            imageVector = Icons.Rounded.Settings,
            contentDescription = "환경 설정",
            modifier = Modifier
                .width(24.dp)
                .height(24.dp)
                .align(Alignment.CenterEnd)
                .clickable {
                    context.startActivity(intent)
                },
            tint = mono500
        )
    }
}
@Composable
fun MainNavGraph(
    navController: NavHostController,
    taskViewModel: TaskViewModel,
    taskPointViewModel: TaskPointViewModel,
    launcher: ActivityResultLauncher<Intent>? = null
) {
    NavHost(
        navController,
        startDestination = MainRoute.Todo.route
    )  {
        composable(route = MainRoute.Todo.route) {
            TodoPage(navController, taskViewModel, launcher)
        }
        composable(MainRoute.TodoStatus.route) {
            // GraphPage(navController)
            TodoStatusPage(navController, taskViewModel, launcher)
        }
        composable(MainRoute.Profile.route) {
            ProfilePage(navController, taskViewModel, taskPointViewModel, launcher)
        }
    }
}

@Composable
fun MainBottomNavigation(
    navController: NavHostController,
    pageViewModel:PageViewModel,
) {
    val items = listOf<MainRoute>(
        MainRoute.Todo,
        MainRoute.TodoStatus,
        MainRoute.Profile
    )
    val iconList = listOf(
        Icons.Rounded.List,
        ImageVector.vectorResource(id = R.drawable.ic_fire),
        Icons.Rounded.Person
    )


    BottomNavigation(
        backgroundColor = Color.White,
        contentColor = Color(0xff0a090a)
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEachIndexed { index, item ->
            val itemTitle = stringResource(id = item.title)
            BottomNavigationItem(
                icon = {
                    if(currentRoute == item.route){
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                                .padding(start = 8.dp, end = 8.dp, top = 0.dp, bottom = 2.dp)
                        ){
                            Box(modifier = Modifier
                                .fillMaxWidth()
                                .height(1.5.dp)
                                .background(purple200)
                                .align(Alignment.TopCenter)
                            )
                            Icon(
                                iconList[index],
                                contentDescription = stringResource(id = item.title),
                                modifier = Modifier
                                    .width(26.dp)
                                    .height(26.dp)
                                    .align(Alignment.Center),
                                tint = purple200
                            )
                        }
                    }else {
                        Icon(
                            iconList[index],
                            contentDescription = stringResource(id = item.title),
                            modifier = Modifier
                                .width(26.dp)
                                .height(26.dp),
                            tint = mono600
                        )
                    }
                },
                selectedContentColor = purple200,
                unselectedContentColor = mono300,
                selected = currentRoute == item.route,
                alwaysShowLabel = false,
                onClick = {
                    navController.navigate(item.route) {
                        navController.graph.startDestinationRoute?.let {
                            popUpTo(it) { saveState = true }
                        }
                        launchSingleTop = true
                        restoreState = true
                        pageViewModel.setPageName(itemTitle)
                        Timber.i("bottom : %s : ", pageViewModel.pageName)
                    }
                }
            )
        }
    }
}