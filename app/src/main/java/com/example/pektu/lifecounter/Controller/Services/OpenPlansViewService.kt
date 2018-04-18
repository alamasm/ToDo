package com.example.pektu.lifecounter.Controller.Services

import android.app.IntentService
import android.content.Intent
import com.example.pektu.lifecounter.Controller.ControllerSingleton
import com.example.pektu.lifecounter.Model.DayDate

class OpenPlansViewService: IntentService("OpenPlansViewService") {
    companion object {
        const val INTENT_DATE = "0"
    }
    override fun onHandleIntent(intent: Intent?) {
        if (intent == null) return
        val date = DayDate(intent.getStringExtra(INTENT_DATE))
        openPlansView(date)
    }

    private fun openPlansView(date: DayDate) {
        ControllerSingleton.controller.onNotificationClicked(date)
    }

}