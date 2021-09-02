package com.enesky.evimiss.ui.custom.bottomNav

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.enesky.evimiss.ui.theme.primaryDark
import com.enesky.evimiss.ui.theme.secondary
import com.enesky.evimiss.ui.theme.secondaryLight

@Composable
fun BottomNavigationBar(navController: NavController? = null) {
    val items = listOf(
        BottomNavItem.Notes,
        BottomNavItem.Calendar,
        BottomNavItem.More
    )
    BottomNavigation(
        backgroundColor = primaryDark,
        contentColor = secondary
    ) {
        val currentRoute = if (navController != null) {
                               val navBackStackEntry by navController.currentBackStackEntryAsState()
                               navBackStackEntry?.destination?.route
                           } else
                               null

        items.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(painterResource(id = item.icon), contentDescription = item.title) },
                label = { Text(text = item.title) },
                selectedContentColor = secondaryLight,
                unselectedContentColor = secondary,
                alwaysShowLabel = true,
                selected = currentRoute == item.route,
                onClick = {
                    navController?.navigate(item.route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
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
fun BottomNavigationBarPreview() {
    BottomNavigationBar(navController = rememberNavController())
}