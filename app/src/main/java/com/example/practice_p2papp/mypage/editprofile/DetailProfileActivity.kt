package com.example.practice_p2papp.mypage.editprofile

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.example.practice_p2papp.FirebaseKey
import com.example.practice_p2papp.FirebaseKey.Companion.DB_USER_INFO
import com.example.practice_p2papp.FirebaseKey.Companion.DB_USER_PROFILE
import com.example.practice_p2papp.StartActivity
import com.example.practice_p2papp.abstracts.PermissionActivity
import com.example.practice_p2papp.databinding.ActivityDetailProfileBinding
import com.example.practice_p2papp.extensions.authCheck
import com.example.practice_p2papp.extensions.circleCropImage
import com.example.practice_p2papp.item.UserItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat

// 프로필 수정 및 로그아웃 Activity
class DetailProfileActivity : PermissionActivity() {

	private var editImageUrl : String? = null
	private var loadListenerImage : String? = null

	private var captureImageUri: Uri? = null

	private val auth: FirebaseAuth by lazy {
		Firebase.auth
	}
	private val userDB: DatabaseReference by lazy {
		Firebase.database.reference.child(DB_USER_INFO)
	}
	private val storage: FirebaseStorage by lazy {
		Firebase.storage
	}
	private val loadListener = object : ChildEventListener {
		override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
			val model = snapshot.getValue(UserItem::class.java)
			model ?: return

			binding.nickNameTextView.text = model.nickName
			binding.profileImageView.circleCropImage(model.imageUrl!!)
			loadListenerImage = model.imageUrl
		}

