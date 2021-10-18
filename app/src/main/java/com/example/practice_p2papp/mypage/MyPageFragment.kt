package com.example.practice_p2papp.mypage

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.practice_p2papp.FirebaseKey
import com.example.practice_p2papp.databinding.FragmentMypageBinding
import com.example.practice_p2papp.extensions.circleCropImage
import com.example.practice_p2papp.item.UserItem
import com.example.practice_p2papp.mypage.editprofile.DetailProfileActivity
import com.example.practice_p2papp.viewmodel.FirebaseDBViewModel
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

// co 15 다른 아이디로 로그인해도 최초 로그인한 아이디로 로그인됨.
//		뭐가 문제일까. 아마도 DB 최신순으로 업데이트 된 데이터를 가져와서 그런듯. 원랜 안그랬는데 뭐때문에?
// 		로그아웃 눌러도 DB에서 계속 정보를 View에 뿌려주고 있음. 새로운 로그인해도 새로운 아이디로 로그인이 안됨. AuthUID랑 DB userId랑 뭐가 문제가 있나?
//		DB를 새로 추가하면서 업데이트하는 버그를 고치고 난 이후에도 똑같다. 다른 점은
//		BBB를 로그아웃하고 AAA를 로그인했지만 계속 BBB가 뜬다. 그상태에서 BBB의 회원정보를 바꾸면 앱이 꺼지고 DB에선 AAA의 정보가 바뀐것으로 뜬다. 아마 DB 불러오는것에 문제가 있는듯.
//			push()로 child를 고유 값 저장 했을 떄만 ChildEventListener()로 불러오는게 가능한듯 싶다.
// xo 해결해야할 것. 1. DB 추가 없이 업데이트 2. DB최신순으로 데이터 불러오는게 아니라 Auth UID대로 정보 불러오기(이거 가능하면 push말고 userId로 데이터 저장해도됨) 3. 채팅...
// co 해결했다. push로 안하고 userId로 child항목 만들어도 if문으로 uid 체크 해주고 값 가져오게 하면 됨. child 순서 상관없이 로그인도 잘 되고 업데이트도 잘됨. 업데이트 해도 항목 새로 안생김

class MyPageFragment : Fragment() {

	// 하위 child읽어올려고 하면 에러 뜸. 상위 child에서 갱신되는 것들 읽어오는 형식임.
	private val listener = object : ChildEventListener {
		override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
			val model = snapshot.getValue(UserItem::class.java)
			model ?: return

			if(firebaseViewModel.auth.currentUser!!.uid == model.userId){
				binding.profileImageView.circleCropImage(model.imageUrl!!)
				binding.nickNameHint.text = model.nickName
			}
		}
		override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
			val model = snapshot.getValue(UserItem::class.java)
			model ?: return

			binding.profileImageView.circleCropImage(model.imageUrl!!)
			binding.nickNameHint.text = model.nickName
		}
		override fun onChildRemoved(snapshot: DataSnapshot) {}
		override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
		override fun onCancelled(error: DatabaseError) {}
	}


	private var _binding: FragmentMypageBinding? = null
	private val binding: FragmentMypageBinding
		get() = _binding!!

	private lateinit var firebaseViewModel : FirebaseDBViewModel

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentMypageBinding.inflate(inflater, container, false)
		firebaseViewModel = ViewModelProvider(this)[FirebaseDBViewModel::class.java]
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		if(firebaseViewModel.auth.currentUser == null){
			Toast.makeText(activity, "로그인 해주세요.", Toast.LENGTH_SHORT).show()
			return
		}
		firebaseViewModel.userDB.child(FirebaseKey.DB_USER_INFO).addChildEventListener(listener)

		setEditProfileButtonListener()


	}

	private fun setEditProfileButtonListener() = with(binding) {
		profileImageView.setOnClickListener {
			if (firebaseViewModel.auth.currentUser == null) {
				return@setOnClickListener
			}
			val intent = Intent(requireContext(), DetailProfileActivity::class.java)
			startActivity(intent)
		}

	}


	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}