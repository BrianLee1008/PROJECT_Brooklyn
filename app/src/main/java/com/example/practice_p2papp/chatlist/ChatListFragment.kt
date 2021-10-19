package com.example.practice_p2papp.chatlist

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.practice_p2papp.FirebaseKey
import com.example.practice_p2papp.adapter.ChatListAdapter
import com.example.practice_p2papp.chatroom.ChatRoomActivity
import com.example.practice_p2papp.databinding.FragmentChatlistBinding
import com.example.practice_p2papp.item.ChatRoomItem
import com.example.practice_p2papp.item.ChatRoomListItem
import com.example.practice_p2papp.viewmodel.FirebaseDBViewModel
import com.example.practice_p2papp.viewmodel.factory.FirebaseViewModelFactory
import com.example.practice_p2papp.viewmodel.repository.AppRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class ChatListFragment : Fragment() {

	private lateinit var chatListAdapter: ChatListAdapter
	private val chatList = mutableListOf<ChatRoomListItem>()


	private var _binding: FragmentChatlistBinding? = null
	private val binding: FragmentChatlistBinding
		get() = _binding!!


	private val appRepository = AppRepository()

	private val firebaseDBViewModel by viewModels<FirebaseDBViewModel>{ FirebaseViewModelFactory(appRepository) }
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

		initAdapterAndStartChatRoom()
		initRecyclerView()
		initChatList()

	}

	// 메세지 제외한 정보 담아서 ChatRoom으로 이동
	private fun initAdapterAndStartChatRoom(){
		chatListAdapter = ChatListAdapter(
			chatListClickListener = { chatRoomListItem ->
				val chatRoomInfo = ChatRoomItem(
					userId = chatRoomListItem.buyerNickName,
					sellerNickName = chatRoomListItem.sellerNickName,
					message = "",
					currentTime = chatRoomListItem.currentTime,
					articleTitle = chatRoomListItem.articleTitle
				)

				// 채팅 메세지 계층 고유 문자열
				startChatRoomActivity(
					chatRoomInfo = chatRoomInfo,
					key = "${chatRoomListItem.buyerNickName}${chatRoomListItem.sellerId}${chatRoomListItem.currentTime}"
				)

			}
		)
	}


	private fun initRecyclerView() = with(binding) {
		chatListRecyclerView.adapter = chatListAdapter
		chatListRecyclerView.layoutManager =
			LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true).apply {
				stackFromEnd = true
			}
	}


	@SuppressLint("NotifyDataSetChanged")
	private fun initChatList() {
		if(firebaseDBViewModel.auth.currentUser == null){
			return
		}

		firebaseDBViewModel.chatDB.child(FirebaseKey.DB_CHAT).addListenerForSingleValueEvent(object : ValueEventListener {

			override fun onDataChange(snapshot: DataSnapshot) {
				snapshot.children.forEach {
					val model = it.getValue(ChatRoomListItem::class.java)
					model ?: return

					// co DetailArticleActivity에서 sellerID, articleTitle 데이터 가져와야함. 검증에 필요 (똑같은 채팅방 중복 생성 방지)
					if(firebaseDBViewModel.auth.currentUser!!.uid != model.sellerId &&
						firebaseDBViewModel.auth.currentUser!!.uid != model.buyerNickName){
						return@forEach
					}
					chatList.add(model)
				}
				chatListAdapter.submitList(chatList)
				chatListAdapter.notifyDataSetChanged()
			}

			override fun onCancelled(error: DatabaseError) {
				Toast.makeText(activity, "채팅방 생성에 실패했어요. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
			}

		})
	}

	private fun startChatRoomActivity(
		chatRoomInfo: ChatRoomItem,
		key: String
	) {
		val intent = Intent(activity, ChatRoomActivity::class.java)
		intent.putExtra("path", chatRoomInfo)
		intent.putExtra("key", key)
		startActivity(intent)
	}


	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}


}