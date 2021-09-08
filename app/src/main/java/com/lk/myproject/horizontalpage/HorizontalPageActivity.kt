package com.lk.myproject.horizontalpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lk.myproject.R
import com.lk.myproject.activity.BaseActivity
import com.lk.myproject.horizontalpage.view.DividerItemDecoration
import com.lk.myproject.horizontalpage.view.HorizontalPageLayoutManager
import com.lk.myproject.horizontalpage.view.PagingItemDecoration
import com.lk.myproject.horizontalpage.view.PagingScrollHelper
import com.lk.myproject.horizontalpage.view.PagingScrollHelper.onPageChangeListener
import java.util.ArrayList
import java.util.Random

class HorizontalPageActivity : BaseActivity(), onPageChangeListener, View.OnClickListener {
    var recyclerView: RecyclerView? = null
    var myAdapter: MyAdapter? = null
    var tv_title: TextView? = null
    var scrollHelper = PagingScrollHelper()
    var rg_layout: RadioGroup? = null
    var btnUpdate: Button? = null
    var tv_page_total: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_horizontal_page)
        init()
        rg_layout = findViewById<View>(R.id.rg_layout) as RadioGroup
        rg_layout!!.setOnCheckedChangeListener { group, checkedId -> switchLayout(checkedId) }
        tv_title = findViewById<View>(R.id.tv_title) as TextView
        tv_page_total = findViewById<View>(R.id.tv_page_total) as TextView
        btnUpdate = findViewById<View>(R.id.btn_update) as Button
        btnUpdate!!.setOnClickListener(this)
        myAdapter = MyAdapter()
        recyclerView = findViewById(R.id.recyclerview)
        recyclerView!!.adapter = myAdapter
        scrollHelper.setUpRecycleView(recyclerView)
        scrollHelper.setOnPageChangeListener(this)
        switchLayout(R.id.rb_horizontal_page)
        recyclerView!!.isHorizontalScrollBarEnabled = true
        //获取总页数,采用这种方法才能获得正确的页数。否则会因为RecyclerView.State 缓存问题，页数不正确。
        recyclerView!!.post { tv_page_total!!.text = "共" + scrollHelper.pageCount + "页" }
    }

    private var lastItemDecoration: RecyclerView.ItemDecoration? = null
    private var horizontalPageLayoutManager: HorizontalPageLayoutManager? = null
    private var hLinearLayoutManager: LinearLayoutManager? = null
    private var vLinearLayoutManager: LinearLayoutManager? = null
    private var hDividerItemDecoration: DividerItemDecoration? = null
    private var vDividerItemDecoration: DividerItemDecoration? = null
    private var pagingItemDecoration: PagingItemDecoration? = null
    private fun init() {
        hLinearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        hDividerItemDecoration = DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL)
        vDividerItemDecoration = DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        vLinearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        horizontalPageLayoutManager = HorizontalPageLayoutManager(2, 5)
        pagingItemDecoration = PagingItemDecoration(this, horizontalPageLayoutManager)
    }

    private fun switchLayout(checkedId: Int) {
        var layoutManager: RecyclerView.LayoutManager? = null
        var itemDecoration: RecyclerView.ItemDecoration? = null
        when (checkedId) {
            R.id.rb_horizontal_page -> {
                layoutManager = horizontalPageLayoutManager
                itemDecoration = pagingItemDecoration
            }
            R.id.rb_vertical_page -> {
                layoutManager = vLinearLayoutManager
                itemDecoration = vDividerItemDecoration
            }
            R.id.rb_vertical_page2 -> {
                layoutManager = hLinearLayoutManager
                itemDecoration = hDividerItemDecoration
            }
        }
        if (layoutManager != null) {
            recyclerView!!.layoutManager = layoutManager
            if (lastItemDecoration != null) {
                recyclerView!!.removeItemDecoration(lastItemDecoration!!)
            }
            recyclerView!!.addItemDecoration(itemDecoration!!)
            scrollHelper.updateLayoutManger()
            scrollHelper.scrollToPosition(0)
            lastItemDecoration = itemDecoration
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPageChange(index: Int) {
        tv_title!!.text = "第" + (index + 1) + "页"
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_update -> updateData()
        }
    }

    private fun updateData() {
        myAdapter!!.updateData()
        myAdapter!!.notifyDataSetChanged()
        //滚动到第一页
        scrollHelper.scrollToPosition(0)
        recyclerView!!.post { tv_page_total!!.text = "共" + scrollHelper.pageCount + "页" }
    }

    inner class MyAdapter : RecyclerView.Adapter<MyViewHolder>() {
        private val random = Random()
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(R.layout.layout_item, parent, false)
            return MyViewHolder(view)
        }

        private fun setData() {
            val size = random.nextInt(70)
            for (i in 1..size) {
                data.add("$data_name-$i")
            }
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val title = data[position]
            holder.tv_title.text = title
            holder.itemView.setOnClickListener { v ->
                Toast.makeText(
                    v.context,
                    "item$title 被点击了",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        override fun getItemCount(): Int {
            return data.size
        }

        fun updateData() {
            data_name++
            data.clear()
            setData()
        }

        init {
            setData()
        }
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv_title: TextView

        init {
            tv_title = itemView.findViewById<View>(R.id.tv_title) as TextView
        }
    }

    companion object {
        private val data: MutableList<String> = ArrayList()
        private var data_name = 0
    }
}