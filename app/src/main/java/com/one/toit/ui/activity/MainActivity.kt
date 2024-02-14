package com.one.toit.ui.activity

import android.Manifest
import android.app.Activity
import android.app.TaskInfo
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
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
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.ads.MobileAds
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.coroutine.TedPermission
import com.one.toit.R
import com.one.toit.base.fatory.ApplicationFactory
import com.one.toit.base.ui.BaseComposeActivity
import com.one.toit.data.dto.TaskDTO
import com.one.toit.ui.compose.nav.MainRoute
import com.one.toit.ui.compose.style.MyApplicationTheme
import com.one.toit.ui.compose.style.mono300
import com.one.toit.ui.compose.style.mono500
import com.one.toit.ui.compose.style.mono600
import com.one.toit.ui.compose.style.purple200
import com.one.toit.ui.compose.style.white
import com.one.toit.ui.compose.ui.page.GraphPage
import com.one.toit.ui.compose.ui.page.ProfilePage
import com.one.toit.ui.compose.ui.page.StatisticsPage
import com.one.toit.ui.compose.ui.page.TodoPage
import com.one.toit.ui.viewmodel.MainMenuViewModel
import com.one.toit.ui.viewmodel.TaskInfoViewModel
import com.one.toit.ui.viewmodel.TaskRegisterViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import timber.log.Timber

class MainActivity : BaseComposeActivity(), LifecycleObserver {
    // viewModel
    private lateinit var mainMenuViewModel: MainMenuViewModel
    // 부모 엔티티
    private lateinit var taskRegisterViewModel: TaskRegisterViewModel
    // 자식 엔티티
    private lateinit var taskInfoViewModel: TaskInfoViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MobileAds.initialize(this) {} // 광고 init
        requestPermission(this) // 권한 체크
        setContent {
            MainScreenView(mainMenuViewModel, taskRegisterViewModel, taskInfoViewModel)
        }
    }
    override fun initViewModel() {
        super.initViewModel()
        mainMenuViewModel = getApplicationScopeViewModel(MainMenuViewModel::class.java)
        mainMenuViewModel.init()
        mainMenuViewModel.setPageName(baseContext.getString(R.string.p_todo))
        val factory = ApplicationFactory(this.application)
        taskRegisterViewModel = getApplicationScopeViewModel(TaskRegisterViewModel::class.java, factory)
        taskInfoViewModel = getApplicationScopeViewModel(TaskInfoViewModel::class.java, factory)
    }

    //  권한 요청 함수
    private fun requestPermission(context: Context){
        // API Version < 33
        val ALL_PERMISSION:Array<String> = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.POST_NOTIFICATIONS,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA
        )
        // API Version >= 33
        val ALL_PERMISSION_33:Array<String> = arrayOf(
            Manifest.permission.READ_MEDIA_VIDEO,
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.POST_NOTIFICATIONS,
            Manifest.permission.ACCESS_FINE_LOCATION,
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
    viewModel: MainMenuViewModel,
    taskRegisterViewModel: TaskRegisterViewModel,
    taskInfoViewModel: TaskInfoViewModel
) {
    val navController = rememberNavController()
    Scaffold(
        topBar = { MainTopBarComponent(viewModel) },
        bottomBar = { MainBottomNavigation(navController = navController, menuViewModel = viewModel) }
    ) {
        Box(Modifier.padding(it)){
            MainNavGraph(navController, taskRegisterViewModel, taskInfoViewModel)
        }
    }
}

@Composable
fun MainTopBarComponent(
    viewModel:MainMenuViewModel,
){
    val context = LocalContext.current
    val intent = Intent(context, SettingActivity::class.java)
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(48.dp)
        .background(white)
        .padding(horizontal = 16.dp, vertical = 12.dp)
    ){
        // 타이틀
        Text(
            text = "${viewModel.pageName.value}",
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
    taskRegisterViewModel: TaskRegisterViewModel,
    taskInfoViewModel: TaskInfoViewModel
) {
    NavHost(
        navController,
        startDestination = MainRoute.Todo.route
    )  {
        composable(route = MainRoute.Todo.route) {
            TodoPage(navController, taskRegisterViewModel, taskInfoViewModel)
        }
        composable(MainRoute.Statistics.route) {
            // GraphPage(navController)
            StatisticsPage(navController)
        }
        composable(MainRoute.Profile.route) {
            ProfilePage()
        }
        composable(MainRoute.Graph.route){
            GraphPage(navController)
        }
    }
}

@Composable
fun MainBottomNavigation(
    navController: NavHostController,
    menuViewModel:MainMenuViewModel,

) {
    val items = listOf<MainRoute>(
        MainRoute.Todo,
        MainRoute.Statistics,
        MainRoute.Profile
    )
    BottomNavigation(
        backgroundColor = Color.White,
        contentColor = Color(0xff0a090a)
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
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
                               item.icon as ImageVector,
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
                           item.icon as ImageVector,
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
                        menuViewModel.setPageName(itemTitle)
                        Timber.i("bottom : %s : ", menuViewModel.pageName)
                    }
                }
            )
        }
    }
}