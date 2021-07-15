package com.lk.myproject.widget.viewpage

import android.content.Context
import android.content.res.Resources
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.viewpager.widget.ViewPager
import kotlin.math.max

class FlipViewPager(
    context: Context,
    attrs: AttributeSet? = null
) : ViewPager(context, attrs), ViewPager.PageTransformer {
    private var maxScale = 0.0f
    private var mPageMargin: Int
    private var animationEnabled = true
    private var fadeEnabled = false
    private var fadeFactor = 0.5f
    private fun dp2px(resource: Resources, dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            resource.displayMetrics
        ).toInt()
    }

    fun setAnimationEnabled(enable: Boolean) {
        animationEnabled = enable
    }

    fun setFadeEnabled(fadeEnabled: Boolean) {
        this.fadeEnabled = fadeEnabled
    }

    fun setFadeFactor(fadeFactor: Float) {
        this.fadeFactor = fadeFactor
    }

    override fun setPageMargin(marginPixels: Int) {
        mPageMargin = marginPixels
        setPadding(mPageMargin, mPageMargin, mPageMargin, mPageMargin)
    }

    override fun transformPage(page: View, _position: Float) {
        var position = _position
        if (mPageMargin <= 0 || !animationEnabled) {
            return
        }
        page.setPadding(mPageMargin / 3, mPageMargin / 3, mPageMargin / 3, mPageMargin / 3)
        if (maxScale == 0.0f && position > 0.0f && position < 1.0f) {
            maxScale = position
        }
        position -= maxScale
        val absolutePosition = Math.abs(position)
        if (position <= -1.0f || position >= 1.0f) {
            if (fadeEnabled) {
                page.alpha = fadeFactor
            }
            // Page is not visible -- stop any running animations
        } else if (position == 0.0f) {

            // Page is selected -- reset any views if necessary
            page.scaleX = 1 + maxScale
            page.scaleY = 1 + maxScale
            page.alpha = 1f
        } else {
            page.scaleX = 1 + maxScale * (1 - absolutePosition)
            page.scaleY = 1 + maxScale * (1 - absolutePosition)
            if (fadeEnabled) {
                page.alpha = max(fadeFactor, 1 - absolutePosition)
            }
        }
    }

    init {
        // clipping should be off on the pager for its children so that they can scale out of bounds.
        clipChildren = false
        clipToPadding = false
        // to avoid fade effect at the end of the page
        overScrollMode = 2
        setPageTransformer(false, this)
        mPageMargin = dp2px(context.resources, 15)
        setPadding(mPageMargin, mPageMargin, mPageMargin, mPageMargin)
    }
}