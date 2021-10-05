package com.example.practice_p2papp.item

import android.net.Uri

data class ArticleListItem(
	val userId: String,
	val title: String,
	val content: String,
	val price: String,
	val date: Long,
	val imageUriList: List<Uri>
) {

	constructor() : this("", "", "", "",0, listOf())
}