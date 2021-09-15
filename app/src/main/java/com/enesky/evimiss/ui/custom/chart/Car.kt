package com.enesky.evimiss.ui.custom.chart

data class Car(
    var name: String = "Renault Megane Joy Comfort 2021",
    var fuelTank: Float = 50f, //litre
    var fuelType: String = "Benzin",
)

data class Refuel(
    var expense: Expense,
    var fuel: Float, //litre
    var price: Float,
    var takenLitre: Float = expense.amount/price,
    var isTankFulled: Boolean = false,
    var discount: Float,
    var station: String,
)
