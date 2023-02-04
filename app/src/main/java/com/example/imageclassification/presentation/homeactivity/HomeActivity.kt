package com.example.imageclassification.presentation.homeactivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.example.imageclassification.R
import com.example.imageclassification.bases.BaseActivity
import com.example.imageclassification.databinding.ActivityHomeBinding
import com.example.imageclassification.presentation.authenticationactivities.AuthenticationViewModel
import com.google.firebase.auth.ktx.userProfileChangeRequest
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.system.exitProcess

@AndroidEntryPoint
class HomeActivity : BaseActivity() {
    private val binding: ActivityHomeBinding by binding(R.layout.activity_home)

    @Inject
    lateinit var authViewModel: AuthenticationViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.executePendingBindings()
        binding.lifecycleOwner = this
        navigationMenuSetup()
        authViewModel.setupFirebaseAuth()
        authViewModel.user.observe(this) {
            Toast.makeText(
                this,
                "Welcome " + authViewModel.name.value.toString(),
                Toast.LENGTH_LONG
            ).show()
        }
    }


    private fun navigationMenuSetup() {
        binding.menuOpenBtn.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
        binding.navigationView.getHeaderView(0).findViewById<ImageButton>(R.id.menuCloseBtn)
            .setOnClickListener {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            }
        binding.navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.logoutMenuItem -> {
                    authViewModel.logout()
                    finish()
                }
            }
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }
}