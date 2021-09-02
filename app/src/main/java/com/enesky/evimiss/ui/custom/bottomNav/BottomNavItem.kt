package com.enesky.evimiss.ui.custom.bottomNav

import com.enesky.evimiss.R

sealed class BottomNavItem(var route: String, var icon: Int, var title: String) {
    object Notes : BottomNavItem("notes", R.drawable.ic_notes, "Notes")
    object Calendar : BottomNavItem("calendar", R.drawable.ic_calendar, "Calendar")
    object More : BottomNavItem("more", R.drawable.ic_apps_corousel, "More")
}