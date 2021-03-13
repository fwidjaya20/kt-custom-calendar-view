package com.example.customcalendar

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList

class CalendarAdapter(
    private val daysOfMonth: ArrayList<CalendarCell>,
    private val onItemListener: OnItemListener
): RecyclerView.Adapter<CalendarViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.calendar_cell, null, false)
        return CalendarViewHolder(view, this.onItemListener)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        val cell = this.daysOfMonth[position]

        if (cell is NullCalendarCell) {
            holder.dayOfMonth.text = ""
            return
        }

        holder.dayOfMonth.text = this.daysOfMonth[position].D.toString()

        this.disableWeekend(holder, cell)
        this.disableAnotherMonthDate(holder, cell)
        this.markDefaultDate(holder, cell)
    }

    private fun disableWeekend(holder: CalendarViewHolder, cell: CalendarCell) {
        val cal = Calendar.getInstance()
        cal.time = cell.date

        val SUNDAY = 1
        val SATURDAY = 7

        when(cal.get(Calendar.DAY_OF_WEEK)) {
            SUNDAY, SATURDAY -> holder.dayOfMonth.setTextColor(Color.GRAY)
        }
    }

    private fun disableAnotherMonthDate(holder: CalendarViewHolder, cell: CalendarCell) {
        val TODAY = cell.defaultDate

        if (cell.M != TODAY.month || cell.Y != TODAY.year) {
            holder.dayOfMonth.setTextColor(Color.GRAY)
        }
    }

    private fun markDefaultDate(holder: CalendarViewHolder, cell: CalendarCell) {
        if (cell.isDefault) {
            holder.dayOfMonth.setTextColor(Color.BLUE)
        }
    }

    override fun getItemCount(): Int {
        return this.daysOfMonth.size
    }

    interface OnItemListener {
        fun onItemClick(position: Int, dayText: String?)
    }
}