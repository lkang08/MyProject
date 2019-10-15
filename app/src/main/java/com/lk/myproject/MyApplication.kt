package com.lk.myproject

import android.app.Application

class MyApplication : Application() {
    companion object {
        lateinit var app: MyApplication
    }

    override fun onCreate() {
        super.onCreate()
        app = this
    }
}