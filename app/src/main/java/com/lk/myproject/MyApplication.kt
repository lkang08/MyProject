package com.lk.myproject

import android.app.Application
import android.content.Context
import android.os.SystemClock
import android.util.Log

class MyApplication : Application() {
    companion object {
        lateinit var app: MyApplication
        var begin: Long = 0L
        fun l(msg: String) {
            Log.d(
                "abctest",
                "$msg time = ${SystemClock.currentThreadTimeMillis() - begin}"
            )
        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        begin = SystemClock.currentThreadTimeMillis()
    }

    override fun onCreate() {
        super.onCreate()
        app = this
        l("app onCreate")
    }
}