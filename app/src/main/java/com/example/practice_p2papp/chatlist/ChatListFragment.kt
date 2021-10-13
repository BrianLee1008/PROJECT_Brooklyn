package com.example.practice_p2papp.chatlist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.practice_p2papp.FirebaseKey
import com.example.practice_p2papp.adapter.ChatListAdapter
import com.example.practice_p2papp.chatroom.ChatRoomActivity
import com.example.practice_p2papp.databinding.FragmentChatlistBinding
import com.example.practice_p2papp.item.ChatRoomItem
import com.example.practice_p2papp.item.ChatRoomListItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ChatListFragment : Fragment() {

	private val auth: FirebaseAuth by lazy {
		Firebase.auth
	}

	private lateinit var chatListAdapter: ChatListAdapter
	private val chatList = mutableListOf<ChatRoomListItem>()

	private val chatDB: DatabaseReference by lazy {
		Firebase.database.reference.child(FirebaseKey.DB_CHAT).child(FirebaseKey.DB_BUYER_CHAT)
			.child(auth.currentUser!!.uid)
	}


	private var _binding: FragmentChatlistBinding? = null
	private val binding: FragmentChatlistBinding
		get() = _binding!!


	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentChatlistBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		chatList.clear()

		initRecyclerView()

		chatDB.addChildEventListener(object : ChildEventListener {
			override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
				val model = snapshot.getValue(ChatRoomListItem::class.java)
				model ?: return

				if (auth.currentUser!!.uid != model.sellerId) {
					chatList.add(model)
					chatListAdapter.submitList(chatList)
					chatListAdapter.notifyDataSetChanged()
				}
			}

			override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
			override fun onChildRemoved(snapshot: DataSnapshot) {}
			override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
			override fun onCancelled(error: DatabaseError) {}
		})


	}

	private fun initRecyclerView() = with(binding) {
		// 메세지 제외한 정보 담아서 ChatRoom으로 이동
		chatListAdapter = ChatListAdapter(
			chatListClickListener = { chatRoomListItem ->
				val chatRoomInfo = ChatRoomItem(
					sellerId = chatRoomListItem.sellerId,
					buyerNickName = chatRoomListItem.buyerNickName,
					sellerNickName = chatRoomListItem.sellerNickName,
					message = ""
				)
				startChatRoomActivity(chatRoomInfo)
			}
		)
		chatListRecyclerView.adapter = chatListAdapter
		chatListRecyclerView.layoutManager =
			LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true).apply {
				stackFromEnd = true
			}

	}

	private fun startChatRoomActivity(chatRoomInfo: ChatRoomItem) {
		val intent = Intent(activity, ChatRoomActivity::class.java)
		intent.putExtra("path", chatRoomInfo)
		startActivity(intent)
	}

	override fun onResume() {
		super.onResume()
		chatListAdapter.notifyDataSetChanged()
	}


	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}