package com.example.practice_p2papp.signinup

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.practice_p2papp.MainActivity
import com.example.practice_p2papp.abstracts.PermissionActivity
import com.example.practice_p2papp.databinding.ActivitySignInBinding
import com.example.practice_p2papp.extensions.circleCropImage
import com.example.practice_p2papp.viewmodel.FirebaseDBViewModel
import kotlinx.coroutines.*

// 세부정보 기입 후 관리
class SignInActivity : PermissionActivity() {

	private var storageUrlResult: String? = null

	private var captureImageUri: Uri? = null

	private lateinit var viewModel: FirebaseDBViewModel

	private lateinit var binding: ActivitySignInBinding
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		viewModel = ViewModelProvider(this)[FirebaseDBViewModel::class.java]
		binding = ActivitySignInBinding.inflate(layoutInflater)
		setContentView(binding.root)

		setSignUpButtonListener()
		setProfileImage()
	}


	private fun setProfileImage() = with(binding) {
		profileImageView.setOnClickListener {
			showChooseCameraOrGalleryPopup()
		}
	}

	private fun openGallery() {
		val intent = Intent(Intent.ACTION_PICK)
		intent.type = "image/*"
		startActivityForResult(intent, GALLERY_RESULT_CODE)
	}

	private fun openCamera() {
		val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
		createImageUri(viewModel.newFileName(), "image/*").let { uri ->
			captureImageUri = uri
			intent.putExtra(MediaStore.EXTRA_OUTPUT, captureImageUri)
			startActivityForResult(intent, CAMERA_RESULT_CODE)
		}
	}

	// 촬영한 이미지 Uri를 MediaStore에 생성. contentResolver를 반환하는데 EXTERNAL_CONTENT_URI, 파일명과 확장자를 담은 value를 반환한다.
	private fun createImageUri(fileName: String, mimeType: String): Uri? {
		val displayName = MediaStore.Images.Media.DISPLAY_NAME
		val mmimeType = MediaStore.Images.Media.MIME_TYPE
		val insert = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
		val value = ContentValues()

		value.put(
			displayName,
			fileName
		)
		value.put(
			mmimeType,
			mimeType
		)

		return contentResolver.insert(insert, value)
	}


	// 이미지 뷰에 뿌리고 Storage, DB에 저장
	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)

		if (resultCode != Activity.RESULT_OK) {
			Toast.makeText(this, "이미지를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
			return
		}
		when (requestCode) {

			GALLERY_RESULT_CODE -> {

				data?.let { intent ->
					val notNullData = intent.data != null

					when {
						notNullData -> {
							lifecycleScope.launch {
								showProgress()
								storageUrlResult = viewModel.imageStorageUpload(intent.data!!)
								binding.profileImageView.circleCropImage(storageUrlResult!!)
								hideProgress()
							}
						}
						else -> {
						}
					}
				}

			}
			CAMERA_RESULT_CODE -> {

				captureImageUri?.let { uri ->
					lifecycleScope.launch {
						showProgress()
						storageUrlResult = viewModel.imageStorageUpload(uri)
						binding.profileImageView.circleCropImage(storageUrlResult!!)
						hideProgress()
					}
					captureImageUri = null
				}
			}
		}


	}


	override fun permissionGranted(requestCode: Int) {
		when (requestCode) {
			GALLERY_REQUEST_CODE -> {
				openGallery()
			}
			CAMERA_REQUEST_CODE -> {
				openCamera()
			}
		}
	}

	override fun permissionDenied(requestCode: Int) {
		when (requestCode) {
			GALLERY_REQUEST_CODE, CAMERA_REQUEST_CODE -> {
				Toast.makeText(this, "권한 거부됨", Toast.LENGTH_SHORT).show()
				return
			}
		}
	}


	private fun showChooseCameraOrGalleryPopup() {
		AlertDialog.Builder(this)
			.setTitle("사진 등록")
			.setMessage("카메라와 갤러리 중 하나를 택해주세요.")
			.setPositiveButton("카메라") { _, _ ->
				requirePermissions(CAMERA_REQUEST_CODE)
			}
			.setNegativeButton("갤러리") { _, _ ->
				requirePermissions(GALLERY_REQUEST_CODE)
			}
			.create().show()
	}

	private fun showProgress() {
		binding.progressBar.isVisible = true
	}

	private fun hideProgress() {
		binding.progressBar.isVisible = false
	}

	// DB에 UserInfo child 만들고 저장
	private fun setSignUpButtonListener() = with(binding) {
		signUpButton.setOnClickListener {

			// 예외처리
			if (viewModel.auth.currentUser == null) { // 회원 목록에 없다면
				Toast.makeText(
					this@SignInActivity,
					"회원가입 정보가 저장되지 않았습니다. 다시 가입해주세요",
					Toast.LENGTH_SHORT
				).show()
				return@setOnClickListener

			} else { // 회원 목록엔 있지만 nickName 적지 않았다면

				if(nickNameEditText.text.isEmpty()){
					nickNameEmptyCheck.isVisible = true
					return@setOnClickListener
				}

				val nickName = nickNameEditText.text.toString()
				val userId = viewModel.auth.currentUser!!.uid

				lifecycleScope.launch {
					if (storageUrlResult == null) {
						binding.imageViewEmptyCheck.isVisible = true
						return@launch
					}
					viewModel.uploadUserInfo(
						userId = userId,
						nickName = nickName,
						imageUrl = storageUrlResult!!
					)
					startMainActivity()
					nickNameEmptyCheck.isVisible = false
					binding.imageViewEmptyCheck.isVisible = false
				}

			}

		}

	}


	private fun startMainActivity() {
		startActivity(Intent(this, MainActivity::class.java))
		finish()
	}
}