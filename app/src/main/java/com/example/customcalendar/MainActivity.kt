package com.example.customcalendar

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MainActivity: AppCompatActivity(), CalendarAdapter.OnItemListener {
    private lateinit var displayMonthYear: TextView
    private lateinit var calendarGrid: RecyclerView
    private lateinit var selectedDate: Date
    private lateinit var minimumSelectDate: Date
    var calc: Calendar = Calendar.getInstance()
    var staticCal: Calendar = Calendar.getInstance()
    var default = ""
    val adapter = CalendarAdapter()
    val formatter = SimpleDateFormat("yyyy-MM-dd")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.displayMonthYear = findViewById(R.id.displayMonthYear)
        this.calendarGrid = findViewById(R.id.calendarGrid)
        this.selectedDate = Date()
        calc.time = this.selectedDate

        this.selectTomorrowWeekDays()
        this.setCalendarMonthView(this.selectedDate,false)
        this.calendarActionListener()
        this.applyButtonListener()
    }

    private fun calendarActionListener() {
        nextButton.setOnClickListener {
            this.calc.set(Calendar.MONTH, this.calc.get(Calendar.MONTH) + 1)
            this.calc.set(Calendar.DATE, 1)
            this.setCalendarMonthView(calc.time, true)
        }

        previousButton.setOnClickListener {
            if (calc.time <= this.minimumSelectDate) {
                this.calc.time = this.minimumSelectDate
                return@setOnClickListener
            }

            this.calc.set(Calendar.MONTH, this.calc.get(Calendar.MONTH) - 1)
            this.calc.set(Calendar.DATE, 1)
            this.setCalendarMonthView(calc.time,true)
        }
    }

    private fun applyButtonListener() {
        btn_apply.setOnClickListener {
            Toast.makeText(this, default, Toast.LENGTH_SHORT).show()
        }
    }


    private fun setCalendarMonthView(dateInput : Date, isModify : Boolean) {
        val form = SimpleDateFormat("MMM yyyy")
        this.displayMonthYear.text = form.format(dateInput)

        val days: ArrayList<CalendarCell> = this.initDaysInMonth(dateInput)
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(applicationContext, 7)

        adapter.daysOfMonth = days
        this.calendarGrid.layoutManager = layoutManager
        this.calendarGrid.setHasFixedSize(true)
        this.calendarGrid.setItemViewCacheSize(30)
        adapter.adapterHandler = object : AdapterHandler() {
            override fun bindAdd(selected: String) {
                default = selected
                btn_apply.isEnabled = true
            }
        }
        adapter.todayMonth = staticCal.get(Calendar.MONTH)
        adapter.todayYear = staticCal.get(Calendar.YEAR)
        adapter.todayDate = staticCal.get(Calendar.DAY_OF_MONTH)
        if (!isModify) {
            adapter.indexes = filterPosition(days)
            default = formatter.format(days[filterPosition(days)].date)
            btn_apply.isEnabled = true
        }
        else {
            default = ""
            adapter.indexes = -1
            adapter.checkedPosition = -1
            btn_apply.isEnabled = false
        }
        this.calendarGrid.adapter = adapter
    }

    private fun selectTomorrowWeekDays() {
        val cal: Calendar = Calendar.getInstance()
        while (true) {
            cal.time = this.selectedDate
            cal.add(Calendar.DATE, 1)
            this.selectedDate = cal.time
            this.minimumSelectDate = cal.time
            if (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY && cal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) break
        }
        this.calc.time = this.selectedDate
    }

    private fun initDaysInMonth(date: Date): ArrayList<CalendarCell> {
        val days: ArrayList<CalendarCell> = ArrayList<CalendarCell>()
        val firstOfMonth = Calendar.getInstance()
        firstOfMonth.time = date
        firstOfMonth.set(Calendar.DAY_OF_MONTH, 1)
        val dayOfWeek = firstOfMonth.get(Calendar.DAY_OF_WEEK) - 1
        val newCal = Calendar.getInstance()
        newCal.time = date
        firstOfMonth.add(Calendar.DAY_OF_MONTH, -dayOfWeek)
        while (days.size < 42) {
            days.add(CalendarCell(
                firstOfMonth.time,
                firstOfMonth.get(Calendar.DATE),
                firstOfMonth.get(Calendar.MONTH),
                firstOfMonth.get(Calendar.YEAR),
                formatter.format(firstOfMonth.time) == formatter.format(this.selectedDate),
                newCal
            ))
            firstOfMonth.add(Calendar.DAY_OF_MONTH, 1)
        }
        return days
    }

    override fun onItemClick(position: Int, dayText: Date?) {
        if (dayText != null) {
            default = formatter.format(dayText)
            btn_apply.isEnabled = default.isNotEmpty()
        }
    }

    private fun filterPosition(datum : ArrayList<CalendarCell>) : Int {
        val list = datum.find { it.isDefault }
        return datum.indexOf(list)
    }
}
