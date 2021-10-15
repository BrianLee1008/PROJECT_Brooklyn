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

		fun bindSendingMessage(chatRoomItem: ChatRoomItem) = with(binding){
			if(auth.currentUser!!.uid == chatRoomItem.buyerNickName){ // 내가 보낸 메세지라면
				buyerMessageTextView.text = chatRoomItem.message
				buyerMessageTextView.isVisible = true

			} else { // 상대방이 보낸 메세지라면
				sellerMessageTextView.text = chatRoomItem.message
				sellerMessageTextView.isVisible = true
			}

		}

	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatRoomViewHolder =
		ChatRoomViewHolder(
			ItemMessageBoxBinding.inflate(
				LayoutInflater.from(parent.context),
				parent,
				false
			)
		)

	override fun onBindViewHolder(holder: ChatRoomViewHolder, position: Int) =
		holder.bindSendingMessage(currentList[position])
}