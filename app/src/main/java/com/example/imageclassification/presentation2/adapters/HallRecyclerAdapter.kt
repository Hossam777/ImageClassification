package com.example.imageclassification.presentation2.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.imageclassification.data.local.Hall
import com.example.imageclassification.databinding.FloorItemBinding

class HallRecyclerAdapter (private val onItemClicked: (Hall) -> Unit)
    : RecyclerView.Adapter<HallRecyclerAdapter.HallRecyclerHolder>(){

    private var hallItems: MutableList<Hall> = mutableListOf()

    fun setItems (items: MutableList<Hall>){
        hallItems = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HallRecyclerHolder {
        val inflate = LayoutInflater.from(parent.context)
        val binding = FloorItemBinding.inflate(inflate, parent, false)
        return HallRecyclerHolder(binding)
    }

    override fun getItemCount(): Int {
        return hallItems.size
    }

    override fun onBindViewHolder(holder: HallRecyclerHolder, position: Int) {
        holder.binding.floorName.text = hallItems[position].name
        holder.binding.root.setOnClickListener {
            onItemClicked(hallItems[position])
        }
    }

    inner class HallRecyclerHolder(
        val binding: FloorItemBinding
    ): RecyclerView.ViewHolder(binding.root) {
    }
}