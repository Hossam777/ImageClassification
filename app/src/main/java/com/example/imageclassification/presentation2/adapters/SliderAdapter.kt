package com.example.imageclassification.presentation2.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.imageclassification.R
import com.smarteist.autoimageslider.SliderViewAdapter


class SliderAdapter(private val sliderList: List<Int>, private val onItemClicked: (Int) -> Unit) :
    SliderViewAdapter<SliderAdapter.SliderViewHolder>() {

    class SliderViewHolder(ItemView: View) : SliderViewAdapter.ViewHolder(ItemView) {
        val sliderIV: ImageView = itemView.findViewById(R.id.idIVSliderItem)
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
    }
}