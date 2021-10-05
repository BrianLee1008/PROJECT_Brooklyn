package com.example.practice_p2papp.articlelist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.practice_p2papp.databinding.ActivityAddArticleBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class AddArticleActivity : AppCompatActivity() {

	private val auth : FirebaseAuth by lazy {
		Firebase.auth
	}
	private val articleDB : DatabaseReference by lazy {
		Firebase.database.reference
	}


	private lateinit var binding: ActivityAddArticleBinding
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityAddArticleBinding.inflate(layoutInflater)
		setContentView(binding.root)

		setImageAddButtonListener()
	}

	// 이미지 업로드
	// TODO 3. xml RecylclerView로 변경
	private fun setImageAddButtonListener(){

	}

	// 아이템 등록 버튼
	// DB에 항목 업로드, userId, nickName, title, content, price, date, imageUriList
	private fun setSubmitButtonListener() = with(binding){
		submitButton.setOnClickListener {
			val userId = auth.currentUser?.uid.toString()


			// imageUrlList가 있을 떄와 없을떄로 나누어짐

		}

	}



}