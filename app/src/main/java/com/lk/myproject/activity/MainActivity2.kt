package com.lk.myproject.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.lk.myproject.R
import com.lk.myproject.utils.StatusBarUtils
import com.lk.myproject.utils.Utils
import com.lk.myproject.utils.log
import com.lk.myproject.widget.DatePickerPopWin
import kotlinx.android.synthetic.main.activity_main_new.*

class MainActivity2 : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtils.setNoStatusBar(this)
        setContentView(R.layout.activity_main_new)
        log("getdip = ${Utils.getDpi(this)} getScreenHeight = ${Utils.getScreenHeight(this)}")
        log("getStatusBarHeight = ${Utils.getStatusBarHeight(this)} getStatusHeight = ${Utils.getStatusHeight(this)}")
        button.setOnClickListener {
            loadGif()
            //loadDate()
            shimmer_view.startShimmerAnimation()
            shimmer_view0.startShimmerAnimation()
            shimmer_text.startShimmerAnimation()
        }
        goBtn.setOnClickListener {
            //startActivity(Intent(this@MainActivity2, GifActivity::class.java))
            startActivity(Intent(this@MainActivity2, NotificationActivity::class.java))
        }
    }

    private fun loadDate() {

        val timePickerPopWin = DatePickerPopWin.Builder(this@MainActivity2,
            DatePickerPopWin.OnDatePickedListener { year, month, day, dateDesc -> Toast.makeText(this@MainActivity2, "year = $year month = $month", Toast.LENGTH_SHORT).show() })
            .textConfirm("确定")
            .textCancel("取消")
            .btnTextSize(16)
            .viewTextSize(25)
            .colorCancel(Color.parseColor("#999999"))
            .colorConfirm(Color.parseColor("#009900"))
            .build()
        timePickerPopWin.showPopWin(this@MainActivity2)
    }

    private fun loadGif() {
        Glide.with(this).load(R.drawable.pink).into(loading_view)
        Glide.with(this).load(R.drawable.gif_emoji_3).into(gif)
        Glide.with(this).load(R.drawable.gif0).into(gifImageView)
        Glide.with(this).load(R.drawable.gif4).into(gif2)
        Glide.with(this).load(R.drawable.gif4).circleCrop().into(gif3)
    }
}