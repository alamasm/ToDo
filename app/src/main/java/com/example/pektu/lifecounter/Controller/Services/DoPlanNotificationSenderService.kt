package com.example.pektu.lifecounter.Controller.Services

import android.app.PendingIntent
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import com.example.pektu.lifecounter.Controller.ControllerSingleton
import com.example.pektu.lifecounter.Controller.TimeManager
import com.example.pektu.lifecounter.Model.Plan
import com.example.pektu.lifecounter.R

class DoPlanNotificationSenderService : StickyService() {
    val model = ControllerSingleton.controller.getModel()

    companion object {
        val SEND_NOTIFICATION_DELTA_MS = 1000L //10 minutes
        val START_SEND_NOTIFICATION_TIME_DELTA_MINUTES = 30
        val NOTIFICATION_ID = 0
    }

    override fun loop() {
        while (true) {
            val plans = model.getPlansForNotify()
            if (plans.isEmpty()) sleep(SEND_NOTIFICATION_DELTA_MS)
            else {
                sendNotification(plans, NOTIFICATION_ID)
                sleep(SEND_NOTIFICATION_DELTA_MS)
            }
        }
    }

    private fun sendNotification(plans: List<Plan>, notificationId: Int) {
        val remainingTimeHours = TimeManager.dayRemainingTime / 60
        val remainingTimeMinutes = TimeManager.dayRemainingTime % 60

        val notificationBuilder = NotificationCompat.Builder(this)
        notificationBuilder.setContentTitle("You have $remainingTimeHours h and $remainingTimeMinutes m to do this plans")
        notificationBuilder.setContentText("...")

        val notifyIntent = Intent(this, OpenPlansViewService::class.java)
        notifyIntent.putExtra(OpenPlansViewService.INTENT_DATE, ControllerSingleton.controller.getModel().getCurrentCalendarDate().toString())
        val pendingIntent = PendingIntent.getService(this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        notificationBuilder.setSmallIcon(R.drawable.ic_arrow_forward_black_24dp)
        notificationBuilder.setContentIntent(pendingIntent)
        val inboxStyle = NotificationCompat.InboxStyle()

        var i = 1
        plans.forEach { inboxStyle.addLine("${i++}) ${it.plan} - ${it.getSpentTimeText()}") }

        notificationBuilder.setStyle(inboxStyle)
        val notification = notificationBuilder.build()
        val manager = NotificationManagerCompat.from(this)
        manager.notify(notificationId, notification)
    }
}