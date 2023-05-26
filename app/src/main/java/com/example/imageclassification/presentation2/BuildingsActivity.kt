package com.example.imageclassification.presentation2

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.helper.widget.Carousel
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.imageclassification.R
import com.example.imageclassification.bases.BaseActivity
import com.example.imageclassification.data.local.DialogItem
import com.example.imageclassification.databinding.ActivityBuildingsBinding
import com.example.imageclassification.databinding.CustomDialogBinding
import com.example.imageclassification.presentation.homeactivity.HomeActivity
import com.example.imageclassification.presentation2.adapters.DialogRecyclerAdapter
import com.example.imageclassification.presentation2.adapters.SliderAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.smarteist.autoimageslider.SliderView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BuildingsActivity : BaseActivity() {
    private val binding: ActivityBuildingsBinding by binding(R.layout.activity_buildings)
    private lateinit var materialDialog: MaterialAlertDialogBuilder
    private lateinit var adapter: DialogRecyclerAdapter
    private val carosalImgs = listOf<Int>(R.drawable.castle_thumb, R.drawable.main_thumb, R.drawable.section_thumb, R.drawable.theater_thumb, R.drawable.search)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.executePendingBindings()
        binding.lifecycleOwner = this
        binding.backBtn.setOnClickListener { finish() }
        adapter = DialogRecyclerAdapter()
        materialDialog = MaterialAlertDialogBuilder(this)
        val inflater = LayoutInflater.from(this)
        val bindingDialog = CustomDialogBinding.inflate(inflater)
        materialDialog.setView(bindingDialog.root)
        bindingDialog.dialogRecycler.adapter = adapter
        bindingDialog.dialogRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val dialog = materialDialog.show()
        dialog.dismiss()
        /*val intent = Intent(this, BuidlingDetailsActivity::class.java)
        binding.theaterLL.setOnClickListener {
            intent.putExtra("buildingId", 3)
            startActivity(intent)
        }
        binding.artBuildingLL.setOnClickListener {
            intent.putExtra("buildingId", 2)
            startActivity(intent)
        }
        binding.economyBuildingLL.setOnClickListener {
            intent.putExtra("buildingId", 1)
            startActivity(intent)
        }
        binding.learningBuildingLL.setOnClickListener {
            intent.putExtra("buildingId", 0)
            startActivity(intent)
        }
        binding.scanBuildingLL.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }*/
        val sliderAdapter = SliderAdapter(carosalImgs){ onItemClicked(it) }
        binding.slider.autoCycleDirection = SliderView.LAYOUT_DIRECTION_LTR
        binding.slider.scrollTimeInSec = 3
        binding.slider.isAutoCycle = true
        binding.slider.startAutoCycle()
        binding.slider.setSliderAdapter(sliderAdapter)
    }
    private fun onItemClicked(index: Int){
        var intent = Intent(this, BuidlingDetailsActivity::class.java)
        if(index == 4)
            intent = Intent(this, HomeActivity::class.java)
        else
            intent.putExtra("buildingId", index)
        startActivity(intent)
    }
    fun showDialog(dialog: AlertDialog, list: MutableList<DialogItem>){
        adapter.setItems(list)
        dialog.show()
    }
}