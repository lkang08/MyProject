package com.lk.myproject.activity

import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.lk.myproject.R
import com.lk.myproject.utils.NavigationBarUtils
import com.lk.myproject.utils.StatusBarUtils
import kotlinx.android.synthetic.main.activity_main_new.*

class MainActivity2 : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtils.setNoStatusBar(this)
        setContentView(R.layout.activity_main_new)
        button.setOnClickListener {
            Log.d("lk###", "getNavigationHeight = ${NavigationBarUtils.getNavigationHeight(this@MainActivity2)} " +
                ",isNavigationBarExist = ${NavigationBarUtils.isNavigationBarExist(this@MainActivity2)}")
            Log.d("lk###", "getVirtualBarHeight = ${Utils.getVirtualBarHeight(this@MainActivity2)}")
        }
    }
}