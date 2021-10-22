package com.example.project_brooklyn.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.project_brooklyn.viewmodel.FirebaseDBViewModel
import com.example.project_brooklyn.viewmodel.repository.DBRepository
import java.lang.IllegalArgumentException

class FirebaseViewModelFactory(private val repository: DBRepository) : ViewModelProvider.Factory {
	override fun <T : ViewModel> create(modelClass: Class<T>): T {
		if (modelClass.isAssignableFrom(FirebaseDBViewModel::class.java)) {
			return FirebaseDBViewModel(repository) as T
		}
		throw IllegalArgumentException("")
	}
}