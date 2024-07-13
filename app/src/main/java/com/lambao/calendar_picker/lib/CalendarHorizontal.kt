package com.lambao.calendar_picker.lib

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.lambao.calendar_picker.R
import com.lambao.calendar_picker.adapter.CalendarHorizontalAdapter
import com.lambao.calendar_picker.callback.CallbackCalendarHorizontal
import com.lambao.calendar_picker.callback.OnclickListenerRecyclerViewParent
import com.lambao.calendar_picker.databinding.CalendarViewBinding
import com.lambao.calendar_picker.model.CalendarModel
import com.lambao.calendar_picker.utils.DataCalendar
import com.lambao.calendar_picker.utils.DatePickerKey
import com.lambao.calendar_picker.utils.DatePickerKey.TOTAL_MONTH
import com.lambao.calendar_picker.utils.convertFormatDate
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class CalendarHorizontal : LinearLayout,
    View.OnClickListener, OnclickListenerRecyclerViewParent {

    lateinit var binding: CalendarViewBinding
    lateinit var callback: CallbackCalendarHorizontal
    val adapter = CalendarHorizontalAdapter(context)
    lateinit var data: ArrayList<CalendarModel>
    private var isPageSelectedCalled: Boolean = false

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {

//        val styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.CalendarHorizontal)
//        styledAttrs.recycle()

        init()
    }

    private fun init() {
        orientation = VERTICAL
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE).let {
            binding = DataBindingUtil.inflate(
                it as LayoutInflater,
                R.layout.calendar_view,
                this,
                true
            )
        }
        View.inflate(context, R.layout.calendar_view, this)

        setDefaultData()
        initPagerView()
    }

    private fun setDefaultData() {
        DatePickerKey.startSelectDate = ""
        DatePickerKey.endSelectDate = ""

        DatePickerKey.TYPE_SELECTED = DatePickerKey.DOUBLE_SELECTED
        DatePickerKey.formatDateOutput = ""
    }

    val dataCalendar = DataCalendar()
    val mCalendar = Calendar.getInstance()
    private fun initPagerView() {
        //set data calendar


        data = dataCalendar.getDataDates(mCalendar, TOTAL_MONTH)
        adapter.setData(data)
        adapter.setOnclickListener(this)

        //init ViewPager

        binding.vpCalendar.adapter = adapter
        binding.vpCalendar.registerOnPageChangeCallback(onPageChangeListener)
        binding.vpCalendar.setCurrentItem(TOTAL_MONTH / 2, false)
    }

    val onPageChangeListener = object : ViewPager2.OnPageChangeCallback() {
        @RequiresApi(Build.VERSION_CODES.N)
        @SuppressLint("NotifyDataSetChanged")
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            binding.headerCalendar.tvTitleMonth.text =
                SimpleDateFormat("MMM").format(data[position].date)
            binding.headerCalendar.tvTitleYear.text =
                SimpleDateFormat("yyyy").format(data[position].date)
            val anim = AnimationUtils.loadAnimation(context, R.anim.slide_left_in)
            binding.headerCalendar.tvTitleMonth.startAnimation(anim)

            if (position == TOTAL_MONTH / 2) {
                return
            }
            // 0 1 2 3 4

            if (position == data.size - 1 || position == 0) {
                data.clear()
                data = dataCalendar.getDataDates(
                    dataCalendar.mapCalendar.getOrDefault(
                        position,
                        mCalendar
                    ), TOTAL_MONTH
                )
                adapter.notifyDataSetChanged()
                binding.vpCalendar.setCurrentItem(TOTAL_MONTH / 2, false)
            }
        }
    }

    fun removeCallBackPageChange() {
        binding.vpCalendar.unregisterOnPageChangeCallback(onPageChangeListener)
    }

    override fun onClick(v: View?) {

    }

    fun callbackCalendarListener(callbackCalendar: CallbackCalendarHorizontal) {
        callback = callbackCalendar
    }


    fun setMinDate(minDate: Date) {
        DatePickerKey.minDate = minDate
    }

    fun setMaxDate(maxDate: Date) {
        DatePickerKey.maxDate = maxDate
    }

    fun setStartDateSelected(date: Date) {
        DatePickerKey.startSelectDate = SimpleDateFormat(DatePickerKey.formatDate).format(date)
        adapter.notifyDataSetChanged()
    }

    fun setEndDateSelected(date: Date) {
        DatePickerKey.endSelectDate = SimpleDateFormat(DatePickerKey.formatDate).format(date)
        adapter.notifyDataSetChanged()
    }

    fun setFontYearHeader(fontYear: Int) {
        setTextFont(binding.headerCalendar.tvTitleYear, fontYear)
    }

    fun setFontMonthHeader(fontMonth: Int) {
        setTextFont(binding.headerCalendar.tvTitleMonth, fontMonth)
    }

    fun setFontTitleDayHeader(fontTitleDay: Int) {
        setTextFont(binding.headerCalendar.tvTitleMonth, fontTitleDay)
        setTextFont(binding.headerCalendar.tvDaySun, fontTitleDay)
        setTextFont(binding.headerCalendar.tvDayMon, fontTitleDay)
        setTextFont(binding.headerCalendar.tvDayTue, fontTitleDay)
        setTextFont(binding.headerCalendar.tvDayWed, fontTitleDay)
        setTextFont(binding.headerCalendar.tvDayThu, fontTitleDay)
        setTextFont(binding.headerCalendar.tvDayFri, fontTitleDay)
        setTextFont(binding.headerCalendar.tvDaySat, fontTitleDay)
    }

    private fun setTextFont(textView: TextView, fontDay: Int) {
        try {
            val typefaceTitle = ResourcesCompat.getFont(context, fontDay)
            textView.setTypeface(typefaceTitle)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun setTextFontDay(fontDay: Int) {
        adapter.setFontDay(fontDay)
    }

    fun typeSelected(type: Int) {
        DatePickerKey.TYPE_SELECTED = type
    }

    fun setFormatDateOutput(format: String) {
        DatePickerKey.formatDateOutput = format
    }

    fun setMessageWarningSelectPreviosDate(message: String) {
        DatePickerKey.titleWarningPreviousDate = message
    }

    override fun callbackRecyclerView(
        viewParent: Int,
        positionParent: Int,
        view: Int,
        position: Int
    ) {
        when (viewParent) {
            DatePickerKey.ONCLICK_DATE -> {
                val mCalendar = Calendar.getInstance()

                if (DatePickerKey.TYPE_SELECTED == DatePickerKey.SINGGLE_SELECTED) {
                    DatePickerKey.startSelectDate =
                        SimpleDateFormat(DatePickerKey.formatDate).format(data[positionParent].data[position].date)
                    adapter.notifyDataSetChanged()
                    if (DatePickerKey.formatDateOutput.isNotEmpty()) {
                        callback.startDate(
                            convertFormatDate(
                                DatePickerKey.startSelectDate,
                                DatePickerKey.formatDate,
                                DatePickerKey.formatDateOutput
                            )
                        )
                    } else {
                        callback.startDate(DatePickerKey.startSelectDate)
                    }
                }

                /*if (DatePickerKey.startSelectDate.isEmpty()) {
                    if (!data[positionParent].data[position].date.before(mCalendar.time) || (SimpleDateFormat(
                            DatePickerKey.formatDate
                        ).format(mCalendar.time) == data[positionParent].data[position].fullDay)
                    ) {
                        DatePickerKey.startSelectDate =
                            SimpleDateFormat(DatePickerKey.formatDate).format(data[positionParent].data[position].date)
                        adapter.notifyDataSetChanged()

                        if (DatePickerKey.formatDateOutput.isNotEmpty()) {
                            callback.startDate(
                                convertFormatDate(
                                    DatePickerKey.startSelectDate,
                                    DatePickerKey.formatDate,
                                    DatePickerKey.formatDateOutput
                                )
                            )
                        } else {
                            callback.startDate(DatePickerKey.startSelectDate)
                        }
                    }
                } else {
                    val dateSelected =
                        SimpleDateFormat(DatePickerKey.formatDate).format(data[positionParent].data[position].date)
                    if (DatePickerKey.startSelectDate == dateSelected) {
                        DatePickerKey.startSelectDate = ""
                        DatePickerKey.endSelectDate = ""
                        adapter.notifyDataSetChanged()
                    } else {
                        if (DatePickerKey.TYPE_SELECTED == DatePickerKey.SINGGLE_SELECTED) {
                            DatePickerKey.startSelectDate =
                                SimpleDateFormat(DatePickerKey.formatDate).format(data[positionParent].data[position].date)
                            adapter.notifyDataSetChanged()
                            if (DatePickerKey.formatDateOutput.isNotEmpty()) {
                                callback.startDate(
                                    convertFormatDate(
                                        DatePickerKey.startSelectDate,
                                        DatePickerKey.formatDate,
                                        DatePickerKey.formatDateOutput
                                    )
                                )
                            } else {
                                callback.startDate(DatePickerKey.startSelectDate)
                            }
                        } else {
                            if (data[positionParent].data[position].date.before(
                                    SimpleDateFormat(DatePickerKey.formatDate).parse(
                                        DatePickerKey.startSelectDate
                                    )
                                )
                            ) {
                                Toast.makeText(
                                    context, DatePickerKey.titleWarningPreviousDate,
                                    Toast.LENGTH_LONG
                                ).show()
                            } else {
                                DatePickerKey.endSelectDate = dateSelected
                                adapter.notifyDataSetChanged()

                                if (DatePickerKey.formatDateOutput.isNotEmpty()) {
                                    callback.startDate(
                                        convertFormatDate(
                                            DatePickerKey.startSelectDate,
                                            DatePickerKey.formatDate,
                                            DatePickerKey.formatDateOutput
                                        )
                                    )
                                    callback.endDate(
                                        convertFormatDate(
                                            DatePickerKey.endSelectDate,
                                            DatePickerKey.formatDate,
                                            DatePickerKey.formatDateOutput
                                        )
                                    )
                                } else {
                                    callback.startDate(DatePickerKey.startSelectDate)
                                    callback.endDate(DatePickerKey.endSelectDate)
                                }
                            }
                        }

                    }
                }*/

                if (data[positionParent].data[position].typeDay == DatePickerKey.DAY_PREVIOUS_MONTH) {
                    if (positionParent != 0) {
                        binding.vpCalendar.setCurrentItem((positionParent - 1), false)
//                        isPageSelectedCalled = false
                    }
                }

                if (data[positionParent].data[position].typeDay == DatePickerKey.DAY_NEXT_MONTH) {
                    if (positionParent < data.size) {
                        binding.vpCalendar.setCurrentItem((positionParent + 1), false)
//                        isPageSelectedCalled = false
                    }
                }
            }
        }
    }

}