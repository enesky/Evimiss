package com.enesky.evimiss.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.enesky.evimiss.ui.scaffold.FloatingAddButton
import com.enesky.evimiss.ui.scaffold.bottomNav.BottomNav
import com.enesky.evimiss.ui.screens.calendar.CalendarScreen
import com.enesky.evimiss.ui.screens.SplashScreen
import com.enesky.evimiss.ui.theme.EvimissTheme
import com.enesky.evimiss.ui.theme.primary
import com.enesky.evimiss.ui.theme.primaryDark

@ExperimentalAnimationApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NavigationFromSplash()
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun Main() {
    val navController = rememberNavController()
    MainScaffold(
        content = { Navigation(navController = navController) },
        navController = navController
    )
}

@Composable
fun MainScaffold(content: @Composable () -> Unit, navController: NavController? = null) {
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
                FloatingAddButton {
                    /*
                        Todo: Add bottom sheet here
                     */
                }
            },
            isFloatingActionButtonDocked = true,
            backgroundColor = primary
        ) {
            content()
        }
    }
}

@ExperimentalAnimationApi
@Preview(showBackground = true)
@Composable
fun MainPreview() {
    MainScaffold(
        content = { CalendarScreen() }
    )
}

@ExperimentalAnimationApi
@Composable
fun NavigationFromSplash() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController = navController) }
        composable("main") { Main() }
    }
}