package com.one.toit.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.one.toit.R
import com.one.toit.compose.nav.BoardRoute
import com.one.toit.compose.style.MyApplicationTheme
import com.one.toit.compose.style.mono300
import com.one.toit.compose.style.mono600
import com.one.toit.compose.style.purple200
import com.one.toit.compose.style.white
import com.one.toit.compose.ui.page.ProfilePage
import com.one.toit.compose.ui.page.StatisticsPage
import com.one.toit.compose.ui.page.TodoPage
import timber.log.Timber

class BoardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Timber.plant(Timber.DebugTree())
            BoardScreenView()
        }
    }
}

@Composable
fun BoardScreenView() {
    val navController = rememberNavController()
    Scaffold(
        topBar = { TopBarComponent(navController) },
        bottomBar = { BottomNavigation(navController = navController) }
    ) {
        Box(Modifier.padding(it)){
            BoardNavGraph(navController = navController)
        }
    }
}

@Composable
fun TopBarComponent(navController: NavHostController){
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(48.dp)
        .background(white)
        .padding(horizontal = 16.dp, vertical = 12.dp)
    ){

        Text(
            text = "title",
            style = MaterialTheme.typography.subtitle1
                .copy(
                    fontSize = 16.sp
                ),
            modifier = Modifier
                .align(Alignment.CenterStart)
        )

        // 아이콘
        Icon(
            painter = painterResource(id = R.drawable.ic_setting),
            contentDescription = "환경 설정",
            modifier = Modifier
                .width(24.dp)
                .height(24.dp)
                .align(Alignment.CenterEnd)
                .clickable {
                    val entry = navController.currentBackStackEntry
                    Timber.i("info : %s %s", entry?.destination, entry?.arguments)
                }
        )

    }
}

@Composable
fun BoardNavGraph(navController: NavHostController) {
    NavHost(navController, startDestination = "${BoardRoute.Todo.route}/Toit")  {
        composable(
            route = "${BoardRoute.Todo.route}/{pageName}",
            arguments = listOf(
                navArgument("pageName") {
                    type = NavType.StringType
                    defaultValue = ""
                },

            ),
        ) {
            TodoPage()
        }
        composable(
            route = "${BoardRoute.Statistics.route}/{pageName}",
            arguments = listOf(
                navArgument("pageName") {
                    type = NavType.StringType
                    defaultValue = ""
                },
            ),
        ) {
            StatisticsPage()
        }
        composable(
            route = "${BoardRoute.Profile.route}/{pageName}",
            arguments = listOf(
                navArgument("pageName") {
                    type = NavType.StringType
                    defaultValue = ""
                },
            ),
        ) {
            ProfilePage()
        }
    }
}

@Composable
fun BottomNavigation(navController: NavHostController) {
    val items = listOf<BoardRoute>(
        BoardRoute.Todo,
        BoardRoute.Statistics,
        BoardRoute.Profile
    )

    androidx.compose.material.BottomNavigation(
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
                    navController.navigate(
                        route = "${item.route}/$itemTitle"
                    ) {
                        navController.graph.startDestinationRoute?.let {
                            popUpTo(it) { saveState = true }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        BoardScreenView()
    }
}
