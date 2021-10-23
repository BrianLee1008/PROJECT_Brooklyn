package com.example.project_brooklyn.map

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_brooklyn.adapter.SearchResultAdapter
import com.example.project_brooklyn.databinding.ActivitySearchResultBinding
import com.example.project_brooklyn.item.retrofitmodel.LocationLatLngItem
import com.example.project_brooklyn.item.retrofitmodel.SearchResultItem
import com.example.project_brooklyn.item.retrofitmodel.retrofitData.Pois
import com.example.project_brooklyn.viewmodel.POIViewModel
import com.example.project_brooklyn.viewmodel.factory.PoiViewModelFactory

/**
 * 1. 검색화면 리사이클러뷰로 구성.
 * 2. 검색 바 누르면 이동
 * 3. 최근검색보기는 Room으로 구성
 * 4. POI데이터로 검색하고 검색데이터 받아오면 리사이클러뷰에 뿌려주기.
 * 5. 아이템에 클릭리스너 달아서 클릭하면 지도에 마커 표시
 * 6. 마커 표시한 곳에 대한 정보 BottomSheet에 표시
 * */

class SearchResultActivity : AppCompatActivity() {

	private lateinit var adapter: SearchResultAdapter

	private lateinit var binding: ActivitySearchResultBinding

	private val viewModel by viewModels<POIViewModel> { PoiViewModelFactory() }
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivitySearchResultBinding.inflate(layoutInflater)
		setContentView(binding.root)

		initAdapterAndItemClickListener()
		initRecyclerView()
		getSearchPoiResult()
	}

	private fun initAdapterAndItemClickListener() {
		adapter = SearchResultAdapter{
			Toast.makeText(this, "위도 : ${it.locationLatLngItem.latitude} // 경도 : ${it.locationLatLngItem.longitude}", Toast.LENGTH_SHORT).show()
		}
		// todo 클릭리스너
	}

	private fun initRecyclerView() = with(binding) {
		recentSearchRecyclerView.adapter = adapter
		recentSearchRecyclerView.layoutManager = LinearLayoutManager(this@SearchResultActivity)
	}

	// 키워드 입력
	private fun getSearchPoiResult() = with(binding) {
		searchImageView.setOnClickListener {
			viewModel.getSearchPoiResult(searchEditText.text.toString())

			getSearchResult()
		}


	}

	// Poi에서 받는 데이터 SearchResponseItem에 대입
	private fun getSearchResult() {
		viewModel.searchPoiLiveData.observe(
			this@SearchResultActivity,
			{
				try {
					if (it.isSuccessful.not()) {
						Toast.makeText(this@SearchResultActivity, "레트로핏 통신 실패", Toast.LENGTH_SHORT)
							.show()
						Log.d("retrofitTest",it.body().toString())
						return@observe
					}
					it.body()?.let { searchResponse ->
						setMockingData(searchResponse.searchPoiInfo.pois)
					}

				} catch (e:Exception){
					e.printStackTrace()
					Toast.makeText(this@SearchResultActivity, "검색이 제대로 되지 않았습니다.", Toast.LENGTH_SHORT).show()
				}

			}
		)
	}


	private fun setMockingData(pois: Pois) {
		val dataList = pois.poi.map {
			SearchResultItem(
				locationName = "건물 : ${it.name ?: "건물명 없음"}",
				fullAddress = "주소 : ${it.roadName}",
				locationLatLngItem = LocationLatLngItem(
					// 좌표값 명시
					latitude = it.noorLat,
					longitude = it.noorLon
				)
			)
		}
		adapter.submitList(dataList)
	}

}