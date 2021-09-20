package com.enesky.evimiss.ui.screens.main

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.enesky.evimiss.main.BottomNavigation
import com.enesky.evimiss.main.MainActivity
import com.enesky.evimiss.ui.scaffold.FloatingAddButton
import com.enesky.evimiss.ui.scaffold.bottomNav.BottomNav
import com.enesky.evimiss.ui.screens.calendar.CalendarScreen
import com.enesky.evimiss.ui.theme.EvimissTheme
import com.enesky.evimiss.ui.theme.primary
import com.enesky.evimiss.ui.theme.primaryDark
import com.enesky.evimiss.utils.CalendarUtil.getCalendarAddEvent
import com.enesky.evimiss.utils.PermissionsUtil
import com.google.accompanist.permissions.ExperimentalPermissionsApi

@ExperimentalPermissionsApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    MainScaffold(
        content = { BottomNavigation(navController = navController) },
        navController = navController
    )
}

@ExperimentalPermissionsApi
@ExperimentalAnimationApi
@Composable
fun MainScaffold(content: @Composable () -> Unit, navController: NavController? = null) {
    val activity = LocalContext.current as MainActivity

    EvimissTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                BottomAppBar(
                    cutoutShape = CircleShape,
                    backgroundColor = primaryDark
                ) {
                    BottomNav(navController)
                }
            },
            floatingActionButton = {
                FloatingAddButton() {
                    activity?.getCalendarAddEvent()
                }
            },
            isFloatingActionButtonDocked = true,
            backgroundColor = primary
        ) {
            content()
        }
    }

    PermissionsUtil().requestPermissions {}
}

@ExperimentalPermissionsApi
@ExperimentalAnimationApi
@Preview(showBackground = true)
@Composable
fun MainPreview() {
    MainScaffold(
        content = { CalendarScreen() }
    )
}