package com.lk.myproject.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

import java.util.Timer
import java.util.TimerTask

object KeybordUtil {
    fun showKeyBoard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, 2)
    }

    fun showKeyBoard(context: Context) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(2, 2)
    }

    /**
     * 自动弹软键盘
     *
     * @param context
     * @param et
     */
    fun showSoftInput(context: Context, et: EditText) {
        Timer().schedule(object : TimerTask() {
            override fun run() {
                (context as Activity).runOnUiThread {
                    et.isFocusable = true
                    et.isFocusableInTouchMode = true
                    //请求获得焦点
                    et.requestFocus()
                    //调用系统输入法
                    val inputManager = et
                            .context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputManager.showSoftInput(et, 0)
                }
            }
        }, 200)
    }

    /**
     * 自动关闭软键盘
     *
     * @param activity
     */
    fun closeKeybord(activity: Activity) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm?.hideSoftInputFromWindow(activity.window.decorView.windowToken, 0)
    }

    /**
     * 打开关闭相互切换
     *
     * @param activity
     */
    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm.isActive) {
            if (activity.currentFocus!!.windowToken != null) {
                imm.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            }
        }
    }

}
