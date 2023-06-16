package com.example.imageclassification.presentation2.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.imageclassification.R
import com.example.imageclassification.data.local.buildings
import com.smarteist.autoimageslider.SliderViewAdapter


class SliderAdapter(private val sliderList: List<Int>, private val onItemClicked: (Int) -> Unit) :
    SliderViewAdapter<SliderAdapter.SliderViewHolder>() {

    class SliderViewHolder(ItemView: View) : SliderViewAdapter.ViewHolder(ItemView) {
        val sliderIV: ImageView = itemView.findViewById(R.id.idIVSliderItem)
        val sliderName: TextView = itemView.findViewById(R.id.buildingNameTV)
    }

    override fun getCount(): Int {
        return sliderList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?): SliderAdapter.SliderViewHolder {
        val itemView: View =
            LayoutInflater.from(parent!!.context).inflate(R.layout.image_slider_item, null)
        return SliderViewHolder(itemView)
    }

    override fun onBindViewHolder(viewHolder: SliderAdapter.SliderViewHolder?, position: Int) {
        viewHolder?.sliderIV?.setImageResource(sliderList[position])
        viewHolder?.sliderIV?.setOnClickListener { onItemClicked(position) }
        if(position < 4) viewHolder?.sliderName?.text = buildings[position].name
        else {
            viewHolder?.sliderName?.text = "البحث عن مبني"
            viewHolder?.sliderIV?.scaleType = ImageView.ScaleType.FIT_CENTER
        }
    }
}