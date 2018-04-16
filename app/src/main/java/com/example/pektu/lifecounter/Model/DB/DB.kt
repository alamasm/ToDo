package com.example.pektu.lifecounter.Model.DB

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.pektu.lifecounter.Model.DayDate
import com.example.pektu.lifecounter.Model.Plan
import java.util.*

class DB(private val db: SQLiteDatabase) : DBInterface {
    override fun getPlan(id: Int): Plan {
        val c = db.query(DBHelper.TABLE_NAME, DBHelper.ALL_COLUMNS, "id = ?", listOf(id.toString()).toTypedArray(), null, null, "id")
        c.moveToFirst()
        return getPlan(c)
    }

    override fun getPlans(date: DayDate): List<Plan> {
        val c = db.query(DBHelper.TABLE_NAME, DBHelper.ALL_COLUMNS, "date LIKE '$date'", null, null, null, "id")
        if (!c.moveToFirst()) return emptyList()
        val plans = ArrayList<Plan>()
        do {
            plans.add(getPlan(c))
        } while (c.moveToNext())
        return plans
    }

    private fun getPlan(c: Cursor): Plan {
        val id = c.getInt(c.getColumnIndex("id"))
        val plan = c.getString(c.getColumnIndex("plan"))
        val hours = c.getInt(c.getColumnIndex("hours"))
        val minutes = c.getInt(c.getColumnIndex("minutes"))
        val done = c.getInt(c.getColumnIndex("done")) == 1
        val date = DayDate(c.getString(c.getColumnIndex("date")))
        val doing = c.getInt(c.getColumnIndex("doing")) == 1
        val spentHours = c.getInt(c.getColumnIndex("spent_hours"))
        val spentMinutes = c.getInt(c.getColumnIndex("spent_minutes"))
        val createDate = c.getLong(c.getColumnIndex("create_date"))
        val startDoingDate = c.getLong(c.getColumnIndex("start_doing_date"))
        val lastNotificationTime = c.getLong(c.getColumnIndex("last_notification_time"))
        val undone = c.getInt(c.getColumnIndex("undone")) == 1

        return Plan(id, plan, hours, minutes, done, doing, date, spentHours, spentMinutes, createDate,
                startDoingDate, lastNotificationTime, undone)
    }

    override fun save(plan: String, hours: Int, minutes: Int, done: Boolean, doing: Boolean,
                      date: DayDate, spentHours: Int, spentMinutes: Int, createDate: Long, startDoingDate: Long) {
        db.insert(DBHelper.TABLE_NAME, null, getContentValues(plan, hours, minutes,
                done, doing, date, spentHours, spentMinutes, createDate, startDoingDate))
    }

    private fun getContentValues(plan: String, hours: Int, minutes: Int, done: Boolean, doing: Boolean,
                                 date: DayDate, spentHours: Int, spentMinutes: Int,
                                 createDate: Long, startDoingDate: Long): ContentValues {
        val cv = ContentValues()
        cv.put("plan", plan)
        cv.put("hours", hours)
        cv.put("minutes", minutes)
        cv.put("done", if (done) 1 else 0)
        cv.put("doing", if (doing) 1 else 0)
        cv.put("date", date.toString())
        cv.put("spent_hours", spentHours)
        cv.put("spent_minutes", spentMinutes)
        cv.put("create_date", createDate)
        cv.put("start_doing_date", startDoingDate)
        return cv
    }

    override fun setDone(id: Int) {
        val cv = ContentValues()
        cv.put("done", 1)
        db.update(DBHelper.TABLE_NAME, cv, "id = ?", listOf(id.toString()).toTypedArray())
    }

    override fun setDoing(id: Int) {
        val cv = ContentValues()
        cv.put("doing", 1)
        db.update(DBHelper.TABLE_NAME, cv, "id = ?", listOf(id.toString()).toTypedArray())
    }

    override fun remove(id: Int) {
        db.delete(DBHelper.TABLE_NAME, "id = ?", listOf(id.toString()).toTypedArray())
    }

    override fun update(id: Int, plan: String, hours: Int, minutes: Int, done: Boolean, doing: Boolean,
                        date: DayDate, spentHours: Int, spentMinutes: Int, createDate: Long, startDoingDate: Long) {
        val cv = getContentValues(plan, hours, minutes, done, doing, date, spentHours, spentMinutes, createDate, startDoingDate)

        db.update(DBHelper.TABLE_NAME, cv, "id = $id", null)
    }

    override fun setLastNotificationTime(id: Int, lastNotificationTime: Long) {
        val cv = ContentValues()
        cv.put("last_notification_time", lastNotificationTime)
        db.update(DBHelper.TABLE_NAME, cv, "id = $id", null)
    }

    override fun setUndone(id: Int) {
        val cv = ContentValues()
        cv.put("undone", 1)
        db.update(DBHelper.TABLE_NAME, cv, "id = $id", null)
    }
}