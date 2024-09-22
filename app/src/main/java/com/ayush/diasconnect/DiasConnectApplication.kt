package com.ayush.diasconnect

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DiasConnectApplication :Application() {
    override fun onCreate() {
        super.onCreate()
    }
}