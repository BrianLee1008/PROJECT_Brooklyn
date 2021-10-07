package com.example.practice_p2papp.abstracts

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

// 퍼미션 전용 추상클래스
abstract class PermissionActivity : AppCompatActivity() {


	companion object {
		const val GALLERY_REQUEST_CODE = 1001
		const val GALLERY_RESULT_CODE = 1002
		const val GALLERY_CODE = 1003
		const val CAMERA_REQUEST_CODE = 2001
		const val CAMERA_RESULT_CODE = 2002
		const val CAMERA_CODE = 2003
	}

	abstract fun permissionGranted(requestCode: Int)
	abstract fun permissionDenied(requestCode: Int)


	fun requirePermissions(requestCode: Int) {

			val gallery = Manifest.permission.READ_EXTERNAL_STORAGE
			val camera = Manifest.permission.CAMERA


		when (requestCode) {
			GALLERY_REQUEST_CODE -> {
				if (ContextCompat.checkSelfPermission(
						this,
						gallery
					) == PackageManager.PERMISSION_GRANTED
				) {
					permissionGranted(GALLERY_REQUEST_CODE)
				} else {
					requestPermissions(
						arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
						GALLERY_CODE
					)
				}
			}
			CAMERA_REQUEST_CODE -> {
				if (ContextCompat.checkSelfPermission(
						this,
						camera
					) == PackageManager.PERMISSION_GRANTED
				) {
					permissionGranted(CAMERA_REQUEST_CODE)
					Log.d("testtest","$CAMERA_RESULT_CODE 1")
				} else {
					requestPermissions(
						arrayOf(Manifest.permission.CAMERA),
						CAMERA_CODE
					)
					Log.d("testtest","$CAMERA_RESULT_CODE 2")
				}
			}
			else -> {
				//
			}
		}
	}

	override fun onRequestPermissionsResult(
		requestCode: Int,
		permissions: Array<out String>,
		grantResults: IntArray
	) {
		when (requestCode) {
			GALLERY_CODE -> {
				if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					permissionGranted(GALLERY_REQUEST_CODE)
				} else {
					Toast.makeText(this, "권한 거부됨", Toast.LENGTH_SHORT).show()
				}
			}
			CAMERA_CODE -> {
				if (grantResults.isNotEmpty()) {
					permissionGranted(CAMERA_REQUEST_CODE)
				} else {
					Toast.makeText(this, "권한 거부됨", Toast.LENGTH_SHORT).show()
				}
			}
		}
		super.onRequestPermissionsResult(requestCode, permissions, grantResults)
	}




}