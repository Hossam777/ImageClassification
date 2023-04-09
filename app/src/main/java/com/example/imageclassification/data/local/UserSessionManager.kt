package com.example.imageclassification.data.local

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import com.example.imageclassification.utils.SingletonHolder

class UserSessionManager(context: Context) {
    private val editor: SharedPreferences.Editor
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        "ImageClassificationSharedPref",
        Context.MODE_PRIVATE
    )
    init {
        editor = sharedPreferences.edit()
        editor.apply()
    }
    lateinit var processedImage: Bitmap

    var isLoggedIn : Boolean
        get() {
            return sharedPreferences.getBoolean("isLoggedIn",false)
        }
        set(value) {
            editor.putBoolean("isLoggedIn",value)
            editor.commit()
        }
    fun logout() {
        isLoggedIn = false
    }
    companion object : SingletonHolder<UserSessionManager, Context>(::UserSessionManager){
        private const val currentUserKey = "currentUser"
        private const val SHARED_PREF_NAME = "userSession"
    }
}