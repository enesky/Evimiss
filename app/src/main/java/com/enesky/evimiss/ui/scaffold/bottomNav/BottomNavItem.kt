package com.enesky.evimiss.ui.scaffold.bottomNav

import com.enesky.evimiss.R

sealed class BottomNavItem(var route: String, var icon: Int, var title: String) {
    object Notes : BottomNavItem("notes", R.drawable.ic_notes, "Notlar")
    object Calendar : BottomNavItem("calendar", R.drawable.ic_calendar, "Takvim")
    object Expense : BottomNavItem("expense", R.drawable.ic_expense, "Harcamalar")
}