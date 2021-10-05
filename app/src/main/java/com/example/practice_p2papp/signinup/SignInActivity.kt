package com.example.practice_p2papp.signinup

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.practice_p2papp.FirebaseKey.Companion.DB_USER_INFO
import com.example.practice_p2papp.MainActivity
import com.example.practice_p2papp.databinding.ActivitySignInBinding
import com.example.practice_p2papp.item.UserItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

// 세부정보 기입 후 관리
// TODO 2. UserDB 만든다음 nickName DB에 저장. 꺼내올 때 해당 UserId와 똑같은 것에서 꺼내오게 auth.currentUserId 도 같이 저장
class SignInActivity : AppCompatActivity() {

	private val auth: FirebaseAuth by lazy {
		Firebase.auth
	}

	private val userDB: DatabaseReference by lazy {
		Firebase.database.reference
	}

	private lateinit var binding: ActivitySignInBinding
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivitySignInBinding.inflate(layoutInflater)
		setContentView(binding.root)

		setSignUpButtonListener()
	}

	// DB에 UserInfo child 만들고 저장
	private fun setSignUpButtonListener() = with(binding) {
		signUpButton.setOnClickListener {

			if (auth.currentUser == null) {
				Toast.makeText(this@SignInActivity, "회원가입 정보가 저장되지 않았습니다. 다시 가입해주세요", Toast.LENGTH_SHORT).show()
				return@setOnClickListener

			} else {

//				val isUntilFourNickName = nickNameEditText.text.toString().toInt()
				val isEmptyNickName = nickNameEditText.text.isEmpty()

				when {
					isEmptyNickName -> {
						nickNameEmptyCheck.isVisible = true
						return@setOnClickListener
					}
					else -> {

					}

				}

				val userId = auth.currentUser!!.uid
				val nickName = nickNameEditText.text.toString()

				uploadUserInfo(userId, nickName)
				nickNameEmptyCheck.isVisible = false
			}

		}

	}

	private fun uploadUserInfo(userId : String, nickName : String){
		val model = UserItem(userId, nickName)

		userDB.child(DB_USER_INFO).push().setValue(model)
		startMainActivity()
	}

	private fun startMainActivity() {
		startActivity(Intent(this, MainActivity::class.java))
		finish()
	}
}