package com.example.imageclassification.presentation2

import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.imageclassification.data.local.DialogItem
import com.example.imageclassification.databinding.DialogItemBinding

class DialogRecyclerAdapter ()
    : RecyclerView.Adapter<DialogRecyclerAdapter.DialogRecyclerHolder>(){

    private var dialogItems: MutableList<DialogItem> = mutableListOf()

    fun setItems (items: MutableList<DialogItem>){
        dialogItems = items
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
        dialogItems[position].title?.let { holder.binding.itemHeading.text = it }
        dialogItems[position].text?.let { holder.binding.itemText.text = it }
        if(dialogItems[position].img == null)
            holder.binding.itemImg.visibility = View.GONE
        else
            holder.binding.itemImg.setImageResource(dialogItems[position].img!!)
    }

    inner class DialogRecyclerHolder(
        val binding: DialogItemBinding
    ): RecyclerView.ViewHolder(binding.root) {
    }
}