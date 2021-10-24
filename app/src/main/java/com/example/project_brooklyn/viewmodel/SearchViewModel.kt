package com.example.project_brooklyn.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_brooklyn.item.room.entity.HistoryEntity
import com.example.project_brooklyn.viewmodel.repository.SearchHistoryRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchViewModel(private val searchHistoryRepo: SearchHistoryRepo) : ViewModel() {


	val searchHistoryLiveData = searchHistoryRepo.getAllHistory()


	fun insertKeyword(keyword: String) {
		viewModelScope.launch(Dispatchers.IO) {
			val data = HistoryEntity(uid = null, keyword = keyword)

			searchHistoryRepo.historyInsert(data)
		}
	}


	fun removeHistory(keyword: String){
		viewModelScope.launch(Dispatchers.IO) {
			searchHistoryRepo.historyDelete(keyword)
		}


	}


}