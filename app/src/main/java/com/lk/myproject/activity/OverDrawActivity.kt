package com.lk.myproject.activity

import android.os.Bundle
import android.view.Gravity
import android.widget.TextView
import com.lk.myproject.R
import kotlinx.android.synthetic.main.activity_overdraw_main.*

class OverDrawActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overdraw_main)
        findViewById<TextView>(R.id.test).setOnClickListener {
            drawer.openDrawer(Gravity.START)
        }
    }
}