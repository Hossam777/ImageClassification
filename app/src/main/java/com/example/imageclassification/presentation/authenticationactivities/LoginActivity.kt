package com.example.imageclassification.presentation.authenticationactivities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.example.imageclassification.R
import com.example.imageclassification.bases.BaseActivity
import com.example.imageclassification.databinding.ActivityLoginBinding
import com.example.imageclassification.presentation.homeactivity.HomeActivity
import com.example.imageclassification.utils.NetworkManager
import com.example.imageclassification.utils.hideKeyboard
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : BaseActivity() {
    private val binding: ActivityLoginBinding by binding(R.layout.activity_login)
    @Inject
    lateinit var authViewModel: AuthenticationViewModel
    @Inject
    lateinit var networkManager: NetworkManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.executePendingBindings()
        binding.backButton.setOnClickListener {
            finish()
        }
        binding.loginBtn.setOnClickListener {
            if(authViewModel.isLoading.value == true) return@setOnClickListener
            currentFocus?.hideKeyboard()
            if(!networkManager.checkForInternet()){
                AlertDialog.Builder(this)
                    .setTitle(resources.getString(R.string.alert_title))
                    .setMessage(resources.getString(R.string.err_no_internet))
                    .setNegativeButton(resources.getString(R.string.close)) { dialog, which -> }.show()
                return@setOnClickListener
            }
            val error = isValidData()
            if(error == null){
                authViewModel.login(binding.etEmail.text.toString(), binding.etPassword.text.toString())
            }
        }
        authViewModel.user.observe(this) {
            it.let { startActivity(Intent(this, HomeActivity::class.java)) }
        }
        binding.signupScreenBtn.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))

        }
        authViewModel.isLoading.observe(this) {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }
        authViewModel.errMessage.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
        authViewModel.setupFirebaseAuth()
        if(networkManager.checkForInternet()){ authViewModel.tryLoginWithoutCredentials() }
        setWatchers()
    }
    private fun isValidData(): String? {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return when {
            binding.etEmail.text.toString().isEmpty() -> {
                binding.tlmail.error = getString(R.string.err_please_enter_mail)
                getString(R.string.err_please_enter_mail)
            }
            !binding.etEmail.text.toString().matches(emailPattern.toRegex()) -> {
                binding.tlmail.error = getString(R.string.err_please_enter_valid_mail)
                getString(R.string.err_please_enter_valid_mail)
            }
            binding.etPassword.text.toString().isEmpty() -> {
                binding.tlPassword.error = getString(R.string.err_please_enter_password)
                getString(R.string.err_please_enter_password)
            }
            binding.etPassword.text.toString().length < 6 -> {
                binding.tlPassword.error = getString(R.string.err_password_at_least)
                getString(R.string.err_password_at_least)
            }
            else -> null
        }
    }
    private fun setWatchers(){
        binding.etEmail.addTextChangedListener {
            binding.tlmail.isErrorEnabled = false
        }
        binding.etPassword.addTextChangedListener {
            binding.tlPassword.isErrorEnabled = false
        }
    }

}