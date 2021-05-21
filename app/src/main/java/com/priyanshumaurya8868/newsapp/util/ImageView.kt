package com.priyanshumaurya8868.newsapp.util

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy



fun ImageView.loadImage(uri: String ) {
    val  iv = this
    Glide.with(iv)
        .load(uri).apply {
            preload()
            downsample(DownsampleStrategy.AT_LEAST)
                into(iv)
        }
}
