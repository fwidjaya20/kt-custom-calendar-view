package com.example.customcalendar

import java.util.*

open class CalendarCell(
    var date: Date,
    var D: Int,
    var M: Int,
    var Y : Int,
    var isDefault: Boolean,
    var defaultDate: Date
)

class NullCalendarCell: CalendarCell(Date(), 0, 0, 0, false, Date())