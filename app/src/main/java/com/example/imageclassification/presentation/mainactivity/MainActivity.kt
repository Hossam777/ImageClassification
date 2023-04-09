package com.example.imageclassification.presentation.mainactivity

import android.content.Intent
import android.os.Bundle
import androidx.core.graphics.drawable.toBitmap
import com.example.imageclassification.R
import com.example.imageclassification.bases.BaseActivity
import com.example.imageclassification.data.local.UserSessionManager
import com.example.imageclassification.databinding.ActivityMainBinding
import com.example.imageclassification.presentation.authenticationactivities.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity(){
    private val binding: ActivityMainBinding by binding(R.layout.activity_main)
    @Inject
    lateinit var userSessionManager: UserSessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.executePendingBindings()
        userSessionManager.processedImage = resources.getDrawable(R.drawable.ain_shams).toBitmap()
        binding.continueBtn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}