package com.lk.myproject

import android.app.Application
import android.content.Context
import android.os.SystemClock
import android.util.Log
import com.alibaba.android.arouter.launcher.ARouter
import com.facebook.react.PackageList
import com.facebook.react.ReactApplication
import com.facebook.react.ReactNativeHost
import com.facebook.react.ReactPackage
import com.facebook.react.defaults.DefaultReactNativeHost
import com.facebook.soloader.SoLoader

class MyApplication : Application(), ReactApplication {
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

        var time = System.currentTimeMillis()
        ARouter.openLog()
        ARouter.openDebug()
        ARouter.init(this)
        SoLoader.init(this, false)
        Log.d("TestPerformance", "ar time: ${System.currentTimeMillis() - time}")
    }

    private val mReactNativeHost: ReactNativeHost = object : DefaultReactNativeHost(this) {
        override fun getUseDeveloperSupport(): Boolean {
            return BuildConfig.DEBUG
        }

        override fun getPackages(): List<ReactPackage> {
            // Packages that cannot be autolinked yet can be added manually here, for example:
            // packages.add(new MyReactNativePackage());
            return PackageList(this).packages
        }

        override fun getJSMainModuleName(): String {
            return "index"
        }

        override val isNewArchEnabled: Boolean
            get() = true
        override val isHermesEnabled: Boolean?
            get() = true
    }

    override fun getReactNativeHost(): ReactNativeHost {
        return mReactNativeHost
    }
}