package com.example.project_brooklyn.retrofit

import com.example.project_brooklyn.item.retrofitmodel.retrofitData.SearchResponse
import com.example.project_brooklyn.retrofit.utility.RetrofitUrl
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query


// 레트로핏 인터페이스
interface TmapService {

	@GET(RetrofitUrl.T_MAP_END_POINT)
	suspend fun searchKeyword(
		@Header("appkey") appkey: String = RetrofitUrl.API_KEY,
		@Query("version") version: Int = 1,
		@Query("callback") callback: String? = null,
		@Query("count") count: Int = 20,
		@Query("searchKeyword") keyword: String,
		@Query("areaLLCode") areaLLCode: String? = null,
		@Query("areaLMCode") areaLMCode: String? = null,
		@Query("resCoordType") resCoordType: String? = null,
		@Query("searchType") searchType: String? = null,
		@Query("multiPoint") multiPoint: String? = null,
		@Query("searchtypCd") searchtypCd: String? = null,
		@Query("radius") radius: String? = null,
		@Query("reqCoordType") reqCoordType: String? = null,
		@Query("centerLon") centerLon: String? = null,
		@Query("centerLat") centerLat: String? = null
	): Response<SearchResponse>
}