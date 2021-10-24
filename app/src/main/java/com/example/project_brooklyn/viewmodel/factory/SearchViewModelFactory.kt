package com.example.project_brooklyn.viewmodel.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.project_brooklyn.viewmodel.SearchViewModel
import com.example.project_brooklyn.viewmodel.repository.SearchHistoryRepo
import java.lang.IllegalArgumentException

class SearchViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
	private val searchHistoryRepo = SearchHistoryRepo(application)

	override fun <T : ViewModel> create(modelClass: Class<T>): T {
		if(modelClass.isAssignableFrom(SearchViewModel::class.java)){
			return SearchViewModel(searchHistoryRepo) as T
		}
		throw IllegalArgumentException("")
	}
}