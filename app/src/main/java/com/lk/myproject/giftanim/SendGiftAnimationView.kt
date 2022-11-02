package com.lk.myproject.giftanim

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.lk.myproject.R
import com.lk.myproject.giftanim.SendGiftAnimationView
import com.lk.myproject.utils.FP
import java.util.ArrayList

class SendGiftAnimationView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle), AnimatorUpdateListener {

    companion object {
        private const val TAG = "SendGiftAnimationView"
        private const val MAX_CACHE_SIZE = 3
    }

    var size = resources.getDimensionPixelSize(R.dimen.room_animator_gift_size)
    private var animating = false
    private var isAnimationStart = false
    var onAnimationListener: OnAnimationListener? = null
    private val mItems: ArrayList<AnimationHolder>? = ArrayList()
    private val mScrapViews: ArrayList<View?> = ArrayList()
    private val mAnimators = ArrayList<Animator>()
    override fun onDetachedFromWindow() {
        Log.i(TAG, "onDetachedFromWindow")
        super.onDetachedFromWindow()
        mScrapViews?.clear()
        mItems?.clear()
    }

    private fun getAnimationHolder(id: Long, url: String): AnimationHolder {
        val view: View?
        if (mScrapViews.isNotEmpty() && indexOfChild(mScrapViews[0]) == -1) {
            Log.i(TAG, "startGiftAnimation1  getView from cache ")
            view = mScrapViews[0]
            mScrapViews.removeAt(0)
        } else {
            Log.i(TAG, "startGiftAnimation1  getView from new View ")
            //view = View.inflate(context, R.layout.layout_gift_anim_send, null)
            view = ImageView(context)
        }
        val giftView = view as ImageView
        /*val tv = view.findViewById<TextView>(R.id.tv_weekstar_tile)
        if (id != 0L) {
            val name = ""
            if (!FP.empty(name)) {
                tv.text = name
                Log.i(TAG, "startGiftAnimation name visable")
                tv.visibility = View.VISIBLE
            } else {
                Log.i(TAG, "startGiftAnimation name gone")
                tv.visibility = View.GONE
            }
        }*/
        Glide.with(this).load(url).into(giftView)
        addView(view, LayoutParams(size, size))
        val holder = AnimationHolder()
        holder.view = view
        holder.index = mItems!!.size
        mItems.add(holder)
        return holder
    }

    fun startGiftAnimation(
        id: Long, url: String, startX: Int, startY: Int, middleX: Int,
        middleY: Int, endX: Int,
        endY: Int, minScale: Float, maxScale: Float,
        inDuration: Long, outDuration: Long, middleDuration: Long
    ) {
        val holder = getAnimationHolder(id, url)

        val initScaleX1 = PropertyValuesHolder.ofFloat("scaleX", 0.0f, minScale)
        val initScaleY1 = PropertyValuesHolder.ofFloat("scaleY", 0.0f, minScale)
        val initPvhX = PropertyValuesHolder.ofInt("x", startX, startX)
        val initPvhY = PropertyValuesHolder.ofInt("y", startY, startY)
        val initScaleAnimator1 = ObjectAnimator.ofPropertyValuesHolder(holder, initScaleX1, initScaleY1,
            initPvhX, initPvhY)
        val duration1 = (inDuration * 0.2f).toLong()
        initScaleAnimator1.duration = duration1
        initScaleAnimator1.addUpdateListener(this)

        val initScaleX2 = PropertyValuesHolder.ofFloat("scaleX", minScale,
            minScale * 0.6f)
        val initScaleY2 = PropertyValuesHolder.ofFloat("scaleY", minScale,
            minScale * 0.6f)
        val initScaleAnimator2 = ObjectAnimator.ofPropertyValuesHolder(holder, initScaleX2, initScaleY2)
        initScaleAnimator2.duration = (duration1 * 0.8f).toLong()
        initScaleAnimator2.addUpdateListener(this)

        val startPvhX = PropertyValuesHolder.ofInt("x", startX, middleX)
        val startPvhY = PropertyValuesHolder.ofInt("y", startY, middleY)
        val startPvhScaleX = PropertyValuesHolder.ofFloat("scaleX",
            minScale * 0.6f, maxScale)
        val startPvhScaleY = PropertyValuesHolder.ofFloat("scaleY",
            minScale * 0.6f, maxScale)
        val startObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(holder, startPvhX, startPvhY,
            startPvhScaleX, startPvhScaleY)
        startObjectAnimator.duration = inDuration
        startObjectAnimator.startDelay = (duration1 * 0.8f).toLong()
        startObjectAnimator.interpolator = AccelerateDecelerateInterpolator()
        startObjectAnimator.addUpdateListener(this)

        val endPvhX = PropertyValuesHolder.ofInt("x", middleX, endX)
        val endPvhY = PropertyValuesHolder.ofInt("y", middleY, endY)
        val endPvhScaleX = PropertyValuesHolder.ofFloat("scaleX",
            maxScale, minScale * 0.8f)
        val endPvhScaleY = PropertyValuesHolder.ofFloat("scaleY",
            maxScale, minScale * 0.8f)
        val endObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(holder, endPvhX, endPvhY,
            endPvhScaleX, endPvhScaleY)
        endObjectAnimator.duration = outDuration
        endObjectAnimator.interpolator = AccelerateDecelerateInterpolator()
        endObjectAnimator.addUpdateListener(this)

        val finishScaleX1 = PropertyValuesHolder.ofFloat("scaleX",
            minScale * 0.8f, minScale)
        val finishScaleY1 = PropertyValuesHolder.ofFloat("scaleY",
            minScale * 0.8f, minScale)
        val finishScaleAnimator1 = ObjectAnimator.ofPropertyValuesHolder(holder, finishScaleX1,
            finishScaleY1)
        val duration2 = (outDuration * 0.2f).toLong()
        finishScaleAnimator1.duration = (duration2 * 0.8f).toLong()
        finishScaleAnimator1.addUpdateListener(this)
        val finishScaleX2 = PropertyValuesHolder.ofFloat("scaleX",
            minScale, 0.0f)
        val finishScaleY2 = PropertyValuesHolder.ofFloat("scaleY",
            minScale, 0.0f)
        val finishScaleAnimator2 = ObjectAnimator.ofPropertyValuesHolder(holder,
            finishScaleX2, finishScaleY2)
        finishScaleAnimator2.duration = duration2
        finishScaleAnimator2.startDelay = (duration1 * 0.8f).toLong()
        finishScaleAnimator2.addUpdateListener(this)
        val set = AnimatorSet()
        mAnimators.add(set)
        set.addListener(MyAnimatorListenerAdapter(holder))
        set.play(initScaleAnimator1).before(initScaleAnimator2)
        set.play(initScaleAnimator2).before(startObjectAnimator)
        set.play(startObjectAnimator).before(endObjectAnimator)
        set.play(endObjectAnimator).before(finishScaleAnimator1)
        set.play(finishScaleAnimator1).before(finishScaleAnimator2)
        set.start()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
    }

