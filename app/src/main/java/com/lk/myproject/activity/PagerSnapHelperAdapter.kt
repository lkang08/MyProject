package com.lk.myproject.activity

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lk.myproject.R
import java.util.ArrayList

class PagerSnapHelperAdapter//     //
    (// 数据集
    private val mDataList: ArrayList<String>, private val mWidth: Int, private val mHeight: Int
) : RecyclerView.Adapter<PagerSnapHelperAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ViewHolder {
        Log.e("xiaxl: ", "---onCreateViewHolder---")
        // 创建一个View，简单起见直接使用系统提供的布局，就是一个TextView
        val view = View.inflate(
            viewGroup.context,
            R.layout.recycle_pager_item,
            null
        )
        val contentView =
            view.findViewById<View>(R.id.add_btn)
        val rl =
            contentView.layoutParams as RelativeLayout.LayoutParams
        rl.width = mWidth
        rl.height = mHeight
        contentView.layoutParams = rl
        // 创建一个ViewHolder
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        Log.e("xiaxl: ", "---onBindViewHolder---")
        // 绑定数据到ViewHolder上
        viewHolder.itemView.tag = position
        //
        viewHolder.mTextView.text = "$position item"
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    /**
     *
     */
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mTextView: TextView

        init {
            mTextView = itemView.findViewById<View>(R.id.add_btn) as TextView
        }
    }
}