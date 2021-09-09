package com.enesky.evimiss.ui.scaffold.bottomNav

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.enesky.evimiss.ui.theme.primaryDark
import com.enesky.evimiss.ui.theme.secondary
import com.enesky.evimiss.ui.theme.secondaryLight

@Composable
fun BottomNav(navController: NavController? = null) {
    val screens = listOf(
        BottomNavItem.Notes,
        BottomNavItem.Calendar,
        BottomNavItem.Expense
    )
    Surface(
        color = primaryDark,
        elevation = 8.dp
    ) {
        BottomNavigation(
            modifier = Modifier.padding(end = 72.dp),
            elevation = 0.dp,
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
                    icon = {
                        Icon(
                            painter = painterResource(id = screen.icon),
                            contentDescription = screen.title
                        )
                    },
                    label = {
                        Text(
                            text = screen.title,
                            style = MaterialTheme.typography.caption,
                            textAlign = TextAlign.Center
                        )
                    },
                    selectedContentColor = secondaryLight,
                    unselectedContentColor = secondary,
                    alwaysShowLabel = false,
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
}

@Preview(showBackground = true)
@Composable
fun BottomNavigationBarPreview() {
    BottomNav(navController = rememberNavController())
}