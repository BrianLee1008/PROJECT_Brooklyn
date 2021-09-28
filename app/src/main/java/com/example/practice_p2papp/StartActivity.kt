package com.example.practice_p2papp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.practice_p2papp.databinding.ActivityStartBinding

class StartActivity : AppCompatActivity() {

	//여기서 로그인 or 회원가입 해야만 MainActivity로 이동

	private lateinit var binding : ActivityStartBinding
	override fun onCreate(savedInstanceState: Bundle?) {
		binding = ActivityStartBinding.inflate(layoutInflater)
		super.onCreate(savedInstanceState)
		setContentView(binding.root)

		setStartButtonListener()
		setSignInButtonListener()
	}

	private fun setStartButtonListener(){
		binding.startButton.setOnClickListener {
			val intent = Intent(this, MainActivity::class.java)
			startActivity(intent)
		}
	}

	private fun setSignInButtonListener(){

	}
}