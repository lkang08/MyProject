package com.lk.myproject.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lk.myproject.R
import com.lk.myproject.ext.log
import com.lk.myproject.itemanimation.SlideItemAnimator
import com.lk.myproject.robust.PatchManipulateImp
import com.lk.myproject.robust.PermissionUtils
import com.lk.myproject.robust.RobustCallBackSample
import com.lk.myproject.toast.ToastUtils
import com.meituan.robust.PatchExecutor
import com.meituan.robust.patch.RobustModify
import kotlinx.android.synthetic.main.activity_recyclerview_anim.*

class RecyclerViewAnimActivity : BaseActivity() {
    var index = 1001
    var dataList = mutableListOf<String>(
        "注释什么很清晰，通过注释就可以明白每个方法的作用了",
        "现在看下使用方法，只需要继承BaseItemAnimator，然后实现对应的抽象方法即可",
        "这里需要注意的地方是不能为动画设置自己想要的动画时间",
        "现在看下使用方法，只需要继承BaseItemAnimator，然后实现对应的抽象方法即可"
    )
    var oldStr = "注释什么很清晰，通过注释就可以明白每个方法的作用了"
    var newStr = "现在看下使用方法，只需要继承BaseItemAnimator，然后实现对应的抽象方法即可，然后实现对应的抽象方法即可"
    var adapter = MyAdapter(dataList)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recyclerview_anim)
        sureUpdate.setOnClickListener {
            var lastId = dataList.size - 1
            dataList[lastId] = if (index++ % 2 == 0) oldStr else newStr
            adapter.notifyItemChanged(lastId)
        }
        sureAdd.setOnClickListener {
            dataList.add(0, newStr + index++)
            adapter.notifyItemInserted(0)
        }
        sureDel.setOnClickListener {
            RobustModify.modify()
            ToastUtils.showToast(this, "####modify hello world", Toast.LENGTH_SHORT)
            Log.d("RobustCallBack", "###modify hello world")
            if (dataList.size > 2) {
                dataList.removeAt(0)
                adapter.notifyItemRemoved(0)
            }
        }
        sureNotify.setOnClickListener {
            runRobust()
            adapter.notifyDataSetChanged()
        }

        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter
        recyclerView.itemAnimator = SlideItemAnimator()
    }

    private fun runRobust() {
        if (PermissionUtils.isGrantSDCardReadPermission(this)) {
            PatchExecutor(applicationContext, PatchManipulateImp(), RobustCallBackSample()).start()
        } else {
            PermissionUtils.requestSDCardReadPermission(this, 1000)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000) {
            Log.d(
                "RobustCallBack", "has permission:" + PermissionUtils
                    .isGrantSDCardReadPermission(this)
            )
        }
    }
}

class MyAdapter(var list: MutableList<String>) : RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var v = LayoutInflater.from(parent.context).inflate(
            R.layout.layout_test_item,
            parent, false
        )
        return MyViewHolder(v)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(viewHolder: MyViewHolder, position: Int) {
        viewHolder.textView?.text = list[position]
    }
}

class MyViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    var headerView: ImageView? = null
    var textView: TextView? = null

    init {
        headerView = v.findViewById(R.id.headView)
        textView = v.findViewById(R.id.tvText)
    }
}