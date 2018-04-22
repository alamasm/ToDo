package com.example.pektu.lifecounter.View.View.Activities

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import com.example.pektu.lifecounter.Controller.ControllerSingleton
import com.example.pektu.lifecounter.Model.DayDate
import com.example.pektu.lifecounter.Model.Plan
import com.example.pektu.lifecounter.R
import com.example.pektu.lifecounter.View.View.Adapters.PlansAdapter
import com.example.pektu.lifecounter.View.View.Interfaces.PlansView

class DayPlansActivity : AppCompatActivity(), PlansView {
    override lateinit var date: DayDate

    private lateinit var fab: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PlansAdapter
    private var adapterInited = false

    companion object {
        const val CONTEXT_MENU_CHANGE_BUTTON_ID = 0
        const val CONTEXT_MENU_REMOVE_BUTTON_ID = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_day_plans)

        date = DayDate(intent.getStringExtra(PlansView.INTENT_DATE))
        initComponents()
        ControllerSingleton.controller.onPlansViewCreated(this)
        title = "Plans for ${date.toStringForUser()}"
        if (intent.extras.getInt(PlansView.INTENT_REQUEST_CODE) == PlansView.REQUEST_CODE_FOR_ADD_NEW_PLAN) {
            ControllerSingleton.controller.onDayPlansActivityCreatedForAddNewPlan(this)
        }
    }

    private fun initComponents() {
        fab = findViewById(R.id.day_plans_fab)
        fab.setOnClickListener { ControllerSingleton.controller.onAddNewPlanButtonClickedInDaysActivity(this) }

        recyclerView = findViewById(R.id.plans_recycler_view)
        recyclerView.addOnScrollListener(PlansOnScrollListener())

        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager
        registerForContextMenu(recyclerView)
    }

    override fun onContextItemSelected(item: MenuItem?): Boolean {
        if (item == null) return false
        when (item.itemId) {
            CONTEXT_MENU_REMOVE_BUTTON_ID -> ControllerSingleton.controller.onRemovePlanContextMenuButtonClicked(adapter.currentLongClickPlan)
            CONTEXT_MENU_CHANGE_BUTTON_ID -> ControllerSingleton.controller.onChangePlanContextMenuButtonClicked(adapter.currentLongClickPlan)
        }
        return true
    }

    override fun showAddNewPlanActivity() {
        val intent = Intent(this, PlanEditorActivity::class.java)
        intent.putExtra(PlansView.INTENT_DATE, date.toString())
        intent.action = PlanEditorActivity.INTENT_ACTION_ADD_NEW_PLAN
        startActivity(intent)
    }

    override fun showChangePlanActivity(planDescription: String, planTimeHours: Int, planTimeMinutes: Int) {
        val intent = Intent(this, PlanEditorActivity::class.java)
        intent.putExtra(PlansView.INTENT_DATE, date.toString())
        intent.action = PlanEditorActivity.INTENT_ACTION_CHANGE_PLAN
        intent.putExtra(PlanEditorActivity.INTENT_PLAN_DESCRIPTION, planDescription)
        intent.putExtra(PlanEditorActivity.INTENT_PLAN_TIME_HOURS, planTimeHours)
        intent.putExtra(PlanEditorActivity.INTENT_PLAN_TIME_MINUTES, planTimeMinutes)
        startActivity(intent)
    }

    override fun onPostResume() {
        super.onPostResume()
        ControllerSingleton.controller.onPlansViewResumed(this)
    }

    override fun onBackPressed() {
        ControllerSingleton.controller.onPlansViewClosed()
        finish()
        super.onBackPressed()
    }

    override fun updatePlansList(plans: List<Plan>) {
        if (!adapterInited) {
            adapter = PlansAdapter(plans, this)
            recyclerView.setHasFixedSize(true)
            val layoutManager = LinearLayoutManager(this)
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = adapter

            adapterInited = true
        } else {
            adapter.updateData(plans)
        }
    }

    inner class PlansOnScrollListener : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            if (dy > 0) {
                fab.hide()
            } else {
                fab.show()
            }
            super.onScrolled(recyclerView, dx, dy)
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
        }
    }
}
