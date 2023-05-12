package com.example.imageclassification.presentation2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.imageclassification.R
import com.example.imageclassification.bases.BaseActivity
import com.example.imageclassification.data.local.DialogItem
import com.example.imageclassification.data.local.ro2yetElKolya
import com.example.imageclassification.databinding.ActivityHome2Binding
import com.example.imageclassification.databinding.CustomDialogBinding
import com.example.imageclassification.presentation.homeactivity.HomeActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity2 : BaseActivity() {
    private val binding: ActivityHome2Binding by binding(R.layout.activity_home2)
    private lateinit var materialDialog: MaterialAlertDialogBuilder
    private lateinit var adapter: DialogRecyclerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.executePendingBindings()
        binding.lifecycleOwner = this
        adapter = DialogRecyclerAdapter()
        materialDialog = MaterialAlertDialogBuilder(this)
        val inflater = LayoutInflater.from(this)
        val bindingDialog = CustomDialogBinding.inflate(inflater)
        materialDialog.setView(bindingDialog.root)
        bindingDialog.dialogRecycler.adapter = adapter
        bindingDialog.dialogRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val dialog = materialDialog.show()
        dialog.dismiss()
        bindingDialog.closeBtn.setOnClickListener {
            dialog.dismiss()
        }
        binding.sh2onLL.setOnClickListener { showDialog(dialog, ro2yetElKolya) }
        binding.aboutLL.setOnClickListener { startActivity(Intent(this, AboutCollegeActivity::class.java)) }
        binding.buildingLL.setOnClickListener { startActivity(Intent(this, BuildingsActivity::class.java)) }
        binding.sectionsLL.setOnClickListener { showDialog(dialog, ro2yetElKolya) }
        binding.aboutAppLL.setOnClickListener { showDialog(dialog, ro2yetElKolya) }
        binding.locationLL.setOnClickListener {  }
        binding.searchForBuildingLL.setOnClickListener { startActivity(Intent(this, HomeActivity::class.java)) }

    }
    fun showDialog(dialog: AlertDialog, list: MutableList<DialogItem>){
        adapter.setItems(list)
        dialog.show()
    }
}