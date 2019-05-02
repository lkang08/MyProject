package com.lk.myproject.activity

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.lk.myproject.R
import kotlinx.android.synthetic.main.activity_gif.*

class GifActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gif)
        loadGif()
    }

    private fun loadGif() {
        Glide.with(this).load(R.drawable.pink).into(loading_view)
        Glide.with(this).load(R.drawable.gif_emoji_3).into(gif)
        Glide.with(this).load(Uri.parse("http://img.soogif.com/5D4bWERvgwvi95peLnA1pIzplnjObBQK.gif")).diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.gif4).circleCrop().into(gif2)
        Glide.with(this).load(Uri.parse("http://img.soogif.com/ADVfaaMIVqmbC2aMAHLQk5aXChNZboC2.gif")).diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.gif0).circleCrop().into(gifImageView)
        Glide.with(this).load(R.drawable.demo).circleCrop().into(iv_gif)
    }

    override fun onStart() {
        super.onStart()
        gifImageView.startAnimation()
    }

    override fun onStop() {
        super.onStop()
        gifImageView.stopAnimation()
    }
}