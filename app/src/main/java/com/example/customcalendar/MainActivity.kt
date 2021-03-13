package com.example.customcalendar

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MainActivity: AppCompatActivity(), CalendarAdapter.OnItemListener {
    private lateinit var displayMonthYear: TextView
    private lateinit var calendarGrid: RecyclerView
    private lateinit var selectedDate: Date

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.displayMonthYear = findViewById(R.id.displayMonthYear)
        this.calendarGrid = findViewById(R.id.calendarGrid)
        this.selectedDate = Date()

        this.setCalendarMonthView()
    }

    private fun setCalendarMonthView() {
        this.selectTomorrowWeekDays()

        val formatter = SimpleDateFormat("MMM yyyy")
        this.displayMonthYear.text = formatter.format(this.selectedDate)

        val days: ArrayList<CalendarCell> = this.initDaysInMonth(this.selectedDate)

        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(applicationContext, 7)
        val adapter = CalendarAdapter(days, this)

        this.calendarGrid.layoutManager = layoutManager
        this.calendarGrid.adapter = adapter
    }

    private fun selectTomorrowWeekDays() {
        val SUN = 1
        val SAT = 7

        val cal: Calendar = Calendar.getInstance()

        while (true) {
            cal.time = this.selectedDate
            cal.add(Calendar.DATE, 1)
            this.selectedDate = cal.time

            if (cal.get(Calendar.DAY_OF_WEEK) != SUN && cal.get(Calendar.DAY_OF_WEEK) != SAT) {
                return
            }
        }
    }

    private fun initDaysInMonth(date: Date): ArrayList<CalendarCell> {
        val days: ArrayList<CalendarCell> = ArrayList<CalendarCell>()

        val daysInMonth = this.getDaysInMonth(date)

        val firstOfMonth = Calendar.getInstance()
        firstOfMonth.time = this.selectedDate
        firstOfMonth.set(Calendar.DAY_OF_MONTH, 1)
        val dayOfWeek = firstOfMonth.get(Calendar.DAY_OF_WEEK) - 1

        firstOfMonth.add(Calendar.DAY_OF_MONTH, -dayOfWeek)

        val formatter = SimpleDateFormat("yyyy-MM-dd")
        while (days.size < 35) {
            val current = firstOfMonth.time

            days.add(CalendarCell(
                current,
                current.date,
                current.month,
                current.year,
                formatter.format(current) == formatter.format(this.selectedDate),
                this.selectedDate
            ))

            firstOfMonth.add(Calendar.DAY_OF_MONTH, 1)
        }

//        for (i in 1..42) {
//            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
//                days.add(NullCalendarCell())
//            } else {
//                val formatter = SimpleDateFormat("yyyy-MM-dd")
//
//                val current = Date(this.selectedDate.year, this.selectedDate.month, (i - dayOfWeek))
//
//                days.add(CalendarCell(
//                    current,
//                    current.date,
//                    current.month,
//                    current.year,
//                    formatter.format(current) == formatter.format(this.selectedDate)
//                ))
//            }
//        }

        return days
    }

    private fun getDaysInMonth(date: Date): Int {
        val cal = Calendar.getInstance()
        cal.time = date
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH)
    }

    override fun onItemClick(position: Int, dayText: String?) {
        if (dayText != null) {
            val formatter = SimpleDateFormat("MMM yyyy")
            val message = "Selected Date " + dayText.toString() + " - " + formatter.format(this.selectedDate)
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }
}
