package com.example.imageclassification.di

import android.app.Application
import android.content.Context
import com.example.imageclassification.data.local.UserSessionManager
import com.example.imageclassification.presentation.homeactivity.objectdetection.ImageClassifier
import com.example.imageclassification.utils.NetworkManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application
    }

    @Provides
    @Singleton
    fun provideUserNetworkManager(context: Context): NetworkManager =
        NetworkManager(context)

    @Provides
    @Singleton
    fun provideUserSessionManager(context: Context): UserSessionManager =
        UserSessionManager(context)
    @Provides
    @Singleton
    fun provideImageClassifier(context: Context): ImageClassifier =
        ImageClassifier(context)


}