package com.one.toit.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Build
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleObserver
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.one.toit.R
import com.one.toit.base.fatory.ApplicationFactory
import com.one.toit.base.ui.BaseComposeActivity
import com.one.toit.data.viewmodel.TaskPointViewModel
import com.one.toit.data.viewmodel.TaskViewModel
import com.one.toit.ui.compose.nav.StatisticsRoute
import com.one.toit.ui.compose.style.mono300
import com.one.toit.ui.compose.style.mono500
import com.one.toit.ui.compose.style.mono600
import com.one.toit.ui.compose.style.purple200
import com.one.toit.ui.compose.style.white
import com.one.toit.ui.compose.ui.page.AllStatisticsPage
import com.one.toit.ui.compose.ui.page.DailyOutlinePage
import com.one.toit.ui.compose.ui.page.WeeklyPage
import com.one.toit.ui.viewmodel.PageViewModel
import timber.log.Timber

class StatisticsActivity :  BaseComposeActivity(), LifecycleObserver{
    // viewModel
    private lateinit var pageViewModel: PageViewModel
    private lateinit var taskViewModel: TaskViewModel
    private lateinit var taskPointViewModel: TaskPointViewModel
    private lateinit var launcher: ActivityResultLauncher<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            StatisticsScreenView(
                pageViewModel, taskViewModel,
                taskPointViewModel, launcher
            )
        }
    }
    override fun initViewModel() {
        super.initViewModel()
        pageViewModel = getApplicationScopeViewModel(PageViewModel::class.java)
        pageViewModel.init()
        val context = this
        pageViewModel.setPageName(context.getString(R.string.title_dilay_outline))
        val factory = ApplicationFactory(this.application)
        taskViewModel = getApplicationScopeViewModel(TaskViewModel::class.java, factory)
        taskPointViewModel = getApplicationScopeViewModel(TaskPointViewModel::class.java, factory)
    }
}

@Composable
fun StatisticsScreenView(
    pageViewModel: PageViewModel,
    taskViewModel: TaskViewModel,
    taskPointViewModel: TaskPointViewModel,
    launcher: ActivityResultLauncher<Intent>? = null
){
    val navController = rememberNavController()
    Scaffold(
        topBar = { StatisticsTopBarComponent(pageViewModel) },
        bottomBar = {
            StatisticsBottomNavigation(navController, pageViewModel)
        }

    ) {
        Box(Modifier.padding(it)){
            StatisticsNavGraph(
                navController, taskViewModel,
                taskPointViewModel, launcher
            )
        }
    }
}
@Composable
fun StatisticsTopBarComponent(
    pageViewModel: PageViewModel,
){
    val activity = (LocalContext.current as? Activity)
    val context = LocalContext.current
    var pageTitle by remember { mutableStateOf("") }
    LaunchedEffect(pageViewModel.pageName.value){
        pageTitle = pageViewModel.pageName.value
    }

    Box(modifier = Modifier
        .fillMaxWidth()
        .height(48.dp)
        .background(white)
        .padding(horizontal = 16.dp, vertical = 12.dp)
    ){
        Icon(
            imageVector = Icons.Rounded.ArrowBack,
            contentDescription = "뒤로 가기",
            modifier = Modifier
                .width(24.dp)
                .height(24.dp)
                .align(Alignment.CenterStart)
                .clickable {
                    activity?.finish()
                },
            tint = mono500
        )
        // 타이틀
        Text(
            text = pageTitle,
            style = MaterialTheme.typography.subtitle1
                .copy(
                    fontSize = 16.sp
                ),
            modifier = Modifier
                .align(Alignment.Center)
        )
    }
}

@Composable
fun StatisticsNavGraph(
    navController: NavHostController,
    taskViewModel: TaskViewModel,
    taskPointViewModel: TaskPointViewModel,
    launcher: ActivityResultLauncher<Intent>? = null
) {
    NavHost(
        navController,
        startDestination = StatisticsRoute.DailyOutline.route
    )  {
        composable(route = StatisticsRoute.DailyOutline.route) {
            DailyOutlinePage(navController, taskViewModel, launcher)
        }
        composable(route = StatisticsRoute.WeeklyPage.route){
            WeeklyPage(navController, taskViewModel, launcher)
        }
        composable(route = StatisticsRoute.AllStatisticsPage.route){
            AllStatisticsPage(navController, taskViewModel, taskPointViewModel, launcher)
        }
    }
}

@Composable
fun StatisticsBottomNavigation(
    navController: NavHostController,
    menuViewModel: PageViewModel,
) {
    val items = listOf(
        StatisticsRoute.DailyOutline,
        StatisticsRoute.WeeklyPage,
        StatisticsRoute.AllStatisticsPage
    )
    val icons = listOf(
        Icons.Rounded.CheckCircle,
        Icons.Rounded.Build,
        Icons.Rounded.LocationOn
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
                                icons[index],
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
                            icons[index],
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