package com.lk.myproject.robust

import android.os.Bundle
import android.widget.Toast
import com.lk.myproject.R
import com.lk.myproject.activity.BaseActivity
import com.lk.myproject.toast.ToastUtils
import kotlinx.android.synthetic.main.activity_robust.*

class RobustActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_robust)
        tvStart.setOnClickListener {
            var tip = "click tvStart"
            ToastUtils.showToast(this, tip, Toast.LENGTH_SHORT)
        }

        tvAdd.setOnClickListener {
            ToastUtils.showToast(this, "click tvAdd", Toast.LENGTH_SHORT)
        }

        tvLoad.setOnClickListener {

        }
    }
}