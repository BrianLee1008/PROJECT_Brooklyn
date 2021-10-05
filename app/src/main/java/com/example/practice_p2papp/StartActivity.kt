package com.example.practice_p2papp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.practice_p2papp.databinding.ActivityStartBinding
import com.example.practice_p2papp.signinup.LoginMethodActivity
import com.example.practice_p2papp.signinup.SignUpActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

//여기서 로그인 or 회원가입 해야만 MainActivity로 이동
class StartActivity : AppCompatActivity() {

	private val auth by lazy {
		Firebase.auth
	}

	private lateinit var binding : ActivityStartBinding
	override fun onCreate(savedInstanceState: Bundle?) {
		binding = ActivityStartBinding.inflate(layoutInflater)
		super.onCreate(savedInstanceState)
		setContentView(binding.root)

		if(auth.currentUser != null){
			startActivity(Intent(this, MainActivity::class.java))
			finish()
		}

		setStartButtonListener()
		startLoginMethodActivity()


	}

	// 회원가입
	private fun setStartButtonListener(){
		binding.startButton.setOnClickListener {
			val intent = Intent(this, SignUpActivity::class.java)
			startActivity(intent)
			finish()
		}
	}

	// 로그인 방법 선택
	private fun startLoginMethodActivity(){
		binding.signInButton.setOnClickListener {
			val intent = Intent(this, LoginMethodActivity()::class.java)
			startActivity(intent)
		}

	}
}