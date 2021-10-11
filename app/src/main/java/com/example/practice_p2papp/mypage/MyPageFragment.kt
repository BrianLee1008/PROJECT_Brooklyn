package com.example.practice_p2papp.mypage

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.practice_p2papp.FirebaseKey
import com.example.practice_p2papp.databinding.FragmentMypageBinding
import com.example.practice_p2papp.extensions.authCheck
import com.example.practice_p2papp.extensions.circleCropImage
import com.example.practice_p2papp.item.UserItem
import com.example.practice_p2papp.mypage.editprofile.DetailProfileActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MyPageFragment : Fragment() {

	private val auth: FirebaseAuth by lazy {
		Firebase.auth
	}
	private val userDB: DatabaseReference by lazy {
		Firebase.database.reference.child(FirebaseKey.DB_USER_INFO)
	}

	// 하위 child읽어올려고 하면 에러 뜸. 상위 child에서 갱신되는 것들 읽어오는 형식임.
	private val listener = object : ChildEventListener {
		override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
			val model = snapshot.getValue(UserItem::class.java)
			model ?: return

			binding.profileImageView.circleCropImage(model.imageUrl!!)
			binding.nickNameHint.text = model.nickName
		}

		override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
		override fun onChildRemoved(snapshot: DataSnapshot) {}
		override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
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

		userDB.addChildEventListener(listener)

		setEditProfileButtonListener()


	}

	private fun setEditProfileButtonListener() = with(binding) {
		auth.authCheck()
		profileImageView.setOnClickListener {
			startActivity(Intent(requireContext(), DetailProfileActivity::class.java))
		}

	}


	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}