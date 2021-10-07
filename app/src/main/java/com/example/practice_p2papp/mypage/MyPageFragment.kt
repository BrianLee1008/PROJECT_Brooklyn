package com.example.practice_p2papp.mypage

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.practice_p2papp.FirebaseKey
import com.example.practice_p2papp.StartActivity
import com.example.practice_p2papp.databinding.FragmentMypageBinding
import com.example.practice_p2papp.item.UserItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MyPageFragment  : Fragment() {

	private val auth : FirebaseAuth by lazy {
		Firebase.auth
	}
	private val userDB : DatabaseReference by lazy {
		Firebase.database.reference.child(FirebaseKey.DB_USER_INFO)
	}
	private val listener = object : ValueEventListener{
		override fun onDataChange(snapshot: DataSnapshot) {
			snapshot.children.forEach{
				val model = it.getValue(UserItem::class.java)
				model ?: return@forEach

				binding.nickNameHint.text = model.nickName
			}
		}
		override fun onCancelled(error: DatabaseError) {}
	}


	private var _binding: FragmentMypageBinding? = null
	private val binding: FragmentMypageBinding
		get() = _binding!!

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentMypageBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		userDB.addListenerForSingleValueEvent(listener)

		setSignOutButtonListener()


	}

	private fun setSignOutButtonListener(){
		binding.signOutButton.setOnClickListener {
			if(auth.currentUser == null){
				return@setOnClickListener
			}
			auth.signOut()
			Toast.makeText(requireActivity(), "로그아웃 했습니다", Toast.LENGTH_SHORT).show()
			startActivity(Intent(context,StartActivity::class.java))
		}
	}


	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}