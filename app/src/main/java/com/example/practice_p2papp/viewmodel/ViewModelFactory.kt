package com.example.practice_p2papp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class ViewModelFactory : ViewModelProvider.Factory{


	override fun <T : ViewModel> create(modelClass: Class<T>): T {
		//modelClass에서 할당한 ViewModel이라면 파라미터 적용 가능
		if(modelClass.isAssignableFrom(ArticleViewModel::class.java)){
			return ArticleViewModel() as T
		}
		throw IllegalArgumentException("")


	}
}