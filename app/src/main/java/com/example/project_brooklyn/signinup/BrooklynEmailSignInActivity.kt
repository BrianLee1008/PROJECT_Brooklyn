package com.example.project_brooklyn.signinup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.example.project_brooklyn.MainActivity
import com.example.project_brooklyn.databinding.ActivityBrooklynEmailSignInBinding
import com.example.project_brooklyn.viewmodel.FirebaseDBViewModel
import com.example.project_brooklyn.viewmodel.factory.FirebaseViewModelFactory
import com.example.project_brooklyn.viewmodel.repository.AppRepository

class BrooklynEmailSignInActivity : AppCompatActivity() {


	private lateinit var binding : ActivityBrooklynEmailSignInBinding
	private val appRepository = AppRepository()

	private val firebaseDBViewModel by viewModels<FirebaseDBViewModel>{ FirebaseViewModelFactory(appRepository) }

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityBrooklynEmailSignInBinding.inflate(layoutInflater)
		setContentView(binding.root)

		if (firebaseDBViewModel.auth.currentUser != null){
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

			firebaseDBViewModel.auth.signInWithEmailAndPassword(email,passwordd).
				addOnCompleteListener(this@BrooklynEmailSignInActivity) { task ->
					if(task.isSuccessful){
						successSignIn()
						Toast.makeText(this@BrooklynEmailSignInActivity, "로그인에 성공했습니다", Toast.LENGTH_SHORT)
							.show()
						startMainActivity()
						finishAffinity()
					} else {
						Toast.makeText(this@BrooklynEmailSignInActivity, "로그인에 실패했습니다. 이메일과 비밀번호를 확인해주세요", Toast.LENGTH_SHORT)
							.show()
					}
				}
			}
	}

	private fun successSignIn() = with(binding){
		if(firebaseDBViewModel.auth.currentUser == null){
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