package com.example.imageclassification.presentation.homeactivity

import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.core.view.get
import androidx.navigation.findNavController
import com.example.imageclassification.R
import com.example.imageclassification.bases.BaseActivity
import com.example.imageclassification.databinding.ActivityHomeBinding
import com.example.imageclassification.presentation.authenticationactivities.AuthenticationViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : BaseActivity() {
    private val binding: ActivityHomeBinding by binding(R.layout.activity_home)
    private var aboutFragmentIsVisible = false

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
        binding.navigationView.menu[0].isChecked = true
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
                R.id.homeMenuItem -> {
                    binding.titleTv.text = resources.getString(R.string.home_page)
                    if(aboutFragmentIsVisible)
                        findNavController(R.id.navHostFragment).popBackStack()
                    aboutFragmentIsVisible = false
                }
                R.id.aboutMenuItem -> {
                    binding.titleTv.text = resources.getString(R.string.about_us)
                    findNavController(R.id.navHostFragment).navigate("about")
                    aboutFragmentIsVisible = true
                }
                R.id.logoutMenuItem -> {
                    authViewModel.logout()
                    finish()
                }
            }
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    override fun onBackPressed() {
        if(aboutFragmentIsVisible){
            binding.navigationView.menu[0].isChecked = true
            aboutFragmentIsVisible = false
        }
        super.onBackPressed()
    }
}