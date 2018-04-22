package com.example.pektu.lifecounter.Model

import java.util.*

data class Plan(val id: Int, val plan: String, val timeHours: Int, val timeMinutes: Int,
                var done: Boolean, val doing: Boolean, val date: DayDate, val spentHours: Int,
                val spentMinutes: Int, val createDate: Long = Date().time, var startDoingDate: Long = -1L,
                var lastNotificationTime: Long = -1L, var undone: Boolean = false, var moved: Boolean = false,
                var newPlanId: Int = -1, var spentTimeBeforeMoving: Int = 0,
                var spentTimeBeforePause: Int = 0) {

    fun getSpentTimeText(): String {
        var spentHours = ""
        var planHours = ""
        var spentMinutes = ""
        var planMinutes = ""

        val spentHoursWithMoveAndPause = this.spentHours + spentTimeBeforeMoving / 60 + spentTimeBeforePause / 60
        val spentMinutesWithMoveAndPause = this.spentMinutes + spentTimeBeforeMoving % 60 + spentTimeBeforePause % 60

        if (spentHoursWithMoveAndPause< 10) spentHours = "0"
        if (spentMinutesWithMoveAndPause < 10) spentMinutes = "0"
        if (this.timeHours < 10) planHours = "0"
        if (this.timeMinutes < 10) planMinutes = "0"
        spentHours += spentHoursWithMoveAndPause
        planHours += this.timeHours
        spentMinutes += spentMinutesWithMoveAndPause
        planMinutes += this.timeMinutes
        return "$spentHours:$spentMinutes/$planHours:$planMinutes"
    }
}