package com.example.imageclassification.presentation2

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.imageclassification.R
import com.example.imageclassification.bases.BaseActivity
import com.example.imageclassification.data.local.buildings
import com.example.imageclassification.databinding.ActivityBuidlingDetailsBinding
import com.example.imageclassification.presentation2.adapters.FloorRecyclerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BuidlingDetailsActivity : BaseActivity() {
    private val binding: ActivityBuidlingDetailsBinding by binding(R.layout.activity_buidling_details)
    private lateinit var adapter: FloorRecyclerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.executePendingBindings()
        binding.lifecycleOwner = this
        binding.backBtn.setOnClickListener { finish() }
        val intent = Intent(this, HallDetailsActivity::class.java)
        adapter = FloorRecyclerAdapter(){
            intent.putExtra("buildingId", it.buildingId)
            intent.putExtra("floorId", it.id)
            startActivity(intent)
        }
        getIntent().getIntExtra("buildingId", 0).let {buildingId ->
            println(buildingId)
            binding.floorRecycler.adapter = adapter
            binding.floorRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            buildings[buildingId].floors?.let { adapter.setItems(it)}
        }

    }
}