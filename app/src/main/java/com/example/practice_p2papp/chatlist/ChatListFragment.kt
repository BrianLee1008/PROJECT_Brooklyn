package com.example.practice_p2papp.chatlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.practice_p2papp.databinding.FragmentChatlistBinding

class ChatListFragment : Fragment() {


	private var _binding: FragmentChatlistBinding? = null
	private val binding: FragmentChatlistBinding
		get() = _binding!!

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentChatlistBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

	}


	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}