package com.enesky.evimiss.ui.scaffold.bottomNav

import com.enesky.evimiss.R
import com.enesky.evimiss.main.CALENDAR
import com.enesky.evimiss.main.EXPENSE
import com.enesky.evimiss.main.NOTES

sealed class BottomNavItem(var route: String, var icon: Int, var title: String) {
    object Notes : BottomNavItem(NOTES, R.drawable.ic_notes, "Notlar")
    object Calendar : BottomNavItem(CALENDAR, R.drawable.ic_calendar, "Takvim")
    object Expense : BottomNavItem(EXPENSE, R.drawable.ic_expense, "Harcamalar")
}