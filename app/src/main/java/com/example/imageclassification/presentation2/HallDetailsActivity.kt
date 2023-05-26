package com.example.imageclassification.presentation2

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.imageclassification.R
import com.example.imageclassification.bases.BaseActivity
import com.example.imageclassification.data.local.DialogItem
import com.example.imageclassification.data.local.buildings
import com.example.imageclassification.databinding.ActivityHallDetailsBinding
import com.example.imageclassification.databinding.CustomDialogBinding
import com.example.imageclassification.presentation2.adapters.DialogRecyclerAdapter
import com.example.imageclassification.presentation2.adapters.HallRecyclerAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HallDetailsActivity : BaseActivity() {
    private val binding: ActivityHallDetailsBinding by binding(R.layout.activity_hall_details)
    private lateinit var adapter: HallRecyclerAdapter
    private lateinit var materialDialog: MaterialAlertDialogBuilder
    private lateinit var dialogAdapter: DialogRecyclerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.executePendingBindings()
        binding.lifecycleOwner = this
        binding.backBtn.setOnClickListener { finish() }
        dialogAdapter = DialogRecyclerAdapter()
        materialDialog = MaterialAlertDialogBuilder(this)
        val inflater = LayoutInflater.from(this)
        val bindingDialog = CustomDialogBinding.inflate(inflater)
        bindingDialog.dialogRecycler.adapter = dialogAdapter
        bindingDialog.dialogRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        materialDialog.setView(bindingDialog.root)
        val dialog = materialDialog.show()
        dialog.dismiss()
        adapter = HallRecyclerAdapter(){
            showDialog(dialog, it.imgs)
        }
        binding.hallRecycler.adapter = adapter
        binding.hallRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val buildingId = intent.getIntExtra("buildingId", 0)
        val floorId = intent.getIntExtra("floorId", 0)
        val halls = buildings[buildingId].floors?.get(floorId)?.halls
        adapter.setItems(halls!!)
    }
    fun showDialog(dialog: AlertDialog, list: MutableList<Int>){
        val dialogItems = mutableListOf<DialogItem>()
        list.forEach { dialogItems.add(DialogItem(null, null, it)) }
        dialogAdapter.setItems(dialogItems)
        dialog.show()
    }
}