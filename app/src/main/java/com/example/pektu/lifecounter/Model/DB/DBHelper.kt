package com.example.pektu.lifecounter.Model.DB

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, CURRENT_VERSION) {

    companion object {
        val TABLE_NAME = "plans"
        val ALL_COLUMNS = listOf("id", "plan", "hours", "minutes", "done", "date", "doing", "spent_hours",
                "spent_minutes", "create_date", "start_doing_date", "last_notification_time", "undone").toTypedArray()
        private val DB_NAME = "plans_database"
        private val CREATE_DB_SQL = "CREATE TABLE $TABLE_NAME ( id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "plan TEXT, hours INTEGER, minutes INTEGER, done INTEGER, date TEXT, doing INTEGER, spent_hours INTEGER, " +
                "spent_minutes INTEGER, create_date INTEGER, start_doing_date INTEGER, last_notification_time INTEGER, undone INTEGER);"
        private val CURRENT_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL(CREATE_DB_SQL)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}