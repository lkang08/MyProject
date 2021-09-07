package com.lk.myproject.activity

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.lk.myproject.R
import com.lk.myproject.ext.dp2px
import com.lk.myproject.ext.screenWidth
import com.lk.myproject.toast.ToastUtils
import com.lk.myproject.utils.log
import kotlinx.android.synthetic.main.activity_first.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FirstActivity : BaseActivity() {
    var index = 1

    companion object {
        var defaulUrl = "https://img-res.mejiaoyou.com/20200827222235565_bs2_format.png"
        var IMAGE = "https://img-res.mejiaoyou.com"

        @JvmStatic
        val RANDOM_CALL_CIRCLE_1 = IMAGE + "/20200707103713370_bs2_format.webp"

        @JvmStatic
        val RANDOM_CALL_CIRCLE_2 = IMAGE + "/20200707103932069_bs2_format.webp"

        @JvmStatic
        val RANDOM_CALL_CIRCLE_3 = IMAGE + "/20200707103958382_bs2_format.webp"

        @JvmStatic
        val FEMALE_HEAD_BG = IMAGE + "/20200707144501541_bs2_format.webp"

        @JvmStatic
        val HEART_BEAT_BG = IMAGE + "/20200630110338573_bs2_format.webp"

        @JvmStatic
        val WHOLE_GIFT_BG = IMAGE + "/20200630103913223_bs2_format.gif"

        @JvmStatic
        val TEENAGER_BG = IMAGE + "/20200630105656702_bs2_format.webp"

        @JvmStatic
        val FULL_HOUSE_BG = IMAGE + "/20200703145127299_bs2_format.png"

        @JvmStatic
        val ROSE_URL = IMAGE + "/20200703152536877_bs2_format.webp"

        @JvmStatic
        val WING_PROPS = IMAGE + "/20200706150543700_bs2_format.webp"

        @JvmStatic
        var ATTENTION_BUTTON_BG = IMAGE + "/20200706172305273_bs2_format.svga"

        @JvmStatic
        var APPLY_MIC_BG = IMAGE + "/20200706172605443_bs2_format.svga"

        var NEW_USER_RED_PACKAGE_BG = IMAGE + "/20210705153902342_bs2_format.png"
    }

    var temp = 1
    var executor: ExecutorService = Executors.newSingleThreadExecutor { r ->
        Thread(
            r,
            "test_thread_${System.currentTimeMillis()}"
        )
    }

    fun test() {
        /*GlobalScope.launch {
            Thread({
                "${Thread.currentThread().name} run $temp ".log()
            }, "test_${temp++}").start()
        }*/
        /*for (i in 1..10) {
            executor.execute {
                "${Thread.currentThread().name} run $i ".log()
            }
        }*/
        GlobalScope.launch {
            var pro = 5
            withContext(Dispatchers.Main) {
                progress.setMaxProgress(60)
                progress.progress = pro
                while (pro < 100) {
                    delay(1000)
                    pro += 5
                    progress.progress = pro
                }
            }
        }
        GlobalScope.launch {
            var host =
                Uri.parse("https://img-res.mejiaoyou.com/20200211002139390_bs2_format.png?ips_thumbnail/4/1/w/200/h/200")
            host.host

            var datas = mutableListOf<String>()
            datas.add(("hello 1"))
            datas.add(("hello 2"))
            datas.add(("hello 3"))

            var realSize = datas.size

            datas.add(datas[0])
            datas.add(0, datas[realSize - 1])
            datas.size
        }
        ivAnim.drawable?.takeIf {
            it is AnimationDrawable
        }?.let {
            (it as AnimationDrawable).let { anim ->
                anim.start()
                /*if (anim.isRunning) {
                    anim.stop()
                } else {
                    anim.start()
                }*/
            }
        }
        if (count++ % 2 == 0) {
            playAnimRight()
        } else {
            playAnimLeft()
        }
    }

    var count = 0

    val animDuration = 100L
    var scaleSize = 1.33f
    var animSet: AnimatorSet = AnimatorSet()

    fun playAnim() {
        var anim1: ObjectAnimator =
            ObjectAnimator.ofFloat(proBg2, "scaleX", scaleSize).setDuration(animDuration)
        var anim2: ObjectAnimator =
            ObjectAnimator.ofFloat(proBg2, "scaleY", scaleSize).setDuration(animDuration)

        var anim3: ObjectAnimator =
            ObjectAnimator.ofFloat(proBg2, "scaleX", 1f).setDuration(animDuration)
        var anim4: ObjectAnimator =
            ObjectAnimator.ofFloat(proBg2, "scaleY", 1f).setDuration(animDuration)
        anim3.startDelay = animDuration
        anim4.startDelay = animDuration
        animSet.cancel()
        animSet.playTogether(anim1, anim2, anim3, anim4)
        animSet.setDuration(animDuration * 2).start()
    }

    fun clearLayoutRule() {
        (proBg2.layoutParams as RelativeLayout.LayoutParams).let {
            it.removeRule(RelativeLayout.ALIGN_PARENT_LEFT)
            it.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT)
        }
    }

    fun playAnimRight() {
        flRight.visibility = View.VISIBLE
        (proBg2.layoutParams as RelativeLayout.LayoutParams).let {
            it.marginStart = 0
            it.marginEnd = 10.dp2px
            it.removeRule(RelativeLayout.ALIGN_PARENT_LEFT)
            it.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE)
        }
        flRight.visibility = View.VISIBLE
        var anim1: ObjectAnimator =
            ObjectAnimator.ofFloat(tvRight, "translationX", 1080f - 75.dp2px, 0f)
                .setDuration(2000)
        anim1.start()
        anim1.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                flRight.visibility = View.GONE
                clearLayoutRule()
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationRepeat(animation: Animator?) {
            }
        })
    }

    fun playAnimLeft() {
        flLeft.visibility = View.VISIBLE
        (proBg2.layoutParams as RelativeLayout.LayoutParams).let {
            it.marginStart = 10.dp2px
            it.marginEnd = 0
            it.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT)
            it.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE)
        }
        var anim1: ObjectAnimator =
            ObjectAnimator.ofFloat(tvLeft, "translationX", -1080f + 75.dp2px.toFloat(), 0f)
                .setDuration(2000)
        anim1.start()
        anim1.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                flLeft.visibility = View.GONE
                clearLayoutRule()
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationRepeat(animation: Animator?) {
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)
        tvText.setOnClickListener {
            //onClick()
            //Glide.get(this).clearMemory()
            ToastUtils.showToast(this, "click${index++}", Toast.LENGTH_SHORT)
            test()
        }
        tvToPager.setOnClickListener {
            startActivity(Intent(this, PagerSnapHelperActivity::class.java))
        }
        Glide.with(this)
            .load(FULL_HOUSE_BG)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            //.override(screenWidth, 80.dp2px)
            .into(bgImage)
        //Glide.with(this).load(R.drawable.bg_dynamic_no_data).into(ivNoData)

        /*Glide.with(this).load(RANDOM_CALL_CIRCLE_1).preload()
        Glide.with(this).load(NEW_USER_RED_PACKAGE_BG).preload()
        Glide.with(this).load(RANDOM_CALL_CIRCLE_2).preload()
        Glide.with(this).load(RANDOM_CALL_CIRCLE_3).preload()
        Glide.with(this).load(APPLY_MIC_BG).preload()
        Glide.with(this).load(ATTENTION_BUTTON_BG).preload()
        Glide.with(this).load(ROSE_URL).preload()

        Glide.with(this).load(WING_PROPS).preload()
        Glide.with(this).load(FULL_HOUSE_BG).preload()
        Glide.with(this).load(TEENAGER_BG).preload()
        Glide.with(this).load(WHOLE_GIFT_BG).preload()*/
        //Glide.get(this).setMemoryCategory(MemoryCategory.LOW)
        log("size = ${Glide.get(this).bitmapPool.maxSize / (1024 * 1024)}")
    }

    fun showAnim() {
        layoutRecharge.visibility = View.VISIBLE
        var x = layoutRecharge.translationX
        log("x=$x")
        val animator: ObjectAnimator = ObjectAnimator.ofFloat(
            layoutRecharge, "translationX",
            screenWidth.toFloat(), 0f
        )
        animator.setDuration(1000)
        animator.start()
    }

    fun onClick() {
        load()
    }

    fun load() {
        tvThisWeek.text = "This week: ${Util.getThisWeekInterval()}"
        tvLastWeek.text = "Last week: ${Util.getLastWeekInterval()}"
        tvThisMonth.text = "This month: ${Util.getThisMonthInterval()}"
        tvLastMonth.text = "Last month: ${Util.getLastMonthInterval()}"
        GlobalScope.launch {
            Util.getLastWeekInterval()
        }
    }
}