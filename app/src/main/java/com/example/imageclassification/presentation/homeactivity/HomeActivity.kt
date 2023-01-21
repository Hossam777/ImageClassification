package com.example.imageclassification.presentation.homeactivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.ImageButton
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import com.example.imageclassification.R
import com.example.imageclassification.databinding.ActivityHomeBinding
import com.example.imageclassification.presentation.authenticationactivities.LoginActivity

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        binding.lifecycleOwner = this
        navigationMenuSetup()
    }

    private fun navigationMenuSetup() {
        binding.menuOpenBtn.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
        binding.navigationView.getHeaderView(0).findViewById<ImageButton>(R.id.menuCloseBtn).setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }
        binding.navigationView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.logintMenuItem -> {
                    startActivity(Intent(this, LoginActivity::class.java))
                }
            }
            true
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
    }
}