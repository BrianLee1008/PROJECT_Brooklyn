package com.example.project_brooklyn.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_brooklyn.item.retrofitmodel.LocationLatLngItem
import com.example.project_brooklyn.item.retrofitmodel.SearchResultItem
import com.example.project_brooklyn.item.retrofitmodel.retrofitData.SearchResponse
import com.example.project_brooklyn.viewmodel.repository.RetrofitRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class POIViewModel(private val repository: RetrofitRepository) : ViewModel() {

	private var _searchPoiLiveData = MutableLiveData<Response<SearchResponse>>()
	val searchPoiLiveData: MutableLiveData<Response<SearchResponse>>
		get() = _searchPoiLiveData

	// 레트로핏 서버 통신 후 키워드 입력하면 가져오는 데이터에 LiveData 구독
	fun getSearchPoiResult(keyword: String) {
		viewModelScope.launch {
			val response = repository.getSearchView(keyword)
			_searchPoiLiveData.value = response
		}
	}

	private var _poiResultLiveData = MutableLiveData<SearchResultItem>()
	val poiResultLiveData: LiveData<SearchResultItem>
		get() = _poiResultLiveData

	fun insertSearchResult(searchResultItem: SearchResultItem){
			val data = SearchResultItem(
				fullAddress = searchResultItem.fullAddress,
				locationName = searchResultItem.locationName,
				locationLatLngItem = LocationLatLngItem(
					latitude = searchResultItem.locationLatLngItem.latitude,
					longitude = searchResultItem.locationLatLngItem.longitude
				)
			)
		//xo 여기서 값이 안들어감
			_poiResultLiveData.value = data

	}
}