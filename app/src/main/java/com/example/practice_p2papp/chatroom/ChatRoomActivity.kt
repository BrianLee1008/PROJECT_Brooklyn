package com.example.practice_p2papp.chatroom

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.practice_p2papp.FirebaseKey
import com.example.practice_p2papp.adapter.ChatRoomAdapter
import com.example.practice_p2papp.databinding.ActivityChatroomBinding
import com.example.practice_p2papp.item.ChatRoomItem
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*

class ChatRoomActivity : AppCompatActivity() {

	private val auth by lazy {
		Firebase.auth
	}

	private val chatDetailDB: DatabaseReference by lazy {
		Firebase.database.reference.child(FirebaseKey.DB_CHAT_DETAIL_MASSAGE)
	}

	private val chatList = mutableListOf<ChatRoomItem>()

	private lateinit var chatRoomAdapter : ChatRoomAdapter
	private var key : String? = null


	private lateinit var binding: ActivityChatroomBinding
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityChatroomBinding.inflate(layoutInflater)
		setContentView(binding.root)

		chatList.clear()
		key = intent.getStringExtra("key")


		initRecyclerView()
		// 저장되어있는 메세지 불러오기.
		chatDetailDB.child(key!!).addChildEventListener(object : ChildEventListener{
			override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
				val chat = snapshot.getValue(ChatRoomItem::class.java)
				chat ?: return

				chatList.add(chat)
				chatRoomAdapter.submitList(chatList)
				chatRoomAdapter.notifyDataSetChanged()
			}
			override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
			override fun onChildRemoved(snapshot: DataSnapshot) {}
			override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
			override fun onCancelled(error: DatabaseError) {}

		})

		val chatInfo = intent.getSerializableExtra("path") as ChatRoomItem
		initViews(chatInfo.sellerNickName)

		setSendButtonListener(
			userId = auth.currentUser!!.uid,
			sellerNickName = chatInfo.sellerNickName,
			articleTitle = chatInfo.articleTitle
		)

	}


	private fun initViews(
		sellerNickName: String
	) = with(binding) {
		sellerNickNameTextView.text = sellerNickName
	}

	private fun initRecyclerView() = with(binding){
		chatRoomAdapter = ChatRoomAdapter()
		chatRoomRecyclerView.adapter = chatRoomAdapter
		chatRoomRecyclerView.layoutManager = LinearLayoutManager(this@ChatRoomActivity)

	}


	// 전송 버튼 누르면 DB 저장
	private fun setSendButtonListener(userId: String, sellerNickName: String, articleTitle:String) = with(binding){
		sendButton.setOnClickListener {
			val message = ChatRoomItem(
				userId = userId,
				sellerNickName = sellerNickName,
				message = messageEditText.text.toString(),
				currentTime = System.currentTimeMillis(),
				articleTitle = articleTitle
			)

			// 어차피 채팅 자체가 하나씩 스택 쌓이는 구조이니 push()로 해준다.
			chatDetailDB.child(key!!).push().setValue(message)
			binding.messageEditText.text.clear()
		}
	}
}