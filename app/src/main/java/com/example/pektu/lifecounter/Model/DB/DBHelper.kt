package com.example.pektu.lifecounter.Model.DB

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, CURRENT_VERSION) {

    companion object {
        private const val CURRENT_VERSION = 1
        private const val DB_NAME = "plans_database"

        const val PLANS_TABLE_NAME = "plans"
        const val DAYS_RATING_TABLE_NAME = "days_rating"

        val ALL_COLUMNS = listOf("id", "plan", "hours", "minutes", "done", "date", "doing", "spent_hours",
                "spent_minutes", "create_date", "start_doing_date", "last_notification_time", "undone", "moved", "new_plan_id", "spent_time_before_move").toTypedArray()

        private const val CREATE_PLANS_TABLE_SQL = "CREATE TABLE $PLANS_TABLE_NAME ( id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "plan TEXT, hours INTEGER, minutes INTEGER, done INTEGER, date TEXT, doing INTEGER, spent_hours INTEGER, " +
                "spent_minutes INTEGER, create_date INTEGER, start_doing_date INTEGER, last_notification_time INTEGER, undone INTEGER, " +
                "moved INTEGER, new_plan_id INTEGER, spent_time_before_move INTEGER);"
        private const val CREATE_DAYS_RATING_TABLE = "CREATE TABLE $DAYS_RATING_TABLE_NAME ( id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "date TEXT, good INTEGER);"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL(CREATE_PLANS_TABLE_SQL)
        db.execSQL(CREATE_DAYS_RATING_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}