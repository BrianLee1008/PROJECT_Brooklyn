package com.example.fastcampus_advanced_cameraapp.extensions

import android.content.res.Resources

// Dp를 Px로 변환할 수 있는 확장함수

internal fun Float.fromDpToPx() : Int {
	return (this * Resources.getSystem().displayMetrics.density).toInt()
}