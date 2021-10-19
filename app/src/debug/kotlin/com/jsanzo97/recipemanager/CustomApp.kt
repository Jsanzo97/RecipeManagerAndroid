package com.jsanzo97.recipemanager

import android.app.Application
import timber.log.Timber

abstract class CustomApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}