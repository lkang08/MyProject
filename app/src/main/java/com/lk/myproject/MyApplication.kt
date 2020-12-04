package com.lk.myproject

import android.content.Context
import android.os.SystemClock
import android.util.Log
import com.uuch.adlibrary.LApplication

class MyApplication : LApplication() {
    companion object {
        lateinit var app: MyApplication
        var begin: Long = 0L
        fun l(msg: String) {
            Log.d("abctest",
                "$msg time = ${SystemClock.currentThreadTimeMillis() - begin}")
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