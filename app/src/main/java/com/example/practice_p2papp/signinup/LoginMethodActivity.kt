package com.example.practice_p2papp.signinup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.practice_p2papp.databinding.ActivityLoginMethodBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

// 로그인 방법 정리
class LoginMethodActivity : AppCompatActivity() {

	private lateinit var binding : ActivityLoginMethodBinding
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityLoginMethodBinding.inflate(layoutInflater)
		setContentView(binding.root)

		setBasicSignInButtonListener()

	}

	private fun setBasicSignInButtonListener(){
		binding.basicSignInButton.setOnClickListener {
			val intent = Intent(this, BrooklynEmailSignInActivity::class.java)
			startActivity(intent)
		}
	}
}