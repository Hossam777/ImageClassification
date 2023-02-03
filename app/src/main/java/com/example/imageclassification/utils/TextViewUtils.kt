package com.example.imageclassification.utils

import android.content.Context
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun String.isValidEmail():Boolean{
    return Patterns.EMAIL_ADDRESS.matcher(this.trim { it<=' ' }).matches()
}