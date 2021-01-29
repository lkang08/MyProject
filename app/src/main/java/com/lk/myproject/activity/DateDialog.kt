package com.lk.myproject.activity

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.NumberPicker
import android.widget.RelativeLayout
import android.widget.Toast
import com.lk.myproject.R
import kotlinx.android.synthetic.main.dialog_date_pick.*
import java.lang.reflect.Field
import java.util.Calendar

//type = 0 充值 1消费
class DateDialog(
    context: Context?, themeResId: Int, var type: Int = 0,
    var originFromData: String = "", var originToData: String = ""
) : Dialog
(context,
    themeResId) {
    val TAG = "PurseDateDialog"
    var toDate: MyDate
    var fromDate: MyDate
    var maxDate = System.currentTimeMillis()
    var minDate = System.currentTimeMillis()
    var calendar = Calendar.getInstance()

    var action: (s: String, s2: String) -> Unit = { _, _ ->

    }

    fun show(action: (s: String, s2: String) -> Unit) {
        super.show()
        this.action = action
    }

    init {
        setContentView(R.layout.dialog_date_pick)
        setCanceledOnTouchOutside(true)
        val window = window
        window!!.decorView.setPadding(0, 0, 0, 0)
        val paramsWindow = window.attributes
        paramsWindow.width = RelativeLayout.LayoutParams.MATCH_PARENT
        paramsWindow.gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
        paramsWindow.windowAnimations = R.style.select_popup_bottom
        window.attributes = paramsWindow
        calendar.timeInMillis = System.currentTimeMillis()
        var year = calendar.get(Calendar.YEAR)
        var month = calendar.get(Calendar.MONTH)
        var day = calendar.get(Calendar.DAY_OF_MONTH)
        toDate = MyDate(year, month, day)
        fromDate = MyDate(year, month, day)
        initView()
    }

    private fun initView() {
        tvSure.setOnClickListener {
            Log.d(TAG, "from = ${rbFrom.text} to = ${rbTo.text} type = $type")
            if (rbTo.text == "结束日期" || rbFrom.text == "开始日期") {
                Toast.makeText(context, "请选择正确的时间范围~", Toast.LENGTH_LONG)
                return@setOnClickListener
            }
            action.invoke(rbFrom.text.toString(), rbTo.text.toString())
            dismiss()
        }
        tvCancel.setOnClickListener {
            dismiss()
        }
        timeGroup.setOnCheckedChangeListener { group, checkedId ->
            resetDataPickVisible(checkedId == R.id.rbFrom)
            if (checkedId == R.id.rbFrom) {
                if (rbTo.text != "结束日期") {
                    calendar.set(toDate.year, toDate.month, toDate.day, 23, 59, 59)
                    resetDate(fromDatePicker, minDate, calendar.timeInMillis)
                } else {
                    resetDate(fromDatePicker, minDate, maxDate)
                }
            } else {
                if (rbFrom.text != "开始日期") {
                    calendar.set(fromDate.year, fromDate.month, fromDate.day, 0, 0, 0)
                    resetDate(toDatePicker, calendar.timeInMillis, maxDate)
                } else {
                    resetDate(toDatePicker, minDate, maxDate)
                }
            }
        }
        initDate()
    }

    private fun resetDataPickVisible(isFrom: Boolean) {
        fromDatePicker.visibility = if (isFrom) View.VISIBLE else View.GONE
        toDatePicker.visibility = if (!isFrom) View.VISIBLE else View.GONE
    }

    private fun resetDate(datePicker: DatePicker, begin: Long, end: Long) {
        try {
            //datePicker.maxDate = System.currentTimeMillis()
            datePicker.minDate = begin
            datePicker.maxDate = end
        } catch (e: java.lang.Exception) {
            Log.d(TAG, "########## ${e.message}")
        }
    }

    private fun initDate() {

        if (type == 0) { //充值，31天记录
            minDate = System.currentTimeMillis() - 30 * 24 * 60 * 60 * 1000L
        } else { //消费，7天
            minDate = System.currentTimeMillis() - 6 * 24 * 60 * 60 * 1000L
        }
        var init = false
        if (!TextUtils.isEmpty(originToData)) {
            originToData.split("-")?.takeIf {
                it.size == 3
            }?.let {
                toDate = MyDate(it[0].toInt(), it[1].toInt() - 1, it[2].toInt())
                calendar.set(it[0].toInt(), it[1].toInt() - 1, it[2].toInt(), 23, 59, 0)
                resetDate(fromDatePicker, minDate, calendar.timeInMillis)
                init = true
            }
        }
        if (!init) {
            resetDate(fromDatePicker, minDate, maxDate)
        }

        init = false
        if (!TextUtils.isEmpty(originFromData)) {
            originFromData.split("-")?.takeIf {
                it.size == 3
            }?.let {
                fromDate = MyDate(it[0].toInt(), it[1].toInt() - 1, it[2].toInt())
                calendar.set(it[0].toInt(), it[1].toInt() - 1, it[2].toInt(), 1, 0, 0)
                resetDate(toDatePicker, calendar.timeInMillis, maxDate)
            }
        }
        if (!init) {
            resetDate(toDatePicker, minDate, maxDate)
        }

        var fromListener = DatePicker.OnDateChangedListener { view, y, m, d ->
            var showMonth = if (m < 9) "0${m + 1}" else "${m + 1}"
            var showDay = if (d < 10) "0$d" else "$d"
            rbFrom.text = "$y-$showMonth-$showDay"
            fromDate.apply {
                year = y
                month = m
                day = d
            }
        }
        var toListener = DatePicker.OnDateChangedListener { view, y, m, d ->
            var showMonth = if (m < 9) "0${m + 1}" else "${m + 1}"
            var showDay = if (d < 10) "0$d" else "$d"
            rbTo.text = "$y-$showMonth-$showDay"
            toDate.apply {
                year = y
                month = m
                day = d
            }
        }
        if (!TextUtils.isEmpty(originFromData)) {
            originFromData.split("-")?.takeIf {
                it.size == 3
            }?.let {
                fromDatePicker.init(it[0].toInt(), it[1].toInt() - 1, it[2].toInt(), fromListener)
                rbFrom.text = originFromData
            }
        } else {
            fromDatePicker.init(toDate.year, toDate.month, toDate.day, fromListener)
        }

        if (!TextUtils.isEmpty(originToData)) {
            originToData.split("-")?.takeIf {
                it.size == 3
            }?.let {
                toDatePicker.init(it[0].toInt(), it[1].toInt() - 1, it[2].toInt(), toListener)
                rbTo.text = originToData
            }
        } else {
            toDatePicker.init(toDate.year, toDate.month, toDate.day, toListener)
        }

        resizePicker(fromDatePicker)
        resizePicker(toDatePicker)
        //setDatePickerDividerColor(datePicker)
    }

    private fun resizePicker(tp: FrameLayout) {        //DatePicker和TimePicker继承自FrameLayout
        findNumberPicker(tp)?.forEach {
            it?.let { numPicker ->
                resizeNumberPicker(numPicker)
            }
        }
    }

    /**
     * 得到viewGroup 里面的numberpicker组件
     */
    private fun findNumberPicker(viewGroup: ViewGroup?): List<NumberPicker?> {
        val npList: MutableList<NumberPicker?> = ArrayList()
        var child: View? = null
        if (null != viewGroup) {
            for (i in 0 until viewGroup.childCount) {
                child = viewGroup.getChildAt(i)
                if (child is NumberPicker) {
                    npList.add(child as NumberPicker?)
                } else if (child is LinearLayout) {
                    val result = findNumberPicker(child as ViewGroup?)
                    if (result.size > 0) {
                        return result
                    }
                }
            }
        }
        return npList
    }

    /**
     * 调整numberpicker大小
     */
    private fun resizeNumberPicker(np: NumberPicker): Unit {
        val params = LinearLayout.LayoutParams(150, LinearLayout.LayoutParams.WRAP_CONTENT)
        params.setMargins(10, 0, 10, 0)
        np.layoutParams = params
    }

    /**
     * 设置时间选择器的分割线颜色
     *
     * @param datePicker
     */
    private fun setDatePickerDividerColor(datePicker: DatePicker) {

        // 获取 mSpinners
        val llFirst = datePicker.getChildAt(0) as LinearLayout

        // 获取 NumberPicker
        val mSpinners = llFirst.getChildAt(0) as LinearLayout
        for (i in 0 until mSpinners.childCount) {
            val picker = mSpinners.getChildAt(i) as NumberPicker
            val pickerFields: Array<Field> = NumberPicker::class.java.declaredFields
            for (pf in pickerFields) {
                if (pf.getName().equals("mSelectionDivider")) {
                    pf.setAccessible(true)
                    try {
                        pf.set(picker, ColorDrawable(Color.parseColor("#bd1b21"))) //设置分割线颜色
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    break
                }
            }
        }
    }
}

data class MyDate(var year: Int, var month: Int, var day: Int)