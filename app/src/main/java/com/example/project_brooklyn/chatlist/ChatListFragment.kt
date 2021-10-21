package com.example.project_brooklyn.chatlist

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_brooklyn.adapter.ChatListAdapter
import com.example.project_brooklyn.chatroom.ChatRoomActivity
import com.example.project_brooklyn.databinding.FragmentChatlistBinding
import com.example.project_brooklyn.item.ChatRoomItem
import com.example.project_brooklyn.item.ChatRoomListItem
import com.example.project_brooklyn.viewmodel.FirebaseDBViewModel
import com.example.project_brooklyn.viewmodel.factory.FirebaseViewModelFactory
import com.example.project_brooklyn.viewmodel.repository.AppRepository

class ChatListFragment : Fragment() {

	private lateinit var chatListAdapter: ChatListAdapter

	private var _binding: FragmentChatlistBinding? = null
	private val binding: FragmentChatlistBinding
		get() = _binding!!


	private val appRepository = AppRepository()

	private val firebaseDBViewModel by viewModels<FirebaseDBViewModel> {
		FirebaseViewModelFactory(
			appRepository
		)
	}

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

		chatListObserve()
		initAdapterAndStartChatRoom()
		initRecyclerView()

	}

	// 메세지 제외한 정보 담아서 ChatRoom으로 이동
	private fun initAdapterAndStartChatRoom() {
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

	// ChatList Read
	@SuppressLint("NotifyDataSetChanged")
	private fun chatListObserve() {
		firebaseDBViewModel.chatListLiveData
			.observe(
				viewLifecycleOwner,
				{
					it.forEach { chatRoomListItem ->

						// co DetailArticleActivity에서 sellerID, articleTitle 데이터 가져와야함. 검증에 필요 (똑같은 채팅방 중복 생성 방지)
						if (firebaseDBViewModel.auth.currentUser!!.uid != chatRoomListItem.sellerId &&
							firebaseDBViewModel.auth.currentUser!!.uid != chatRoomListItem.buyerNickName
						) {
							Toast.makeText(activity, "채팅방 생성에 실패했어요. 다시 시도해주세요.", Toast.LENGTH_SHORT)
								.show()
							return@observe
						}
					}

					chatListAdapter.submitList(it)
					chatListAdapter.notifyDataSetChanged()

				}
			)
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