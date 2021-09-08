package com.lk.myproject.horizontalpage.view

import android.graphics.Rect
import android.util.Log
import android.util.SparseArray
import androidx.recyclerview.widget.RecyclerView

class HorizontalPageLayoutManager(rows: Int, columns: Int) : RecyclerView.LayoutManager(),
    PageDecorationLastJudge {
    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams? {
        return null
    }

    var totalHeight = 0
    var totalWidth = 0
    var offsetY = 0
    var offsetX = 0
    override fun canScrollHorizontally(): Boolean {
        return true
    }

    override fun scrollHorizontallyBy(
        dx: Int,
        recycler: RecyclerView.Recycler,
        state: RecyclerView.State
    ): Int {
        detachAndScrapAttachedViews(recycler)
        val newX = offsetX + dx
        var result = dx
        if (newX > totalWidth) {
            result = totalWidth - offsetX
        } else if (newX < 0) {
            result = 0 - offsetX
        }
        offsetX += result
        offsetChildrenHorizontal(-result)
        recycleAndFillItems(recycler, state)
        return result
    }

    private val allItemFrames = SparseArray<Rect>()
    private val usableWidth: Int
        private get() = width - paddingLeft - paddingRight
    private val usableHeight: Int
        private get() = height - paddingTop - paddingBottom
    var rows = 0
    var columns = 0
    var pageSize = 0
    var itemWidth = 0
    var itemHeight = 0
    var onePageSize = 0
    var itemWidthUsed = 0
    var itemHeightUsed = 0
    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        if (itemCount == 0) {
            removeAndRecycleAllViews(recycler)
            return
        }
        if (state.isPreLayout) {
            return
        }
        //获取每个Item的平均宽高
        itemWidth = usableWidth / columns
        itemHeight = usableHeight / rows

        //计算宽高已经使用的量，主要用于后期测量
        itemWidthUsed = (columns - 1) * itemWidth
        itemHeightUsed = (rows - 1) * itemHeight

        //计算总的页数

//        pageSize = state.getItemCount() / onePageSize + (state.getItemCount() % onePageSize == 0 ? 0 : 1);
        computePageSize(state)
        Log.i(
            "zzz",
            "itemCount=" + itemCount + " state itemCount=" + state.itemCount + " pageSize=" + pageSize
        )
        //计算可以横向滚动的最大值
        totalWidth = (pageSize - 1) * width

        //分离view
        detachAndScrapAttachedViews(recycler)
        val count = itemCount
        var p = 0
        while (p < pageSize) {
            var r = 0
            while (r < rows) {
                var c = 0
                while (c < columns) {
                    val index = p * onePageSize + r * columns + c
                    if (index == count) {
                        //跳出多重循环
                        c = columns
                        r = rows
                        p = pageSize
                        break
                    }
                    val view = recycler.getViewForPosition(index)
                    addView(view)
                    //测量item
                    measureChildWithMargins(view, itemWidthUsed, itemHeightUsed)
                    val width = getDecoratedMeasuredWidth(view)
                    val height = getDecoratedMeasuredHeight(view)
                    //记录显示范围
                    var rect = allItemFrames[index]
                    if (rect == null) {
                        rect = Rect()
                    }
                    val x = p * usableWidth + c * itemWidth
                    val y = r * itemHeight
                    rect[x, y, width + x] = height + y
                    allItemFrames.put(index, rect)
                    c++
                }
                r++
            }
            //每一页循环以后就回收一页的View用于下一页的使用
            removeAndRecycleAllViews(recycler)
            p++
        }
        recycleAndFillItems(recycler, state)
    }

    private fun computePageSize(state: RecyclerView.State) {
        pageSize = state.itemCount / onePageSize + if (state.itemCount % onePageSize == 0) 0 else 1
    }

    override fun onDetachedFromWindow(view: RecyclerView, recycler: RecyclerView.Recycler) {
        super.onDetachedFromWindow(view, recycler)
        offsetX = 0
        offsetY = 0
    }

    private fun recycleAndFillItems(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        if (state.isPreLayout) {
            return
        }
        val displayRect = Rect(
            paddingLeft + offsetX,
            paddingTop,
            width - paddingLeft - paddingRight + offsetX,
            height - paddingTop - paddingBottom
        )
        val childRect = Rect()
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            childRect.left = getDecoratedLeft(child!!)
            childRect.top = getDecoratedTop(child)
            childRect.right = getDecoratedRight(child)
            childRect.bottom = getDecoratedBottom(child)
            if (!Rect.intersects(displayRect, childRect)) {
                removeAndRecycleView(child, recycler)
            }
        }
        for (i in 0 until itemCount) {
            if (Rect.intersects(displayRect, allItemFrames[i])) {
                val view = recycler.getViewForPosition(i)
                addView(view)
                measureChildWithMargins(view, itemWidthUsed, itemHeightUsed)
                val rect = allItemFrames[i]
                layoutDecorated(
                    view,
                    rect.left - offsetX,
                    rect.top,
                    rect.right - offsetX,
                    rect.bottom
                )
            }
        }
    }

    override fun isLastRow(index: Int): Boolean {
        if (index >= 0 && index < itemCount) {
            var indexOfPage = index % onePageSize
            indexOfPage++
            if (indexOfPage > (rows - 1) * columns && indexOfPage <= onePageSize) {
                return true
            }
        }
        return false
    }

    override fun isLastColumn(position: Int): Boolean {
        var position = position
        if (position >= 0 && position < itemCount) {
            position++
            if (position % columns == 0) {
                return true
            }
        }
        return false
    }

    override fun isPageLast(position: Int): Boolean {
        var position = position
        position++
        return position % onePageSize == 0
    }

    override fun computeHorizontalScrollRange(state: RecyclerView.State): Int {
        computePageSize(state)
        return pageSize * width
    }

    override fun computeHorizontalScrollOffset(state: RecyclerView.State): Int {
        return offsetX
    }

    override fun computeHorizontalScrollExtent(state: RecyclerView.State): Int {
        return width
    }

    init {
        this.rows = rows
        this.columns = columns
        onePageSize = rows * columns
    }
}