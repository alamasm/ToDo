package com.example.pektu.lifecounter.View.View.Adapters

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.pektu.lifecounter.Controller.ControllerSingleton
import com.example.pektu.lifecounter.Controller.TimeManager
import com.example.pektu.lifecounter.Model.Plan
import com.example.pektu.lifecounter.R
import com.example.pektu.lifecounter.View.View.Interfaces.PlansView
class PlansAdapter(var plans: Array<Plan>, private val plansView: PlansView) :
        RecyclerView.Adapter<PlansAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val planText = view.findViewById<TextView>(R.id.recycler_item_plan_text)
        val planSpentTimeView = view.findViewById<TextView>(R.id.recycler_view_item_spent_time_text)
        val cardView = view.findViewById<CardView>(R.id.recycle_item_card_view)
        lateinit var plan: Plan

    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): PlansAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.planText.text = plans[position].plan
        holder.planSpentTimeView.text = plans[position].getSpentTimeText()
        holder.plan = plans[position]
        setItemColor(holder)

        holder.itemView.setOnClickListener({ ControllerSingleton.controller.onPlansViewItemClicked(holder, plans[position], position, plansView) })
    }

    private fun setItemColor(holder: ViewHolder) {
        val resources = holder.cardView.resources
        when {
            holder.plan.undone -> holder.cardView.setCardBackgroundColor(resources.getColor(R.color.color_undone))
            holder.plan.done -> holder.cardView.setCardBackgroundColor(resources.getColor(R.color.color_done))
            holder.plan.doing -> holder.cardView.setCardBackgroundColor(resources.getColor(R.color.color_doing))
            (holder.plan.date == ControllerSingleton.controller.getModel().getCurrentDay() && TimeManager.dayTimeGettingOut()) ->
                holder.cardView.setCardBackgroundColor(resources.getColor(R.color.color_not_done))
        }
    }

    override fun getItemCount() = plans.size
}