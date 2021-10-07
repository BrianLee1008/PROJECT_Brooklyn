package com.example.practice_p2papp.item

data class UserItem(
	val userId : String,
	val nickName : String
) {

	constructor() : this("","")
}