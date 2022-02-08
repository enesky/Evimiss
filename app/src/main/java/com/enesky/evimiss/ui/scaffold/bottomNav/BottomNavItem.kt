package com.enesky.evimiss.ui.scaffold.bottomNav

import androidx.annotation.DrawableRes
import com.enesky.evimiss.R
import com.enesky.evimiss.main.CALENDAR
import com.enesky.evimiss.main.EXPENSE
import com.enesky.evimiss.main.NOTES
import com.enesky.evimiss.utils.getString

sealed class BottomNavItem(var route: String, @DrawableRes var icon: Int, var title: String?) {

    object Notes : BottomNavItem(NOTES, R.drawable.ic_notes, getString(R.string.title_notes))
    object Calendar : BottomNavItem(CALENDAR, R.drawable.ic_calendar, getString(R.string.title_calendar))
    object Expense : BottomNavItem(EXPENSE, R.drawable.ic_expense, getString(R.string.title_expenses))

}