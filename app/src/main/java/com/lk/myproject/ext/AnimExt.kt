package com.lk.myproject.ext

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator

/**
 *  Animator dsl 封装
 */


abstract class Anim {

    abstract var animator: Animator

    var builder: AnimatorSet.Builder? = null

    var duration
        get() = 300L
        set(value) {
            animator.duration = value
        }

    var interpolator
        get() = LinearInterpolator() as Interpolator
        set(value) {
            animator.setInterpolator(value)
        }

    var delay
        get() = 0L
        set(value) {
            animator.startDelay = value
        }

    var onRepeat: ((Animator) -> Unit)? = null
    var onEnd: ((Animator) -> Unit)? = null
    var onCancel: ((Animator) -> Unit)? = null
    var onStart: ((Animator) -> Unit)? = null

    /**
     * reverse the value of [ValueAnimator]
     */
    abstract fun reverse()

    /**
     * to the beginning of animation
     */
    abstract fun toBeginning()

    internal fun addListener() {
        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
                animation?.let { onRepeat?.invoke(it) }
            }

            override fun onAnimationEnd(animation: Animator?) {
                animation?.let { onEnd?.invoke(it) }
            }

            override fun onAnimationCancel(animation: Animator?) {
                animation?.let { onCancel?.invoke(it) }
            }

            override fun onAnimationStart(animation: Animator?) {
                animation?.let { onStart?.invoke(it) }
            }
        })
    }
}

/**
 * 用法
 *
 * animSet {
 *      anim {
 *          values = floatArrayOf(1.0f, 1.4f)
 *          action = { value -> tv.scaleX = (value as Float) }
 *      } with anim {
 *          values = floatArrayOf(0f, -200f)
 *          action = { value -> tv.translationY = (value as Float) }
 *      }
 *      duration = 200L
 * }
 *
 *
 * animSet {
 *      objectAnim {
 *          target = tvTitle
 *          translationX = floatArrayOf(0f, 200f)
 *          alpha = floatArrayOf(1.0f, 0.3f)
 *          scaleX = floatArrayOf(1.0f, 1.3f)
 *      }
 *      duration = 100L
 * }
 *
 */
class AnimSet : Anim() {
    override var animator: Animator = AnimatorSet()

    private val animatorSet
        get() = animator as AnimatorSet

    private val anims by lazy { mutableListOf<Anim>() }

    /**
     * 动画是否在起点
     */
    var isAtStartPoint: Boolean = true

    /**
     * 动画值是否反转
     */
    private var hasReverse: Boolean = false

    fun anim(animCreation: ValueAnim.() -> Unit): Anim = ValueAnim()
        .apply(animCreation)
        .also { it.addListener() }
        .also { anims.add(it) }

    fun objectAnim(action: ObjectAnim.() -> Unit): Anim = ObjectAnim()
        .apply(action)
        .also { it.setPropertyValueHolder() }
        .also { it.addListener() }
        .also { anims.add(it) }

    fun isRunning(): Boolean = animatorSet.isRunning

    fun start(startDirect: Boolean = false) {
        if (!startDirect && animatorSet.isRunning) return
        anims.takeIf { hasReverse }?.forEach { anim -> anim.reverse() }.also { hasReverse = false }
        if (anims.size == 1) animatorSet.play(anims.first().animator)
        if (hasReverse && isAtStartPoint) {
            animatorSet.start()
            isAtStartPoint = false
        } else {
            animatorSet.start()
        }
    }

    override fun reverse() {
        if (animatorSet.isRunning) return
        anims.takeIf { !hasReverse }?.forEach { anim -> anim.reverse() }.also { hasReverse = true }
        if (hasReverse && !isAtStartPoint) {
            animatorSet.start()
            isAtStartPoint = true
        }
    }

    override fun toBeginning() {
        anims.forEach { it.toBeginning() }
    }

    fun getAnim(index: Int) = anims.takeIf { index in 0 until anims.size }?.let { it[index] }

    fun cancel() {
        animatorSet.cancel()
        animatorSet.removeAllListeners()
    }

    /**
     * 如果想一个接一个的播放动画，可以用 before
     * animSet {
     *      anim {
     *          value = floatArrayOf(1.0f, 1.4f)
     *          action = { value -> tv.scaleX = (value as Float) }
     *      } before anim {
     *          values = floatArrayOf(0f, -200f)
     *          action = { value -> btn.translationY = (value as Float) }
     *      }
     *      duration = 200L
     * }
     *
     */
    infix fun Anim.before(anim: Anim): Anim {
        animatorSet.play(animator).before(anim.animator).let { this.builder = it }
        return anim
    }

