package com.example.practice_p2papp.item

data class UserItem(
	val userId: String,
	val nickName: String,
	val imageUrl: String?
) {

	constructor() : this("", "", "")


	//  xo 1. DB 수정을 위한 Map
	fun toMap(): Map<String, Any?> = mapOf(
		"userId" to userId,
		"nickName" to nickName,
		"imageUrl" to imageUrl
	)

}