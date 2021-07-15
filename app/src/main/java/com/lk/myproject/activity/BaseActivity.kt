package com.lk.myproject.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.util.concurrent.CancellationException
import kotlin.coroutines.CoroutineContext

abstract class BaseActivity : AppCompatActivity() {
    var test: String = "BaseActivity"

    //Kotlin 协程作用域，防止内存泄漏
    private val mCoroutineScope = MainScope()

    /*override val coroutineContext: CoroutineContext
        get() = mCoroutineScope.coroutineContext*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("lk###", "BaseActivity onCreate")
        test = "BaseActivity change"
    }

    override fun onDestroy() {
        super.onDestroy()
        //cancel(CancellationException())
    }
}