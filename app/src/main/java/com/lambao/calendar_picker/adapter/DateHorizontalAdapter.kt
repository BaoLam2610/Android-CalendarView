package com.lambao.calendar_picker.adapter

import android.view.View
import android.view.ViewGroup
import android.content.Context
import java.text.SimpleDateFormat
import android.view.LayoutInflater
import kotlin.collections.ArrayList
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.lambao.calendar_picker.model.DayDataModel
import com.lambao.calendar_picker.callback.OnclickListenerRecyclerView
import com.lambao.calendar_picker.utils.DatePickerKey
import com.lambao.calendar_picker.R
import com.lambao.calendar_picker.databinding.ItemDateViewBinding

class DateHorizontalAdapter (var context: Context, var items: ArrayList<DayDataModel>, var fontDay:Int): RecyclerView.Adapter<DateHorizontalAdapter.ViewHolder>() {

    lateinit var binding: ItemDateViewBinding
    lateinit var onclick: OnclickListenerRecyclerView

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemDateViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    fun setOnclickListener(onclickListenerRecyclerView: OnclickListenerRecyclerView){
        this.onclick = onclickListenerRecyclerView
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data = items.get(position)
        binding.tvItemDate.setText(data.day.toString())

        if (fontDay!=-1){
            try {
                val typefaceTitle = ResourcesCompat.getFont(context, fontDay)
                binding.tvItemDate.setTypeface(typefaceTitle)
            }catch (e:Exception){
                e.printStackTrace()
            }
        }

        // Nếu date nhỏ hơn minDate thì disable
        if (data.date.before(DatePickerKey.minDate)&&(SimpleDateFormat("dd-MM-yyyy").format(
                DatePickerKey.minDate)!=data.fullDay)){
            binding.tvItemDate.setTextColor(context.resources.getColor(R.color.colorTextNonActived))
        }
        // Nếu date lớn hơn maxDate thì disable
        else if (data.date.after(DatePickerKey.maxDate)){
            binding.tvItemDate.setTextColor(context.resources.getColor(R.color.colorTextNonActived))
        }
        // Ngược lại thì enable
        else{
            if (data.typeDay== DatePickerKey.DAY_NEXT_MONTH||data.typeDay== DatePickerKey.DAY_PREVIOUS_MONTH){
                binding.tvItemDate.setTextColor(context.resources.getColor(R.color.colorTextNonActived))
            }
            /*else if(data.typeDay==DatePickerKey.DAY_SUNDAY){
                binding.tv.setTextColor(context.resources.getColor(R.color.colorTextHoliday))
            }*/
            else {
                if (DatePickerKey.startSelectDate.isNotEmpty()){
                    if (data.fullDay== DatePickerKey.startSelectDate){
                        setViewSelected()
                    }
                    else {
                        if (DatePickerKey.TYPE_SELECTED== DatePickerKey.SINGGLE_SELECTED){
                            binding.tvItemDate.setTextColor(context.resources.getColor(R.color.colorTextHeaderTitle))
                        }
                        else{
                            if (DatePickerKey.endSelectDate.isNotEmpty()){
                                if (data.date.after(SimpleDateFormat("dd-MM-yyyy").parse(
                                        DatePickerKey.startSelectDate))&&data.date.before(SimpleDateFormat("dd-MM-yyyy").parse(
                                        DatePickerKey.endSelectDate))){
                                    setViewSelected()
                                }
                                else if(data.fullDay== DatePickerKey.endSelectDate){
                                    setViewSelected()
                                }
                                else {
                                    binding.tvItemDate.setTextColor(context.resources.getColor(R.color.colorTextHeaderTitle))
                                }
                            }
                            else {
                                binding.tvItemDate.setTextColor(context.resources.getColor(R.color.colorTextHeaderTitle))
                            }
                        }
                    }
                }
                else {
                    binding.tvItemDate.setTextColor(context.resources.getColor(R.color.colorTextHeaderTitle))
                }
            }

            binding.root.setOnClickListener {
                onclick.callbackRecyclerView(-1,position)
            }
        }
    }

    private fun setViewSelected() {
        binding.tvItemDate.background = ContextCompat.getDrawable(context, R.drawable.rounded_selected)
        binding.tvItemDate.setTextColor(context.resources.getColor(R.color.colorWhite))
    }

    class ViewHolder(row: View) : RecyclerView.ViewHolder(row) {

    }
}