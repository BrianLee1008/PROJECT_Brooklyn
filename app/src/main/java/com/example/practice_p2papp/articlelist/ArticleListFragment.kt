package com.example.practice_p2papp.articlelist


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.practice_p2papp.FirebaseKey.Companion.DB_ARTICLES
import com.example.practice_p2papp.adapter.ArticleListAdapter
import com.example.practice_p2papp.databinding.FragmentArticleListBinding
import com.example.practice_p2papp.item.ArticleListItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase



class ArticleListFragment : Fragment() {

	private val auth: FirebaseAuth by lazy {
		Firebase.auth
	}

	private lateinit var articleListAdapter: ArticleListAdapter

	private val articleDB: DatabaseReference by lazy {
		Firebase.database.reference.child(DB_ARTICLES)
	}

	private val articleList = mutableListOf<ArticleListItem>()

	private val listener = object : ChildEventListener {
		override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
			val model = snapshot.getValue(ArticleListItem::class.java)
			model ?: return

			articleList.add(model)
			articleListAdapter.submitList(articleList)
		}

		override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
		override fun onChildRemoved(snapshot: DataSnapshot) {}
		override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
		override fun onCancelled(error: DatabaseError) {}

	}

	private var _binding: FragmentArticleListBinding? = null
	private val binding: FragmentArticleListBinding
		get() = _binding!!

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentArticleListBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		articleList.clear()

		initRecyclerView()
		articleDB.addChildEventListener(listener)

	}


	private fun initRecyclerView() = with(binding) {
		articleListAdapter = ArticleListAdapter(

			// 아이템 클릭하면 아이템 홀딩된 데이터 정렬해서 전달
			onClickListener = { articleListItem ->
				if (auth.currentUser == null) {
					return@ArticleListAdapter
				}

				if (auth.currentUser!!.uid != articleListItem.userId) { // 현재 아이디와 아이템에 등록된 아이디 다를 경우
					val articleInfo = ArticleListItem(
						userId = articleListItem.userId,
						nickName = articleListItem.nickName,
						title = articleListItem.title,
						content = articleListItem.content,
						price = articleListItem.price,
						date = articleListItem.date,
						imageUriList = articleListItem.imageUriList,
						userProfileImage = articleListItem.userProfileImage
					)
					val intent = Intent(activity, DetailArticleActivity::class.java)
					intent.putExtra("path", articleInfo)
					startActivity(intent)


				} else { // 같을 경우
					Toast.makeText(requireContext(), "내가 쓴 글", Toast.LENGTH_SHORT).show()
					return@ArticleListAdapter
				}
			}
		)
		articleRecyclerView.run {
			adapter = articleListAdapter
			// 리사이클러뷰 최신순 정렬
			layoutManager =
				LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, true).apply {
					stackFromEnd = true
				}
		}
	}


	@SuppressLint("NotifyDataSetChanged")
	override fun onResume() {
		super.onResume()
		articleListAdapter.notifyDataSetChanged()
	}


	override fun onDestroyView() {
		super.onDestroyView()
		articleDB.removeEventListener(listener)
		_binding = null
	}
}