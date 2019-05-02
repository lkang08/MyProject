package com.lk.myproject.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.bumptech.glide.Glide
import com.lk.myproject.R
import com.lk.myproject.utils.StatusBarUtils
import kotlinx.android.synthetic.main.activity_main_new.*

class MainActivity2 : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtils.setNoStatusBar(this)
        setContentView(R.layout.activity_main_new)
        button.setOnClickListener {
            loadGif()
        }
        goBtn.setOnClickListener {
            startActivity(Intent(this@MainActivity2, GifActivity::class.java))
        }
    }

    private fun loadGif() {
        Glide.with(this).load(R.drawable.pink).into(loading_view)
        Glide.with(this).load(R.drawable.gif_emoji_3).into(gif)
        Glide.with(this).load(R.drawable.gif0).into(gifImageView)
        Glide.with(this).load(R.drawable.gif4).into(gif2)
        Glide.with(this).load(R.drawable.gif4).circleCrop().into(gif3)
    }

}