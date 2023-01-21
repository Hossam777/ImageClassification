package com.example.imageclassification.di
import com.example.imageclassification.presentation.mainactivity.MainActivityViewModel
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.provider

val mainActivityModule = Kodein.Module("MainActivityModule"){
    bind<MainActivityViewModel>() with provider { MainActivityViewModel() }
}