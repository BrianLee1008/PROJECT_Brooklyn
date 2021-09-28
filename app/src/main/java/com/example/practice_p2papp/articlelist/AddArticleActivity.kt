package com.example.practice_p2papp.articlelist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.practice_p2papp.databinding.ActivityAddArticleBinding

class AddArticleActivity : AppCompatActivity() {


	private lateinit var binding: ActivityAddArticleBinding
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityAddArticleBinding.inflate(layoutInflater)
		setContentView(binding.root)
	}
}