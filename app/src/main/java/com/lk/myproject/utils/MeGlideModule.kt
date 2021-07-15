package com.lk.myproject.utils

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.module.AppGlideModule

@GlideModule
class MeGlideModule : AppGlideModule() {
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        super.applyOptions(context, builder)
        var memoryCacheSizeBytes = 1024 * 1024 * 2L
        //builder.setMemoryCache(LruResourceCache(memoryCacheSizeBytes))
        builder.setBitmapPool(LruBitmapPool(memoryCacheSizeBytes))
        log("applyOptions ..")
    }
}