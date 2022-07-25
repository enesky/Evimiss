package com.eky.evimiss.ui.screens.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.eky.evimiss.main.BottomNavigation
import com.eky.evimiss.main.CALENDAR
import com.eky.evimiss.main.EXPENSE
import com.eky.evimiss.main.NOTES
import com.eky.evimiss.ui.scaffold.FloatingAddButton
import com.eky.evimiss.ui.scaffold.bottomNav.BottomNav
import com.eky.evimiss.ui.screens.calendar.CalendarScreen
import com.eky.evimiss.ui.theme.EvimissTheme
import com.eky.evimiss.ui.theme.primary
import com.eky.evimiss.ui.theme.transparent
import com.eky.evimiss.utils.activity
import com.eky.evimiss.utils.showToast

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    MainScaffold(
        content = { BottomNavigation(navController = navController) },
        navController = navController
    )
}

@Composable
fun MainScaffold(content: @Composable () -> Unit, navController: NavController? = null) {
    val activity = LocalContext.current.activity
    EvimissTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                BottomAppBar(
                    cutoutShape = CircleShape,
                    backgroundColor = transparent
                ) {
                    BottomNav(navController)
                }
            },
            floatingActionButton = {
                FloatingAddButton {
                    when (navController?.currentDestination?.route) {
                        NOTES -> {
                            activity?.showToast(NOTES)
                        }
                        CALENDAR -> {
                            activity?.showToast(CALENDAR)
                        }
                        EXPENSE -> {
                            activity?.showToast(EXPENSE)
                        }
                    }
                }
            },
            isFloatingActionButtonDocked = true,
            backgroundColor = primary
        ) {
            content()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    MainScaffold(
        content = { CalendarScreen() }
    )
}