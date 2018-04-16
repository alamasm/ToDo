package com.example.pektu.lifecounter.Model

import java.text.SimpleDateFormat
import java.util.*

data class DayDate(private val year: Int, private val month: Int, private val day: Int) {

    //constructor(date: Date) : this(date.year, date.month, date.day)
    constructor(calendar: Calendar) : this(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
    constructor(s: String) : this(s.split(":")[0].toInt(), s.split(":")[1].toInt(), s.split(":")[2].toInt())

    override fun toString(): String {
        return "$year:$month:$day"
    }

    fun toStringForUser(): String {
        var monthToView = ""
        var dayToView = ""
        var dayForSDF = ""
        if (month + 1 < 10) monthToView = "0"
        if (day < 10) {
            dayToView = "0"
            dayForSDF = "0"
        }
        monthToView += (month + 1)
        dayToView += day
        dayForSDF += (day - 1)
        val result = "$year.$monthToView.$dayToView"

        val sdf = SimpleDateFormat("yyyy.mm.dd")
        val dayOfWeekFormat = SimpleDateFormat("EEEE", Locale.US)
        val dayOfWeek = dayOfWeekFormat.format(sdf.parse("$year.$monthToView.$dayForSDF"))
        return "$result, $dayOfWeek"
    }
}