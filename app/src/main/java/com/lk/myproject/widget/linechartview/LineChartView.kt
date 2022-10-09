package com.lk.myproject.widget.linechartview

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PathEffect
import android.graphics.PathMeasure
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import androidx.annotation.ColorInt
import com.lk.myproject.R
import com.lk.myproject.ext.dp2px
import java.util.Locale
import kotlin.math.abs
import kotlin.math.sqrt

/**
 * Created by zhpan on 2017/3/14.
 */
class LineChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var xOrigin = 0f //  x轴原点坐标
    private var yOrigin = 0f//  y轴原点坐标
    private val mMargin10: Int //  10dp的间距
    private var mWidth = 0//  控件宽度
    private var mHeight = 0 //  控件高度
    private var max = 50
    private var min = -50 //  最大值、最小值
    private var yInterval = 0f //  y轴坐标间隔
    private var xInterval = 0f//  x轴坐标间隔
    private var startTime = "15"
    private var endTime = "今天"
    private var timeWidth = 0//  日期宽度

    var dataList: List<ChartDataBean>//  折线数据
    private var mShadeColors: IntArray? = null
    private var mAxesColor: Int //  坐标轴颜色
    private var mAxesWidth: Float //  坐标轴宽度
    private var mTextColor: Int //  字体颜色
    private var mTextSize: Float //  字体大小
    private var mLineColor: Int //  折线颜色
    private var mPaintText: Paint? = null//  画文字的画笔
    private var mPaintAxes: Paint? = null //  坐标轴画笔
    private var mPaintLine: Paint? = null //  折线画笔
    private var mPaintCircle: Paint? = null //  内圆圈画笔
    private var mPaintCircleOuter: Paint? = null //  圆圈画笔
    private var mPath: Path? = null //    折线路径
    private var mPaintShader: Paint? = null //  渐变色画笔
    private var mPathShader: Path? = null //  渐变色路径
    private var mProgress = 1f //  动画进度
    private var interpolator: Interpolator? = null
    private val circleRadius = DensityUtils.dp2px(getContext(), 3f)

    private var defaultBean = ChartDataBean(0, tip = "点击节点可查\n看数据详情") //首次没选中的时候提示
    var onItemSelected: (ChartDataBean) -> Unit = {}

    private fun init() {
        //  初始化坐标轴画笔
        interpolator = LinearInterpolator()
        mPaintAxes = Paint().apply {
            color = mAxesColor
            style = Paint.Style.STROKE
            pathEffect = DashPathEffect(floatArrayOf(10f, 10f), 0f)
            strokeWidth = mAxesWidth / 2
        }

        //  初始化文字画笔
        mPaintText = Paint()
        mPaintText!!.style = Paint.Style.FILL
        mPaintText!!.isAntiAlias = true //抗锯齿
        mPaintText!!.textSize = mTextSize
        mPaintText!!.color = mTextColor
        mPaintText!!.textAlign = Paint.Align.LEFT

        //  初始化折线的画笔
        mPaintLine = Paint()
        mPaintLine!!.style = Paint.Style.STROKE
        mPaintLine!!.isAntiAlias = true
        mPaintLine!!.strokeWidth = mAxesWidth
        mPaintLine!!.color = mLineColor

        //  初始化折线路径
        mPath = Path()
        mPathShader = Path()

        //  阴影画笔
        mPaintShader = Paint()
        mPaintShader!!.isAntiAlias = true
        mPaintShader!!.strokeWidth = 2f

        // 画圆圈
        mPaintCircle = Paint().apply {
            isAntiAlias = true
            color = Color.parseColor("#ffffff")
            style = Paint.Style.FILL_AND_STROKE
            xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)
        }
        mPaintCircleOuter = Paint().apply {
            isAntiAlias = true
            color = mLineColor
            strokeWidth = mAxesWidth * 2f
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x.toInt()
        val y = event.y.toInt()
        if (event.action == MotionEvent.ACTION_DOWN) {
            var isClickItem = false
            var lastSelectedIndex = -1
            var newSelectIndex = -1
            for (i in dataList.indices) {
                dataList[i].let {
                    if (it.isSelected) {
                        lastSelectedIndex = i
                    }
                    if (abs(it.x - x) <= 10.dp2px && abs(it.y - y) <= 10.dp2px) {
                        isClickItem = true
                        it.isSelected = true
                        newSelectIndex = i
                    } else {
                        it.isSelected = false
                    }
                }
            }
            if (isClickItem) {
                invalidate()
                onItemSelected.invoke(dataList[newSelectIndex])
            } else { //不是有效点击位置，复原
                if (newSelectIndex > 0) {
                    dataList[newSelectIndex].isSelected = false
                }
                if (lastSelectedIndex > 0) {
                    dataList[lastSelectedIndex].isSelected = true
                }
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (changed) {
            mWidth = width
            mHeight = height
            timeWidth = mPaintText!!.measureText(startTime).toInt()
            //  初始化原点坐标
            xOrigin = 3 * mMargin10.toFloat()
            yOrigin = mHeight - mTextSize - mMargin10
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //  Y轴间距
        yInterval = (max - min) / (yOrigin - mMargin10)
        xInterval = (mWidth - xOrigin - mMargin10) / (dataList.size - 1)
        //  画坐标轴
        drawAxes(canvas)
        if (dataList.isNullOrEmpty()) {
            return
        }
        //  画折线
        drawLine(canvas)
        //  画文字
        drawText(canvas)
        //  设置动画
        setAnim(canvas)
        // 画圆圈
        drawCircle(canvas)
    }

    private fun setAnim(canvas: Canvas) {
        val measure = PathMeasure(mPath, false)
        val pathLength = measure.length
        val effect: PathEffect = DashPathEffect(
            floatArrayOf(
                pathLength,
                pathLength
            ), pathLength - pathLength * mProgress
        )
        mPaintLine!!.pathEffect = effect
        canvas.drawPath(mPath!!, mPaintLine!!)
    }

    private fun drawCircle(canvas: Canvas) {
        var hasSelected = false
        dataList.forEach {
            if (it.isSelected) {
                hasSelected = true
                mPaintCircleOuter!!.style = Paint.Style.FILL_AND_STROKE
                canvas.drawCircle(it.x, it.y, circleRadius.toFloat(), mPaintCircleOuter!!)
            } else {
                mPaintCircleOuter!!.style = Paint.Style.STROKE
                canvas.drawCircle(it.x, it.y, circleRadius.toFloat(), mPaintCircleOuter!!)
                canvas.drawCircle(it.x, it.y, circleRadius.toFloat(), mPaintCircle!!)
            }
        }
        if (!hasSelected) {
            defaultBean.x = dataList[dataList.size / 2].x
            defaultBean.y = dataList[dataList.size / 2].y
            onItemSelected.invoke(defaultBean)
        }
    }

    private fun drawLine(canvas: Canvas) {
        //  画坐标点
        for (i in dataList.indices) {
            val x = i * xInterval + xOrigin + mAxesWidth
            var value = dataList[i].totalNum
            if (i == 0) {
                mPathShader!!.moveTo(x, yOrigin - (value - min) / yInterval)
                mPath!!.moveTo(
                    x,
                    yOrigin - (value - min) / yInterval
                )
                val y = yOrigin - (value - min) / yInterval
                dataList[i].x = x
                dataList[i].y = y
            } else {
                mPath!!.lineTo(
                    x,
                    yOrigin - (value - min) / yInterval
                )
                mPathShader!!.lineTo(
                    x - mMargin10 - mAxesWidth,
                    yOrigin - (value - min) / yInterval
                )
                if (i == dataList.size - 1) {
                    mPathShader!!.lineTo(x - mMargin10 - mAxesWidth, yOrigin)
                    mPathShader!!.lineTo(xOrigin, yOrigin)
                    mPathShader!!.close()
                }
                val y = yOrigin - (value - min) / yInterval
                dataList[i].x = x
                dataList[i].y = y
            }
        }
        //不画渐变
        mPaintShader!!.color = Color.argb(0, 0, 0, 0)
        /*if (null == mShadeColors) {
            mPaintShader!!.color = Color.argb(0, 0, 0, 0)
        } else {
            val mShader: Shader =
                LinearGradient(
                    0f, 0f, 0f, height.toFloat(), mShadeColors!!,
                    null, Shader.TileMode.CLAMP
                )
            mPaintShader!!.shader = mShader
        }
        canvas.drawPath(mPathShader!!, mPaintShader!!)
         */
    }

    private fun formatNum(num: Int): String {
        return if (num > 100000) {
            String.format(Locale.getDefault(), "%.0fw", num / 10000.0)
        } else if (num > 10000) {
            String.format(Locale.getDefault(), "%.1fw", num / 10000.0)
        } else if (num > 1000) {
            String.format(Locale.getDefault(), "%.1fk", num / 1000.0)
        } else {
            num.toString()
        }
    }

    private fun getTextMargin(text: String): Float {
        return mPaintText!!.measureText(text) + 6.dp2px
    }

    private fun drawText(canvas: Canvas) {
        //  绘制最大值
        var text = formatNum(max)
        canvas.drawText(
            text, xOrigin - getTextMargin(text), mMargin10.toFloat() + mTextSize / 2,
            mPaintText!!
        )
        //  绘制最小值
        text = formatNum(min)
        canvas.drawText(text, xOrigin - getTextMargin(text), yOrigin, mPaintText!!)
        //  绘制中间值
        text = formatNum((min + max) / 2)
        canvas.drawText(
            text,
            xOrigin - getTextMargin(text),
            (yOrigin + mMargin10) / 2 + mTextSize / 2,
            mPaintText!!
        )
        dataList.forEachIndexed { index, it ->
            var length = mPaintText!!.measureText(it.date)
            var tempX = it.x - length / 2
            if (index == dataList.size - 1 && it.date.length == 5) { //超长处理
                tempX -= 2.dp2px
            }
            canvas.drawText(
                it.date,
                tempX,
                (mHeight - mMargin10 + 5.dp2px).toFloat(),
                mPaintText!!
            )
        }
        /*//  绘制开始日期
        canvas.drawText(startTime, xOrigin, (mHeight - mMargin10).toFloat(), mPaintText!!)
        //  绘制结束日期
        canvas.drawText(
            endTime,
            (mWidth - timeWidth - mMargin10).toFloat(),
            (mHeight - mMargin10).toFloat(),
            mPaintText!!
        )*/
    }

    //  画坐标轴
    private fun drawAxes(canvas: Canvas) {
        //  绘制X轴
        canvas.drawLine(xOrigin, yOrigin, (mWidth - mMargin10).toFloat(), yOrigin, mPaintAxes!!)
        //  绘制X中轴线
        canvas.drawLine(
            xOrigin,
            (yOrigin + mMargin10) / 2,
            (mWidth - mMargin10).toFloat(),
            (yOrigin + mMargin10) / 2,
            mPaintAxes!!
        )
        //  绘制X上边线
        canvas.drawLine(
            xOrigin,
            mMargin10.toFloat(),
            (mWidth - mMargin10).toFloat(),
            mMargin10.toFloat(),
            mPaintAxes!!
        )
        //  绘制画Y轴
        canvas.drawLine(xOrigin, yOrigin, xOrigin, 0f, mPaintAxes!!)
        //  绘制Y右边线
        /*canvas.drawLine(
            (mWidth - mMargin10).toFloat(),
            mMargin10.toFloat(),
            (mWidth - mMargin10).toFloat(),
            yOrigin,
            mPaintAxes!!
        )*/
    }

    //  属性动画的set方法
    fun setPercentage(percentage: Float) {
        require(!(percentage < 0.0f || percentage > 1.0f)) { "setPercentage not between 0.0f and 1.0f" }
        mProgress = percentage
        invalidate()
    }

    /**
     * @param duration 动画持续时间
     */
    fun startAnim(duration: Long) {
        val anim = ObjectAnimator.ofFloat(this, "percentage", 0.0f, 1.0f)
        anim.duration = duration
        anim.interpolator = interpolator
        anim.start()
    }

    fun setInterpolator(interpolator: Interpolator?) {
        this.interpolator = interpolator
    }

    fun setShadeColors(@ColorInt shadeColors: List<Int>) {
        mShadeColors = IntArray(shadeColors.size)
        for (i in shadeColors.indices) {
            mShadeColors!![i] = shadeColors[i]
        }
    }

    /**
     * @param max Y轴最大值
     */
    fun setAxisMaxValue(max: Int) {
        this.max = max
    }

    /**
     * @param min Y轴最小值
     */
    fun setAxisMinValue(min: Int) {
        this.min = min
    }

    fun setStartTime(startTime: String) {
        this.startTime = startTime
    }

    fun setEndTime(endTime: String) {
        this.endTime = endTime
    }

    fun setAxesColor(axesColor: Int) {
        mAxesColor = axesColor
    }

    fun setAxesWidth(axesWidth: Float) {
        mAxesWidth = axesWidth
    }

    fun setTextColor(textColor: Int) {
        mTextColor = textColor
    }

    fun setTextSize(textSize: Float) {
        mTextSize = textSize
    }

    fun setLineColor(lineColor: Int) {
        mLineColor = lineColor
    }

    companion object {
        private const val TAG = "LineChartView"
    }

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.LineChartView)
        mAxesColor =
            typedArray.getColor(R.styleable.LineChartView_axesColor, Color.parseColor("#CCCCCC"))
        mAxesWidth = typedArray.getDimension(R.styleable.LineChartView_axesWidth, 1f)
        mTextColor =
            typedArray.getColor(R.styleable.LineChartView_textColor, Color.parseColor("#ABABAB"))
        mTextSize = typedArray.getDimension(R.styleable.LineChartView_textSize, 32f)
        mLineColor = typedArray.getColor(R.styleable.LineChartView_lineColor, Color.RED)
        typedArray.recycle()
        //  初始化折线数据
        dataList = ArrayList()
        mMargin10 = DensityUtils.dp2px(context, 10f)
        init()
    }
}