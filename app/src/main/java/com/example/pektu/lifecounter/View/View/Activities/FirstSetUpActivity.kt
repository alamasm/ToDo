package com.example.pektu.lifecounter.View.View.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.widget.TimePicker
import com.example.pektu.lifecounter.Controller.ControllerSingleton
import com.example.pektu.lifecounter.R
import com.example.pektu.lifecounter.View.View.Interfaces.SetUpView

class FirstSetUpActivity : AppCompatActivity(), SetUpView {
    private lateinit var timePicker: TimePicker
    private lateinit var fab: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_set_up)
        initComponents()
    }

    private fun initComponents() {
        timePicker = findViewById(R.id.first_setup_timepicker)
        fab = findViewById(R.id.first_setup_fab)
        timePicker.setIs24HourView(true)
        fab.setOnClickListener({ ControllerSingleton.controller.onFinishSetUp(timePicker.currentHour, timePicker.currentMinute, this) })
    }

    override fun finishSetUp() {
        finish()
    }
}
