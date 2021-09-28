package com.example.practice_p2papp.articlelist


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.practice_p2papp.databinding.FragmentArticleListBinding
import com.google.android.material.snackbar.Snackbar


class ArticleListFragment : Fragment() {


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

		startAddArticleActivity()
	}

	private fun startAddArticleActivity() {
		binding.addFloatingButton.setOnClickListener {
//			if (auth.currentUser?.uid != null) {
				val intent = Intent(requireActivity(), AddArticleActivity::class.java)
				startActivity(intent)
//			} else {
//				Snackbar.make(binding.root, "로그인 하셈", Snackbar.LENGTH_LONG).show()
//				return@setOnClickListener
//			}

		}
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}