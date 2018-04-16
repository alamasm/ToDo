package com.example.pektu.lifecounter.Model.DB

import com.example.pektu.lifecounter.Model.DayDate
import com.example.pektu.lifecounter.Model.Plan
import java.util.*

interface DBInterface {
    fun save(plan: String, hours: Int, minutes: Int, done: Boolean, doing: Boolean, date: DayDate,
             spentHours: Int, spentMinutes: Int, createDate: Long, startDoingDate: Long)
    fun setDone(id: Int)
    fun setDoing(id: Int)
    fun remove(id: Int)
    fun update(id: Int, plan: String, hours: Int, minutes: Int, done: Boolean, doing: Boolean,
               date: DayDate, spentHours: Int, spentMinutes: Int, createDate: Long, startDoingDate: Long)
    fun getPlan(id: Int): Plan
    fun getPlans(date: DayDate): List<Plan>

    fun setLastNotificationTime(id: Int, lastNotificationTime: Long)
    fun setUndone(id: Int)
}