package com.example.practice_p2papp.mypage

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.practice_p2papp.StartActivity
import com.example.practice_p2papp.databinding.FragmentMypageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MyPageFragment  : Fragment() {

	private val auth : FirebaseAuth by lazy {
		Firebase.auth
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