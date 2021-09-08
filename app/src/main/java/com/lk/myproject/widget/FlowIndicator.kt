package com.lk.myproject.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.lk.myproject.R
import com.lk.myproject.ext.dp2px
import java.lang.Exception
import java.util.ArrayList

class FlowIndicator : LinearLayout {
    private var mContext: Context? = null
    private var maxNum = 0
    private var mSelectedColorResId = 0
    private var mUnSelectedColorResId = 0
    private var mUnSelectedViews: MutableList<View?>? = null
    private var mSelectedView: View? = null
    private var mSelectedIndicator = 0
    private var mUnselectedIndicator = 0
    private var mLayoutParams: LayoutParams? = null
    private var mUnSelectedPos = 0

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        val ta = context.obtainStyledAttributes(
            attrs,
            R.styleable.flow_indicator
        )
        mSelectedColorResId = ta.getResourceId(
            R.styleable.flow_indicator_selectedColor,
            R.color.color_purple
        )
        mUnSelectedColorResId = ta.getResourceId(
            R.styleable.flow_indicator_unselectedColor,
            R.color.white50
        )
        mSelectedIndicator = ta.getResourceId(R.styleable.flow_indicator_selectedIndicator, 0)
        mUnselectedIndicator = ta.getResourceId(R.styleable.flow_indicator_unselectedIndicator, 0)
        sharedConstructor(context, 0)
        ta.recycle()
    }

    constructor(context: Context, maxNum: Int) : this(context, null) {
        sharedConstructor(context, maxNum)
    }

    private fun sharedConstructor(context: Context, maxNum: Int) {
        mContext = context
        mUnSelectedViews = ArrayList()
        orientation = HORIZONTAL
        minimumHeight = dpToPx(7f)
        minimumWidth = dpToPx(7f)
        setMaxNum(maxNum)
        mLayoutParams = if (mUnselectedIndicator != 0 && mSelectedIndicator != 0) {
            LayoutParams(dpToPx(12f), dpToPx(12f))
        } else {
            LayoutParams(dpToPx(12f), dpToPx(12f))
        }
        mLayoutParams!!.gravity = Gravity.CENTER
    }

    private fun dpToPx(px: Float): Int {
        return px.toInt().dp2px
    }

    fun setMaxNum(maxNum: Int) {
        this.maxNum = maxNum
        mUnSelectedPos = 0
        mUnSelectedViews!!.clear()
        removeAllViews()
        for (i in 0 until maxNum) {
            val v = if (i == 0) newSelectedView() else newUnselectedView()
            addView(v)
        }
        visibility = if (maxNum <= 1) INVISIBLE else VISIBLE
    }

    fun setColorResId(selectId: Int, unselectedId: Int) {
        mSelectedColorResId = selectId
        mUnSelectedColorResId = unselectedId
        mSelectedView = null
        mUnSelectedViews!!.clear()
        setSelectedPos(0)
    }

    fun setSelectedPos(pos: Int) {
        if (pos > maxNum || pos < 0) {
            return
        }
        removeAllViews()
        for (i in 0 until maxNum) {
            try {
                val v = if (i == pos) newSelectedView() else newUnselectedView()
                addView(v)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun newChildView(colorResId: Int): View {
        val v: View = object : View(mContext) {
            var p = Paint()
            override fun onDraw(canvas: Canvas) {
                super.onDraw(canvas)
                p.color = resources.getColor(colorResId)
                p.isAntiAlias = true
                canvas.drawCircle(
                    (width / 2).toFloat(),
                    (height / 2).toFloat(),
                    dpToPx(4f).toFloat(),
                    p
                )
            }
        }
        v.layoutParams = mLayoutParams
        return v
    }

    private fun newImageChildView(res: Int): View {
        val v = ImageView(mContext)
        v.layoutParams = mLayoutParams
        v.setImageResource(res)
        return v
    }

    private fun newSelectedView(): View? {
        if (mSelectedView == null) {
            mSelectedView = if (mSelectedIndicator == 0) {
                newChildView(mSelectedColorResId)
            } else {
                newImageChildView(mSelectedIndicator)
            }
        }
        return mSelectedView
    }

    private fun newUnselectedView(): View? {
        var v: View? = null
        try {
            v = mUnSelectedViews!![mUnSelectedPos % (maxNum - 1)]
        } catch (e: Exception) {
            v = if (mUnselectedIndicator == 0) {
                newChildView(mUnSelectedColorResId)
            } else {
                newImageChildView(mUnselectedIndicator)
            }
            mUnSelectedViews!!.add(v)
        }
        mUnSelectedPos++
        return v
    }
}