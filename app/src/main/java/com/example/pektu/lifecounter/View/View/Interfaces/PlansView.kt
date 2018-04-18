package com.example.pektu.lifecounter.View.View.Interfaces

import com.example.pektu.lifecounter.Model.DayDate
import com.example.pektu.lifecounter.Model.Plan

interface PlansView {
    fun updatePlansList(plans: Array<Plan>)
    fun showAddNewPlanActivity()
    var date: DayDate

    companion object {
        const val INTENT_REQUEST_CODE = "1"
        const val INTENT_DATE = "2"
        const val REQUEST_CODE_FOR_ADD_NEW_PLAN = 1
        const val REQUEST_CODE_NORMAL = 0
    }
}