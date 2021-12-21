package com.lk.myproject.activity

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.fastjson.JSONObject
import com.lk.myproject.Bean.VideoHeartGuardItem
import com.lk.myproject.MyApplication
import com.lk.myproject.R
import com.lk.myproject.adapter.HeartGuardAdapter
import com.lk.myproject.ext.log
import com.lk.myproject.ext.safe
import com.lk.myproject.widget.textview.TextUtils
import kotlinx.android.synthetic.main.activity_recyclerview.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

class RecyclerViewActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recyclerview)
        MyApplication.l("RecyclerViewActivity onCreate")
        initData()
        initView()
        sureBtn.setOnClickListener {
            "click".log()
            /*initData()
            adapter.list = list
            adapter.notifyDataSetChanged()*/
            testWeakRef()
            showAnimation()
            //showAnimation2()
            //showAnimation3()
            load()
        }
        MyApplication.l("RecyclerViewActivity 1 onCreate")
    }

    fun load() {
        var listener = object : TextUtils.OnTextClickListener {
            override fun onActiveClick() {
                "onActiveClick".log()
            }

            override fun onOpenClose(canShow: Boolean, isOpen: Boolean) {
                "onOpenClose".log()
            }
        }
        TextUtils.setLimitLineText(tvTest, 1, "哈哈哈哈",
            "红红火火f短发短发收到；两房阿斯顿发收到发链接啊是；的发生地方阿斯顿发阿斯顿发", listener)
    }

    private fun showAnimation() {
        WholeMicAnimation.showReceviedGiftAnimation(testLayout1, repeart = 2)
        //WholeMicAnimation.showReceviedGiftAnimation(testLayout2, repeart = 2)
        // WholeMicAnimation.showReceviedGiftAnimation(testLayout3, repeart = 2)
        //WholeMicAnimation.showReceviedGiftAnimation(testLayout4, repeart = 2)
    }

    private fun showAnimation2() {
        WholeMicAnimation.showReceviedGiftAnimation(testLayout11, repeart = 4)
        WholeMicAnimation.showReceviedGiftAnimation(testLayout12, repeart = 4)
        WholeMicAnimation.showReceviedGiftAnimation(testLayout13, repeart = 4)
        WholeMicAnimation.showReceviedGiftAnimation(testLayout14, repeart = 4)
    }

    private fun showAnimation3() {
        WholeMicAnimation.showReceviedGiftAnimation(testLayout21, repeart = 6)
        WholeMicAnimation.showReceviedGiftAnimation(testLayout22, repeart = 6)
        WholeMicAnimation.showReceviedGiftAnimation(testLayout23, repeart = 6)
        WholeMicAnimation.showReceviedGiftAnimation(testLayout24, repeart = 6)
    }

    var list = mutableListOf<VideoHeartGuardItem>()
    var adapter = HeartGuardAdapter(list)
    fun initData() {
        var item = VideoHeartGuardItem()
        item.anchorId = R.drawable.ico_image
        list.add(item)

        //list.add(VideoHeartGuardItem().apply { anchorId = R.drawable.ico_image })

        list.add(VideoHeartGuardItem().apply { anchorId = R.drawable.icon_heart })
    }

    fun initView() {
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapter
        setRecyclerViewDecoration(recyclerView)
    }

    fun dp2px(dp: Int): Int {
        var screenDensity = resources.displayMetrics.density
        return (dp.toFloat() * screenDensity + 0.5f).toInt()
    }

    private fun setRecyclerViewDecoration(recyclerView: RecyclerView) {
        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect, view: View, parent: RecyclerView,
                state: RecyclerView.State
            ) {
                super.getItemOffsets(outRect, view, parent, state)
                if (parent.getChildAdapterPosition(view) == 0) {
                    outRect.left = 0
                } else {
                    outRect.left = dp2px(-10)
                }
            }
        })
    }

    private fun showTest(i: Int) {
        "end showTest $i".log()
    }

    var params: String? = null
    private fun testWeakRef() {
        Log.d("BLLog", "${params.isNullOrBlank()}")
        Log.d("BLLog", "${params.isNullOrEmpty()}")
        var jsonObject = JSONObject.parseObject(params)
        Log.d("BLLog", "tt = ${jsonObject?.getString("tt")}")
        var ref = WeakReference(this)
        GlobalScope.launch {
            var count = 10
            repeat(count) {
                "repeat $it".log()
                delay(1000)
            }
            "repeat end ..."
            ref.safe { showTest(count) }
        }
    }
}

