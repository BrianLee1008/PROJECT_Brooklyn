package com.example.practice_p2papp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.practice_p2papp.databinding.ItemMessageBoxBinding
import com.example.practice_p2papp.item.ChatRoomItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class 	ChatRoomAdapter : ListAdapter<ChatRoomItem, ChatRoomAdapter.ChatRoomViewHolder>(diffUtil) {



	private val auth: FirebaseAuth by lazy {
		Firebase.auth
	}

	companion object {
		val diffUtil = object : DiffUtil.ItemCallback<ChatRoomItem>() {
			override fun areItemsTheSame(oldItem: ChatRoomItem, newItem: ChatRoomItem) =
				oldItem == newItem

			override fun areContentsTheSame(oldItem: ChatRoomItem, newItem: ChatRoomItem) =
				oldItem == newItem

		}
	}

	inner class ChatRoomViewHolder(private val binding: ItemMessageBoxBinding) :
		RecyclerView.ViewHolder(binding.root) {

		// 뷰모델에서 각각 데이터 가져와 검증해야할듯? DB에 뭐가 저장되어있는지가 중요한게 아니라 검증할 수있는 데이터를 여기에 가져오는게 중요
		fun bindSendingMessage(chatRoomItem: ChatRoomItem) = with(binding){
			if(auth.currentUser!!.uid == chatRoomItem.userId) {
				buyerMessageTextView.text = chatRoomItem.message
				buyerMessageTextView.isVisible = true
			} else {
				sellerMessageTextView.text = chatRoomItem.message
				sellerMessageTextView.isVisible = true
			}

		}
//		fun bindGivingMessage(chatRoomItem: ChatRoomItem) = with(binding){
//			if(auth.currentUser!!.uid == chatRoomItem.sellerId){
//				sellerMessageTextView.text = chatRoomItem.message
//				sellerMessageTextView.isVisible = true
//			}
//
//		}

	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatRoomViewHolder =
		ChatRoomViewHolder(
			ItemMessageBoxBinding.inflate(
				LayoutInflater.from(parent.context),
				parent,
				false
			)
		)

	override fun onBindViewHolder(holder: ChatRoomViewHolder, position: Int) {
		holder.bindSendingMessage(currentList[position])
	}

}