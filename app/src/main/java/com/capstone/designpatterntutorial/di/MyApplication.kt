package com.capstone.designpatterntutorial.di

import android.app.Application
import com.capstone.designpatterntutorial.BuildConfig
import timber.log.Timber

class MyApplication : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
