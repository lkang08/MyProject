package com.lk.myproject.robust

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.lk.myproject.R
import com.lk.myproject.activity.BaseActivity
import com.lk.myproject.toast.ToastUtils
import com.meituan.robust.PatchExecutor
import kotlinx.android.synthetic.main.activity_robust.*

class RobustActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_robust)
        tvStart.setOnClickListener {
            var tip = "click tvStart"
            ToastUtils.showToast(this, tip, Toast.LENGTH_SHORT)
            Log.d("RobustCallBack", "click tvStart")
        }

        tvAdd.setOnClickListener {
            ToastUtils.showToast(this, "click tvAdd", Toast.LENGTH_SHORT)
            Log.d("RobustCallBack", "click tvAdd")
        }

        tvLoad.setOnClickListener {
            runRobust()
        }
    }

    private fun runRobust() {
        if (PermissionUtils.isGrantSDCardReadPermission(this)) {
            PatchExecutor(applicationContext, PatchManipulateImp(), RobustCallBackSample()).start()
        } else {
            PermissionUtils.requestSDCardReadPermission(this, 1000)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000) {
            Log.d(
                "RobustCallBack", "has permission:" + PermissionUtils
                    .isGrantSDCardReadPermission(this)
            )
        }
    }
}