package com.example.pektu.lifecounter.View.View.Activities

import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.pektu.lifecounter.Controller.ControllerSingleton
import com.example.pektu.lifecounter.Model.DayDate
import com.example.pektu.lifecounter.R
import com.example.pektu.lifecounter.View.View.Fragments.PlanEditorFragment
import com.example.pektu.lifecounter.View.View.Interfaces.PlanEditorView
import com.example.pektu.lifecounter.View.View.Interfaces.PlansView

class PlanEditorActivity : AppCompatActivity(), PlanEditorView {
    companion object {
        const val INTENT_ACTION_ADD_NEW_PLAN = "addNewPlan"
        const val INTENT_ACTION_CHANGE_PLAN = "changePlan"
        const val INTENT_PLAN_DESCRIPTION = "planDescription"
        const val INTENT_PLAN_TIME_HOURS = "planTimeHours"
        const val INTENT_PLAN_TIME_MINUTES = "planTimeMinutes"
    }

    private lateinit var date: DayDate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_plan)

        ControllerSingleton.controller.onAddPlanViewCreated(this)
        date = DayDate(intent.getStringExtra(PlansView.INTENT_DATE))

        if (intent.action == INTENT_ACTION_CHANGE_PLAN) initComponentsForChangePlan(intent)
        else initComponentsForAddNewPlan()
    }

    private fun initComponentsForAddNewPlan() {
        title = "Add plan for ${date.toStringForUser()}"

        val fragment = PlanEditorFragment()
        val arguments = Bundle()
        arguments.putString(PlanEditorFragment.ARGUMENT_ACTION_TAG, PlanEditorFragment.ARGUMENT_ACTION_TO_ADD_NEW)
        fragment.arguments = arguments
        changeFragment(fragment)
    }

    private fun initComponentsForChangePlan(intent: Intent) {
        title = "Edit plan, ${date.toStringForUser()}"

        val planDescription = intent.getStringExtra(INTENT_PLAN_DESCRIPTION)
        val planTimeHours = intent.getIntExtra(INTENT_PLAN_TIME_HOURS, 0)
        val planTimeMinutes = intent.getIntExtra(INTENT_PLAN_TIME_MINUTES, 0)
        val arguments = Bundle()
        arguments.putString(PlanEditorFragment.ARGUMENT_ACTION_TAG, PlanEditorFragment.ARGUMENT_ACTION_TO_CHANGE_PLAN)
        arguments.putString(PlanEditorFragment.ARGUMENTS_PLAN_DESCRIPTION_TEXT_TAG, planDescription)
        arguments.putInt(PlanEditorFragment.ARGUMENTS_TIME_PICKER_INIT_HOURS_TAG, planTimeHours)
        arguments.putInt(PlanEditorFragment.ARGUMENTS_TIME_PICKER_INIT_MINUTES_TAG, planTimeMinutes)

        val fragment = PlanEditorFragment()
        fragment.arguments = arguments
        changeFragment(fragment)
    }

    private fun changeFragment(fragment: Fragment) {
        fragmentManager.beginTransaction().replace(R.id.add_plan_activity_layout, fragment).commit()
    }

    override fun close() {
        ControllerSingleton.controller.onAddPlanViewClosed()
        finish()
    }
}
