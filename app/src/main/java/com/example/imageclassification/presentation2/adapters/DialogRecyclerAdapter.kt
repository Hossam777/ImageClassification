package com.example.imageclassification.presentation2.adapters

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.imageclassification.data.local.DialogItem
import com.example.imageclassification.databinding.DialogItemBinding

class DialogRecyclerAdapter ()
    : RecyclerView.Adapter<DialogRecyclerAdapter.DialogRecyclerHolder>(){

    private var dialogItems: MutableList<DialogItem> = mutableListOf()
    private var isSection: Boolean = false

    fun setItems (items: MutableList<DialogItem>){
        dialogItems = items
        notifyDataSetChanged()
    }
    fun setItems (items: MutableList<DialogItem>, issection: Boolean){
        dialogItems = items
        isSection = issection
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DialogRecyclerHolder {
        val inflate = LayoutInflater.from(parent.context)
        val binding = DialogItemBinding.inflate(inflate, parent, false)
        return DialogRecyclerHolder(binding)
    }

    override fun getItemCount(): Int {
        return dialogItems.size
    }

    override fun onBindViewHolder(holder: DialogRecyclerHolder, position: Int) {
        if(!dialogItems[position].title.isNullOrBlank())
            holder.binding.itemHeading.text = dialogItems[position].title
        else
            holder.binding.itemText.visibility = View.GONE
        if(!dialogItems[position].text.isNullOrBlank())
            holder.binding.itemText.text = dialogItems[position].text
        else
            holder.binding.itemHeading.visibility = View.GONE
        if(dialogItems[position].img != null)
            holder.binding.itemImg.setImageResource(dialogItems[position].img!!)
        else
            holder.binding.itemImg.visibility = View.GONE

        if(isSection)
            holder.binding.itemText.gravity = Gravity.START
        else
            holder.binding.itemText.gravity = Gravity.CENTER
    }

    inner class DialogRecyclerHolder(
        val binding: DialogItemBinding
    ): RecyclerView.ViewHolder(binding.root) {
    }
}