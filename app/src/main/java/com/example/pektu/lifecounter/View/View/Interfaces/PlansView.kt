package com.example.pektu.lifecounter.View.View.Interfaces

import com.example.pektu.lifecounter.Model.Plan

interface PlansView {
    fun updatePlansList(plans: Array<Plan>)
    fun showAddNewPlanActivity()

    companion object {
        val INTENT_REQUEST_CODE = "1"
        val INTENT_DATE = "2"
        val REQUEST_CODE_FOR_ADD_NEW_PLAN = 1
        val REQUEST_CODE_NORMAL = 0
    }
}