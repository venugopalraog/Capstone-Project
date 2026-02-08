package com.capstone.designpatterntutorial.di

import android.app.Application
import android.content.ContentResolver
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val application: Application) {

    @Provides
    @Singleton
    fun provideApplication(): Application = application

    @Provides
    @Singleton
    fun provideContentResolver(): ContentResolver = application.contentResolver
}
