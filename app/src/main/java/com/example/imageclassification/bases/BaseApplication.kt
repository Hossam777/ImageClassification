package com.example.imageclassification.bases

import android.app.Application
import android.content.Context
import com.example.imageclassification.utils.SingletonHolderWithoutContext
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApplication : Application() {
    lateinit var context: Context

    override fun onCreate() {
        super.onCreate()
        context = this
    }

    companion object : SingletonHolderWithoutContext<BaseApplication>(::BaseApplication) {
        var applicationContext: Context? = null
    }

}