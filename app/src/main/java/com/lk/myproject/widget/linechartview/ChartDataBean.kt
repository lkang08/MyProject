package com.lk.myproject.widget.linechartview

import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.TextView
import com.lk.myproject.ext.dp2px

class ChartDataBean(
    var totalNum: Int, var x: Float = 0f, var y: Float = 0f, var visitorNum: Int = 0,
    var greetingCount: Int = 0, var strikeUp: Int = 0, var date: String = "",
    var isSelected: Boolean = false, var tip: String? = ""
) {
    private fun formatNum(num: Int): String {
        return if (num <= 0) "0" else if (num >= 100) "99+" else "$num"
    }

    fun showTip(tvTip: TextView, layoutTip: View) {
        var translationY = this.y - 7.dp2px
        if (TextUtils.isEmpty(this.tip)) {
            tvTip.text = "访问数:${formatNum(visitorNum)} " +
                "\n被撩数:${formatNum(greetingCount)}" +
                "\n被打招呼数:${formatNum(strikeUp)}"
        } else {
            tvTip.text = tip
            translationY += 9.dp2px
        }
        tvTip.post {
            layoutTip.translationX = this.x - tvTip.measuredWidth / 2 + 10.dp2px
            layoutTip.translationY = translationY
        }
    }
}