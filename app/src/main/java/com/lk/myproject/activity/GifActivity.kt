package com.lk.myproject.activity

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.lk.myproject.R
import com.qintong.library.InsLoadingView
import kotlinx.android.synthetic.main.activity_gif.*
import kotlinx.android.synthetic.main.activity_gif_item.view.*

class GifActivity : Activity() {
    var iconList = mutableListOf<String>()
    var dataList = mutableListOf<Item>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gif)
        loadGif()
        init()
        button.setOnClickListener {
            var adapter = MyAdapter(this)
            adapter.dataList = dataList
            recyclerView.layoutManager = LinearLayoutManager(this@GifActivity)
            recyclerView.adapter = adapter
            recyclerView.visibility = if (recyclerView.visibility != View.VISIBLE) View.VISIBLE else View.GONE
        }
        changBtn.setOnClickListener {
            when (index % 3) {
                0 -> loading_view.status = InsLoadingView.Status.UNCLICKED
                1 -> loading_view.status = InsLoadingView.Status.CLICKED
                2 -> loading_view.status = InsLoadingView.Status.LOADING
            }
            index++
        }
    }

    var index: Int = 0

    private fun init() {
        initIconList()
        for (i in 0..40) {
            dataList.add(Item(name = "icon $i", icon = iconList[i % iconList.size], description = "description ${iconList[i % iconList.size]}"))
        }
    }

    private fun initIconList() {
        iconList.add("http://img.soogif.com/5D4bWERvgwvi95peLnA1pIzplnjObBQK.gif")
        iconList.add("http://img.soogif.com/ADVfaaMIVqmbC2aMAHLQk5aXChNZboC2.gif")
        iconList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1556796219151&di=b0cc84494caa5604c6822ae6785d4faa&imgtype=0&src=http%3A%2F%2Fimg.mp.sohu.com%2Fupload%2F20170812%2F924b1c4bff7e4233bc9163619434112a.png")
        iconList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1556796445366&di=bc8cf4a2402be7881300afa99882be7c&imgtype=0&src=http%3A%2F%2Fphotocdn.sohu.com%2F20151024%2Fmp37611207_1445695010757_3.gif")
        iconList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1556800136569&di=7582818990584ea7c618368537fafe9f&imgtype=0&src=http%3A%2F%2Fimg.mp.itc.cn%2Fupload%2F20170301%2F0a4e4ca1480e460ca082224503539342_th.jpg")
        iconList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1556796445363&di=ab884ceabacd4784b6f2eb59f1bfe5f9&imgtype=0&src=http%3A%2F%2Fww1.sinaimg.cn%2Fmw690%2F6adc108fjw1eocnvdf490g208n04v1kx.gif")
        iconList.add("http://img4.duitang.com/uploads/item/201511/23/20151123194252_BkdQJ.thumb.700_0.jpeg")
        iconList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1556800256518&di=1283ea706435b7d0c63c8a5e3733eb47&imgtype=0&src=http%3A%2F%2Fwanzao2.b0.upaiyun.com%2F1483646575079f2a3005f25908e580b55a91f-0.gif")
        iconList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1556796445362&di=31ba987163a58b4a8279aad00261cdd7&imgtype=0&src=http%3A%2F%2Fs9.sinaimg.cn%2Fmw690%2F001l2tbnty6HRTpAE1a38%26690")
        iconList.add(("http://b-ssl.duitang.com/uploads/item/201701/17/20170117110536_jCxYS.jpeg"))
        iconList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1556796445361&di=ae8b23a74997cb0b109fe1ff4985a967&imgtype=0&src=http%3A%2F%2Fimg.mp.itc.cn%2Fupload%2F20160912%2F89201addb1b8442398f715724c44f8c8.gif")
        iconList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1556796445360&di=ad63813aca355fbaf87dad87b21394a0&imgtype=0&src=http%3A%2F%2Ft1.qpic.cn%2Fmblogpic%2F159cdfb0ed979f038c62%2F460")
        iconList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1556800256518&di=5027d688f7fd8586cdc331df0cb686fe&imgtype=0&src=http%3A%2F%2Fdingyue.nosdn.127.net%2FLzKzQRk0dmgm9oGJw1Wz1DWEs2UwmTgtAfDFB%3Dv1AQCjN1538771665875.jpeg")
        iconList.add("http://b-ssl.duitang.com/uploads/item/201610/19/20161019141713_cunTt.thumb.700_0.png")
        iconList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1556796445360&di=c17afba76ad80c66af3053f9fa2f51ff&imgtype=0&src=http%3A%2F%2Fp2.pstatp.com%2Flarge%2F2f4000881da1be2c754")
        iconList.add("http://photocdn.sohu.com/20150928/mp33597295_1443408488193_8.jpeg")
        iconList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1556796445360&di=58a79061320292e906b3cfccd512dfac&imgtype=0&src=http%3A%2F%2F5b0988e595225.cdn.sohucs.com%2Fimages%2F20171221%2F26aedd64fe46418cb1d0dbb618f42d73.gif")
        iconList.add("http://s2.sinaimg.cn/mw690/005Qj6grgy6Wr1FjBtf41&690")
        iconList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1556800256519&di=8e7601f13d59b702355a4237610a1658&imgtype=0&src=http%3A%2F%2Fhbimg.b0.upaiyun.com%2Fca3246479b371619a4aed515ed66ac29ec30404ab0f4-fLpf1D_fw658")
    }

    private fun loadGif() {
        Glide.with(this).load(R.drawable.pink).into(loading_view)
        Glide.with(this).load(R.drawable.gif_emoji_3).into(gif)
        Glide.with(this).load(Uri.parse("http://img.soogif.com/5D4bWERvgwvi95peLnA1pIzplnjObBQK.gif")).diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.gif4).circleCrop().into(gif2)
        Glide.with(this).load(Uri.parse("http://img.soogif.com/ADVfaaMIVqmbC2aMAHLQk5aXChNZboC2.gif"))
                .override(100).diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.gif0).circleCrop().into(gifImageView)
        Glide.with(this).load(R.drawable.demo).circleCrop().into(iv_gif)
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var nameTv: TextView = view.itemNameTv
        var descTv: TextView = view.itemDescTv
        var iconIv: ImageView = view.itemIv
    }

    data class Item(var name: String = "", var icon: String = "", var description: String = "")
    class MyAdapter(var activity: GifActivity? = null) : Adapter<MyViewHolder>() {
        var dataList: List<Item>? = null
        override fun onCreateViewHolder(parent: ViewGroup, pos: Int): MyViewHolder {
            var view = LayoutInflater.from(parent.context).inflate(R.layout.activity_gif_item, parent, false)
            return MyViewHolder(view)
        }

        override fun getItemCount(): Int {
            return dataList?.size ?: 0
        }

        override fun onBindViewHolder(parent: MyViewHolder, p1: Int) {
            if (dataList == null || dataList!!.isEmpty()) {
                return
            }
            var item = dataList!![p1]
            item.apply {
                parent.apply {
                    nameTv.text = name
                    descTv.text = description
                    Glide.with(activity!!).load(Uri.parse(icon)).diskCacheStrategy(DiskCacheStrategy.NONE).circleCrop().placeholder(R.drawable.gif_01).into(iconIv)
                }
            }
        }

    }
}