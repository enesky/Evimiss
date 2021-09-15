package com.enesky.evimiss.ui.custom.chart

import androidx.compose.ui.graphics.Color
import com.enesky.evimiss.ui.theme.onError
import com.enesky.evimiss.ui.theme.secondary
import com.enesky.evimiss.ui.theme.secondaryDark
import com.enesky.evimiss.ui.theme.secondaryLight
import org.threeten.bp.LocalDateTime

data class Expense(
    var amount: Float,
    var type: ExpenseType,
    var dateTime: LocalDateTime,
    var details: String,
    var location: String, //LatLng
    var pictures: String //TODO: fatura resmi vb.???
)

sealed class ExpenseType(val type: String, val color: Color) {
    object FUEL : ExpenseType("Benzin", secondary)
    object CLOTHING : ExpenseType("Giyim", secondaryDark)
    object INTERNET_SHOPPING : ExpenseType("Internet Alışverişi", secondaryLight)
    object MARKET : ExpenseType("Market", onError)
    object BILL : ExpenseType("Fatura", Color.White)
    object COSMETIC : ExpenseType("Kozmetik", Color.Cyan)
}
