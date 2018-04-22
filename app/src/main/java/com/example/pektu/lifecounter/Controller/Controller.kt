package com.example.pektu.lifecounter.Controller

import com.example.pektu.lifecounter.Model.DayDate
import com.example.pektu.lifecounter.Model.Model
import com.example.pektu.lifecounter.Model.Plan
import com.example.pektu.lifecounter.View.View.Adapters.PlansAdapter
import com.example.pektu.lifecounter.View.View.Interfaces.PlanEditorView
import com.example.pektu.lifecounter.View.View.Interfaces.PlansView
import com.example.pektu.lifecounter.View.View.Interfaces.SetUpView

interface Controller {
    fun getCurrentCalendarDate(): DayDate

    fun onAddPlanButtonClickedInMainActivity()
    fun onCalendarDateChanged(date: DayDate)
    //fun onPlanCreatedButtonClicked(plan: Plan)
    fun onDoneButtonClickedInPlanEditor(editorAction: String, planText: String, planTimeHours: Int, planTimeMinutes: Int)

    fun onPlansViewCreated(plansView: PlansView)
    fun onPlansViewItemClicked(holder: PlansAdapter.ViewHolder, plan: Plan, position: Int)
    fun onPlansViewResumed(plansView: PlansView)
    fun onAddNewPlanButtonClickedInDaysActivity(plansView: PlansView)
    fun onDayPlansActivityCreatedForAddNewPlan(plansView: PlansView)
    fun onPlansViewClosed()
    fun onFinishSetUp(sleepTimeHour: Int, sleepTimeMinute: Int, setUpView: SetUpView)
    fun onMainViewInited()
    fun onNotificationClicked(date: DayDate)
    fun onChangeSleepTimeButtonClicked()
    fun onSettingsButtonClick()
    fun onChangePlanContextMenuButtonClicked(plan: Plan)
    fun onRemovePlanContextMenuButtonClicked(plan: Plan)
    fun onAddPlanViewCreated(planEditorView: PlanEditorView)
    fun onAddPlanViewClosed()

    fun getModel(): Model

    fun updateCurrentPlansViewList()
}