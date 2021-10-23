package com.example.project_brooklyn.retrofit.utility

import com.example.project_brooklyn.retrofit.TmapService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// 레트로핏 구현체 빌드
object RetrofitUtility {


	private fun retrofitBuild(): Retrofit {
		return Retrofit.Builder()
			.baseUrl(RetrofitUrl.T_MAP_BASE_URL)
			.addConverterFactory(GsonConverterFactory.create())
			.build()
	}

	// 레트로핏 구현체 - Repo에서 LiveData
	val retrofit: TmapService by lazy {
		retrofitBuild().create(TmapService::class.java)
	}


}