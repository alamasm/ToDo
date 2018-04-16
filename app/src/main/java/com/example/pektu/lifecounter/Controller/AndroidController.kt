package com.example.pektu.lifecounter.Controller

import com.example.pektu.lifecounter.Model.DayDate
import com.example.pektu.lifecounter.Model.Model
import com.example.pektu.lifecounter.Model.Plan
import com.example.pektu.lifecounter.Model.Preferences.Preferences
import com.example.pektu.lifecounter.View.View.Adapters.PlansAdapter
import com.example.pektu.lifecounter.View.View.Interfaces.PlansView
import com.example.pektu.lifecounter.View.View.Interfaces.MainView
import com.example.pektu.lifecounter.View.View.Interfaces.SetUpView

class AndroidController(private val mainView: MainView, private val model: Model) : Controller {
    private lateinit var plansView: PlansView
    private var plansViewOpened = false

    override fun onAddPlanButtonClickedInMainActivity() {
        mainView.showDayActivity(model.getCurrentDay(), PlansView.REQUEST_CODE_FOR_ADD_NEW_PLAN)
    }

    override fun onCalendarDateChanged(date: DayDate) {
        model.setCurrentCalendarDate(date)
        mainView.showDayActivity(date, PlansView.REQUEST_CODE_NORMAL)
    }

    override fun onPlanCreatedButtonClicked(plan: Plan) {
        model.addPlan(plan)
    }

    override fun getCurrentCalendarDate(): DayDate {
        return model.getCurrentCalendarDate()
    }

    override fun onPlansViewCreated(date: DayDate, plansView: PlansView) {
        this.plansView = plansView
        plansViewOpened = true
        plansView.updatePlansList(model.getPlans(date).toTypedArray())
    }

    override fun onPlansViewClosed() {
        plansViewOpened = false
    }

    override fun onPlansViewItemClicked(holder: PlansAdapter.ViewHolder, plan: Plan, position: Int, plansView: PlansView) {
        if (plan.done) return
        if (!plan.doing) {
            model.changePlan(Plan(plan.id, plan.plan, plan.timeHours, plan.timeMinutes,
                    plan.done, true, plan.date, plan.spentHours, plan.spentMinutes, plan.createDate,
                    System.currentTimeMillis()))
        } else model.changePlan(Plan(plan.id, plan.plan, plan.timeHours, plan.timeMinutes, true,
                false, plan.date, plan.spentHours, plan.spentMinutes, plan.createDate, plan.startDoingDate))
        plansView.updatePlansList(model.getPlans(model.getCurrentCalendarDate()).toTypedArray())
    }

    override fun onPlansViewItemLongClicked(holder: PlansAdapter.ViewHolder, plan: Plan, position: Int, plansView: PlansView) {

    }

    override fun onPlansViewResumed(plansView: PlansView) {
        this.plansView = plansView
        plansView.updatePlansList(model.getPlans(model.getCurrentCalendarDate()).toTypedArray())
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

    override fun getModel(): Model {
        return model
    }

    override fun updateCurrentPlansViewList() {
        if (plansViewOpened)
            plansView.updatePlansList(model.getPlans(model.getCurrentDay()).toTypedArray())
    }
}