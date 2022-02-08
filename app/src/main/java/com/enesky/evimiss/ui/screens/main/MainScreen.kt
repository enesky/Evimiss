package com.enesky.evimiss.ui.screens.main

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
import com.enesky.evimiss.main.BottomNavigation
import com.enesky.evimiss.main.CALENDAR
import com.enesky.evimiss.main.EXPENSE
import com.enesky.evimiss.main.NOTES
import com.enesky.evimiss.ui.scaffold.FloatingAddButton
import com.enesky.evimiss.ui.scaffold.bottomNav.BottomNav
import com.enesky.evimiss.ui.screens.expense.ExpenseScreen
import com.enesky.evimiss.ui.theme.EvimissTheme
import com.enesky.evimiss.ui.theme.primary
import com.enesky.evimiss.ui.theme.primaryDark
import com.enesky.evimiss.utils.CalendarUtil
import com.enesky.evimiss.utils.PermissionsUtil
import com.enesky.evimiss.utils.activity
import com.enesky.evimiss.utils.showToast

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

    PermissionsUtil.RequestCalendarPermissions(
        onPermissionsGranted = {
            CalendarUtil.getCalendars(activity?.contentResolver)
            CalendarUtil.getCalendarEventsInGivenDatesMonth(activity?.contentResolver)
        }
    )

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
        content = { ExpenseScreen() }
    )
}