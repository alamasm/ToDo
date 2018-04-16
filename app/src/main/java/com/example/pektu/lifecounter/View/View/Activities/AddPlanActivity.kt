package com.example.pektu.lifecounter.View.View.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.view.KeyEvent
import android.widget.EditText
import android.widget.TimePicker
import com.example.pektu.lifecounter.Controller.ControllerSingleton
import com.example.pektu.lifecounter.Model.DayDate
import com.example.pektu.lifecounter.Model.Plan
import com.example.pektu.lifecounter.R
import com.example.pektu.lifecounter.View.View.Interfaces.PlansView

class AddPlanActivity : AppCompatActivity() {
    private lateinit var fab: FloatingActionButton
    private lateinit var editText: EditText
    private lateinit var timePicker: TimePicker
    private lateinit var date: DayDate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_plan)
        date = DayDate(intent.getStringExtra(PlansView.INTENT_DATE))
        title = "Add plan for ${date.toStringForUser()}"

        initComponents()
    }

    private fun initComponents() {
        fab = findViewById(R.id.add_plan_done_button)
        editText = findViewById(R.id.add_plan_edit_text)
        timePicker = findViewById(R.id.add_plan_time_picker)

        fab.setOnClickListener({ onDone() })
        editText.setOnClickListener { if (editText.text.toString() == getString(R.string.add_plan_activity_description_text)) editText.setText("") }
        editText.setOnKeyListener({ v, code, event ->
            if (code == KeyEvent.KEYCODE_DPAD_CENTER || code == KeyEvent.KEYCODE_ENTER) {
                if (event.action == KeyEvent.ACTION_UP)
                    onDone()
                true
            } else false
        })
        timePicker.setIs24HourView(true)
        timePicker.currentHour = 0
        timePicker.currentMinute = 0
    }

    private fun onDone() {
        val text = editText.text.toString()
        if (text == getString(R.string.add_plan_activity_description_text) || text == "") {
            editText.error = getString(R.string.add_plan_activity_edit_text_error_text)
            return
        }
        val hours = timePicker.currentHour
        val minutes = timePicker.currentMinute
        val plan = Plan(0, text, hours, minutes, false, false, date, 0, 0)
        ControllerSingleton.controller.onPlanCreatedButtonClicked(plan)
        finish()
    }
}
