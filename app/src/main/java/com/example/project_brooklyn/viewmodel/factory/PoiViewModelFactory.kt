package com.example.project_brooklyn.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.project_brooklyn.viewmodel.POIViewModel
import com.example.project_brooklyn.viewmodel.repository.RetrofitRepository
import java.lang.IllegalArgumentException

class PoiViewModelFactory : ViewModelProvider.Factory {

	val poiRepository = RetrofitRepository()

	override fun <T : ViewModel> create(modelClass: Class<T>): T {
		if(modelClass.isAssignableFrom(POIViewModel::class.java)){
			return POIViewModel(poiRepository) as T
		}
		throw IllegalArgumentException("")
	}
}