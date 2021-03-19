package com.example.customcalendar

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.calendar_cell.view.*
import java.util.*
import kotlin.collections.ArrayList

class CalendarAdapter(
    private val daysOfMonth: ArrayList<CalendarCell>,
    private val onItemListener: OnItemListener
): RecyclerView.Adapter<CalendarViewHolder>() {
    var todayMonth = 0
    var todayYear = 0
    var indexes = -1
    var checkedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.calendar_cell, null, false)
        return CalendarViewHolder(view, this.onItemListener)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        val cell = this.daysOfMonth[position]
        val context = holder.itemView.context
        holder.itemView.calendarCell.text = if (daysOfMonth[position].D < 10) " ${this.daysOfMonth[position].D} " else this.daysOfMonth[position].D.toString()
        this.disableWeekend(holder, cell)
        this.disableAnotherMonthDate(holder, cell)
        when (position) {
            checkedPosition, indexes -> {
                holder.itemView.isEnabled = true
                holder.itemView.calendarCell.setTextColor(Color.WHITE)
                holder.itemView.calendarCell.background = ContextCompat.getDrawable(context, R.drawable.bg_rounded)
                onItemListener.onItemClick(position,cell.date)
            }
        }

        if (cell is NullCalendarCell) {
            holder.itemView.calendarCell.text = ""
            return
        }
        holder.itemView.setOnClickListener {
            indexes = -1
            when (position) {
                checkedPosition -> checkedPosition = -1
                else -> {
                    checkedPosition = position
                    notifyDataSetChanged()
                }
            }
        }
    }

    private fun disableWeekend(holder: CalendarViewHolder, cell: CalendarCell) {
        val cal = Calendar.getInstance()
        cal.time = cell.date
        when(cal.get(Calendar.DAY_OF_WEEK)) {
            Calendar.SUNDAY, Calendar.SATURDAY -> disableClick(holder)
        }
    }

    private fun disableAnotherMonthDate(holder: CalendarViewHolder, cell: CalendarCell) {
        val TODAY = cell.defaultDate
        var currentYear = 1900 + cell.Y
        if (cell.M != TODAY.month || cell.Y != TODAY.year) disableClick(holder)
        else if (cell.M == todayMonth && currentYear == todayYear){
            if (cell.D < TODAY.date) disableClick(holder)
        }
    }

    private fun disableClick(holder: CalendarViewHolder){
        holder.itemView.isEnabled = false
        holder.itemView.calendarCell.setTextColor(Color.RED)
    }

    private fun enableClick(holder: CalendarViewHolder){
        holder.itemView.isEnabled = true
        holder.itemView.calendarCell.setTextColor(Color.BLACK)
    }

    override fun getItemCount(): Int {
        return this.daysOfMonth.size
    }

    interface OnItemListener {
        fun onItemClick(position: Int, dayText: Date?)
    }
}