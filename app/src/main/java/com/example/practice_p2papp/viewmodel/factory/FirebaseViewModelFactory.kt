package com.example.practice_p2papp.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.practice_p2papp.viewmodel.FirebaseDBViewModel
import com.example.practice_p2papp.viewmodel.repository.AppRepository
import java.lang.IllegalArgumentException

class FirebaseViewModelFactory(private val repository: AppRepository) : ViewModelProvider.Factory {
	override fun <T : ViewModel> create(modelClass: Class<T>): T {
		if (modelClass.isAssignableFrom(FirebaseDBViewModel::class.java)) {
			return FirebaseDBViewModel(repository) as T
		}
		throw IllegalArgumentException("")
	}
}