package com.lk.myproject.activity

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.lk.myproject.ext.dp2px

object WholeMicAnimation {
    val tag = "WholeMicAnimation"
    const val MAX_ANIMATION_TIME: Int = 5000 //全麦动画超时
    var time = 8L
    var FULL_ANIMATION_TIME = 640 * time + 500 //320 * time + 160 + 160 + 1000 + 160 //1140

    var test = true
    var begin = 0L
    var begin2 = 0L
    //全麦麦位动画
    @JvmStatic
    fun showReceviedGiftAnimation(
        viewGroup: ViewGroup,
        url: String = "http://turnoverpic.bs2dl-ssl.huanjuyun.com/year_card_2019_1572490143188.png",
        repeart: Int = 0
    ) {
        var showTime = if (repeart == 0) time.toInt() else repeart
        val imageView = ImageView(viewGroup.context)
        if (viewGroup is RelativeLayout) {
            val params = RelativeLayout.LayoutParams(viewGroup.width / 2,
                viewGroup.height / 2)
            params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)
            imageView.layoutParams = params
        } else {
            val padding = 10.dp2px
            imageView.layoutParams = ViewGroup.LayoutParams(viewGroup.width - 2 * padding,
                viewGroup.height - 2 * padding)
            imageView.x = padding.toFloat()
            imageView.y = padding.toFloat()
        }

        Glide.with(viewGroup.context).load(url).into(imageView)

        viewGroup.addView(imageView)

        val animatorSet = AnimatorSet()

        val animator1 = ObjectAnimator.ofFloat(imageView, "rotation", 0f, -20f)
        animator1.duration = 200

        val animator2 = ObjectAnimator.ofFloat(imageView, "rotation", -20f, 20f)
        animator2.duration = 400
        animator2.repeatCount = showTime
        animator2.repeatMode = ValueAnimator.REVERSE
        animator2.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
                Log.d("BLLog", "onAnimationRepeat time = " + (System.currentTimeMillis() - begin2))
            }

            override fun onAnimationEnd(animation: Animator?) {
                Log.d("BLLog", "onAnimationEnd time = " + (System.currentTimeMillis() - begin2))
            }

            override fun onAnimationCancel(animation: Animator?) {
                Log.d("BLLog", "onAnimationCancel")
            }

            override fun onAnimationStart(animation: Animator?) {
                Log.d("BLLog", "onAnimationStart")
                begin2 = System.currentTimeMillis()
            }
        })

        val animator3 = ObjectAnimator.ofFloat(imageView, "rotation", 0f)
        animator3.duration = 200

        val animator4 = ObjectAnimator.ofFloat(imageView, "alpha", 1.0f, 0f)
        animator4.duration = 160
        animator4.startDelay = 500

        animatorSet.playSequentially(animator1, animator2, animator3, animator4)
        animatorSet.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                begin = System.currentTimeMillis()
            }

            override fun onAnimationEnd(animation: Animator) {
                viewGroup.removeView(imageView)
                Log.d("BLLog", "time = " + (System.currentTimeMillis() - begin))
            }

            override fun onAnimationCancel(animation: Animator) {
                viewGroup.removeView(imageView)
            }

            override fun onAnimationRepeat(animation: Animator) {
            }
        })
        animatorSet.start()
    }
}