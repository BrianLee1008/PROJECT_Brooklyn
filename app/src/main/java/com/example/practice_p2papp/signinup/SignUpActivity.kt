package com.example.practice_p2papp.signinup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.practice_p2papp.databinding.ActivitySignUpBinding

//회원가입
class SignUpActivity : AppCompatActivity() {

	private lateinit var binding : ActivitySignUpBinding
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivitySignUpBinding.inflate(layoutInflater)
		setContentView(binding.root)
	}
}