package com.example.practice_p2papp.signinup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.practice_p2papp.MainActivity
import com.example.practice_p2papp.databinding.ActivityBrooklynEmailSignInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class BrooklynEmailSignInActivity : AppCompatActivity() {

	private val auth : FirebaseAuth by lazy {
		Firebase.auth
	}

	private lateinit var binding : ActivityBrooklynEmailSignInBinding
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityBrooklynEmailSignInBinding.inflate(layoutInflater)
		setContentView(binding.root)

		if (auth.currentUser != null){
			finish()
		}

		loadSignReferenceToAuth()

	}

	private fun loadSignReferenceToAuth() = with(binding){
		signInButton.setOnClickListener {
			val email = emailEditText.text.toString()
			val passwordd = passwordEditText.text.toString()

			// 예외처리... 중복되는데 확장함수로 뺄 순 없나?
			val emailEditTextEmpty = emailEditText.text.isEmpty()
			val passwordEditTextEmpty = passwordEditText.text.isEmpty()


			when {
				emailEditTextEmpty -> {
					emailErrorCheck.isVisible = true
					return@setOnClickListener
				}
				passwordEditTextEmpty -> {
					passwordErrorCheck.isVisible = true
					emailErrorCheck.isVisible = false
					return@setOnClickListener
				}
				// 8자 이하 예외처리 할려했는데 안됨
				/*untilEtePassword -> {
					passwordErrorCheck.isVisible = true
					checkPasswordErrorCheck.isVisible = false
					emailErrorCheck.isVisible = false
					return@setOnClickListener

				}*/
			}


			if(auth.currentUser == null){ // 아직 로그인을 안했다면
				auth.signInWithEmailAndPassword(email,passwordd).
				addOnCompleteListener { task ->
					if(task.isSuccessful){
						successSignIn()
						Toast.makeText(this@BrooklynEmailSignInActivity, "로그인에 성공했습니다", Toast.LENGTH_SHORT)
							.show()
						startMainActivity()
						finish()
					} else {
						Toast.makeText(this@BrooklynEmailSignInActivity, "로그인에 실패했습니다. 이메일과 비밀번호를 확인해주세요", Toast.LENGTH_SHORT)
							.show()
					}
				}
			} else { // 이미 로그인을 했다면
				Toast.makeText(this@BrooklynEmailSignInActivity, "로그인 이미 했다규", Toast.LENGTH_SHORT)
					.show()
				return@setOnClickListener
			}

		}

	}

	private fun successSignIn() = with(binding){
		if(auth.currentUser == null){
			Toast.makeText(this@BrooklynEmailSignInActivity, "로그인에 실패했습니다", Toast.LENGTH_SHORT).show()
			return
		}
		emailEditText.text.clear()
		passwordEditText.text.clear()
	}

	private fun startMainActivity(){
		val intent = Intent(this, MainActivity::class.java)
		startActivity(intent)
	}
}