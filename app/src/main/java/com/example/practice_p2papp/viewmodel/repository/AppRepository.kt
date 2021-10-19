package com.example.practice_p2papp.viewmodel.repository

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.practice_p2papp.FirebaseKey
import com.example.practice_p2papp.FirebaseKey.Companion.DB_USER_INFO
import com.example.practice_p2papp.item.UserItem
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

// co read만 우선.. create, update 나중에

// FirebaseDB, Retrofit2 CRUD
class AppRepository {


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

			override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
				val model = snapshot.getValue(UserItem::class.java)
				model ?: return

				userList.add(model)
				userLiveData.value = userList
			}

			override fun onChildRemoved(snapshot: DataSnapshot) {}
			override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
			override fun onCancelled(error: DatabaseError) {}
		})

		return userLiveData
	}


}