    /**
     * 如果想同时播放动画，用 with
     *
     * animSet {
     *      play {
     *          value = floatArrayOf(1.0f, 1.4f)
     *          action = { value -> tv.scaleX = (value as Float) }
     *      } with anim {
     *          values = floatArrayOf(0f, -200f)
     *          action = { value -> btn.translationY = (value as Float) }
     *      }
     *      duration = 200L
     * }
     *
     * 如果一个调用链中同时具有[with]和[before]，则[with]具有更高的优先级，例如：`a在b之前与c`意味着b和c将同时播放，并且在它们之前播放
     *
     */
    infix fun Anim.with(anim: Anim): Anim {
        if (builder == null) builder = animatorSet.play(animator).with(anim.animator)
        else builder?.with(anim.animator)
        return anim
    }
}

fun animSet(creation: AnimSet.() -> Unit) = AnimSet().apply { creation() }.also { it.addListener() }


class ObjectAnim : Anim() {
    companion object {
        private const val TRANSLATION_X = "translationX"
        private const val TRANSLATION_Y = "translationY"
        private const val SCALE_X = "scaleX"
        private const val SCALE_Y = "scaleY"
        private const val ALPHA = "alpha"
        private const val ROTATION = "rotation"
        private const val ROTATION_X = "rotationX"
        private const val ROTATION_Y = "rotationY"
    }

    override var animator: Animator = ObjectAnimator()

    private val objectAnimator
        get() = animator as ObjectAnimator

    var translationX: FloatArray? = null
        set(value) {
            field = value
            translationX?.let { PropertyValuesHolder.ofFloat(TRANSLATION_X, *it) }?.let { property ->
                valuesHolder[TRANSLATION_X] = property
                objectAnimator.setValues(*valuesHolder.values.toTypedArray())
            }
        }
    var translationY: FloatArray? = null
        set(value) {
            field = value
            translationY?.let { PropertyValuesHolder.ofFloat(TRANSLATION_Y, *it) }?.let { property ->
                valuesHolder[TRANSLATION_Y] = property
                objectAnimator.setValues(*valuesHolder.values.toTypedArray())
            }
        }
    var scaleX: FloatArray? = null
        set(value) {
            field = value
            scaleX?.let { PropertyValuesHolder.ofFloat(SCALE_X, *it) }?.let { property ->
                valuesHolder[SCALE_X] = property
                objectAnimator.setValues(*valuesHolder.values.toTypedArray())
            }
        }
    var scaleY: FloatArray? = null
        set(value) {
            field = value
            scaleY?.let { PropertyValuesHolder.ofFloat(SCALE_Y, *it) }?.let { property ->
                valuesHolder[SCALE_Y] = property
                objectAnimator.setValues(*valuesHolder.values.toTypedArray())
            }
        }
    var alpha: FloatArray? = null
        set(value) {
            field = value
            alpha?.let { PropertyValuesHolder.ofFloat(ALPHA, *it) }?.let { property ->
                valuesHolder[ALPHA] = property
                objectAnimator.setValues(*valuesHolder.values.toTypedArray())
            }
        }
    var rotation: FloatArray? = null
        set(value) {
            field = value
            rotation?.let { PropertyValuesHolder.ofFloat(ROTATION, *it) }?.let { property ->
                valuesHolder[ROTATION] = property
                objectAnimator.setValues(*valuesHolder.values.toTypedArray())
            }
        }
    var rotationX: FloatArray? = null
        set(value) {
            field = value
            rotationX?.let { PropertyValuesHolder.ofFloat(ROTATION_X, *it) }?.let { property ->
                valuesHolder[ROTATION_X] = property
                objectAnimator.setValues(*valuesHolder.values.toTypedArray())
            }
        }
    var rotationY: FloatArray? = null
        set(value) {
            field = value
            rotationY?.let { PropertyValuesHolder.ofFloat(ROTATION_Y, *it) }?.let { property ->
                valuesHolder[ROTATION_Y] = property
                objectAnimator.setValues(*valuesHolder.values.toTypedArray())
            }
        }

    var target: Any? = null
        set(value) {
            field = value
            (animator as ObjectAnimator).target = value
        }

    var repeatCount
        get() = 0
        set(value) {
            objectAnimator.repeatCount = value
        }

    var repeatMode
        get() = ValueAnimator.RESTART
        set(value) {
            objectAnimator.repeatMode = value
        }

    private val valuesHolder = mutableMapOf<String, PropertyValuesHolder>()

