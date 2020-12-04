package com.lk.myproject.activity

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.facebook.rebound.SimpleSpringListener
import com.facebook.rebound.Spring
import com.facebook.rebound.SpringConfig
import com.facebook.rebound.SpringSystem
import com.lk.myproject.R
import com.lk.myproject.animation.effects.Effectstype
import com.lk.myproject.animation.effects.NiftyDialogBuilder
import com.nineoldandroids.view.ViewHelper
import com.uuch.adlibrary.AdConstant
import com.uuch.adlibrary.AdManager
import com.uuch.adlibrary.anim.AnimSpring
import com.uuch.adlibrary.bean.AdInfo
import com.uuch.adlibrary.transformer.DepthPageTransformer
import kotlinx.android.synthetic.main.activity_dialog_animation.*
import java.lang.Exception

class DialogAnimationActivity : Activity() {

    private var effect: Effectstype? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog_animation)
    }

    var advList = mutableListOf<AdInfo>()
    private fun initData() {
        var adInfo = AdInfo()
        adInfo.activityImg = "https://raw.githubusercontent.com/yipianfengye/android-adDialog/master/images/testImage1.png"
        advList.add(adInfo)
        adInfo = AdInfo()
        adInfo.activityImg = "https://raw.githubusercontent.com/yipianfengye/android-adDialog/master/images/testImage2.png"
        advList.add(adInfo)
    }

    fun dialogShow(v: View) {
        if (v.id == R.id.newAnim) {

            try {
                var a = 0
                a?.let {
                    var b = 10 / it
                }
            } finally {
                Log.d("AndroidR", "#######finally")
            }
            if (1 > 0) {
                var anim = AnimSpring.getInstance()
                anim.startCircleAnim(90, rlLayout)

                return
                val spring: Spring = SpringSystem.create()
                    .createSpring()
                    .setSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(8.toDouble(),
                        2.toDouble()))
                    .addListener(object : SimpleSpringListener() {
                        override fun onSpringUpdate(spring: Spring) {
                            ViewHelper.setTranslationY(rlLayout, spring.getCurrentValue().toFloat())
                        }
                    })
                spring.setCurrentValue(100.toDouble())
                spring.setEndValue(0.0)
                return
            }

            var adManager = AdManager(this, advList)

            adManager.setOnImageClickListener { view, advInfo ->
                Toast.makeText(this,
                    "您点击了ViewPagerItem...", Toast.LENGTH_SHORT).show()
            }
                .setPadding(100)
                .setWidthPerHeight(0.5f)
                .showAdDialog(AdConstant.ANIM_UP_TO_DOWN)
            return
        }
        val dialogBuilder: NiftyDialogBuilder = NiftyDialogBuilder.getInstance(this)
        when (v.id) {
            R.id.fadein -> effect = Effectstype.Fadein
            R.id.slideright -> effect = Effectstype.Slideright
            R.id.slideleft -> effect = Effectstype.Slideleft
            R.id.slidetop -> effect = Effectstype.Slidetop
            R.id.slideBottom -> effect = Effectstype.SlideBottom
            R.id.newspager -> effect = Effectstype.Newspager
            R.id.fall -> effect = Effectstype.Fall
            R.id.sidefall -> effect = Effectstype.Sidefill
            R.id.fliph -> effect = Effectstype.Fliph
            R.id.flipv -> effect = Effectstype.Flipv
            R.id.rotatebottom -> effect = Effectstype.RotateBottom
            R.id.rotateleft -> effect = Effectstype.RotateLeft
            R.id.slit -> effect = Effectstype.Slit
            R.id.shake -> effect = Effectstype.Shake
        }
        dialogBuilder
            .withTitle("Modal Dialog") //.withTitle(null)  no title
            .withTitleColor("#FFFFFF") //def
            .withDividerColor("#11000000") //def
            .withMessage("This is a modal Dialog.") //.withMessage(null)  no Msg
            .withMessageColor("#FFFFFFFF") //def  | withMessageColor(int resid)
            .withDialogColor("#FFE74C3C") //def  | withDialogColor(int resid)                               //def
            .withIcon(resources.getDrawable(R.drawable.icon))
            .isCancelableOnTouchOutside(true) //def    | isCancelable(true)
            .withDuration(700) //def
            .withEffect(effect) //def Effectstype.Slidetop
            .withButton1Text("OK") //def gone
            .withButton2Text("Cancel") //def gone
            .setCustomView(R.layout.custom_view, v.context) //.setCustomView(View or ResId,context)
            .setButton1Click(View.OnClickListener { v -> Toast.makeText(v.context, "i'm btn1", Toast.LENGTH_SHORT).show() })
            .setButton2Click(View.OnClickListener { v -> Toast.makeText(v.context, "i'm btn2", Toast.LENGTH_SHORT).show() })
            .show()
    }
}