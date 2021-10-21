package com.example.project_brooklyn.item

data class ChatRoomListItem(
	val sellerId : String,
	val buyerNickName: String,
	val sellerNickName: String,
	val buyerProfileImage: String,
	val currentTime: Long,
	val articleTitle : String
) {
	constructor() : this("","", "", "", 0,"")
}