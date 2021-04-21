package com.example.customcalendar

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.calendar_cell.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CalendarAdapter : RecyclerView.Adapter<CalendarViewHolder>(),ItemClickListener{
    var adapterHandler: AdapterHandler? = null
    var daysOfMonth = ArrayList<CalendarCell>()
    var todayMonth = 0
    var todayYear = 0
    var todayDate = 0
    var indexes = -1
    var checkedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.calendar_cell, null, false)
        return CalendarViewHolder(view)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        Log.i("todayMonth","$todayDate $todayMonth $todayYear")
        val cell = this.daysOfMonth[position]
        var currentYear = cell.Y
        val context = holder.itemView.context
        holder.itemView.calendarCell.text =
            if (daysOfMonth[position].D < 10) " ${this.daysOfMonth[position].D} " else this.daysOfMonth[position].D.toString()
        this.disableWeekend(holder, cell)
        this.disableAnotherMonthDate(holder, cell)
        when (position) {
            checkedPosition, indexes -> {
                holder.itemView.isEnabled = true
                holder.itemView.calendarCell.setTextColor(Color.WHITE)
                holder.itemView.calendarCell.background =
                    ContextCompat.getDrawable(context, R.drawable.bg_rounded)
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
                    addData(dateBuilder(cell.D,cell.M,currentYear))
                    notifyDataSetChanged()
                }
            }
        }
    }

    private fun disableWeekend(holder: CalendarViewHolder, cell: CalendarCell) {
        val cal = Calendar.getInstance()
        cal.time = cell.date
        when (cal.get(Calendar.DAY_OF_WEEK)) {
            Calendar.SUNDAY, Calendar.SATURDAY -> disableClick(holder)
            else -> enableClick(holder)
        }
    }

    private fun disableAnotherMonthDate(holder: CalendarViewHolder, cell: CalendarCell) {
        val TODAY = cell.defaultDate
        var currentYear = cell.Y
        if (cell.M != TODAY.get(Calendar.MONTH) || cell.Y != TODAY.get(Calendar.YEAR)) disableClick(holder)
        if (cell.D <= todayDate && cell.M <= todayMonth && currentYear <= todayYear) disableClick(holder)

    }

    private fun disableClick(holder: CalendarViewHolder) {
        holder.itemView.isEnabled = false
        holder.itemView.calendarCell.setTextColor(Color.RED)
    }

    private fun enableClick(holder: CalendarViewHolder) {
        holder.itemView.isEnabled = true
        holder.itemView.calendarCell.setTextColor(Color.BLACK)
    }

    private fun dateBuilder(tanggal: Int, bulan: Int, tahun: Int): String {
        val tgl: String = if (tanggal.toString().length == 1) "0$tanggal"
        else "" + tanggal
        val bln: String = if (bulan.toString().length == 1) "0$bulan" else "" + bulan
        return "$tahun-$bln-$tgl"
    }

    override fun getItemCount(): Int {
        return this.daysOfMonth.size
    }

    interface OnItemListener {
        fun onItemClick(position: Int, dayText: Date?)
    }

    override fun addData(selected: String) {
        adapterHandler?.bindAdd(selected)
    }
}
interface ItemClickListener {
    fun addData(selected: String)
}

abstract class AdapterHandler {
    open fun bindAdd(selected: String) {}
}

