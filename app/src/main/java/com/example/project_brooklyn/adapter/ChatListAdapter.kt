package com.example.project_brooklyn.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.project_brooklyn.databinding.ItemChatListBinding
import com.example.project_brooklyn.extensions.circleCropImage
import com.example.project_brooklyn.item.ChatRoomListItem
import java.text.SimpleDateFormat
import java.util.*

class ChatListAdapter(private val chatListClickListener : (ChatRoomListItem) -> Unit) :
	ListAdapter<ChatRoomListItem, ChatListAdapter.ChatListViewHolder>(diffUtil) {

	companion object {
		val diffUtil = object : DiffUtil.ItemCallback<ChatRoomListItem>() {
			override fun areItemsTheSame(
				oldItem: ChatRoomListItem,
				newItem: ChatRoomListItem
			): Boolean =
				oldItem == newItem

			override fun areContentsTheSame(
				oldItem: ChatRoomListItem,
				newItem: ChatRoomListItem
			): Boolean =
				oldItem.articleTitle == newItem.articleTitle

		}
	}

	inner class ChatListViewHolder(private val binding: ItemChatListBinding) :
		RecyclerView.ViewHolder(binding.root) {

		// 메세지 제외한 나머지 항목들
		fun bindViewsWithOutMessage(chatRoomListItem: ChatRoomListItem) = with(binding) {

			val format = SimpleDateFormat("HH시 mm분")
			val currentDate = Date(chatRoomListItem.currentTime)

			profileImageView.circleCropImage(chatRoomListItem.buyerProfileImage)
			chatRoomTitleTextView.text = chatRoomListItem.sellerNickName
			chatRoomArticleTextView.text = chatRoomListItem.articleTitle
			dateTextView.text = format.format(currentDate).toString()

			this.root.setOnClickListener {
				chatListClickListener(chatRoomListItem)
			}
		}
		// 메세지
		fun bindMessage(){

		}

	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatListViewHolder =
		ChatListViewHolder(
			ItemChatListBinding.inflate(
				LayoutInflater.from(parent.context),
				parent,
				false
			)
		)

	override fun onBindViewHolder(holder: ChatListViewHolder, position: Int) =
		holder.bindViewsWithOutMessage(currentList[position])
}