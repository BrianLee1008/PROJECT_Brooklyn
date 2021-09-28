package com.example.practice_p2papp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.practice_p2papp.articlelist.ArticleListFragment
import com.example.practice_p2papp.chatlist.ChatListFragment
import com.example.practice_p2papp.databinding.ActivityMainBinding
import com.example.practice_p2papp.mypage.MyPageFragment

class MainActivity : AppCompatActivity() {

	/* TODO item xml 비롯해 xml 전체적으로 수정*/

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
}