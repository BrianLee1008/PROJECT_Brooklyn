package com.example.project_brooklyn

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.project_brooklyn.articlelist.AddArticleActivity
import com.example.project_brooklyn.articlelist.ArticleListFragment
import com.example.project_brooklyn.chatlist.ChatListFragment
import com.example.project_brooklyn.databinding.ActivityMainBinding
import com.example.project_brooklyn.mypage.MyPageFragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

	// co 바로 MVVM 들어가야 겠다.
	// 일단 채팅방 중복생성에 필요한 검증 개체를 가져오기가 힘듬. 먼저 MVVM 리팩토링 하면서 하나하나 점검하자
	// 그리고 회원가입하고 앱 처음 깔면 이전 아이템들중 이미지는 전부 마지막에 올린 이미지로 도배가 된다.

	private val auth by lazy {
		Firebase.auth
	}


	private val articleFragment = ArticleListFragment()
	private val chatRoomListFragment = ChatListFragment()
	private val myPageFragment = MyPageFragment()


	private lateinit var binding: ActivityMainBinding
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding.root)

		setBottomNavigationView()
		replaceFragment(articleFragment)
		startAddArticleActivity()

		binding.bottomNavigationBar.background = null
		binding.bottomNavigationBar.menu.getItem(2).isEnabled = false

	}

	private fun setBottomNavigationView(){
		binding.bottomNavigationBar.setOnNavigationItemSelectedListener { menuItem ->
			when(menuItem.itemId){
				R.id.home -> replaceFragment(articleFragment)
				R.id.chatList -> replaceFragment(chatRoomListFragment)
				R.id.myPage -> replaceFragment(myPageFragment)

			}
			return@setOnNavigationItemSelectedListener true
		}
	}

	private fun replaceFragment(fragment: Fragment) {
		val transaction = supportFragmentManager.beginTransaction()
		transaction.replace(binding.fragmentContainer.id, fragment).commit()
	}

	private fun startAddArticleActivity() {
		binding.addFloatingButton.setOnClickListener {
			if (auth.currentUser?.uid != null) {
			val intent = Intent(this, AddArticleActivity::class.java)
			startActivity(intent)
			} else {
				Snackbar.make(binding.root, "로그인을 해주세요", Snackbar.LENGTH_LONG).show()
				return@setOnClickListener
			}

		}
	}
}