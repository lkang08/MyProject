package com.lk.myproject.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator

import java.util.ArrayList

/**
 * 波浪
 */
class WaveView : View {

    private var mInitialRadius: Float = 0.toFloat()   // 初始波纹半径
    private var mMaxRadius: Float = 0.toFloat()   // 最大波纹半径
    private var mDuration: Long = 2000 // 一个波纹从创建到消失的持续时间
    private var mSpeed = 500   // 波纹的创建速度，每500ms创建一个
    private var mMaxRadiusRate = 0.85f
    private var mMaxRadiusSet: Boolean = false

    private var mIsRunning: Boolean = false
    private var mLastCreateTime: Long = 0
    private val mCircleList = ArrayList<Circle>()

    private val mCreateCircle = object : Runnable {
        override fun run() {
            if (mIsRunning) {
                newCircle()
                postDelayed(this, mSpeed.toLong())
            }
        }
    }

    private var mInterpolator: Interpolator? = LinearInterpolator()

    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val stopRunnable = Runnable { stop() }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun setStyle(style: Paint.Style) {
        mPaint.style = style
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        if (!mMaxRadiusSet) {
            mMaxRadius = Math.min(w, h) * mMaxRadiusRate / 2.0f
        }
    }

    fun setMaxRadiusRate(maxRadiusRate: Float) {
        mMaxRadiusRate = maxRadiusRate
    }

    fun setColor(color: Int) {
        mPaint.color = color
    }

    /**
     * 开始
     */
    fun start() {
        if (!mIsRunning) {
            mIsRunning = true
            mCreateCircle.run()
        }
    }

    /**
     * 开始, 定时关闭 time
     */
    fun start(time: Int) {
        if (!mIsRunning) {
            mIsRunning = true
            mCreateCircle.run()
        }

        removeCallbacks(stopRunnable)
        postDelayed(stopRunnable, time.toLong())
    }

    /**
     * 缓慢停止
     */
    fun stop() {
        mIsRunning = false
    }

    /**
     * 立即停止
     */
    fun stopImmediately() {
        mIsRunning = false
        mCircleList.clear()
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        val iterator = mCircleList.iterator()
        while (iterator.hasNext()) {
            val circle = iterator.next()
            val radius = circle.currentRadius
            if (System.currentTimeMillis() - circle.mCreateTime < mDuration) {
                mPaint.alpha = circle.alpha
                canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), radius, mPaint)
            } else {
                iterator.remove()
            }
        }
        if (mCircleList.size > 0) {
            postInvalidateDelayed(100)
        }
    }

    fun setInitialRadius(radius: Float) {
        mInitialRadius = radius
    }

    fun setDuration(duration: Long) {
        mDuration = duration
    }

    fun setMaxRadius(maxRadius: Float) {
        mMaxRadius = maxRadius
        mMaxRadiusSet = true
    }

    fun setSpeed(speed: Int) {
        mSpeed = speed
    }

    private fun newCircle() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - mLastCreateTime < mSpeed) {
            return
        }
        val circle = Circle()
        mCircleList.add(circle)
        invalidate()
        mLastCreateTime = currentTime
    }

    private inner class Circle internal constructor() {
        val mCreateTime: Long = System.currentTimeMillis()

        internal val alpha: Int
            get() {
                val percent = (currentRadius - mInitialRadius) / (mMaxRadius - mInitialRadius)
                return (255 - mInterpolator!!.getInterpolation(percent) * 255).toInt()
            }

        internal val currentRadius: Float
            get() {
                val percent = (System.currentTimeMillis() - mCreateTime) * 1.0f / mDuration
                return mInitialRadius + mInterpolator!!.getInterpolation(percent) * (mMaxRadius - mInitialRadius)
            }

    }

    fun setInterpolator(interpolator: Interpolator) {
        mInterpolator = interpolator
        if (mInterpolator == null) {
            mInterpolator = LinearInterpolator()
        }
    }
}
