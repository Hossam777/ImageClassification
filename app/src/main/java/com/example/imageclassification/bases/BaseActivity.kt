package com.example.imageclassification.bases

import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

open class BaseActivity : AppCompatActivity() {

    protected inline fun <reified T : ViewDataBinding> binding(@LayoutRes resId: Int): Lazy<T> =
        lazy { DataBindingUtil.setContentView<T>(this, resId) }

    init {
//    App.instance.updateConfig(this)
    }
}