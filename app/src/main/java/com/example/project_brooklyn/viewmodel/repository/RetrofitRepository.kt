package com.example.project_brooklyn.viewmodel.repository

import com.example.project_brooklyn.item.retrofitmodel.LocationLatLngItem
import com.example.project_brooklyn.item.retrofitmodel.SearchResultItem
import com.example.project_brooklyn.item.retrofitmodel.retrofitData.SearchResponse
import com.example.project_brooklyn.retrofit.utility.RetrofitUtility
import retrofit2.Response

class RetrofitRepository {

	// 레트로핏 서버통신 후 구현
	suspend fun getSearchView(keyword: String): Response<SearchResponse> {
		return RetrofitUtility.retrofit.searchKeyword(keyword = keyword)
	}



}