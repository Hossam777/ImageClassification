package com.example.imageclassification.presentation2

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.imageclassification.R
import com.example.imageclassification.bases.BaseActivity
import com.example.imageclassification.data.local.DialogItem
import com.example.imageclassification.data.local.ahdafElKolya
import com.example.imageclassification.data.local.alAqsamElAmalya
import com.example.imageclassification.data.local.elShoaab
import com.example.imageclassification.data.local.nabzaAanElKolya
import com.example.imageclassification.data.local.resaletElKolya
import com.example.imageclassification.data.local.ro2yetElKolya
import com.example.imageclassification.data.local.seeaatElModaragat
import com.example.imageclassification.databinding.ActivityAboutCollegeBinding
import com.example.imageclassification.databinding.CustomDialogBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AboutCollegeActivity : BaseActivity() {
    private val binding: ActivityAboutCollegeBinding by binding(R.layout.activity_about_college)
    private lateinit var materialDialog: MaterialAlertDialogBuilder
    private lateinit var adapter: DialogRecyclerAdapter
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
        bindingDialog.closeBtn.setOnClickListener {
            dialog.dismiss()
        }
        binding.resalaLL.setOnClickListener { showDialog(dialog, resaletElKolya) }
        binding.nabzaLL.setOnClickListener { showDialog(dialog, nabzaAanElKolya) }
        binding.ahdafLL.setOnClickListener { showDialog(dialog, ahdafElKolya) }
        binding.ro2yaLL.setOnClickListener { showDialog(dialog, ro2yetElKolya) }
        binding.sho3abLL.setOnClickListener { showDialog(dialog, elShoaab) }
        binding.aksamLL.setOnClickListener { showDialog(dialog, alAqsamElAmalya) }
        binding.se3atLL.setOnClickListener { showDialog(dialog, seeaatElModaragat) }
    }
    fun showDialog(dialog: AlertDialog, list: MutableList<DialogItem>){
        adapter.setItems(list)
        dialog.show()
    }
}