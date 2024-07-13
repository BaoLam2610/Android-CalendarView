package com.lambao.calendar_picker.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lambao.calendar_picker.callback.OnclickListenerRecyclerView
import com.lambao.calendar_picker.callback.OnclickListenerRecyclerViewParent
import com.lambao.calendar_picker.databinding.ItemCalendarBinding
import com.lambao.calendar_picker.model.CalendarModel
import com.lambao.calendar_picker.utils.DatePickerKey

class CalendarHorizontalAdapter(var context: Context) :
    RecyclerView.Adapter<CalendarHorizontalAdapter.ViewHolder>() {

    lateinit var binding: ItemCalendarBinding
    lateinit var onclick: OnclickListenerRecyclerViewParent
    var items = ArrayList<CalendarModel>()
    var fontDays = -1

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemCalendarBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding.root)
    }

    fun setOnclickListener(onclickListenerRecyclerView: OnclickListenerRecyclerViewParent) {
        this.onclick = onclickListenerRecyclerView
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        @SuppressLint("RecyclerView") positionParent: Int
    ) {
        val data = items.get(positionParent)
        val dateAdapter by lazy { DateHorizontalAdapter(context, data.data, fontDays) }
        val mLayoutManager = GridLayoutManager(context, 7)
        binding.rvCalendarHorizontal.setLayoutManager(mLayoutManager)
        binding.rvCalendarHorizontal.setHasFixedSize(true)
        binding.rvCalendarHorizontal.setAdapter(dateAdapter)
        binding.rvCalendarHorizontal.setNestedScrollingEnabled(false)

        dateAdapter.setOnclickListener(object : OnclickListenerRecyclerView {
            override fun callbackRecyclerView(view: Int, position: Int) {
                onclick.callbackRecyclerView(
                    DatePickerKey.ONCLICK_DATE,
                    positionParent,
                    -1,
                    position
                )
            }
        })
    }

    fun setData(data: ArrayList<CalendarModel>) {
        items = data
        notifyDataSetChanged()
    }

    fun setFontDay(fontDay: Int) {
        fontDays = fontDay
        notifyDataSetChanged()
    }

    class ViewHolder(row: View) : RecyclerView.ViewHolder(row) {

    }
}