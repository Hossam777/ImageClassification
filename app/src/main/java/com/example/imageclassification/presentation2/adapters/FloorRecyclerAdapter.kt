package com.example.imageclassification.presentation2.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.imageclassification.data.local.Floor
import com.example.imageclassification.databinding.FloorItemBinding

class FloorRecyclerAdapter (private val onItemClicked: (Floor) -> Unit)
    : RecyclerView.Adapter<FloorRecyclerAdapter.FloorRecyclerHolder>(){

    private var floorItems: MutableList<Floor> = mutableListOf()

    fun setItems (items: MutableList<Floor>){
        floorItems = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FloorRecyclerHolder {
        val inflate = LayoutInflater.from(parent.context)
        val binding = FloorItemBinding.inflate(inflate, parent, false)
        return FloorRecyclerHolder(binding)
    }

    override fun getItemCount(): Int {
        return floorItems.size
    }

    override fun onBindViewHolder(holder: FloorRecyclerHolder, position: Int) {
        holder.binding.floorName.text = floorItems[position].name
        holder.binding.root.setOnClickListener {
            onItemClicked(floorItems[position])
        }
    }

    inner class FloorRecyclerHolder(
        val binding: FloorItemBinding
    ): RecyclerView.ViewHolder(binding.root) {
    }
}