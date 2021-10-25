package com.example.project_brooklyn.articlelist

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.project_brooklyn.abstracts.PermissionActivity
import com.example.project_brooklyn.adapter.PhotoAdapter
import com.example.project_brooklyn.databinding.ActivityAddArticleBinding
import com.example.project_brooklyn.viewmodel.FirebaseDBViewModel
import com.example.project_brooklyn.viewmodel.factory.FirebaseViewModelFactory
import com.example.project_brooklyn.viewmodel.repository.DBRepository
import com.google.firebase.database.*
import kotlinx.coroutines.*

class AddArticleActivity : PermissionActivity() {

	private var captureImageUri: Uri? = null

	private val imageUriList = mutableListOf<Uri>()

	private var userNickName: String? = null
	private var userProfileImage: String? = null

	private lateinit var photoAdapter: PhotoAdapter

	private val appRepository = DBRepository()

	private val firebaseDBViewModel by viewModels<FirebaseDBViewModel> {
		FirebaseViewModelFactory(
			appRepository
		)
	}
	private lateinit var binding: ActivityAddArticleBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityAddArticleBinding.inflate(layoutInflater)
		setContentView(binding.root)

		// userDB에서 nickName 가져온다.
		userInfoObserve()
		initAdapterAndOnClickListener()
		initRecyclerView()
		setSubmitButtonListener()
		setImageAddButtonListener()
	}

	// UserInfo Read
	private fun userInfoObserve() {
		firebaseDBViewModel.userInfoLiveData
			.observe(
				this,
				Observer {
					it.map { userItem ->
						if (firebaseDBViewModel.auth.currentUser!!.uid == userItem.userId) {
							userProfileImage = userItem.imageUrl
							userNickName = userItem.nickName
						}


					}
				}
			)
	}


	private fun initAdapterAndOnClickListener() {
		photoAdapter = PhotoAdapter(removePhotoListener = { uri ->
			removePhoto(uri)
		})
	}


	private fun initRecyclerView() = with(binding) {

		photoRecyclerView.run {
			adapter = photoAdapter
		}

	}

	// DB에 항목 업로드, userId, nickName, title, content, price, date, imageUriList
	private fun setSubmitButtonListener() = with(binding) {
		submitButton.setOnClickListener {
			if (firebaseDBViewModel.auth.currentUser == null) {
				return@setOnClickListener
			}

			val userId = firebaseDBViewModel.auth.currentUser?.uid.toString()
			val nickName = userNickName!!
			val title = titleEditText.text.toString()
			val content = contentEditText.text.toString()
			val price = priceEditText.text.toString()
			val date = System.currentTimeMillis()
			val userProfileImage = userProfileImage!!

			showProgress()

			// imageUrlList가 있을 떄와 없을떄로 나누어짐
			if (imageUriList.isNotEmpty()) {
				lifecycleScope.launch {
					val results = firebaseDBViewModel.articleImageStorageUpload(imageUriList)
					firebaseDBViewModel.uploadArticlesInDB(
						userId = userId,
						nickName = nickName,
						title = title,
						content = content,
						price = "$price 원",
						date = date,
						imageUriList = results.filterIsInstance<String>(),
						userProfileImage = userProfileImage
					)
					hideProgress()
					finish()
				}

			} else {
				lifecycleScope.launch {
					firebaseDBViewModel.uploadArticlesInDB(
						userId = userId,
						nickName = nickName,
						title = title,
						content = content,
						price = "$price 원",
						date = date,
						imageUriList = listOf(),
						userProfileImage = userProfileImage
					)
					hideProgress()
					finish()
				}

			}

		}

	}


	private fun removePhoto(uri: Uri) {
		imageUriList.remove(uri)
		photoAdapter.setPhotoList(imageUriList)
	}

	// ---아래로 퍼미션 및 이미지 업로드 관련---

	private fun setImageAddButtonListener() = with(binding) {
		imageAddButton.setOnClickListener {
			showChooseCameraOrGalleryPopup()
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


	private fun openGallery() {
		val intent = Intent(Intent.ACTION_PICK)
		intent.type = "image/*"
		intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
		startActivityForResult(intent, GALLERY_RESULT_CODE)
	}

	private fun openCamera() {
		val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
		createImageUri(firebaseDBViewModel.newFileName(), "image/*").let { uri ->
			captureImageUri = uri
			intent.putExtra(MediaStore.EXTRA_OUTPUT, captureImageUri)
			startActivityForResult(intent, CAMERA_RESULT_CODE)
			Log.d("testtest", "$CAMERA_RESULT_CODE 3")
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


	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)

		val uriList = arrayListOf<Uri>()

		if (resultCode != Activity.RESULT_OK) {
			Toast.makeText(this, "이미지를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
			return
		}
		when (requestCode) {

			GALLERY_RESULT_CODE -> {

				data?.let { intent ->
					firebaseDBViewModel.galleryResult(intent = intent, uriList = uriList)

					imageUriList.addAll(uriList)
					photoAdapter.setPhotoList(imageUriList)
				}

			}
			CAMERA_RESULT_CODE -> {

				captureImageUri?.let { uri ->
					firebaseDBViewModel.cameraResult(uriList = uriList, uri = uri)
					captureImageUri = null
				}
				imageUriList.addAll(uriList)
				photoAdapter.setPhotoList(imageUriList)
			}
		}


	}


	// 권한 승인 받았을 때 이벤트
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


	// 권한 승인 받지 못했을 때 이벤트
	override fun permissionDenied(requestCode: Int) {
		when (requestCode) {
			GALLERY_REQUEST_CODE, CAMERA_REQUEST_CODE -> {
				Toast.makeText(this, "권한 거부됨", Toast.LENGTH_SHORT).show()
				return
			}
		}
	}

	private fun showProgress() {
		binding.progressBar.isVisible = true
	}

	private fun hideProgress() {
		binding.progressBar.isVisible = false
	}


}