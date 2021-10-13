package com.example.practice_p2papp.extensions

import android.annotation.SuppressLint
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.example.fastcampus_advanced_cameraapp.extensions.fromDpToPx
import com.example.practice_p2papp.R


private val factory = DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()



fun ImageView.clear() = Glide.with(context).clear(this)

@SuppressLint("CheckResult")
fun ImageView.loadThumbnailImage(url: List<String>, corner: Float = 0f) {
	Glide.with(this)
		.load(url.first())
		.transition(DrawableTransitionOptions.withCrossFade(factory))
		.diskCacheStrategy(DiskCacheStrategy.ALL).apply {
			if (corner > 0) transform(CenterCrop(), RoundedCorners(corner.fromDpToPx()))
		}
		.error(R.drawable.error)
		.into(this)
}

@SuppressLint("CheckResult")
fun ImageView.loadCenterCrop(url: String, corner: Float = 0f) {
	Glide.with(this)
		.load(url)
		.transition(DrawableTransitionOptions.withCrossFade(factory))
		.diskCacheStrategy(DiskCacheStrategy.ALL).apply {
			if (corner > 0) transform(CenterCrop(), RoundedCorners(corner.fromDpToPx()))
		}
		.error(R.drawable.error)
		.into(this)
}

fun ImageView.circleCropImage(url: String) {
 	Glide.with(this)
		.load(url)
		.circleCrop()
		.into(this)
}

fun ImageView.loadImage(url: String) {
	Glide.with(this)
		.load(url)
		.error(R.drawable.error)
		.into(this)
}