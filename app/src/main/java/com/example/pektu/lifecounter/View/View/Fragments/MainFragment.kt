package com.example.pektu.lifecounter.View.View.Fragments

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import com.example.pektu.lifecounter.Controller.ControllerSingleton
import com.example.pektu.lifecounter.Model.DayDate
import com.example.pektu.lifecounter.R

class MainFragment : Fragment() {
    private lateinit var calendar: CalendarView

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_main, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        calendar = view.findViewById(R.id.main_calendar_view)
        initCalendar()
    }

    private fun initCalendar() {
        calendar.setOnDateChangeListener({ view, year, month, dayOfMonth -> if (ControllerSingleton.inited) ControllerSingleton.controller.onCalendarDateChanged(DayDate(year, month, dayOfMonth)) })
    }
}
