package com.enesky.evimiss.ui.custom.bottomNav

import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.enesky.evimiss.ui.theme.primaryDark
import com.enesky.evimiss.ui.theme.secondary
import com.enesky.evimiss.ui.theme.secondaryLight

@Composable
fun BottomNavigationBar(navController: NavController? = null) {
    val screens = listOf(
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

        screens.forEach { screen ->
            val isSelected = currentRoute == screen.route
            BottomNavigationItem(
                icon = { BottomNavItem(screen = screen, isSelected = isSelected) },
                selectedContentColor = secondaryLight,
                unselectedContentColor = secondary,
                alwaysShowLabel = true,
                selected = isSelected,
                onClick = {
                    navController?.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
fun BottomNavItem(screen: BottomNavItem, isSelected: Boolean) {
    Column(
        horizontalAlignment = CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = screen.icon),
            contentDescription = screen.title
        )
        if (isSelected)
            Text(
                text = screen.title,
                style = MaterialTheme.typography.caption,
                textAlign = TextAlign.Center
                )
    }
}

@Preview(showBackground = true)
@Composable
fun BottomNavigationBarPreview() {
    BottomNavigationBar(navController = rememberNavController())
}