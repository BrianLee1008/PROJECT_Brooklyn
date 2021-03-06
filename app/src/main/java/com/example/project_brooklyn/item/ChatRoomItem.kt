package com.example.project_brooklyn.item

import java.io.Serializable

data class ChatRoomItem(
	val userId : String,
	val sellerNickName : String,
	val sellerProfileImageUrl : String,
	val message : String,
	val currentTime : Long,
	val articleTitle : String

) : Serializable {

	constructor() : this("","","","",0,"")
}