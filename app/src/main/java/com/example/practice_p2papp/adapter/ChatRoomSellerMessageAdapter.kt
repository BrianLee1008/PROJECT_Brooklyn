package com.example.practice_p2papp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.practice_p2papp.databinding.ItemSellerMessageBoxBinding
import com.example.practice_p2papp.item.ChatRoomItem

class ChatRoomSellerMessageAdapter :
	ListAdapter<ChatRoomItem, ChatRoomSellerMessageAdapter.ChatRoomViewHolder>(diffUtil) {

	companion object {
		val diffUtil = object : DiffUtil.ItemCallback<ChatRoomItem>() {
			override fun areItemsTheSame(oldItem: ChatRoomItem, newItem: ChatRoomItem) =
				oldItem == newItem

			override fun areContentsTheSame(oldItem: ChatRoomItem, newItem: ChatRoomItem) =
				oldItem == newItem

		}
	}

	inner class ChatRoomViewHolder(private val binding: ItemSellerMessageBoxBinding) :
		RecyclerView.ViewHolder(binding.root) {

		fun bindSendingMessage(chatRoomItem: ChatRoomItem) = with(binding) {
			sellerMessageTextView.text = chatRoomItem.message

		}

	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatRoomViewHolder =
		ChatRoomViewHolder(
			ItemSellerMessageBoxBinding.inflate(
				LayoutInflater.from(parent.context),
				parent,
				false
			)
		)

	override fun onBindViewHolder(
		holder: ChatRoomViewHolder,
		position: Int
	) =
		holder.bindSendingMessage(currentList[position])
}