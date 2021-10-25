package com.example.project_brooklyn.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.project_brooklyn.databinding.ItemMessageBoxBinding
import com.example.project_brooklyn.extensions.circleCropImage
import com.example.project_brooklyn.item.ChatRoomItem
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

		// 상대방 프로필 이미지 CircleCrop Glide
		fun bindSendingMessage(chatRoomItem: ChatRoomItem) = with(binding){
			if(auth.currentUser!!.uid == chatRoomItem.userId) {
				buyerMessageTextView.text = chatRoomItem.message
				buyerMessageTextView.isVisible = true
			} else {
				sellerMessageTextView.text = chatRoomItem.message
				sellerProfileImageView.circleCropImage(chatRoomItem.sellerProfileImageUrl)
				sellerProfileImageView.isVisible = true
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

	override fun onBindViewHolder(holder: ChatRoomViewHolder, position: Int) {
		holder.bindSendingMessage(currentList[position])
	}

}