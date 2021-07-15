package com.lk.myproject.activity

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.lk.myproject.R
import com.lk.myproject.ext.dp2px
import com.lk.myproject.ext.screenWidth
import com.lk.myproject.toast.ToastUtils
import com.lk.myproject.utils.log
import kotlinx.android.synthetic.main.activity_first.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)
        tvText.setOnClickListener {
            //onClick()
            Glide.get(this).clearMemory()
            ToastUtils.showToast(this, "click${index++}", Toast.LENGTH_SHORT)
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
        val animator: ObjectAnimator = ObjectAnimator.ofFloat(layoutRecharge, "translationX",
            screenWidth.toFloat(), 0f)
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