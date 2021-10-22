package com.example.project_brooklyn.articlelist


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_brooklyn.adapter.ArticleListAdapter
import com.example.project_brooklyn.databinding.FragmentArticleListBinding
import com.example.project_brooklyn.item.ArticleListItem
import com.example.project_brooklyn.viewmodel.FirebaseDBViewModel
import com.example.project_brooklyn.viewmodel.factory.FirebaseViewModelFactory
import com.example.project_brooklyn.viewmodel.repository.DBRepository



class ArticleListFragment : Fragment() {

	private lateinit var articleListAdapter: ArticleListAdapter
	private val appRepository = DBRepository()

	private var _binding: FragmentArticleListBinding? = null
	private val binding: FragmentArticleListBinding
		get() = _binding!!

	private val firebaseDBViewModel by viewModels<FirebaseDBViewModel> {
		FirebaseViewModelFactory(
			appRepository
		)
	}



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

		articleListObserve()
		startDetailArticleActivity()
		initRecyclerView()


	}

	// ArticleList Read
	@SuppressLint("NotifyDataSetChanged")
	private fun articleListObserve() {
		firebaseDBViewModel.articleListLiveData
			.observe(
				viewLifecycleOwner,
				{

					// ForEach가 문제였네... 하나하나 꺼내서 넣어주니까 중복이 생길수밖에 없지
					articleListAdapter.submitList(it)
					articleListAdapter.notifyDataSetChanged()

				}
			)
	}

	private fun startDetailArticleActivity() {
		articleListAdapter = ArticleListAdapter(

			// 아이템 클릭하면 아이템 홀딩된 데이터 정렬해서 전달
			onClickListener = { articleListItem ->
				if (firebaseDBViewModel.auth.currentUser == null) {
					return@ArticleListAdapter
				}

				// 현재 아이디와 아이템에 등록된 아이디 다를 경우
				if (firebaseDBViewModel.auth.currentUser!!.uid != articleListItem.userId) {
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
					// 데이터 가지고 상세페이지 이동
					val intent = Intent(activity, DetailArticleActivity::class.java)
					intent.putExtra("path", articleInfo)
					startActivity(intent)


				} else { // 같을 경우
					Toast.makeText(requireContext(), "내가 쓴 글", Toast.LENGTH_SHORT).show()
					return@ArticleListAdapter
				}
			}
		)
	}

	private fun initRecyclerView() = with(binding) {
		articleRecyclerView.run {
			adapter = articleListAdapter

			// 리사이클러뷰 최신순 정렬
			layoutManager =
				LinearLayoutManager(
					requireContext(),
					LinearLayoutManager.VERTICAL,
					true
				).apply {
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
		_binding = null
	}
}