package com.example.pektu.lifecounter.Controller

import com.example.pektu.lifecounter.Model.DayDate
import com.example.pektu.lifecounter.Model.Model
import com.example.pektu.lifecounter.Model.Plan
import com.example.pektu.lifecounter.View.View.Adapters.PlansAdapter
import com.example.pektu.lifecounter.View.View.Interfaces.PlansView
import com.example.pektu.lifecounter.View.View.Interfaces.SetUpView

interface Controller {
    fun getCurrentCalendarDate(): DayDate

    fun onAddPlanButtonClickedInMainActivity()
    fun onCalendarDateChanged(date: DayDate)
    fun onPlanCreatedButtonClicked(plan: Plan)

    fun onPlansViewCreated(date: DayDate, plansView: PlansView)
    fun onPlansViewItemClicked(holder: PlansAdapter.ViewHolder, plan: Plan, position: Int, plansView: PlansView)
    fun onPlansViewItemLongClicked(holder: PlansAdapter.ViewHolder, plan: Plan, position: Int, plansView: PlansView)
    fun onPlansViewResumed(plansView: PlansView)
    fun onAddNewPlanButtonClickedInDaysActivity(plansView: PlansView)
    fun onDayPlansActivityCreatedForAddNewPlan(plansView: PlansView)
    fun onPlansViewClosed()
    fun onFinishSetUp(sleepTimeHour: Int, sleepTimeMinute: Int, setUpView: SetUpView)
    fun onMainViewInited()
    fun onNotificationClicked(date: DayDate)

    fun getModel(): Model

    fun updateCurrentPlansViewList()
}