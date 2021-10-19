package com.example.practice_p2papp.viewmodel

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practice_p2papp.FirebaseKey
import com.example.practice_p2papp.item.ArticleListItem
import com.example.practice_p2papp.item.ChatRoomItem
import com.example.practice_p2papp.item.ChatRoomListItem
import com.example.practice_p2papp.item.UserItem
import com.example.practice_p2papp.viewmodel.repository.AppRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat

// Firebase Auth, Storage, DB 업로드, 다운로드관련 비즈니스 로직
class FirebaseDBViewModel(private val repository: AppRepository) : ViewModel() {

	val auth: FirebaseAuth by lazy {
		Firebase.auth
	}

	private val storage: FirebaseStorage by lazy {
		Firebase.storage
	}

	val userDB: DatabaseReference by lazy {
		Firebase.database.reference
	}

	val articleDB: DatabaseReference by lazy {
		Firebase.database.reference
	}

	val chatDB: DatabaseReference by lazy {
		Firebase.database.reference
	}

	val chatDetailDB: DatabaseReference by lazy {
		Firebase.database.reference
	}


	// 시간대로 고유 이름 생성
	@SuppressLint("SimpleDateFormat")
	fun newFileName(): String {
		val sdf = SimpleDateFormat("yyyyMMdd_HHmmss")
		val fileName = sdf.format(System.currentTimeMillis())
		return "$fileName.jpg"
	}

	// 갤러리 단수, 복수 선택
	fun galleryResult(intent: Intent, uriList: ArrayList<Uri>) {

		val notNullClipData = intent.clipData != null
		val notNullData = intent.data != null

		when {
			// 복수 이미지 선택시
			notNullClipData -> {

				val count = intent.clipData!!.itemCount

				if (count > 10) {
					//TODO 10장 이상 선택할 수 없습니다. Show
					return
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
	}


	// 카메라 기능 수행
	fun cameraResult(uriList: ArrayList<Uri>, uri: Uri) {
		uriList.add(uri)
	}


	// 회원가입시 선택한 이미지를 스토리지에 저장
	suspend fun imageStorageUpload(uri: Uri) = withContext(Dispatchers.IO) {
		val fileName = "profileImage_${uri}.png"

		val deferred: Deferred<String> =
			viewModelScope.async {
				val storagePutFile = storage.reference.child(FirebaseKey.ARTICLE_STORAGE)
					.child(FirebaseKey.STORAGE_PROFILE).child(fileName).putFile(uri).await()

				return@async storagePutFile.storage.downloadUrl.await().toString()
			}
		return@withContext deferred.await()
	}


	// 회원 정보 DB 업로드
	suspend fun uploadUserInfo(userId: String, nickName: String, imageUrl: String) =
		withContext(Dispatchers.IO) {
			viewModelScope.launch {
				val model = UserItem(userId = userId, nickName = nickName, imageUrl = imageUrl)
				userDB.child(FirebaseKey.DB_USER_INFO).child(userId).setValue(model)
			}
		}

	// UserInfo Read
	val userInfoLiveData = repository.getUserProfileData()


	// 아이템 등록할때 선택한 이미지를 Storage 저장
	suspend fun articleImageStorageUpload(uriList: List<Uri>) = withContext(Dispatchers.IO) {
		val deferred: List<Deferred<Any>> = uriList.mapIndexed { index, uri ->
			viewModelScope.async {
				val fileName = "image_${index}.png"
				val storagePutFile =
					storage.reference.child(FirebaseKey.ARTICLE_STORAGE).child(fileName)
						.putFile(uri).await()
				return@async storagePutFile.storage.downloadUrl.await().toString()
			}
		}
		return@withContext deferred.awaitAll()
	}


	// 아이템 DB 업로드
	suspend fun uploadArticlesInDB(
		userId: String,
		nickName: String,
		title: String,
		content: String,
		price: String,
		date: Long,
		imageUriList: List<String>,
		userProfileImage: String
	) = withContext(Dispatchers.IO) {

		val model = ArticleListItem(
			userId,
			nickName,
			title,
			content,
			price,
			date,
			imageUriList,
			userProfileImage
		)

		viewModelScope.launch {
			articleDB.child(FirebaseKey.DB_ARTICLES).child("$date").setValue(model)
		}
	}

	// Article 정보 토대로 ChatList DB에 업로드
	fun uploadChatListInfoInDB(
		sellerId: String,
		buyerNickName: String,
		sellerNickName: String,
		buyerProfileImage: String,
		currentTime: Long,
		articleTitle: String
	) {
		val chatRoomList = ChatRoomListItem(
			sellerId,
			buyerNickName,
			sellerNickName,
			buyerProfileImage,
			currentTime,
			articleTitle
		)

		chatDB.child(FirebaseKey.DB_CHAT).push().setValue(chatRoomList)
	}

	// message DB에 업로드
	fun uploadMessageInDB(
		userId: String,
		sellerNickName: String,
		message: String,
		currentTime: Long = System.currentTimeMillis(),
		articleTitle: String,
		key: String
	) {
		val messageInfo = ChatRoomItem(
			userId = userId,
			sellerNickName = sellerNickName,
			message = message,
			currentTime = currentTime,
			articleTitle = articleTitle
		)

		// 어차피 채팅 자체가 하나씩 스택 쌓이는 구조이니 push()로 해준다.
		chatDetailDB.child(FirebaseKey.DB_CHAT_DETAIL_MASSAGE).child(key).push()
			.setValue(messageInfo)
	}

}