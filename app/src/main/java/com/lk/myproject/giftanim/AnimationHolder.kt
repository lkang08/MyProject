package com.lk.myproject.giftanim

import android.view.View
import androidx.annotation.Keep

@Keep
class AnimationHolder {
    var x = 0
    var y = 0
    var view: View? = null
    var scaleX = 0f
    var scaleY = 0f
    var index = 0
}