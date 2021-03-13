package com.example.customcalendar

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CalendarViewHolder(view: View, private val onItemListener: CalendarAdapter.OnItemListener): RecyclerView.ViewHolder(view), View.OnClickListener {
    val dayOfMonth: TextView = view.findViewById(R.id.calendarCell)

    init {
        view.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        this.onItemListener.onItemClick(adapterPosition, dayOfMonth.text as String)
    }

}