package com.example.project_brooklyn.chatroom

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_brooklyn.FirebaseKey
import com.example.project_brooklyn.adapter.ChatRoomAdapter
import com.example.project_brooklyn.databinding.ActivityChatroomBinding
import com.example.project_brooklyn.item.ChatRoomItem
import com.example.project_brooklyn.viewmodel.FirebaseDBViewModel
import com.example.project_brooklyn.viewmodel.factory.FirebaseViewModelFactory
import com.example.project_brooklyn.viewmodel.repository.DBRepository
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import java.util.*

class ChatRoomActivity : AppCompatActivity() {

	private val chatList = mutableListOf<ChatRoomItem>()

	private lateinit var chatRoomAdapter: ChatRoomAdapter
	private var key: String? = null


	private lateinit var binding: ActivityChatroomBinding

	private val appRepository = DBRepository()
	private val firebaseViewModel by viewModels<FirebaseDBViewModel>{FirebaseViewModelFactory(appRepository)}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityChatroomBinding.inflate(layoutInflater)
		setContentView(binding.root)

		chatList.clear()
		key = intent.getStringExtra("key")

		initRecyclerView()

		// 저장되어있는 메세지 불러오기. key값 인텐트로 받아와 바로 리스너 달아주기 떄문에 일단 부득이하게 UI Controller에 배치
		firebaseViewModel.chatDetailDB.child(FirebaseKey.DB_CHAT_DETAIL_MASSAGE).child(key!!)
			.addChildEventListener(object : ChildEventListener {

				@SuppressLint("NotifyDataSetChanged")
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
			userId = firebaseViewModel.auth.currentUser!!.uid,
			sellerNickName = chatInfo.sellerNickName,
			articleTitle = chatInfo.articleTitle
		)

	}


	private fun initViews(
		sellerNickName: String
	) = with(binding) {
		sellerNickNameTextView.text = sellerNickName
	}

	private fun initRecyclerView() = with(binding) {
		chatRoomAdapter = ChatRoomAdapter()
		chatRoomRecyclerView.adapter = chatRoomAdapter
		chatRoomRecyclerView.layoutManager = LinearLayoutManager(this@ChatRoomActivity)

	}


	// 전송 버튼 누르면 DB 저장
	private fun setSendButtonListener(
		userId: String,
		sellerNickName: String,
		articleTitle: String
	) = with(binding) {
		sendButton.setOnClickListener {

			firebaseViewModel.uploadMessageInDB(
				userId = userId,
				sellerNickName = sellerNickName,
				message = messageEditText.text.toString(),
				articleTitle = articleTitle,
				key = key!!
			)

			binding.messageEditText.text.clear()
		}
	}
}