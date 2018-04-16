package com.example.pektu.lifecounter.Controller.Services

import android.os.Handler
import android.os.Looper
import com.example.pektu.lifecounter.Controller.ControllerSingleton
import java.util.concurrent.TimeUnit

class plansUpdaterService : StickyService() {
    private val controller = ControllerSingleton.controller
    private val model = controller.getModel()

    override fun loop() {
        while (true) {
            val plans = model.getPlans(model.getCurrentDay())
            for (plan in plans) {
                if (!plan.doing) continue
                val currentTimeMillis = System.currentTimeMillis()
                val spentTimeInMinutesMinutes = TimeUnit.MILLISECONDS.toMinutes(currentTimeMillis - plan.startDoingDate).toInt()

                val spentHours = spentTimeInMinutesMinutes / 60
                val spentMinutes = spentTimeInMinutesMinutes % 60
                model.setPlanSpentTime(plan, spentHours, spentMinutes)
            }
            model.updateBuffer()
            Handler(Looper.getMainLooper()).post({ controller.updateCurrentPlansViewList() })
            sleep(60000) // 1 minute
        }
    }
}