package com.lk.myproject.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.lk.myproject.R
import com.lk.myproject.utils.StatusBarUtils
import com.lk.myproject.utils.Utils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var isStart: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtils.setNoStatusBar(this)
        setContentView(R.layout.activity_main)
        button.setOnClickListener {
            test()
        }
        circle_image_view.setOnClickListener {
            isStart = !isStart
            myText.text = if (isStart) "Running" else "Begin"
            when (isStart) {
                true -> {
                    zero_wave_view.setStyle(Paint.Style.FILL)
                    zero_wave_view.setColor(Color.parseColor("#FF56F203"))
                    zero_wave_view.start()
                }
                else -> {
                    zero_wave_view.stop()
                }
            }
        }
    }

    private fun test() {
        startActivity(Intent(this, PermissionTestActivity::class.java))
        Log.d("lk###", "status bar = ${Utils.getStatusBarHeight(this)} ,status height = ${Utils.getStatusHeight(this)}")
        Log.d("lk###", "dip = ${Utils.getDpi(this)} getScreenHeight = ${Utils.getScreenHeight(this)}")
        Log.d("lk###", "getTitleHeight = ${Utils.getTitleHeight(this)} getBottomStatusHeight = ${Utils.getBottomStatusHeight(this)}")
    }
}
