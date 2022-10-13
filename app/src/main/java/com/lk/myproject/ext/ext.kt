package com.lk.myproject.ext

import android.util.Log
import com.lk.myproject.MyApplication
import java.lang.ref.WeakReference

val displayMetrics = MyApplication.app.resources.displayMetrics!!

inline val screenWidth: Int
    get() = displayMetrics.widthPixels

inline val screenHeight: Int
    get() = displayMetrics.heightPixels

inline val screenDensity: Float
    get() = displayMetrics.density

inline val scaledDensity: Float
    get() = displayMetrics.scaledDensity

val Int.dp2px: Int
    get() = dp2px(this)

val Int.px2dp: Int
    get() = px2dp(this)

val Int.sp2px: Int
    get() = dp2px(this)

val Int.px2sp: Int
    get() = px2dp(this)


val Float.dp2px: Float
    get() = dp2px(this).toFloat()

val Float.px2dp: Float
    get() = px2dp(this).toFloat()

val Float.sp2px: Float
    get() = dp2px(this).toFloat()

val Float.px2sp: Float
    get() = px2dp(this).toFloat()

fun dp2px(dp: Number) = (dp.toFloat() * screenDensity + 0.5f).toInt()

fun px2dp(px: Number) = (px.toFloat() / screenDensity + 0.5f).toInt()

fun sp2px(sp: Number) = (sp.toFloat() * scaledDensity + 0.5f).toInt()

fun px2sp(px: Number) = (px.toFloat() / scaledDensity + 0.5f).toInt()

/**
 * 弱引用
 */
fun <T> WeakReference<T>.safe(body: T.() -> Unit) {
    this.get()?.body()
}

fun <T : Any> T.log() = run { Log.d("MyProject", "Log : $this") }