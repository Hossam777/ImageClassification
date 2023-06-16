package com.example.imageclassification.presentation2

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.imageclassification.R
import com.example.imageclassification.bases.BaseActivity
import com.example.imageclassification.data.local.DialogItem
import com.example.imageclassification.data.local.ahdafElKolya
import com.example.imageclassification.data.local.alAqsamElAmalya
import com.example.imageclassification.data.local.nabzaAanElKolya
import com.example.imageclassification.data.local.resaletElKolya
import com.example.imageclassification.data.local.ro2yetElKolya
import com.example.imageclassification.databinding.ActivityAboutCollegeBinding
import com.example.imageclassification.databinding.CustomDialogBinding
import com.example.imageclassification.presentation2.adapters.DialogRecyclerAdapter
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
        binding.resalaLL.setOnClickListener { showDialog(dialog, resaletElKolya, false) }
        binding.nabzaLL.setOnClickListener { showDialog(dialog, nabzaAanElKolya, false) }
        binding.ahdafLL.setOnClickListener { showDialog(dialog, ahdafElKolya, false) }
        binding.ro2yaLL.setOnClickListener { showDialog(dialog, ro2yetElKolya, false) }
        binding.aksamLL.setOnClickListener { showDialog(dialog, alAqsamElAmalya, true) }
        binding.locationLL.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.google.com/maps/dir//Faculty+Of+Specific+Education+-+Ain+Shams+University,+%D8%B1%D9%85%D8%B3%D9%8A%D8%B3%D8%8C+El-Montaza,+Heliopolis,+%D8%A7%D9%84%D9%82%D8%A7%D9%87%D8%B1%D8%A9%D8%8C%E2%80%AD/@30.0444196,31.2357116,17z/data=!4m9!4m8!1m0!1m5!1m1!1s0x14583f9b089cdf55:0xc02108b0d965a80f!2m2!1d31.2847106!2d30.0758042!3e0?entry=ttu")
            )
            startActivity(intent)
        }
    }
    fun showDialog(dialog: AlertDialog, list: MutableList<DialogItem>, isSections: Boolean){
        adapter.setItems(list, isSections)
        dialog.show()
    }
}