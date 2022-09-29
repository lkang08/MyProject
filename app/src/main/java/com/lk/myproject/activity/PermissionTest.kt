package com.lk.myproject.activity

import android.Manifest
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lk.myproject.toast.ToastUtils
import com.lk.myproject.utils.log
import com.permissionx.guolindev.PermissionX
import com.permissionx.guolindev.callback.RequestCallback

object PermissionTest {
    fun test(activity: AppCompatActivity) {
        var has = PermissionX.isGranted(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
        ToastUtils.showToast(activity, "ACCESS_COARSE_LOCATION：$has", Toast.LENGTH_SHORT)
        PermissionX.init(activity)
            .permissions(Manifest.permission.READ_EXTERNAL_STORAGE)
            .request(RequestCallback { allGranted, grantedList, deniedList ->
                log("allGranted=$allGranted grantedList=$grantedList deniedList=$deniedList")
            })
    }
}