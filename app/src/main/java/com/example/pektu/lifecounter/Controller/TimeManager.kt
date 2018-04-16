package com.example.pektu.lifecounter.Controller

import com.example.pektu.lifecounter.Controller.Services.DoPlanNotificationSenderService
import java.util.*

class TimeManager {
    companion object {
        var dayRemainingTime = 0

        fun dayTimeGettingOut(): Boolean {
            val sleepTime = ControllerSingleton.controller.getModel().getSleepTime()
            var sleepTimeHours = sleepTime.hours
            if (sleepTimeHours == 0) sleepTimeHours = 24
            val sleepTimeMinutes = sleepTime.minutes
            val calendar = Calendar.getInstance()
            calendar.time = Date()
            val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
            val currentMinute = calendar.get(Calendar.MINUTE)

            val summaryPlansTime = ControllerSingleton.controller.getModel().getSummaryPlansTimeForDate(ControllerSingleton.controller.getModel().getCurrentDay())

            val remainingTime = if (sleepTimeHours < 9) {
                (24 - currentHour + sleepTimeHours) * 60 + 60 - currentMinute + sleepTimeMinutes
            } else {
                (sleepTimeHours - currentHour) * 60 + sleepTimeMinutes - currentMinute
            }

            dayRemainingTime = remainingTime
            if (dayRemainingTime < 0) dayRemainingTime = 0
            return (dayRemainingTime - summaryPlansTime <=
                    DoPlanNotificationSenderService.START_SEND_NOTIFICATION_TIME_DELTA_MINUTES) && dayRemainingTime < 3 * 60
        }
    }
}