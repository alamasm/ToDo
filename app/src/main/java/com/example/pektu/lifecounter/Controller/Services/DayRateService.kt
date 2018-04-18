package com.example.pektu.lifecounter.Controller.Services

import android.app.IntentService
import android.app.PendingIntent
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import com.example.pektu.lifecounter.Controller.ControllerSingleton
import com.example.pektu.lifecounter.Model.DayDate
import com.example.pektu.lifecounter.R

class DayRateService: IntentService("RateDateService") {
    companion object {
        const val NOTIFICATION_ID = 0
        const val INTENT_ACTION_SEND_NOTIFICATION = "SEND_NOTIFICATION"
        const val INTENT_ACTION_RATE_DAY = "RATE_DAY"
        const val INTENT_DATE_EXTRA_NAME = "DATE"
        const val INTENT_GOOD_DAY_EXTRA_NAME = "GOOD"

        const val TIME_TO_START_SEND_NOTIFICATION_DELTA_MS = 1000 * 60 * 5
    }
    override fun onHandleIntent(intent: Intent?) {
        if (intent == null) return
        when (intent.action) {
            INTENT_ACTION_SEND_NOTIFICATION -> {
                val date = DayDate(intent.getStringExtra(INTENT_DATE_EXTRA_NAME))
                sendNotification(date)
            }
            INTENT_ACTION_RATE_DAY -> {
                val good = intent.getBooleanExtra(INTENT_GOOD_DAY_EXTRA_NAME, false)
                val date = DayDate(intent.getStringExtra(INTENT_DATE_EXTRA_NAME))
                rateDay(date, good)
            }
        }
    }

    private fun sendNotification(date: DayDate) {
        val notificationBuilder = NotificationCompat.Builder(this)
        notificationBuilder.setContentTitle("How was your day?")

        notificationBuilder.setContentText("")

        val notifyIntent = Intent(this, OpenPlansViewService::class.java)
        val pendingIntent = PendingIntent.getService(this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        notificationBuilder.setContentIntent(pendingIntent)

        notificationBuilder.setSmallIcon(R.drawable.ic_arrow_forward_white_24px)

        val rateDayGoodIntent = Intent(this, DayRateService::class.java)
        rateDayGoodIntent.action = INTENT_ACTION_RATE_DAY
        rateDayGoodIntent.putExtra(INTENT_GOOD_DAY_EXTRA_NAME, true)
        rateDayGoodIntent.putExtra(INTENT_DATE_EXTRA_NAME, date.toString())
        val rateDayGoodPendingIntent = PendingIntent.getService(this, 0, rateDayGoodIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val rateDayBadIntent = Intent(this, DayRateService::class.java)
        rateDayBadIntent.action = INTENT_ACTION_RATE_DAY
        rateDayBadIntent.putExtra(INTENT_DATE_EXTRA_NAME, date.toString())
        val rateDayBadPendingIntent = PendingIntent.getService(this, 1, rateDayBadIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        notificationBuilder.addAction(R.drawable.ic_arrow_forward_white_24px, "Good", rateDayGoodPendingIntent)
        notificationBuilder.addAction(R.drawable.ic_clear_white_24px, "Bad", rateDayBadPendingIntent)
        NotificationManagerCompat.from(this).notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun rateDay(date: DayDate, good: Boolean) {
        ControllerSingleton.controller.getModel().rateDay(date, good)
        NotificationManagerCompat.from(this).cancel(NOTIFICATION_ID)
    }
}