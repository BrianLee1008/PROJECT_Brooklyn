package com.example.project_brooklyn.signinup

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.project_brooklyn.databinding.ActivitySignUpBinding
import com.example.project_brooklyn.viewmodel.FirebaseDBViewModel
import com.example.project_brooklyn.viewmodel.factory.FirebaseViewModelFactory
import com.example.project_brooklyn.viewmodel.repository.DBRepository

//회원가입
class SignUpActivity : AppCompatActivity() {

	private lateinit var binding: ActivitySignUpBinding
	private val appRepository = DBRepository()

	private val firebaseDBViewModel by viewModels<FirebaseDBViewModel>{ FirebaseViewModelFactory(appRepository) }
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivitySignUpBinding.inflate(layoutInflater)
		setContentView(binding.root)

		setSignUpButtonListener()
	}

	// 가입하기 버튼
	private fun setSignUpButtonListener() = with(binding) {
		signUpButton.setOnClickListener {

			// 예외처리
			val emailEditTextEmpty = emailEditText.text.isEmpty()
			val passwordEditTextEmpty =
				passwordEditText.text.isEmpty() || passwordCheckEditText.text.isEmpty()
			val failedCheckPassword =
				passwordCheckEditText.text.toString() != passwordEditText.text.toString()
			// val untilEtePassword = passwordEditText.text.isNotEmpty() || passwordEditText.text.toString().toInt() < 8


			when {
				emailEditTextEmpty -> {
					emailErrorCheck.isVisible = true
					return@setOnClickListener
				}
				passwordEditTextEmpty -> {
					passwordErrorCheck.isVisible = true
					checkPasswordErrorCheck.isVisible = false
					checkPasswordEmptyCheck.isVisible = true
					emailErrorCheck.isVisible = false
					return@setOnClickListener
				}
				// 8자 이하 예외처리
				/*untilEtePassword -> {
					passwordErrorCheck.isVisible = true
					checkPasswordErrorCheck.isVisible = false
					emailErrorCheck.isVisible = false
					return@setOnClickListener

				}*/
				failedCheckPassword -> {
					passwordEmptyCheck.isVisible = false
					passwordErrorCheck.isVisible = false
					checkPasswordErrorCheck.isVisible = true
					emailErrorCheck.isVisible = false

					return@setOnClickListener
				}
			}

			val email = emailEditText.text.toString()
			val password = passwordCheckEditText.text.toString()

			firebaseDBViewModel.auth.createUserWithEmailAndPassword(email, password)
				.addOnCompleteListener { task ->
					if (task.isSuccessful) {
						Toast.makeText(this@SignUpActivity, "회원가입에 성공했습니다.", Toast.LENGTH_SHORT)
							.show()
						emailErrorCheck.isVisible = false
						passwordErrorCheck.isVisible = false
						checkPasswordEmptyCheck.isVisible = false
						passwordEmptyCheck.isVisible = false
						checkPasswordErrorCheck.isVisible = false

						startSignInActivity()

						emailEditText.text.clear()
						passwordEditText.text.clear()
						passwordCheckEditText.text.clear()

					} else {
						Toast.makeText(
							this@SignUpActivity,
							"회원가입에 실패했습니다. 이메일 비밀번호를 다시 설정해주세요",
							Toast.LENGTH_SHORT
						).show()
					}
				}

		}
	}

	private fun startSignInActivity() {
		val intent = Intent(this,SignInActivity::class.java)
		startActivity(intent)
		finishAffinity()

	}
}