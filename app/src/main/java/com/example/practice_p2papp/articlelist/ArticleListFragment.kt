package com.example.practice_p2papp.articlelist


import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.practice_p2papp.FirebaseKey.Companion.DB_ARTICLES
import com.example.practice_p2papp.adapter.ArticleListAdapter
import com.example.practice_p2papp.databinding.FragmentArticleListBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class ArticleListFragment : Fragment() {

	private val imageUriList = mutableListOf<Uri>()

	private lateinit var articleListAdapter: ArticleListAdapter

	private val articleDB: DatabaseReference by lazy {
		Firebase.database.reference.child(DB_ARTICLES)
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

		initRecyclerView()

	}


	private fun initRecyclerView() = with(binding) {
		articleListAdapter = ArticleListAdapter(onClickListener = {})
		articleRecyclerView.run {
			adapter = articleListAdapter
			layoutManager = LinearLayoutManager(requireContext())
		}
	}

	// TODO 4. 아이템 클릭하면 DB에서 값 꺼내오는 동시에 아이템 상세페이지로 이동, 값 전달


	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}