package com.lambao.calendar_picker.model

import java.util.*
import kotlin.collections.ArrayList

data class CalendarModel(var date        : Date = Date(),
                         var dateStr     : String = "",
                         var data        : ArrayList<DayDataModel> = ArrayList(),
                         var typeLayout  : Int
    )