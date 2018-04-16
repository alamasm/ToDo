package com.example.pektu.lifecounter.View.View.Activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.example.pektu.lifecounter.Controller.ControllerSingleton
import com.example.pektu.lifecounter.Model.DayDate
import com.example.pektu.lifecounter.Model.Plan
import com.example.pektu.lifecounter.R
import com.example.pektu.lifecounter.View.View.Adapters.PlansAdapter
import com.example.pektu.lifecounter.View.View.Interfaces.PlansView

class DayPlansActivity : AppCompatActivity(), PlansView {

    private lateinit var fab: FloatingActionButton
    private lateinit var date: DayDate
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PlansAdapter
    private var adapterInited = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_day_plans)

        date = DayDate(intent.getStringExtra(PlansView.INTENT_DATE))
        //val s = intent.getStringExtra(INTENT_DATE)
        //date = ControllerSingleton.controller.getCurrentCalendarDate()
        initComponents()
        ControllerSingleton.controller.onPlansViewCreated(date, this)
        title = "Plans for ${ControllerSingleton.controller.getCurrentCalendarDate().toStringForUser()}"
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
    }

    override fun showAddNewPlanActivity() {
        val intent = Intent(this, AddPlanActivity::class.java)
        intent.putExtra(PlansView.INTENT_DATE, date.toString())
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

    override fun updatePlansList(plans: Array<Plan>) {
        if (!adapterInited) {
            adapter = PlansAdapter(plans, this)
            recyclerView.setHasFixedSize(true)
            val layoutManager = LinearLayoutManager(this)
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = adapter

            adapterInited = true
        } else {
            adapter.plans = plans
            adapter.notifyDataSetChanged()
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
