package com.example.project_brooklyn.viewmodel.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.project_brooklyn.FirebaseKey.Companion.DB_ARTICLES
import com.example.project_brooklyn.FirebaseKey.Companion.DB_CHAT
import com.example.project_brooklyn.FirebaseKey.Companion.DB_USER_INFO
import com.example.project_brooklyn.item.ArticleListItem
import com.example.project_brooklyn.item.ChatRoomItem
import com.example.project_brooklyn.item.ChatRoomListItem
import com.example.project_brooklyn.item.UserItem
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

// co read만 우선.. create, update 나중에

// FirebaseDB, Retrofit2 CRUD
class DBRepository {


	// UserInfo Read
	fun getUserProfileData(): LiveData<List<UserItem>> {

		val userLiveData = MutableLiveData<List<UserItem>>()
		val userList = mutableListOf<UserItem>()

		val userInfo = Firebase.database.reference.child(DB_USER_INFO)

		// 하위 child읽어올려고 하면 에러 뜸. 상위 child에서 갱신되는 것들 읽어오는 형식임.
		userInfo.addChildEventListener(object : ChildEventListener {
			override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
				val model = snapshot.getValue(UserItem::class.java)
				model ?: return

				userList.add(model)
				userLiveData.value = userList
			}

			override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

			override fun onChildRemoved(snapshot: DataSnapshot) {}
			override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
			override fun onCancelled(error: DatabaseError) {}
		})

		return userLiveData
	}

	// Articles Read
	fun getArticleData(): LiveData<List<ArticleListItem>> {

		val articleListLiveData = MutableLiveData<List<ArticleListItem>>()
		val articleList = mutableListOf<ArticleListItem>()

		val articleData = Firebase.database.reference.child(DB_ARTICLES)

		articleData.addChildEventListener(object : ChildEventListener {
			override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
				val model = snapshot.getValue(ArticleListItem::class.java)
				model ?: return

				articleList.add(model)
				articleListLiveData.value = articleList
			}

			override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
			override fun onChildRemoved(snapshot: DataSnapshot) {
				val model = snapshot.getValue(ArticleListItem::class.java)
				model ?: return

				articleList.clear()
				articleListLiveData.value = articleList
			}
			override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
			override fun onCancelled(error: DatabaseError) {}
		})

		return articleListLiveData
	}

	// ChatList Read
	fun getChatListData(): LiveData<List<ChatRoomListItem>> {
		val chatListLiveData = MutableLiveData<List<ChatRoomListItem>>()
		val chatList = mutableListOf<ChatRoomListItem>()

		val chatListData = Firebase.database.reference.child(DB_CHAT)

		chatListData.addChildEventListener(object : ChildEventListener{
			override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
				val model = snapshot.getValue(ChatRoomListItem::class.java)
				model ?: return

				chatList.add(model)
				chatListLiveData.value = chatList
			}

			override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
			override fun onChildRemoved(snapshot: DataSnapshot) {
				val model = snapshot.getValue(ChatRoomListItem::class.java)
				model ?: return

				chatList.clear()
				chatListLiveData.value = chatList
			}
			override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
			override fun onCancelled(error: DatabaseError) {}

		})

		return chatListLiveData
	}



}