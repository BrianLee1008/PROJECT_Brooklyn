package com.example.practice_p2papp.signinup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.practice_p2papp.databinding.ActivitySignInBinding

// 세부정보 기입 후 관리
class SignInActivity : AppCompatActivity() {

	private lateinit var binding : ActivitySignInBinding
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivitySignInBinding.inflate(layoutInflater)
		setContentView(binding.root)
	}
}