package com.lk.myproject.activity

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.app.FragmentActivity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bumptech.glide.Glide
import com.lk.myproject.R
import com.lk.myproject.giftanim.SendGiftAnimationView
import com.lk.myproject.utils.NetWorkSpeedUtils
import kotlinx.android.synthetic.main.activity_overdraw_main.*

class OverDrawActivity : BaseActivity() {
    var TAG = "OverDrawActivity"

    var handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            if (msg?.what == 100) {
                tvSpeed.text = "当前网速：${msg?.obj?.toString()}"
            }
        }
    }

    var url = "https://vipweb.bs2cdn.yy.com/vipinter_d54201ecab49449fa238ede9c1f30b9f.jpg"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overdraw_main)
        findViewById<TextView>(R.id.test).setOnClickListener {
            //drawer.openDrawer(Gravity.START)
        }
        Glide.with(this).load(url).into(imageView)
        send?.setOnClickListener {
            showSingleGiftAnimation(v_1, send,
                190001, "http://s1.yy.com/guild/xh/p_icon/static/190001.png",
                gift_view_holder, this, content)
        }

        send2?.setOnClickListener {
            showSingleGiftAnimation(send2, send,
                190001, "http://s1.yy.com/guild/xh/p_icon/static/190001.png",
                gift_view_holder, this, content)
        }
        receive?.setOnClickListener {
            showSingleGiftAnimation(v_1, send,
                190001, "http://s1.yy.com/guild/xh/p_icon/static/190001.png",
                gift_view_holder, this, content)
        }
        NetWorkSpeedUtils(this, handler).startShowNetSpeed()
    }

    fun showSingleGiftAnimation(
        startView: View?,
        endView: View?,
        id: Long,
        url: String,
        giftViewHolder: ViewGroup?,
        mActivity: FragmentActivity,
        roomView: View,
        startCallback: () -> Unit = {},
        endCallback: () -> Unit = {}
    ) {
        if (startView == null || endView == null || giftViewHolder == null) {
            return
        }
        Log.i(TAG, "->addAnimatorGiftView url= $url")
        val giftSingleEx = null
        val giftSingleDequeEx = null
        Log.i(TAG, "showSingleGiftAnimation 11 giftSingleEx $giftSingleEx")
        if (giftSingleDequeEx == null || giftSingleEx == null) {
            val mSendGiftAnimationView = SendGiftAnimationView(mActivity)
            giftViewHolder.post {
                if (giftViewHolder.indexOfChild(mSendGiftAnimationView) == -1) {
                    giftViewHolder.addView(mSendGiftAnimationView,
                        ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT))
                }
                Log.i(TAG, "->added  AnimatorGiftView ")
                mSendGiftAnimationView.onAnimationListener =
                    object : SendGiftAnimationView.OnAnimationListener {

                        override fun onAnimationStart() {
                            mSendGiftAnimationView?.visibility = View.VISIBLE
                            startCallback.invoke()
                        }

                        override fun onAnimationStop() {
                            mSendGiftAnimationView?.let { }
                            mSendGiftAnimationView?.visibility = View.GONE
                            giftViewHolder.removeView(mSendGiftAnimationView)
                        }

                        override fun onAnimationEnd() {
                            mSendGiftAnimationView?.let { }
                            mSendGiftAnimationView?.visibility = View.GONE
                            giftViewHolder.removeView(mSendGiftAnimationView)
                            endCallback.invoke()
                        }

                        override fun onAnimationCancel() {
                            mSendGiftAnimationView?.let { }
                            mSendGiftAnimationView?.visibility = View.GONE
                            giftViewHolder.removeView(mSendGiftAnimationView)
                        }
                    }
                mSendGiftAnimationView?.let {
                    startSingleAnimation(mSendGiftAnimationView, startView, endView, id, url,
                        mActivity, roomView)
                }
            }
        } else {
            val mSendGiftAnimationView = SendGiftAnimationView(this)
            mSendGiftAnimationView?.let {
                giftViewHolder.post {
                    if (giftViewHolder.indexOfChild(mSendGiftAnimationView) == -1) {
                        giftViewHolder.addView(mSendGiftAnimationView,
                            ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT))
                    }
                    Log.i(TAG, "-> mSendGiftAnimationView added  AnimatorGiftView ")
                    mSendGiftAnimationView.onAnimationListener =
                        object : SendGiftAnimationView.OnAnimationListener {

                            override fun onAnimationStart() {
                                Log.i(TAG,
                                    "->mSendGiftAnimationView added  onAnimationStart ")
                                mSendGiftAnimationView?.visibility = View.VISIBLE
                                startCallback.invoke()
                            }

                            override fun onAnimationStop() {
                                Log.i(TAG,
                                    "->mSendGiftAnimationView added  onAnimationStop ")
                                mSendGiftAnimationView?.let { }
                                mSendGiftAnimationView?.visibility = View.GONE
                            }

                            override fun onAnimationEnd() {
                                Log.i(TAG,
                                    "->mSendGiftAnimationView added  onAnimationEnd ")
                                mSendGiftAnimationView?.let { }
                                mSendGiftAnimationView?.visibility = View.GONE
                                endCallback.invoke()
                            }

                            override fun onAnimationCancel() {
                                Log.i(TAG, "->added  onAnimationCancel ")
                                mSendGiftAnimationView?.let { }
                                mSendGiftAnimationView?.visibility = View.GONE
                            }
                        }
                    mSendGiftAnimationView?.let {
                        startSingleAnimation(mSendGiftAnimationView, startView, endView, id,
                            url, mActivity, roomView)
                    }
                }
            }
        }
    }

    val GIFT_ANIMATION_IN_DURATION = 800
    val GIFT_ANIMATION_OUT_DURATION = 800
    val GIFT_ANIMAION_MIDDLE_DURATION = 100

    val GIFT_ANIMATION_MIN_SCALE = 0.3f
    val GIFT_ANIMATION_MAX_SCALE = 1.7f

    private fun startSingleAnimation(
        animatView: SendGiftAnimationView,
        startView: View,
        endView: View,
        id: Long,
        url: String,
        mActivity: Activity,
        roomView: View
    ) {
        if (mActivity.isFinishing) {
            return
        }
        if (animatView == null) {
            return
        }

        Log.i(TAG, "->startSingleAnimation")

        val size = mActivity.resources.getDimensionPixelSize(R.dimen.room_animator_gift_size)

        val pos = getLocationInWindow(roomView)
        val startCenterPos = getCenterOfViewLocationInWindow(startView)
        val centerCenterPos = getCenterOfViewLocationInWindow(roomView)
        val endCenterPos = getCenterOfViewLocationInWindow(endView)
        val startX = startCenterPos[0] - pos[0] - size / 2
        val startY = startCenterPos[1] - pos[1] - size / 2
        val middleX = centerCenterPos[0] - pos[0] - size / 2
        val middleY = centerCenterPos[1] - pos[1] - size / 2
        val endX = endCenterPos[0] - pos[0] - size / 2
        val endY = endCenterPos[1] - pos[1] - size / 2

        Log.i(TAG,
            "startGiftAnimation id=$id url=$url pos=${pos.toTypedArray()
                .contentToString()} " +
                "startCenterPos=${startCenterPos.toTypedArray()
                    .contentToString()} " +
                "centerCenterPos=${centerCenterPos.toTypedArray().contentToString()} " +
                "endCenterPos=${endCenterPos.toTypedArray().contentToString()}")
        animatView.startGiftAnimation(id, url, startX, startY, middleX, middleY, endX, endY,
            GIFT_ANIMATION_MIN_SCALE, GIFT_ANIMATION_MAX_SCALE,
            GIFT_ANIMATION_IN_DURATION.toLong(), GIFT_ANIMATION_OUT_DURATION.toLong(),
            GIFT_ANIMAION_MIDDLE_DURATION.toLong())
    }

    private fun getLocationInWindow(view: View): IntArray {
        val pos = IntArray(2)
        view.getLocationInWindow(pos)
        return pos
    }

    private fun getCenterOfViewLocationInWindow(view: View): IntArray {
        val pos = IntArray(2)
        view.getLocationInWindow(pos)
        pos[0] = (pos[0] + view.width / 2f).toInt()
        pos[1] = (pos[1] + view.height / 2f).toInt()
        return pos
    }
}