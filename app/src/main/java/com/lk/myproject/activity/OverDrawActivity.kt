package com.lk.myproject.activity

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.app.FragmentActivity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bumptech.glide.Glide
import com.lk.myproject.R
import com.lk.myproject.giftanim.GiftAnimationHelper
import com.lk.myproject.giftanim.SendGiftAnimationView
import com.lk.myproject.utils.NetWorkSpeedUtils
import kotlinx.android.synthetic.main.activity_overdraw_main.*

class OverDrawActivity : BaseActivity() {
    var TAG = "OverDrawActivity"

    var handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            if (msg?.what == 100) {
                tvSpeed.text = "当前网速：${msg?.obj?.toString()}"
            }
        }
    }

    var url = "https://vipweb.bs2cdn.yy.com/vipinter_d54201ecab49449fa238ede9c1f30b9f.jpg"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overdraw_main)
        findViewById<TextView>(R.id.test).setOnClickListener {
            //drawer.openDrawer(Gravity.START)
        }
        Glide.with(this).load(url).into(imageView)
        var helper = GiftAnimationHelper(this)
        send?.setOnClickListener {
            helper.showSingleGiftAnimation(v_1, send,
                190001, "http://s1.yy.com/guild/xh/p_icon/static/190001.png",
                gift_view_holder, this, content)
        }

        send2?.setOnClickListener {
            helper.showSingleGiftAnimation(send2, send,
                190001, "http://s1.yy.com/guild/xh/p_icon/static/190001.png",
                gift_view_holder, this, content)
        }
        receive?.setOnClickListener {
            helper.showSingleGiftAnimation(v_1, send,
                190001, "http://s1.yy.com/guild/xh/p_icon/static/190001.png",
                gift_view_holder, this, content)
        }
        NetWorkSpeedUtils(this, handler).startShowNetSpeed()
    }
}