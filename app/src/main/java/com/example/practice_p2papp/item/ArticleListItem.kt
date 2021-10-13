package com.example.practice_p2papp.item


import java.io.Serializable

data class ArticleListItem(
	val userId: String,
	val nickName: String,
	val title: String,
	val content: String,
	val price: String,
	val date: Long,
	val imageUriList: List<String>,
	val userProfileImage: String
) : Serializable {

	constructor() : this("", "", "", "", "", 0, listOf(), "")
}