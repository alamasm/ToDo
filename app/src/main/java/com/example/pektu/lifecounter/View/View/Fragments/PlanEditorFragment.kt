package com.example.pektu.lifecounter.View.View.Fragments

import android.app.Fragment
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TimePicker
import com.example.pektu.lifecounter.Controller.ControllerSingleton
import com.example.pektu.lifecounter.R

class PlanEditorFragment : Fragment() {
    companion object {
        const val ARGUMENT_ACTION_TAG = "action"
        const val ARGUMENT_ACTION_TO_ADD_NEW = "addNewPlan"
        const val ARGUMENT_ACTION_TO_CHANGE_PLAN = "changePlan"

        const val ARGUMENTS_TIME_PICKER_INIT_HOURS_TAG = "timePickerInitHours"
        const val ARGUMENTS_TIME_PICKER_INIT_MINUTES_TAG = "timePickerInitMinutes"
        const val ARGUMENTS_PLAN_DESCRIPTION_TEXT_TAG = "planDescriptionText"
    }

    private lateinit var doneButton: FloatingActionButton
    private lateinit var planDescriptionEditText: EditText
    private lateinit var timePicker: TimePicker
    private lateinit var currentAction: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_plan_editor, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        currentAction = arguments.getString(ARGUMENT_ACTION_TAG, ARGUMENT_ACTION_TO_ADD_NEW)
        initComponents()
        super.onActivityCreated(savedInstanceState)
    }

    private fun initComponents() {
        doneButton = view!!.findViewById(R.id.plans_editor_done_button)
        doneButton.setOnClickListener({ onDone() })

        timePicker = view!!.findViewById(R.id.plans_editor_time_picker)
        timePicker.setIs24HourView(true)
        timePicker.currentHour = arguments.getInt(ARGUMENTS_TIME_PICKER_INIT_HOURS_TAG, 0)
        timePicker.currentMinute = arguments.getInt(ARGUMENTS_TIME_PICKER_INIT_MINUTES_TAG, 0)

        planDescriptionEditText = view!!.findViewById(R.id.plans_editor_plan_description_edit_text)
        planDescriptionEditText.setOnClickListener({
            if (planDescriptionEditText.text.toString() ==
                    getString(R.string.add_plan_activity_description_text)) planDescriptionEditText.setText("")
        })
        planDescriptionEditText.setOnKeyListener({ view, keyCode, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {
                    onDone()
                    true
                } else
                    false
            } else
                false
        })

        val planDescriptionText = arguments.getString(ARGUMENTS_PLAN_DESCRIPTION_TEXT_TAG, "")
        if (planDescriptionText != "")
            planDescriptionEditText.setText(arguments.getString(ARGUMENTS_PLAN_DESCRIPTION_TEXT_TAG, ""))
    }

    private fun onDone() {
        val text = planDescriptionEditText.text.toString()
        if (text == getString(R.string.add_plan_activity_description_text) || text == "") {
            planDescriptionEditText.error = getString(R.string.add_plan_activity_edit_text_error_text)
            return
        }
        val timeHours = timePicker.currentHour
        val timeMinutes = timePicker.currentMinute
        ControllerSingleton.controller.onDoneButtonClickedInPlanEditor(currentAction, text, timeHours, timeMinutes)
    }
}
