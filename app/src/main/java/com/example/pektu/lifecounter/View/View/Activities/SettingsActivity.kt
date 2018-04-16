package com.example.pektu.lifecounter.View.View.Activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import com.example.pektu.lifecounter.Controller.ControllerSingleton
import com.example.pektu.lifecounter.R

class SettingsActivity : AppCompatActivity() {

    private lateinit var changeSleepTimeButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        initComponents()
    }

    private fun initComponents() {
        changeSleepTimeButton = findViewById(R.id.settings_activity_change_sleep_time_button)
        changeSleepTimeButton.setOnClickListener({ControllerSingleton.controller.onChangeSleepTimeButtonClicked()})
    }
}
