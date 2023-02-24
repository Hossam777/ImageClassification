package com.example.imageclassification.presentation.homeactivity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.imageclassification.data.local.Building
import com.example.imageclassification.data.local.buildings
import com.example.imageclassification.databinding.BuldingItemBinding

class BuildingsRecyclerAdapter (private val onEmptyList: () -> Unit
                                , private val onAdded: () -> Unit
                                , private val onItemClicked: (Int) -> Unit)
    : RecyclerView.Adapter<BuildingsRecyclerAdapter.BuildingRecyclerHolder>(){

    private var buildingsList: MutableList<Building> = mutableListOf()

    fun addBuilding (building: Building){
        if (!buildingsList.contains(building)){
            buildingsList.add(building)
            onAdded()
            notifyDataSetChanged()
        }
    }
    fun deleteBuilding (pos: Int){
        buildingsList.removeAt(pos)
        if(buildingsList.size == 0)
            onEmptyList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuildingRecyclerHolder {
        val inflate = LayoutInflater.from(parent.context)
        val binding = BuldingItemBinding.inflate(inflate, parent, false)
        return BuildingRecyclerHolder(binding)
    }

    override fun getItemCount(): Int {
        return buildingsList.size
    }

    override fun onBindViewHolder(holder: BuildingRecyclerHolder, position: Int) {
        holder.binding.buildingNameTV.text = buildings[position].name
        holder.binding.buidlingIV.setImageResource(buildings[position].photo)
        holder.binding.root.setOnClickListener { onItemClicked(position) }
    }



    inner class BuildingRecyclerHolder(
        val binding: BuldingItemBinding
    ): RecyclerView.ViewHolder(binding.root) {}
}