package com.example.pektu.lifecounter.Controller

import com.example.pektu.lifecounter.Model.DayDate
import com.example.pektu.lifecounter.Model.Model
import com.example.pektu.lifecounter.Model.Plan
import com.example.pektu.lifecounter.Model.Preferences.Preferences
import com.example.pektu.lifecounter.View.View.Adapters.PlansAdapter
import com.example.pektu.lifecounter.View.View.Fragments.PlanEditorFragment
import com.example.pektu.lifecounter.View.View.Interfaces.MainView
import com.example.pektu.lifecounter.View.View.Interfaces.PlanEditorView
import com.example.pektu.lifecounter.View.View.Interfaces.PlansView
import com.example.pektu.lifecounter.View.View.Interfaces.SetUpView

class AndroidController(private val mainView: MainView, private val model: Model) : Controller {
    private lateinit var plansView: PlansView
    private var plansViewOpened = false

    private lateinit var planEditorView: PlanEditorView
    private var planEditorOpened = false

    private lateinit var planToChange: Plan
    private var changingPlan = false

    override fun onAddPlanButtonClickedInMainActivity() {
        mainView.showDayActivity(model.getCurrentDay(), PlansView.REQUEST_CODE_FOR_ADD_NEW_PLAN)
    }

    override fun onCalendarDateChanged(date: DayDate) {
        model.setCurrentCalendarDate(date)
        mainView.showDayActivity(date, PlansView.REQUEST_CODE_NORMAL)
    }

    override fun onDoneButtonClickedInPlanEditor(editorAction: String, planText: String, planTimeHours: Int, planTimeMinutes: Int) {
        val plan: Plan
        when (editorAction) {
            PlanEditorFragment.ARGUMENT_ACTION_TO_ADD_NEW -> {
                plan = Plan(0, planText, planTimeHours, planTimeMinutes, false, false,
                        plansView.date, 0, 0)
                model.addPlan(plan)
            }
            PlanEditorFragment.ARGUMENT_ACTION_TO_CHANGE_PLAN -> {
                plan = Plan(planToChange.id, planText, planTimeHours, planTimeMinutes, planToChange.done,
                        planToChange.doing, planToChange.date, planToChange.spentHours, planToChange.spentMinutes,
                        planToChange.createDate, planToChange.startDoingDate, planToChange.lastNotificationTime,
                        planToChange.undone, planToChange.moved, planToChange.newPlanId, planToChange.spentTimeBeforeMoving)
                model.changePlan(plan)
            }
        }
        planEditorView.close()
    }

    override fun getCurrentCalendarDate(): DayDate {
        return model.getCurrentCalendarDate()
    }

    override fun onPlansViewCreated(plansView: PlansView) {
        this.plansView = plansView
        plansViewOpened = true
        plansView.updatePlansList(model.getPlans(plansView.date))
    }

    override fun onPlansViewClosed() {
        plansViewOpened = false
    }

    override fun onPlansViewItemClicked(holder: PlansAdapter.ViewHolder, plan: Plan, position: Int) {
        if (plan.moved) {
            val newPlan = model.getPlan(plan.newPlanId)
            mainView.showDayActivity(newPlan.date, PlansView.REQUEST_CODE_NORMAL)
        }
        if (plan.done || plan.undone) return
        if (!plan.doing) {
            model.changePlan(Plan(plan.id, plan.plan, plan.timeHours, plan.timeMinutes,
                    plan.done, true, plan.date, plan.spentHours, plan.spentMinutes, plan.createDate,
                    System.currentTimeMillis()))
        } else model.changePlan(Plan(plan.id, plan.plan, plan.timeHours, plan.timeMinutes, true,
                false, plan.date, plan.spentHours, plan.spentMinutes, plan.createDate, plan.startDoingDate))
        plansView.updatePlansList(model.getPlans(plansView.date))
    }

    override fun onPlansViewResumed(plansView: PlansView) {
        this.plansView = plansView
        plansView.updatePlansList(model.getPlans(plansView.date))
    }

    override fun onAddNewPlanButtonClickedInDaysActivity(plansView: PlansView) {
        plansView.showAddNewPlanActivity()
    }

    override fun onDayPlansActivityCreatedForAddNewPlan(plansView: PlansView) {
        plansView.showAddNewPlanActivity()
    }

    override fun onMainViewInited() {
        val sleepTime = model.getSleepTime()

        if (sleepTime.hours == Preferences.INT_IN_PREFS_DEFAULT_VALUE || sleepTime.minutes == Preferences.INT_IN_PREFS_DEFAULT_VALUE) {
            mainView.showFirstSetUpActivity()
        }
    }

    override fun onFinishSetUp(sleepTimeHour: Int, sleepTimeMinute: Int, setUpView: SetUpView) {
        model.saveSleepTime(sleepTimeHour, sleepTimeMinute)
        setUpView.finishSetUp()
    }

    override fun onNotificationClicked(date: DayDate) {
        mainView.showDayActivity(date, PlansView.REQUEST_CODE_NORMAL)
    }

    override fun onChangeSleepTimeButtonClicked() {
        mainView.showFirstSetUpActivity()
    }

    override fun onSettingsButtonClick() {
        mainView.showSettingsActivity()
    }

    override fun onChangePlanContextMenuButtonClicked(plan: Plan) {
        changingPlan = true
        planToChange = plan
        plansView.showChangePlanActivity(plan.plan, plan.timeHours, plan.timeMinutes)
    }

    override fun onRemovePlanContextMenuButtonClicked(plan: Plan) {
        model.removePlan(plan)
        plansView.updatePlansList(model.getPlans(plansView.date))
    }

    override fun onPauseDoingContextMenuButtonClicked(plan: Plan) {
        model.pausePlan(plan)
        plansView.updatePlansList(model.getPlans(plansView.date))
    }

    override fun onAddPlanViewCreated(planEditorView: PlanEditorView) {
        planEditorOpened = true
        this.planEditorView = planEditorView
    }

    override fun onAddPlanViewClosed() {
        planEditorOpened = false
    }

    override fun getModel(): Model {
        return model
    }

    override fun updateCurrentPlansViewList() {
        if (plansViewOpened)
            plansView.updatePlansList(model.getPlans(plansView.date))
    }
}