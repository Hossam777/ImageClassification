package com.example.imageclassification.presentation.authenticationactivities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.compose.ui.text.style.TextAlign
import androidx.core.widget.addTextChangedListener
import com.example.imageclassification.R
import com.example.imageclassification.bases.BaseActivity
import com.example.imageclassification.databinding.ActivityLoginBinding
import com.example.imageclassification.presentation.homeactivity.HomeActivity
import com.example.imageclassification.presentation2.HomeActivity2
import com.example.imageclassification.utils.NetworkManager
import com.example.imageclassification.utils.hideKeyboard
import com.google.android.material.snackbar.Snackbar
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
            it.let { startActivity(Intent(this, HomeActivity2::class.java)) }
        }
        binding.signupScreenBtn.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))

        }
        binding.forgotPasswordTxt.setOnClickListener {
            val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
            if(binding.etEmail.text.toString().isNotEmpty() &&
                binding.etEmail.text.toString().matches(emailPattern.toRegex())){
                authViewModel.sendResetPasswordEmail(binding.etEmail.text.toString(), getString(R.string.password_reset_msg))
            }else{
                showSnackBar(getString(R.string.err_please_enter_valid_mail))
            }
        }
        authViewModel.isLoading.observe(this) {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }
        authViewModel.errMessage.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
        authViewModel.messageToUser.observe(this) {
            showSnackBar(it)
        }
        authViewModel.setupFirebaseAuth()
        if(networkManager.checkForInternet()){ authViewModel.tryLoginWithoutCredentials() }
        setWatchers()
    }

    private fun showSnackBar(message: String) {
        val snackbar = Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
        snackbar.view.setBackgroundColor(resources.getColor(R.color.green_btn_bg))
        snackbar.setTextColor(resources.getColor(R.color.white))
        snackbar.view.layoutDirection = View.LAYOUT_DIRECTION_RTL
        snackbar.show()
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