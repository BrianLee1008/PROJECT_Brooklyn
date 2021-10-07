package com.example.practice_p2papp.articlelist

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.example.practice_p2papp.FirebaseKey.Companion.ARTICLE_STORAGE
import com.example.practice_p2papp.FirebaseKey.Companion.DB_ARTICLES
import com.example.practice_p2papp.FirebaseKey.Companion.DB_USER_INFO
import com.example.practice_p2papp.abstracts.PermissionActivity
import com.example.practice_p2papp.databinding.ActivityAddArticleBinding
import com.example.practice_p2papp.item.ArticleListItem
import com.example.practice_p2papp.item.UserItem
import com.example.practice_p2papp.photo.PhotoAdapter
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

class AddArticleActivity : PermissionActivity() {

	private var captureImageUri: Uri? = null

	private val imageUriList = mutableListOf<Uri>()

	private val auth: FirebaseAuth by lazy {
		Firebase.auth
	}
	private val articleDB: DatabaseReference by lazy {
		Firebase.database.reference
	}

	private val userDB: DatabaseReference by lazy {
		Firebase.database.reference.child(DB_USER_INFO)
	}

	private val storage : FirebaseStorage by lazy {
		Firebase.storage
	}

	private var userNickName: String = ""

	private lateinit var photoAdapter : PhotoAdapter


	private lateinit var binding: ActivityAddArticleBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityAddArticleBinding.inflate(layoutInflater)
		setContentView(binding.root)

		// userDB에서 nickName 가져온다.
		userDB.addChildEventListener(object : ChildEventListener {
			override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
				val model = snapshot.getValue(UserItem::class.java)
				model ?: return

				userNickName = model.nickName
			}

			override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
			override fun onChildRemoved(snapshot: DataSnapshot) {}
			override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
			override fun onCancelled(error: DatabaseError) {}

		})

		initRecyclerView()
		setImageAddButtonListener()
		setSubmitButtonListener()

	}

	//리사이클러뷰 셋업
	private fun initRecyclerView() = with(binding){
		photoAdapter = PhotoAdapter(removePhotoListener = { uri ->
			removePhoto(uri)
		})
		photoRecyclerView.run {
			adapter = photoAdapter
		}

	}


	// DB에 항목 업로드, userId, nickName, title, content, price, date, imageUriList
	private fun setSubmitButtonListener() = with(binding) {
		submitButton.setOnClickListener {
			if (auth.currentUser == null) {
				return@setOnClickListener
			}

			val userId = auth.currentUser?.uid.toString()
			val nickName = userNickName
			val title = titleEditText.text.toString()
			val content = contentEditText.text.toString()
			val price = priceEditText.text.toString()
			val date = System.currentTimeMillis()

			showProgress()

			// imageUrlList가 있을 떄와 없을떄로 나누어짐
			if (imageUriList.isNotEmpty()) {
				lifecycleScope.launch {
					val results = uploadPhoto(imageUriList)
					uploadArticles(userId, nickName, title, content, "$price 원", date, results.filterIsInstance<String>())
				}

			} else {
				uploadArticles(userId, nickName, title, content, "$price 원", date, listOf())

			}

		}

	}

	// Storage 저장
	private suspend fun uploadPhoto(uriList : List<Uri>) = withContext(Dispatchers.IO) {
		val deferred : List<Deferred<Any>> = uriList.mapIndexed { index, uri ->
			lifecycleScope.async {
				val fileName = "image_${index}.png"
				val storagePutFile =  storage.reference.child(ARTICLE_STORAGE).child(fileName).putFile(uri).await()
				return@async storagePutFile.storage.downloadUrl.await().toString()
			}
		}
		return@withContext deferred.awaitAll()
	}

	// DB 저장
	private fun uploadArticles(
		userId: String,
		nickName: String,
		title: String,
		content: String,
		price: String,
		date: Long,
		imageUriList: List<String>
	) {
		val model = ArticleListItem(userId, nickName, title, content, price, date, imageUriList)

		articleDB.child(DB_ARTICLES).push().setValue(model)

		hideProgress()
		finish()

	}

	private fun removePhoto(uri :Uri){
		imageUriList.remove(uri)
		photoAdapter.setPhotoList(imageUriList)
	}

	// 아래로 퍼미션 및 이미지 업로드 관련

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
		createImageUri(newFileName(), "image/*").let { uri ->
			captureImageUri = uri
			intent.putExtra(MediaStore.EXTRA_OUTPUT, captureImageUri)
			startActivityForResult(intent, CAMERA_RESULT_CODE)
			Log.d("testtest","$CAMERA_RESULT_CODE 3")
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
	private fun newFileName(): String {
		val sdf = SimpleDateFormat("yyyyMMdd_HHmmss")
		val fileName = sdf.format(System.currentTimeMillis())
		return "$fileName.jpg"
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
					val notNullClipData = intent.clipData != null
					val notNullData = intent.data != null

					when {
						// 복수 이미지 선택시
						notNullClipData -> {
							val count = intent.clipData!!.itemCount
							if (count > 10) {
								Toast.makeText(this, "한번에 열장 이상 선택하지 못합니다..", Toast.LENGTH_SHORT)
									.show()
								return@let
							}
							for (i in 0 until count) {
								val imageUri = intent.clipData!!.getItemAt(i).uri
								uriList.add(imageUri)
							}
						}
						else -> {
							// 단수 이미지 선택시
							if (notNullData) {
								uriList.add(intent.data!!)
							}
						}
					}
					imageUriList.addAll(uriList)
					photoAdapter.setPhotoList(imageUriList)
				}

			}
			CAMERA_RESULT_CODE -> {

				captureImageUri?.let { uri ->
					uriList.add(uri)
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
				Log.d("testtest","$CAMERA_RESULT_CODE 4")
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