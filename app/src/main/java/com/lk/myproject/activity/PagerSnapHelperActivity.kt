package com.lk.myproject.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.lk.myproject.R
import kotlinx.android.synthetic.main.recycle_pager_activity.*
import java.util.ArrayList

class PagerSnapHelperActivity : BaseActivity() {
    private var mRecyclerView: RecyclerView? = null

    private var mMyadapter: PagerSnapHelperAdapter? = null

    private val mDataList = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recycle_pager_activity)
        for (i in 1..10) {
            mDataList.add("item$i")
        }
        // 纵向List
        initUI()

        flowIndicator.setMaxNum(mDataList.size)
        flowIndicator.setSelectedPos(0)
    }

    fun initUI() {
        mRecyclerView = findViewById<View>(R.id.recyclerview_vertical) as RecyclerView
        mRecyclerView!!.isNestedScrollingEnabled = false
        val snapHelper: PagerSnapHelper = object : PagerSnapHelper() {
            // 在 Adapter的 onBindViewHolder 之后执行
            override fun findTargetSnapPosition(
                layoutManager: RecyclerView.LayoutManager,
                velocityX: Int,
                velocityY: Int
            ): Int {
                // TODO 找到对应的Index
                Log.e("xiaxl: ", "---findTargetSnapPosition---")
                val targetPos = super.findTargetSnapPosition(layoutManager, velocityX, velocityY)
                Log.e("xiaxl: ", "targetPos: $targetPos")
                Toast.makeText(
                    this@PagerSnapHelperActivity,
                    "滑到到 " + targetPos + "位置",
                    Toast.LENGTH_SHORT
                ).show()
                flowIndicator.setSelectedPos(targetPos)
                return targetPos
            }

            // 在 Adapter的 onBindViewHolder 之后执行
            override fun findSnapView(layoutManager: RecyclerView.LayoutManager): View? {
                // TODO 找到对应的View
                Log.e("xiaxl: ", "---findSnapView---")
                val view = super.findSnapView(layoutManager)
                Log.e("xiaxl: ", "tag: " + view!!.tag)
                return view
            }
        }
        snapHelper.attachToRecyclerView(mRecyclerView)
        // ---布局管理器---
        val linearLayoutManager = LinearLayoutManager(this)
        // 默认是Vertical (HORIZONTAL则为横向列表)
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        //
        mRecyclerView!!.layoutManager = linearLayoutManager
        // TODO 这么写是为了获取RecycleView的宽高
        mRecyclerView!!.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                mRecyclerView!!.viewTreeObserver.removeOnGlobalLayoutListener(this)
                /**
                 * 这么写是为了获取RecycleView的宽高
                 */
                mMyadapter =
                    PagerSnapHelperAdapter(mDataList, mRecyclerView!!.width, mRecyclerView!!.height)
                mRecyclerView!!.adapter = mMyadapter
            }
        })
    }
}