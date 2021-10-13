package com.example.practice_p2papp.item

import java.io.Serializable

data class ChatRoomItem(
	val sellerId : String,
	val buyerNickName : String,
	val sellerNickName : String,
	val message : String

) : Serializable {

	constructor() : this("","","","")
}