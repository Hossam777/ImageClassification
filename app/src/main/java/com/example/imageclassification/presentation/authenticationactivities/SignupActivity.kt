package com.example.imageclassification.presentation.authenticationactivities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.example.imageclassification.R
import com.example.imageclassification.bases.BaseActivity
import com.example.imageclassification.databinding.ActivitySignupBinding
import com.example.imageclassification.presentation.homeactivity.HomeActivity
import com.example.imageclassification.presentation2.HomeActivity2
import com.example.imageclassification.utils.NetworkManager
import com.example.imageclassification.utils.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignupActivity : BaseActivity() {
    private val binding: ActivitySignupBinding by binding(R.layout.activity_signup)
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
        authViewModel.user.observe(this) {
            it.let { startActivity(Intent(this, HomeActivity2::class.java)) }
        }
        binding.signupBtn.setOnClickListener {
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
                authViewModel.signup(binding.etMail.text.toString(), binding.etPassword.text.toString()
                    , binding.etName.text.toString() + " " + binding.etTitle.text.toString())
            }
        }
        authViewModel.isLoading.observe(this) {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }
        authViewModel.errMessage.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
        authViewModel.setupFirebaseAuth()
        setWatchers()
    }
    private fun isValidData(): String? {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return when {
            binding.etName.text.toString().isEmpty() -> {
                binding.tlName.error = getString(R.string.err_please_enter_name)
                getString(R.string.err_please_enter_name)
            }
            binding.etTitle.text.toString().isEmpty() -> {
                binding.tlTitle.error = getString(R.string.err_please_enter_title)
                getString(R.string.err_please_enter_title)
            }
            binding.etMail.text.toString().isEmpty() -> {
                binding.tlMail.error = getString(R.string.err_please_enter_mail)
                getString(R.string.err_please_enter_mail)
            }
            !binding.etMail.text.toString().matches(emailPattern.toRegex()) -> {
                binding.tlMail.error = getString(R.string.err_please_enter_valid_mail)
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
            !binding.etConfirmPassword.text.toString().equals(binding.etPassword.text.toString()) -> {
                binding.tlConfirmPassword.error = getString(R.string.err_confirm_pass_wrong)
                getString(R.string.err_confirm_pass_wrong)
            }
            else -> null
        }
    }
    private fun setWatchers(){
        binding.etName.addTextChangedListener {
            binding.tlName.isErrorEnabled = false
        }
        binding.etTitle.addTextChangedListener {
            binding.tlTitle.isErrorEnabled = false
        }
        binding.etMail.addTextChangedListener {
            binding.tlMail.isErrorEnabled = false
        }
        binding.etPassword.addTextChangedListener {
            binding.tlPassword.isErrorEnabled = false
        }
        binding.etConfirmPassword.addTextChangedListener {
            binding.tlConfirmPassword.isErrorEnabled = false
        }
    }
}