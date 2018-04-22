package com.example.pektu.lifecounter.View.View.Adapters

import android.support.v7.widget.RecyclerView
import android.view.*
import com.example.pektu.lifecounter.Controller.ControllerSingleton
import com.example.pektu.lifecounter.Controller.TimeManager
import com.example.pektu.lifecounter.Model.Plan
import com.example.pektu.lifecounter.R
import com.example.pektu.lifecounter.View.View.Activities.DayPlansActivity
import com.example.pektu.lifecounter.View.View.Interfaces.PlansView
import kotlinx.android.synthetic.main.recycler_view_item.view.*

class PlansAdapter(var plans: List<Plan>, private val plansView: PlansView) :
        RecyclerView.Adapter<PlansAdapter.ViewHolder>() {
    lateinit var currentLongClickPlan: Plan

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnCreateContextMenuListener {
        lateinit var plan: Plan

        fun bind(plan: Plan) {
            itemView.recycler_item_plan_text.text = plan.plan
            itemView.recycler_view_item_spent_time_text.text = plan.getSpentTimeText()
            this.plan = plan
            itemView.setOnClickListener { ControllerSingleton.controller.onPlansViewItemClicked(this, plan, position) }
            itemView.setOnLongClickListener({ onLongClick() })
            setItemColor()
            itemView.setOnCreateContextMenuListener(this)

        }

        private fun onLongClick(): Boolean {
            currentLongClickPlan = plan
            itemView.showContextMenu()
            return true
        }

        override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
            menu!!.add(Menu.NONE, DayPlansActivity.CONTEXT_MENU_CHANGE_BUTTON_ID, 0,
                    itemView.resources.getString(R.string.plans_view_context_menu_change_button_text))
            menu.add(Menu.NONE, DayPlansActivity.CONTEXT_MENU_REMOVE_BUTTON_ID, 0,
                    itemView.resources.getString(R.string.plans_view_contetx_menu_remove_button_text))
        }

        private fun setItemColor() {
            val resources = itemView.recycle_item_card_view.resources
            val cardView = itemView.recycle_item_card_view
            when {
                plan.undone -> cardView.setCardBackgroundColor(resources.getColor(R.color.color_undone))
                plan.moved -> cardView.setCardBackgroundColor(resources.getColor(R.color.color_moved))
                plan.done -> cardView.setCardBackgroundColor(resources.getColor(R.color.color_done))
                plan.doing -> cardView.setCardBackgroundColor(resources.getColor(R.color.color_doing))
                (plan.date == ControllerSingleton.controller.getModel().getCurrentDay() && TimeManager.dayTimeGettingOut()) ->
                    cardView.setCardBackgroundColor(resources.getColor(R.color.color_not_done))
                else -> cardView.setCardBackgroundColor(resources.getColor(R.color.color_normal))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlansAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_item, parent, false)
        return ViewHolder(view)
    }

    override fun onViewRecycled(holder: ViewHolder?) {
        holder!!.itemView.setOnLongClickListener(null)
        super.onViewRecycled(holder)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(plans[position])
    }

    fun updateData(plans: List<Plan>) {
        this.plans = plans.toMutableList()
        notifyDataSetChanged()
    }

    override fun getItemCount() = plans.size
}