		override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
		override fun onChildRemoved(snapshot: DataSnapshot) {}
		override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
		override fun onCancelled(error: DatabaseError) {}
	}


	private lateinit var binding: ActivityDetailProfileBinding
	override fun onCreate(savedInstanceState: Bundle?) {
		binding = ActivityDetailProfileBinding.inflate(layoutInflater)
		super.onCreate(savedInstanceState)
		setContentView(binding.root)

		setSignOutButtonListener()
		userDB.addChildEventListener(loadListener)
		binding.nickNameTextView.isVisible = true

		initNickNameTest()
		editNickNameTest()

		setProfileImage()

	}


	private fun setSignOutButtonListener() = with(binding) {
		signOutButton.setOnClickListener {
			auth.authCheck()
			auth.signOut()
			Toast.makeText(this@DetailProfileActivity, "로그아웃 했습니다", Toast.LENGTH_SHORT).show()
			startActivity(Intent(this@DetailProfileActivity, StartActivity::class.java))
			finish()
		}
	}

	// 이미지 수정 - 카메라/ 갤러리 선택 후 이미지 뿌리기 - Storage에 저장 및 DB 업데이트
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
		createImageUri(newFileName(), "image/*").let { uri ->
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

	// 고유 이름 생성
	@SuppressLint("SimpleDateFormat")
	private fun newFileName(): String {
		val sdf = SimpleDateFormat("yyyyMMdd_HHmmss")
		val fileName = sdf.format(System.currentTimeMillis())
		return "$fileName.jpg"
	}

	// 이미지 스토리지 업로드
	private suspend fun imageStorageUpload(uri: Uri) = withContext(Dispatchers.IO) {
		val fileName = "profileImage_${uri}.png"
		val deferred: Deferred<String> =
			lifecycleScope.async {
				val storagePutFile = storage.reference.child(FirebaseKey.ARTICLE_STORAGE)
					.child(FirebaseKey.STORAGE_PROFILE).child(fileName).putFile(uri).await()
				return@async storagePutFile.storage.downloadUrl.await().toString()
			}
		return@withContext deferred.await()
	}

	// 이미지 DB 업로드
	private suspend fun imageDBUpload(userId: String, nickName: String, imageUrl: String) =
		withContext(Dispatchers.IO) {
			lifecycleScope.launch {
				val model = UserItem(userId, nickName, imageUrl)
				userDB.push().setValue(model)
			}
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
							if (notNullData) {
								lifecycleScope.launch {
									showProgress()
									val urlResult = imageStorageUpload(intent.data!!)
									binding.profileImageView.circleCropImage(urlResult)
									editImageUrl = urlResult
									hideProgress()
								}
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
						val urlResult = imageStorageUpload(uri)
						binding.profileImageView.circleCropImage(urlResult)
						editImageUrl = urlResult
						hideProgress()
					}
					captureImageUri = null
				}
			}
		}


	}


	/* xo 2.DB 수정
	*   이 예시에서는 push()를 사용하여 모든 사용자의 게시글을 포함하는 노드(/UserInfo/$userId)에서 게시글을 작성하는 동시에 getKey()로 키를 검색합니다.
	*   그런 다음 이 키를 사용하여 /user-posts/$userid/$postid에서 사용자의 게시물에 두 번째 항목을 작성합니다.
	*   이 경로를 사용하면 이 예시에서 두 위치에 새 게시물을 생성한 것처럼 updateChildren()를 한 번만 호출하여 JSON 트리의 여러 위치에서 동시에 업데이트를 수행할 수 있습니다.
	*   이러한 동시 업데이트는 원자적인 성격을 갖습니다. 즉, 모든 업데이트가 한꺼번에 성공하거나 실패합니다.*/
	private fun editProfileAndUpdateDB(userId: String, nickName: String, imageUrl: String?) {
		val key = userDB.push().key
		if (key == null) {
			Toast.makeText(this, "수정에 실패 했습니다.", Toast.LENGTH_SHORT).show()
			return
		}

		val profile = UserItem(userId = userId, nickName = nickName, imageUrl = imageUrl)
		val profileValue = profile.toMap()

		val childUpdates = hashMapOf<String, Any>(
			"/$DB_USER_INFO/$key" to profileValue,
			 // "/$DB_USER_PROFILE/$userId/$key" to profileValue -> 동시 다른 하위목록 업데이트 가능
		)


		/*xo 3. DB 업데이트 완료 콜백
		*  데이터가 커밋되는 시점을 파악하려면 완료 리스너를 추가합니다. setValue() 및 updateChildren()은 모두 쓰기가 데이터베이스에 커밋될 때 호출되는 선택적 완료 리스너를 사용합니다.
		*  호출이 실패하면 실패 이유를 나타내는 오류 객체가 리스너로 전달됩니다.*/
		// 프로필 닉네임 수정
		Firebase.database.reference.updateChildren(childUpdates)
			.addOnSuccessListener {
				Toast.makeText(this, "프로필이 변경되었어요.", Toast.LENGTH_SHORT).show()
			}
			.addOnFailureListener {
				Toast.makeText(this, "프로필 수정에 실패했어요.", Toast.LENGTH_SHORT).show()
				return@addOnFailureListener
			}

	}

	private fun initNickNameTest() = with(binding) {
		nickNameContainer.setOnClickListener {
			nickNameTextView.isVisible = false
			nickNameEditText.isVisible = true
			submitButton.isVisible = true
		}
	}

	private fun editNickNameTest() = with(binding) {
		submitButton.setOnClickListener {
			auth.authCheck()
			if (nickNameEditText.text.isEmpty()){
				Toast.makeText(this@DetailProfileActivity, "닉네임은 2글자 이상으로 작성해주셔야 해요.", Toast.LENGTH_SHORT).show()
				return@setOnClickListener
			}

			// 이미지 변경이 있으면 변경된 이미지 저장. 없으면 원래 불러왔던 이미지 저장
			if(editImageUrl != null){
				editProfileAndUpdateDB(
					userId = auth.currentUser!!.uid,
					nickName = nickNameEditText.text.toString(),
					imageUrl = editImageUrl
				)
				editImageUrl = null
			} else {
				editProfileAndUpdateDB(
					userId = auth.currentUser!!.uid,
					nickName = nickNameEditText.text.toString(),
					imageUrl = loadListenerImage
				)
				loadListenerImage = null
			}
			nickNameTextView.isVisible = true
			nickNameEditText.isVisible = false
			submitButton.isVisible = false
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