package com.lk.myproject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.lk.myproject.Bean.VideoHeartGuardItem
import com.lk.myproject.R

class MyViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    var headerView: ImageView? = null
    var championView: ImageView? = null

    init {
        headerView = v.findViewById(R.id.headView)
        championView = v.findViewById(R.id.ivChampion)
    }
}

class HeartGuardAdapter(var list: MutableList<VideoHeartGuardItem>) : RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var v = LayoutInflater.from(parent.context).inflate(R.layout.layout_room_video_heart_guard_item,
            parent, false)
        return MyViewHolder(v)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(viewHolder: MyViewHolder, position: Int) {
        viewHolder.headerView?.apply {
            setImageResource(list[position].anchorId)
        }
    }
}