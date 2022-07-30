package com.eky.evimiss.main

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.eky.evimiss.ui.custom.calendar.viewmodel.MyCalendarVM
import com.eky.evimiss.ui.scaffold.bottomNav.BottomNavItem
import com.eky.evimiss.ui.screens.SplashScreen
import com.eky.evimiss.ui.screens.calendar.CalendarScreen
import com.eky.evimiss.ui.screens.expense.ExpenseScreen
import com.eky.evimiss.ui.screens.login.LoginScreen
import com.eky.evimiss.ui.screens.main.MainScreen
import com.eky.evimiss.ui.screens.notes.NotesScreen

const val SPLASH = "splash"
const val LOGIN = "login"
const val MAIN = "main"
const val CALENDAR = "calendar"
const val NOTES = "notes"
const val EXPENSE = "expense"

@Composable
fun BottomNavigation(navController: NavHostController) {
    NavHost(navController, startDestination = BottomNavItem.Calendar.route) {
        composable(BottomNavItem.Notes.route) { NotesScreen() }
        composable(BottomNavItem.Calendar.route) {
            val calendarVM = hiltViewModel<MyCalendarVM>()
            CalendarScreen(calendarVM)
        }
        composable(BottomNavItem.Expense.route) { ExpenseScreen() }
    }
}

@Composable
fun NavigationFromSplash() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = SPLASH) {
        composable(SPLASH) { SplashScreen(navController) }
        composable(LOGIN) { LoginScreen(navController) }
        composable(MAIN) { MainScreen() }
    }
}