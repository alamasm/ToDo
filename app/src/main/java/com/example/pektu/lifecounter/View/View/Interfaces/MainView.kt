package com.example.pektu.lifecounter.View.View.Interfaces

import com.example.pektu.lifecounter.Model.DayDate

interface MainView {
    fun showStartActivity()
    fun showDayActivity(date: DayDate, requestCode: Int)
    fun showFirstSetUpActivity()
    fun showSettingsActivity()
}