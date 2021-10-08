package com.example.chatapp

import android.app.Application

class sharedApp:Application() {

    companion object {
        lateinit var prefs: Prefs
    }
    override fun onCreate() {
        super.onCreate()
        prefs = Prefs(applicationContext)
    }
}