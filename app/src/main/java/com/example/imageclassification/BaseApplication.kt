package com.example.imageclassification

import android.app.Application
import com.example.imageclassification.di.mainActivityModule
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule

class BaseApplication : Application(), KodeinAware {
    override val kodein = Kodein{
        import(androidXModule(this@BaseApplication))
        import(mainActivityModule)
    }

}