package com.example.pektu.lifecounter.Model
import com.example.pektu.lifecounter.Controller.TimeManager
import com.example.pektu.lifecounter.Model.DB.DB
import com.example.pektu.lifecounter.Model.Preferences.Preferences
import java.util.*

class AndroidModel(private val DB: DB, private val preferences: Preferences) : Model {
    private val SLEEP_TIME_HOUR_TAG = "sleep_time_hour"
    private val SLEEP_TIME_MINUTE_TAG = "sleep_time_minute"

    private var currentDate = getCurrentDayDate()
    private var plansForCurrentDayChanged = false
    private var plansBufferForCurrentDateInited = false
    private lateinit var plansBufferForCurrentDate: List<Plan>

    private var sleepTime: HoursAndMinutesTime

    private var currentDateInCalendar = getCurrentDayDate()

    init {
        val sleepTimeHours = preferences.getInt(SLEEP_TIME_HOUR_TAG)
        val sleepTImeMinutes = preferences.getInt(SLEEP_TIME_MINUTE_TAG)
        sleepTime = HoursAndMinutesTime(sleepTimeHours, sleepTImeMinutes)
    }

    private fun getCurrentDayDate(): DayDate {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        return DayDate(calendar)
    }

    override fun addPlan(plan: Plan): Boolean {
        DB.save(plan.plan, plan.timeHours, plan.timeMinutes, plan.done, plan.doing, plan.date,
                plan.spentHours, plan.spentMinutes, plan.createDate, plan.startDoingDate)
        if (plan.date == currentDate) updateBuffer()
        return true
    }

    override fun changePlan(plan: Plan): Boolean {
        DB.update(plan.id, plan.plan, plan.timeHours, plan.timeMinutes, plan.done, plan.doing,
                plan.date, plan.spentHours, plan.spentMinutes, plan.createDate, plan.startDoingDate)
        if (plan.date == currentDate) updateBuffer()
        return true
    }

    override fun removePlan(plan: Plan): Boolean {
        DB.remove(plan.id)
        if (plan.date == currentDate) updateBuffer()
        return true
    }

    override fun getPlan(id: Int): Plan {
        return DB.getPlan(id)
    }

    override fun getPlans(date: DayDate): List<Plan> {
        if (date == currentDate) {
            if (!plansBufferForCurrentDateInited || plansForCurrentDayChanged)
                plansBufferForCurrentDate = DB.getPlans(date)
            plansBufferForCurrentDateInited = true
            return plansBufferForCurrentDate
        }
        return DB.getPlans(date)
    }

    override fun markUndone(planId: Int) {
        DB.setUndone(planId)
        updateBuffer()
    }

    override fun getUndonePlans(date: DayDate): List<Plan> {
        return getPlans(date).filter { !it.done && !it.undone }
    }

    override fun saveSleepTime(hour: Int, minutes: Int): Boolean {
        preferences.saveInt(SLEEP_TIME_HOUR_TAG, hour)
        preferences.saveInt(SLEEP_TIME_MINUTE_TAG, minutes)
        sleepTime = HoursAndMinutesTime(hour, minutes)
        return true
    }

    override fun getSleepTime(): HoursAndMinutesTime {
        return sleepTime
    }

    override fun getCurrentCalendarDate(): DayDate {
        return currentDateInCalendar
    }

    override fun setCurrentCalendarDate(date: DayDate) {
        currentDateInCalendar = date
    }

    override fun getCurrentDay(): DayDate {
        return currentDate
    }

    override fun getPlansForNotify(): List<Plan> {
        if (dateChanged()) {
            currentDate = getCurrentDayDate()
            plansBufferForCurrentDateInited = false
        }
        if (!plansBufferForCurrentDateInited) {
            updateBuffer()
        }

        return if (TimeManager.dayTimeGettingOut()) {
            plansBufferForCurrentDate.filter { !it.done && !it.undone }
        } else {
            emptyList()
        }
    }

    private fun dateChanged(): Boolean {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        val currentDate = DayDate(calendar)
        if (currentDate != this.currentDate) {
            val hours = calendar.get(Calendar.HOUR_OF_DAY)
            val minutes = calendar.get(Calendar.MINUTE)
            val sleepTime = getSleepTime()
            if (hours > sleepTime.hours || (hours == sleepTime.hours && minutes > sleepTime.hours)) {
                return true
            }
            return false
        }
        return false
    }

    override fun updateBuffer() {
        plansBufferForCurrentDate = DB.getPlans(currentDate)
        plansBufferForCurrentDateInited = true
    }

    override fun setPlanSpentTime(plan: Plan, spentHours: Int, spentMinutes: Int): Boolean {
        DB.update(plan.id, plan.plan, plan.timeHours, plan.timeMinutes, plan.done, plan.doing,
                plan.date, spentHours, spentMinutes, plan.createDate, plan.startDoingDate)
        if (plan.date == currentDate) updateBuffer()
        return true
    }

    override fun getSummaryPlansTimeForDate(date: DayDate): Int {
        return getSummaryPlansTimeInMinutes(getPlans(date))
    }

    private fun getSummaryPlansTimeInMinutes(plans: List<Plan>): Int {
        var s = 0
        for ((_, _, timeHours, timeMinutes, done, _, _, spentHours, spentMinutes) in plans) {
            if (done) continue
            s += timeHours * 60
            s += timeMinutes
            s -= spentMinutes
            s -= spentHours * 60
        }
        return s
    }
}