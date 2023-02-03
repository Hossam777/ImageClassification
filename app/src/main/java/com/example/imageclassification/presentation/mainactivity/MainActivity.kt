package com.example.imageclassification.presentation.mainactivity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.imageclassification.R
import com.example.imageclassification.bases.BaseActivity
import com.example.imageclassification.databinding.ActivityMainBinding
import com.example.imageclassification.presentation.authenticationactivities.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity(){
    private val binding: ActivityMainBinding by binding(R.layout.activity_main)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.executePendingBindings()
        binding.continueBtn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }


}