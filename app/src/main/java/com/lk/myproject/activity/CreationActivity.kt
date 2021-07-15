package com.lk.myproject.activity

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.lk.myproject.R

class CreationActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.hot_line_creation_page)

        val mRecyclerView = findViewById(R.id.recyclerView) as RecyclerView

        // 两列
        val spanCount = 5

        // StaggeredGridLayoutManager管理RecyclerView的布局。
        val mLayoutManager = StaggeredGridLayoutManager(spanCount,
            StaggeredGridLayoutManager.HORIZONTAL)
        mRecyclerView.layoutManager = mLayoutManager

        //为RecyclerView增加分割线，水平和垂直方向都有。增加分割线值比如为32。
        val decoration = RecyclerViewItemDecoration(32)
        mRecyclerView.addItemDecoration(decoration)

        val mAdapter = RecyclerViewAdapter(this)
        mRecyclerView.adapter = mAdapter
    }
}