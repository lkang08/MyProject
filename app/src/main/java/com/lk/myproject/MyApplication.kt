package com.lk.myproject

import android.app.Application
import android.content.Context
import android.os.SystemClock
import android.util.Log
import com.lk.myproject.hook.ThreadMethodHook
import com.lk.myproject.performance.PERF
import java.io.File

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
        initPerformanceTool()
        ThreadMethodHook.testHook()
        l("app onCreate")
    }

    fun initPerformanceTool() {
        var builder = PERF.Builder().globalTag("p-tool")
            .checkThread(true)
            .checkUI(false, 100) // 检查 ui 线程, 超过指定时间还未结束，会被认为 ui 线程 block
            .checkFps(false) // 检查 Fps
            .checkIPC(false) // 检查 IPC 调用
            .issueSupplier(object : PERF.IssueSupplier {
                override fun maxCacheSize(): Long {
                    return 1024 * 1024 * 20
                }

                override fun cacheRootDir(): File {
                    return cacheDir
                }

                override fun upLoad(p0: File?): Boolean {
                    return false
                }
            }).build()
        PERF.init(builder)
    }
}