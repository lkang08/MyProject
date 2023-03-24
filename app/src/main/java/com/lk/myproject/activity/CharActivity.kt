package com.lk.myproject.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.animation.DecelerateInterpolator
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartView
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import com.lk.myproject.R
import com.lk.myproject.ext.dp2px
import com.lk.myproject.reactnative.MyReactActivity
import com.lk.myproject.widget.linechartview.ChartDataBean
import kotlinx.android.synthetic.main.activity_char.*
import kotlinx.android.synthetic.main.activity_overdraw_main.lineView
import java.util.Date
import java.util.Random

class CharActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_char)
        //loadLineView()
        //loadCharView()
        loadCharView2()

        tv_go.setOnClickListener {
            startActivity(Intent(this, MyReactActivity::class.java))
        }
    }

    private fun loadCharView2() {
        //  初始化折线数据
        val listValues: MutableList<ChartDataBean> = ArrayList()
        val random = Random()
        var startValue: Float = random.nextFloat() * 10
        /*listValues.add(startValue)
        for (i in 0..29) {
            startValue += random.nextFloat() * 10 - 5
            listValues.add(startValue)
        }*/

        var time = 1662998400000
        var d = Date(time)
        d.day
        listValues.apply {
            add(ChartDataBean(0, visitorNum = 1, greetingCount = 1, strikeUp = 30, date = "25"))
            add(ChartDataBean(0, visitorNum = 1, greetingCount = 1, strikeUp = 30, date = "1"))
            add(
                ChartDataBean(
                    0,
                    visitorNum = 11,
                    greetingCount = 11,
                    strikeUp = 10,
                    date = "8",
                    isSelected = false
                )
            )
            add(
                ChartDataBean(
                    0,
                    visitorNum = 12,
                    greetingCount = 100,
                    strikeUp = 0,
                    date = "9"
                )
            )
            add(ChartDataBean(0, visitorNum = 14, greetingCount = 1, strikeUp = 30, date = "29"))
            add(
                ChartDataBean(
                    0,
                    visitorNum = 15,
                    greetingCount = 14,
                    strikeUp = 130,
                    date = "11"
                )
            )
            add(
                ChartDataBean(
                    199,
                    visitorNum = 16,
                    greetingCount = 111,
                    strikeUp = 30,
                    date = "12"
                )
            )
        }
        var max = listValues[0].totalNum
        listValues.forEach {
            if (it.totalNum > max) {
                max = it.totalNum
            }
        }
        val listShadeColors: MutableList<Int> = ArrayList()
        listShadeColors.add(Color.argb(100, 255, 86, 86))
        listShadeColors.add(Color.argb(15, 255, 86, 86))
        listShadeColors.add(Color.argb(0, 255, 86, 86))
        //  设置折线数据
        lineChartView.dataList = listValues
        //  设置渐变颜色
        lineChartView.setShadeColors(listShadeColors)
        //  设置动画插值器
        lineChartView.setInterpolator(DecelerateInterpolator())
        lineChartView.setAxisMinValue(0)
        lineChartView.setAxisMaxValue(max)
        lineChartView.setStartTime("15")
        lineChartView.setEndTime("今天")

        lineChartView.onItemSelected = {
            Log.d("LineChartView", "####onItemSelected ${it.x} ${it.y}")
            it.showTip(tvTip, layoutTip)
        }

        tvStart.setOnClickListener {
            lineChartView.startAnim(2500)
        }
    }

    private fun loadCharView() {
        val aaChartView = findViewById<AAChartView>(R.id.aa_chart_view)
        val aaChartModel = AAChartModel()
            .chartType(AAChartType.Area)
            .title("title")
            .subtitle("subtitle")
            .backgroundColor("#4b2b7f")
            .dataLabelsEnabled(true)
            .series(
                arrayOf(
                    AASeriesElement()
                        .name("Tokyo")
                        .data(
                            arrayOf(
                                7.0,
                                6.9,
                                9.5,
                                14.5,
                                18.2,
                                21.5,
                                25.2,
                                26.5,
                                23.3,
                                18.3,
                                13.9,
                                9.6
                            )
                        ),
                    AASeriesElement()
                        .name("NewYork")
                        .data(
                            arrayOf(
                                0.2,
                                0.8,
                                5.7,
                                11.3,
                                17.0,
                                22.0,
                                24.8,
                                24.1,
                                20.1,
                                14.1,
                                8.6,
                                2.5
                            )
                        )
                )
            )
        aaChartView.aa_drawChartWithChartModel(aaChartModel)
    }

    private fun loadLineView() {
        var datas = mutableListOf<Double>()
        datas.apply {
            add(9.0)
            add(110.0)
            add(12.0)
            add(14.0)
            add(32.0)
            add(88.0)
            add(22.0)
            add(120.0)
        }
        var desc = mutableListOf<String>()
        desc.apply {
            add("9")
            add("10")
            add("11")
            add("12")
            add("13")
            add("14")
            add("15")
            add("今天")
        }

        lineView.setDatas(datas, desc)
        lineView
    }
}