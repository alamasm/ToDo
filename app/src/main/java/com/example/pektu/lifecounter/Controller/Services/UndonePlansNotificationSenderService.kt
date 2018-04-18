package com.example.pektu.lifecounter.Controller.Services

import android.app.IntentService
import android.app.PendingIntent
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import com.example.pektu.lifecounter.Controller.ControllerSingleton
import com.example.pektu.lifecounter.Model.Plan
import com.example.pektu.lifecounter.R

class UndonePlansNotificationSenderService : IntentService("UndonePlansNotificationSenderService") {

    companion object {
        const val TIME_TO_SlEEP_START_SEND_NOTIFICATIONS_MS = 60 * 2000

        const val PLAN_ID = "PLAN_ID"
        const val OPERATION_SEND_NOTIFICATION = "SENT_NOTIFICATION"
        const val OPERATION_ADD_TO_NEXT_DAY = "ADD_TO_NEXT_DAY"
        const val OPERATION_MARK_UNDONE = "MARK_UNDONE"

        const val PLAN_ID_TO_NOTIFICATION_ID_DELTA = 2
    }

    override fun onHandleIntent(intent: Intent?) {
        if (intent == null) return
        if (intent.action != OPERATION_SEND_NOTIFICATION) {
            val planId = intent.getIntExtra(PLAN_ID, -1)
            if (planId == -1) return
            when (intent.action) {
                OPERATION_ADD_TO_NEXT_DAY -> addToNextDay(planId)
                OPERATION_MARK_UNDONE -> markUndone(planId)
            }
        } else {
            sendNotifications()
        }
    }

    private fun sendNotifications() {
        val undonePlans = ControllerSingleton.controller.getModel().getUndonePlans(ControllerSingleton.controller.getModel().getCurrentDay())
        undonePlans.forEach { sendNotification(it) }
    }

    private fun sendNotification(plan: Plan) {
        val notificationBuilder = NotificationCompat.Builder(this)
        notificationBuilder.setContentTitle("Undone plan, choose what to do")

        notificationBuilder.setContentText("${plan.plan} - ${plan.getSpentTimeText()}")

        val notifyIntent = Intent(this, OpenPlansViewService::class.java)
        notifyIntent.putExtra(OpenPlansViewService.INTENT_DATE, ControllerSingleton.controller.getModel().getCurrentCalendarDate().toString())
        val pendingIntent = PendingIntent.getService(this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        notificationBuilder.setContentIntent(pendingIntent)

        notificationBuilder.setSmallIcon(R.drawable.ic_arrow_forward_white_24px)

        val addToNextDayIntent = Intent(this, UndonePlansNotificationSenderService::class.java)
        addToNextDayIntent.putExtra(PLAN_ID, plan.id)
        addToNextDayIntent.action = OPERATION_ADD_TO_NEXT_DAY
        val addToNextDayPendingIntent = PendingIntent.getService(this, plan.id, addToNextDayIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val markUndoneIntent = Intent(this, UndonePlansNotificationSenderService::class.java)
        markUndoneIntent.putExtra(PLAN_ID, plan.id)
        markUndoneIntent.action = OPERATION_MARK_UNDONE
        val markUndonePendingIntent = PendingIntent.getService(this, plan.id, markUndoneIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        notificationBuilder.addAction(R.drawable.ic_arrow_forward_white_24px, "Add to next day", addToNextDayPendingIntent)
        notificationBuilder.addAction(R.drawable.ic_clear_white_24px, "Mark undone", markUndonePendingIntent)
        NotificationManagerCompat.from(this).notify(plan.id + PLAN_ID_TO_NOTIFICATION_ID_DELTA, notificationBuilder.build())
    }

    private fun markUndone(planId: Int) {
        ControllerSingleton.controller.getModel().markUndone(planId)
        cancelNotification(planId + PLAN_ID_TO_NOTIFICATION_ID_DELTA)
        Handler(Looper.getMainLooper()).post({ ControllerSingleton.controller.updateCurrentPlansViewList() })
    }

    private fun addToNextDay(planId: Int) {
        ControllerSingleton.controller.getModel().movePlanToNextDay(planId)
        cancelNotification(planId + PLAN_ID_TO_NOTIFICATION_ID_DELTA)
    }

    private fun cancelNotification(id: Int) {
        NotificationManagerCompat.from(this).cancel(id)
    }
}