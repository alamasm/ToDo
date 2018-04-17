package com.example.pektu.lifecounter.Model

interface Model {
    fun addPlan(plan: Plan): Int
    fun changePlan(plan: Plan): Boolean
    fun removePlan(plan: Plan): Boolean
    fun getPlan(id: Int): Plan
    fun getPlans(date: DayDate): List<Plan>
    fun markUndone(planId: Int)
    fun getUndonePlans(date: DayDate): List<Plan>
    fun movePlanToNextDay(planId: Int): Boolean

    fun saveSleepTime(hour: Int, minutes: Int): Boolean
    fun getSleepTime(): HoursAndMinutesTime

    fun getCurrentCalendarDate(): DayDate
    fun setCurrentCalendarDate(date: DayDate)

    fun getCurrentDay(): DayDate

    fun getPlansForNotify(): List<Plan>

    fun setPlanSpentTime(plan: Plan, spentHours: Int, spentMinutes: Int): Boolean

    fun updateBuffer()

    fun getSummaryPlansTimeForDate(date: DayDate): Int
}