package com.example.pektu.lifecounter.Model

import java.util.*

data class Plan(val id: Int, val plan: String, val timeHours: Int, val timeMinutes: Int,
                var done: Boolean, val doing: Boolean, val date: DayDate, val spentHours: Int,
                val spentMinutes: Int, val createDate: Long = Date().time, var startDoingDate: Long = -1L,
                var lastNotificationTime: Long = -1L, var undone: Boolean = false) {

    fun getSpentTimeText(): String {
        var spentHours = ""
        var planHours = ""
        var spentMinutes = ""
        var planMinutes = ""

        if (this.spentHours < 10) spentHours = "0"
        if (this.spentMinutes < 10) spentMinutes = "0"
        if (this.timeHours < 10) planHours = "0"
        if (this.timeMinutes < 10) planMinutes = "0"
        spentHours += this.spentHours
        planHours += this.timeHours
        spentMinutes += this.spentMinutes
        planMinutes += this.timeMinutes
        return "$spentHours:$spentMinutes/$planHours:$planMinutes"
    }
}