package com.example.practice_p2papp.item

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