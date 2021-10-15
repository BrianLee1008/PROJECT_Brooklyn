package com.example.practice_p2papp.chatlist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ChatListFragment : Fragment() {


	private val auth: FirebaseAuth by lazy {
		Firebase.auth
	}


	private lateinit var chatListAdapter: ChatListAdapter
	private val chatList = mutableListOf<ChatRoomListItem>()


	private var sellerId: String? = null
	private var buyerId: String? = null

//	// 사려는 사람 전용
//	private val buyerDB: DatabaseReference by lazy {
//		Firebase.database.reference.child(FirebaseKey.DB_CHAT).child(DB_BUYER_CHAT)
//			.child(auth.currentUser!!.uid)
//	}

//	// 파는 사람 전용
//	private val sellerDB: DatabaseReference by lazy {
//		Firebase.database.reference.child(FirebaseKey.DB_CHAT).child(DB_BUYER_CHAT).child(sellerId!!)
//	}

	// co 테스트 전용
	private val chatDB: DatabaseReference by lazy {
		Firebase.database.reference.child(FirebaseKey.DB_CHAT)
	}


	private var _binding: FragmentChatlistBinding? = null
	private val binding: FragmentChatlistBinding
		get() = _binding!!

	override fun onAttach(context: Context) {
		super.onAttach(context)
		Log.d("testtest", "onAttach")
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		Log.d("testtest", "onCreate")

		sellerId = arguments?.getString("sellerId")
		buyerId = arguments?.getString("buyerId")
		Log.d("testtest", "${arguments?.getString("buyerId")}")

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


		chatList.clear()

		initRecyclerView()
		initChatList()

	}


	private fun initRecyclerView() = with(binding) {
		// 메세지 제외한 정보 담아서 ChatRoom으로 이동
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
		chatListRecyclerView.adapter = chatListAdapter
		chatListRecyclerView.layoutManager =
			LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true).apply {
				stackFromEnd = true
			}
	}

	private fun initChatList() {
		chatDB.addListenerForSingleValueEvent(object : ValueEventListener {
			override fun onDataChange(snapshot: DataSnapshot) {
				snapshot.children.forEach {
					val model = it.getValue(ChatRoomListItem::class.java)
					model ?: return

					// co DetailArticleActivity에서 sellerID, articleTitle 데이터 가져와야함. 검증에 필요 (똑같은 채팅방 중복 생성 방지)
//					if(articleTitle == model.articleTitle){
//						return@forEach
//					}
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
		Log.d("testtest", "onDestroyView")
		_binding = null
	}


	//	private fun  initChatList(){
//		when (auth.currentUser!!.uid) {
//			sellerId -> { // 내가 파는 사람이면
//				sellerDB.addChildEventListener(object : ChildEventListener {
//					override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
//						val model = snapshot.getValue(ChatRoomListItem::class.java)
//						model ?: return
//						if (auth.currentUser!!.uid == model.sellerId) {
//							chatList.add(model)
//							chatListAdapter.submitList(chatList)
//							chatListAdapter.notifyDataSetChanged()
//						}
//
//					}
//
//					override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
//					override fun onChildRemoved(snapshot: DataSnapshot) {}
//					override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
//					override fun onCancelled(error: DatabaseError) {}
//				})
//
//			}
//			else -> {
//				buyerDB.addChildEventListener(object : ChildEventListener {
//					override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
//						val model = snapshot.getValue(ChatRoomListItem::class.java)
//						model ?: return
//
//						if(auth.currentUser!!.uid == model.buyerNickName){ // 아 이게 왜 안되냐면. child에 sellerId라는건 없으니까..라는건가
//							chatList.add(model)
//							chatListAdapter.submitList(chatList)
//							chatListAdapter.notifyDataSetChanged()
//						}
//					}
//
//					override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
//					override fun onChildRemoved(snapshot: DataSnapshot) {}
//					override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
//					override fun onCancelled(error: DatabaseError) {}
//				})
//			}
//		}
//	}

}