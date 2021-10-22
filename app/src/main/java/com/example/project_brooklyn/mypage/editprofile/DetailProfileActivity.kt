package com.example.project_brooklyn.mypage.editprofile

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.example.project_brooklyn.FirebaseKey.Companion.DB_USER_INFO
import com.example.project_brooklyn.StartActivity
import com.example.project_brooklyn.abstracts.PermissionActivity
import com.example.project_brooklyn.databinding.ActivityDetailProfileBinding
import com.example.project_brooklyn.extensions.circleCropImage
import com.example.project_brooklyn.item.UserItem
import com.example.project_brooklyn.viewmodel.FirebaseDBViewModel
import com.example.project_brooklyn.viewmodel.factory.FirebaseViewModelFactory
import com.example.project_brooklyn.viewmodel.repository.DBRepository
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*

// 프로필 수정 및 로그아웃 Activity
class DetailProfileActivity : PermissionActivity() {

	private var editImageUrl: String? = null
	private var loadListenerImage: String? = null
	private var captureImageUri: Uri? = null


	private lateinit var binding: ActivityDetailProfileBinding
	private val appRepository = DBRepository()

	private val firebaseDBViewModel by viewModels<FirebaseDBViewModel> {
		FirebaseViewModelFactory(
			appRepository
		)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		binding = ActivityDetailProfileBinding.inflate(layoutInflater)
		super.onCreate(savedInstanceState)
		setContentView(binding.root)

		binding.nickNameTextView.isVisible = true

		userInfoObserve()
		setSignOutButtonListener()
		initNickName()
		editNickName()
		setProfileImage()

	}

	// UserInfo Read
	private fun userInfoObserve() {
		firebaseDBViewModel.userInfoLiveData
			.observe(
				this,
				{
					it.forEach { userItem ->
						// 특정 child 값 받아오는 로직 필요
						if (firebaseDBViewModel.auth.currentUser!!.uid == userItem.userId) {
							binding.nickNameTextView.text = userItem.nickName
							binding.profileImageView.circleCropImage(userItem.imageUrl!!)
							loadListenerImage = userItem.imageUrl
						}
					}
				}
			)
	}


	private fun setSignOutButtonListener() = with(binding) {
		signOutButton.setOnClickListener {
			if (firebaseDBViewModel.auth.currentUser == null) {
				Toast.makeText(this@DetailProfileActivity, "로그인을 다시 해주세요.", Toast.LENGTH_SHORT)
					.show()
				return@setOnClickListener
			}
			firebaseDBViewModel.auth.signOut()
			Toast.makeText(this@DetailProfileActivity, "로그아웃 했습니다", Toast.LENGTH_SHORT).show()
			startActivity(Intent(this@DetailProfileActivity, StartActivity::class.java))
			//모든 Acivity 경로 지우기
			ActivityCompat.finishAffinity(this@DetailProfileActivity)
		}
	}


	private fun initNickName() = with(binding) {
		nickNameContainer.setOnClickListener {
			nickNameTextView.isVisible = false
			nickNameEditText.setText(nickNameTextView.text)
			nickNameEditText.isVisible = true
			submitButton.isVisible = true
		}

	}

	private fun editNickName() = with(binding) {
		submitButton.setOnClickListener {
			if (firebaseDBViewModel.auth.currentUser == null) {
				return@setOnClickListener
			}

			if (nickNameEditText.text.isEmpty()) {
				Toast.makeText(
					this@DetailProfileActivity,
					"닉네임은 2글자 이상으로 작성해주셔야 해요.",
					Toast.LENGTH_SHORT
				).show()
				return@setOnClickListener
			}

			// 이미지 변경이 있으면 변경된 이미지 저장. 없으면 원래 불러왔던 이미지 저장
			if (editImageUrl != null) {
				editProfileAndUpdateDB(
					userId = firebaseDBViewModel.auth.currentUser!!.uid,
					nickName = nickNameEditText.text.toString(),
					imageUrl = editImageUrl
				)
				editImageUrl = null
			} else {
				editProfileAndUpdateDB(
					userId = firebaseDBViewModel.auth.currentUser!!.uid,
					nickName = nickNameEditText.text.toString(),
					imageUrl = loadListenerImage
				)
				loadListenerImage = null
			}
			nickNameTextView.isVisible = true
			nickNameEditText.isVisible = false
			submitButton.isVisible = false
			nickNameTextView.text = nickNameEditText.text.toString()
		}

	}


	// 프로필 업데이트 후 데이터 교체
	private fun editProfileAndUpdateDB(userId: String, nickName: String, imageUrl: String?) {
		val key = firebaseDBViewModel.userDB.child(userId).key
		if (key == null) {
			Toast.makeText(this, "수정에 실패 했습니다.", Toast.LENGTH_SHORT).show()
			return
		}

		val profile = UserItem(userId = userId, nickName = nickName, imageUrl = imageUrl)
		val profileValue = profile.toMap()

		val childUpdates = hashMapOf<String, Any>(
			"/$DB_USER_INFO/$userId" to profileValue,
			// "/$DB_USER_PROFILE/$userId/$key" to profileValue -> 동시 다른 하위목록 업데이트 가능
		)

		Firebase.database.reference.updateChildren(childUpdates)
			.addOnSuccessListener {
				Toast.makeText(this, "프로필이 변경되었어요.", Toast.LENGTH_SHORT).show()
			}
			.addOnFailureListener {
				Toast.makeText(this, "프로필 수정에 실패했어요.", Toast.LENGTH_SHORT).show()
				return@addOnFailureListener
			}

	}

	// 이미지 수정 - 카메라/ 갤러리 선택 후 이미지 뿌리기 - Storage에 저장 및 DB 업데이트
	private fun setProfileImage() = with(binding) {
		profileImageView.setOnClickListener {
			showChooseCameraOrGalleryPopup()
			submitButton.isVisible = true
		}
	}

	private fun openGallery() {
		val intent = Intent(Intent.ACTION_PICK)
		intent.type = "image/*"
		startActivityForResult(intent, GALLERY_RESULT_CODE)
	}

	private fun openCamera() {
		val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
		createImageUri(firebaseDBViewModel.newFileName(), "image/*").let { uri ->
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
								val urlResult =
									firebaseDBViewModel.imageStorageUpload(intent.data!!)
								binding.profileImageView.circleCropImage(urlResult)
								editImageUrl = urlResult
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
						val urlResult = firebaseDBViewModel.imageStorageUpload(uri)
						binding.profileImageView.circleCropImage(urlResult)
						editImageUrl = urlResult
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
}