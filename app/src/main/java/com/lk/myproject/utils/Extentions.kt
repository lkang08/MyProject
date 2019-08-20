package com.lk.myproject.utils

import android.util.Log

inline fun Any.log(msg: String) {
    Log.d("lk###", msg)
}