    override fun dispatchDraw(canvas: Canvas) {
        for (holder in mItems!!) {
            /*Log.debug(TAG, "draw x = " + holder.x + " y = " + holder.y + " sx=" + holder.scaleX + "
            sy=" + holder.scaleY + " items size=" + mItems.size()
                    + " index=" + holder.index);*/
            canvas.save()
            canvas.translate(holder.x.toFloat(), holder.y.toFloat())
            //  canvas.scale(holder.scaleX, holder.scaleY);
            canvas.scale(holder.scaleX, holder.scaleY, holder.view!!.width / 2f,
                holder.view!!.height / 2f)
            holder.view!!.draw(canvas)
            canvas.restore()
        }
    }

    override fun onAnimationUpdate(arg0: ValueAnimator) {
        this.invalidate()
    }

    fun stopAnimation() {
        val iterator: Iterator<Animator> = mAnimators.iterator()
        while (iterator.hasNext()) {
            iterator.next().cancel()
        }
    }

    /*@Override
    public void attachLifecycle(FragmentActivity lifecycleOwner) {
        if (lifecycleOwner == null) {
            recycle();
            return;
        }

        lifecycleOwner.getLifecycle().addObserver(new GenericLifecycleObserver() {
            @Override
            public void onStateChanged(LifecycleOwner source, Lifecycle.Event event) {
                Log.i(TAG, event.name());
                if (event == Lifecycle.Event.ON_DESTROY) {
                    Log.i(TAG, "Lifecycle.Event.ON_DESTROY");
                    recycle();
                } else if (event == Lifecycle.Event.ON_STOP) {
                    Log.i(TAG, "Lifecycle.Event.ON_STOP");
                    if (mOnAnimationListener != null) {
                        mOnAnimationListener.onAnimationStop();
                    }
                    recycle();
                }
            }
        });
    }

    @Override*/
    fun recycle() {
        mItems!!.clear()
        stopAnimation()
        mAnimators.clear()
        animating = false
        isAnimationStart = false
        onAnimationListener = null
    }

    interface OnAnimationListener {
        fun onAnimationStart()
        fun onAnimationEnd()
        fun onAnimationCancel()
        fun onAnimationStop()
    }

    internal class AnimationHolder {
        var x = 0
        var y = 0
        var view: View? = null
        var scaleX = 0f
        var scaleY = 0f
        var index = 0
    }

    internal inner class MyAnimatorListenerAdapter(private var holder: AnimationHolder) : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            super.onAnimationEnd(animation)
            if (holder.view != null) {
                holder.view!!.visibility = View.GONE
            }
            mAnimators.remove(animation)
            mItems!!.remove(holder)
            if (mScrapViews.size <= MAX_CACHE_SIZE) {
                mScrapViews.add(holder.view)
                holder.view = null
            }
            Log.i(TAG, "onAnimationEnd ")
            if (mItems.isEmpty()) {
                animating = false
                isAnimationStart = false
                if (onAnimationListener != null) {
                    onAnimationListener!!.onAnimationEnd()
                }
            }
        }

        override fun onAnimationCancel(animation: Animator) {
            Log.i(TAG, "onAnimationCancel")
            if (holder.view != null) {
                holder.view!!.visibility = View.GONE
            }
            super.onAnimationCancel(animation)
            mAnimators.remove(animation)
            mItems!!.remove(holder)
            if (mScrapViews.size <= MAX_CACHE_SIZE) {
                mScrapViews.add(holder.view)
                holder.view = null
            }
            if (mItems.isEmpty()) {
                animating = false
                isAnimationStart = false
                if (onAnimationListener != null) {
                    onAnimationListener!!.onAnimationCancel()
                }
            }
        }

        override fun onAnimationStart(animation: Animator) {
            super.onAnimationStart(animation)
            animating = true
            if (holder.view != null) {
                holder.view!!.visibility = View.VISIBLE
            }
            if (mItems!!.size > 0 && !isAnimationStart) {
                Log.i(TAG, "onAnimationStart ")
                isAnimationStart = true
                if (onAnimationListener != null) {
                    onAnimationListener!!.onAnimationStart()
                }
            }
        }
    }
}