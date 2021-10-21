package com.example.project_brooklyn.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.project_brooklyn.viewmodel.ArticleViewModel
import java.lang.IllegalArgumentException

class ArticleViewModelFactory : ViewModelProvider.Factory{


	override fun <T : ViewModel> create(modelClass: Class<T>): T {
		//modelClass에서 할당한 ViewModel이라면 파라미터 적용 가능
		if(modelClass.isAssignableFrom(ArticleViewModel::class.java)){
			return ArticleViewModel() as T
		}
		throw IllegalArgumentException("")


	}
}