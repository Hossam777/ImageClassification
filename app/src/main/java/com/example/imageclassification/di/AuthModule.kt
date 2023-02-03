package com.example.imageclassification.di

import com.example.imageclassification.data.local.UserSessionManager
import com.example.imageclassification.presentation.authenticationactivities.AuthenticationViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object AuthModule {
    @Provides
    fun provideAuthViewModel(userSessionManager: UserSessionManager): AuthenticationViewModel =
        AuthenticationViewModel(userSessionManager)

}