    override fun reverse() {
        valuesHolder.forEach { valuesHolder ->
            when (valuesHolder.key) {
                TRANSLATION_X -> translationX?.let {
                    it.reverse()
                    this.valuesHolder[TRANSLATION_X]?.setFloatValues(*it)
                }
                TRANSLATION_Y -> translationY?.let {
                    it.reverse()
                    this.valuesHolder[TRANSLATION_Y]?.setFloatValues(*it)
                }
                SCALE_X -> scaleX?.let {
                    it.reverse()
                    this.valuesHolder[SCALE_X]?.setFloatValues(*it)
                }
                SCALE_Y -> scaleY?.let {
                    it.reverse()
                    this.valuesHolder[SCALE_Y]?.setFloatValues(*it)
                }
                ALPHA -> alpha?.let {
                    it.reverse()
                    this.valuesHolder[ALPHA]?.setFloatValues(*it)
                }
                ROTATION -> rotation?.let {
                    it.reverse()
                    this.valuesHolder[ROTATION]?.setFloatValues(*it)
                }
                ROTATION_X -> rotationX?.let {
                    it.reverse()
                    this.valuesHolder[ROTATION_X]?.setFloatValues(*it)
                }
                ROTATION_Y -> rotationY?.let {
                    it.reverse()
                    this.valuesHolder[ROTATION_Y]?.setFloatValues(*it)
                }
            }
        }
    }

    override fun toBeginning() {
        valuesHolder.forEach { valuesHolder ->
            when (valuesHolder.key) {
                TRANSLATION_X -> translationX?.let {
                    (target as? View)?.translationX = it.first()
                }
                TRANSLATION_Y -> translationY?.let {
                    (target as? View)?.translationY = it.first()
                }
                SCALE_X -> scaleX?.let {
                    (target as? View)?.scaleX = it.first()
                }
                SCALE_Y -> scaleY?.let {
                    (target as? View)?.scaleY = it.first()
                }
                ALPHA -> alpha?.let {
                    (target as? View)?.alpha = it.first()
                }
                ROTATION -> rotation?.let {
                    (target as? View)?.rotation = it.first()
                }
                ROTATION_X -> rotationX?.let {
                    (target as? View)?.rotationX = it.first()
                }
                ROTATION_Y -> rotationY?.let {
                    (target as? View)?.rotationY = it.first()
                }
            }
        }
    }

    fun setPropertyValueHolder() {
        translationX?.let { PropertyValuesHolder.ofFloat(TRANSLATION_X, *it) }?.let { valuesHolder[TRANSLATION_X] = it }
        translationY?.let { PropertyValuesHolder.ofFloat(TRANSLATION_Y, *it) }?.let { valuesHolder[TRANSLATION_Y] = it }
        scaleX?.let { PropertyValuesHolder.ofFloat(SCALE_X, *it) }?.let { valuesHolder[SCALE_X] = it }
        scaleY?.let { PropertyValuesHolder.ofFloat(SCALE_Y, *it) }?.let { valuesHolder[SCALE_Y] = it }
        alpha?.let { PropertyValuesHolder.ofFloat(ALPHA, *it) }?.let { valuesHolder[ALPHA] = it }
        rotation?.let { PropertyValuesHolder.ofFloat(ROTATION, *it) }?.let { valuesHolder[ROTATION] = it }
        rotationX?.let { PropertyValuesHolder.ofFloat(ROTATION_X, *it) }?.let { valuesHolder[ROTATION_X] = it }
        rotationY?.let { PropertyValuesHolder.ofFloat(ROTATION_Y, *it) }?.let { valuesHolder[ROTATION_Y] = it }
        objectAnimator.setValues(*valuesHolder.values.toTypedArray())
    }
}

class ValueAnim : Anim() {

    override var animator: Animator = ValueAnimator()
    private val valueAnimator
        get() = animator as ValueAnimator

    var values: Any? = null
        set(value) {
            field = value
            value?.let {
                when (it) {
                    is FloatArray -> valueAnimator.setFloatValues(*it)
                    is IntArray -> valueAnimator.setIntValues(*it)
                    else -> throw IllegalArgumentException("unsupported value type")
                }
            }
        }

    var action: ((Any) -> Unit)? = null
        set(value) {
            field = value
            valueAnimator.addUpdateListener { valueAnimator ->
                valueAnimator.animatedValue.let { value?.invoke(it) }
            }
        }

    var repeatCount
        get() = 0
        set(value) {
            valueAnimator.repeatCount = value
        }

    var repeatMode
        get() = ValueAnimator.RESTART
        set(value) {
            valueAnimator.repeatMode = value
        }

    override fun reverse() {
        values?.let {
            when (it) {
                is FloatArray -> {
                    it.reverse()
                    valueAnimator.setFloatValues(*it)
                }
                is IntArray -> {
                    it.reverse()
                    valueAnimator.setIntValues(*it)
                }
                else -> throw IllegalArgumentException("unsupported type of value")
            }
        }
    }

    override fun toBeginning() {
    }
}