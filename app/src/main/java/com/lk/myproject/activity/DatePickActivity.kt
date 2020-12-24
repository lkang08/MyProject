package com.lk.myproject.activity

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.NumberPicker
import com.lk.myproject.R
import kotlinx.android.synthetic.main.activity_date_pick.*
import java.lang.reflect.Field
import java.util.Calendar

class DatePickActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_date_pick)
        textview.setOnClickListener {
            //initDate()
            DateDialog(this, R.style.bottom_dialog, type = 1).show(action = { from, to ->
                tvFrom.text = from
                tvTo.text = to
            })
        }
    }

    private fun initDate() {
        val c: Calendar = Calendar.getInstance()
        var year = c.get(Calendar.YEAR)
        var month = c.get(Calendar.MONTH)
        var day = c.get(Calendar.DAY_OF_MONTH)

        var listener = DatePicker.OnDateChangedListener { view, y, m, d ->
            textview.text = "$y-$m-$d"
        }
        /*datePicker.init(year, month, day, listener)
        resizePicker(datePicker)
        setDatePickerDividerColor(datePicker)*/
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
        // Divider changing:

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