package com.lk.myproject.activity

import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.lk.myproject.Bean.VideoHeartGuardItem
import com.lk.myproject.R
import com.lk.myproject.adapter.HeartGuardAdapter
import kotlinx.android.synthetic.main.activity_recyclerview.*

class RecyclerViewActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recyclerview)
        initData()
        initView()
        sureBtn.setOnClickListener {
            initData()
            adapter.list = list
            adapter.notifyDataSetChanged()
        }
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
}

