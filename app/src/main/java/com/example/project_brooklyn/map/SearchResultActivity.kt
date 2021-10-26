package com.example.project_brooklyn.map

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_brooklyn.adapter.RecentSearchAdapter
import com.example.project_brooklyn.adapter.SearchResultAdapter
import com.example.project_brooklyn.databinding.ActivitySearchResultBinding
import com.example.project_brooklyn.item.retrofitmodel.LocationLatLngItem
import com.example.project_brooklyn.item.retrofitmodel.SearchResultItem
import com.example.project_brooklyn.item.retrofitmodel.retrofitData.Pois
import com.example.project_brooklyn.map.ResultMarkerMapActivity.Companion.INTENT_KEY
import com.example.project_brooklyn.viewmodel.POIViewModel
import com.example.project_brooklyn.viewmodel.SearchViewModel
import com.example.project_brooklyn.viewmodel.factory.PoiViewModelFactory
import com.example.project_brooklyn.viewmodel.factory.SearchViewModelFactory
import kotlinx.android.synthetic.main.activity_search_result.*
import kotlinx.android.synthetic.main.item_recent_search.*
import kotlinx.coroutines.*

class SearchResultActivity : AppCompatActivity() {

	private lateinit var searchResultAdapter: SearchResultAdapter
	private lateinit var recentSearchAdapter: RecentSearchAdapter

	private lateinit var dataList: List<SearchResultItem>


	private lateinit var binding: ActivitySearchResultBinding

	private val viewModel by viewModels<POIViewModel> { PoiViewModelFactory() }
	private val searchViewModel by viewModels<SearchViewModel> { SearchViewModelFactory(application) }
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivitySearchResultBinding.inflate(layoutInflater)
		setContentView(binding.root)

		initSearchViews()
		recentSearchObserver()
		initRecentSearchRecyclerView()
		initAdapterAndItemClickListener()
		initSearchResultRecyclerView()
		initSearchBar()
		getSearchPoiResult()
	}

	private fun initSearchViews() {
		binding.searchResultButton.isVisible = false
		binding.searchResultRecyclerView.isVisible = false
	}

	private fun recentSearchObserver() {
		searchViewModel.searchHistoryLiveData.observe(
			this,
			{
				recentSearchAdapter.submitList(it)
			}
		)
	}


	private fun initAdapterAndItemClickListener() {
		searchResultAdapter = SearchResultAdapter {
			val intent = Intent(this, ResultMarkerMapActivity::class.java).apply {
				putExtra(INTENT_KEY, it)
			}
			startActivity(intent)
			finish()
		}
	}

	// 키워드 입력
	private fun getSearchPoiResult() = with(binding) {
		searchEditText.setOnKeyListener { v, keyCode, event ->
			if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == MotionEvent.ACTION_DOWN) {
				viewModel.getSearchPoiResult(searchEditText.text.toString())
				searchViewModel.insertKeyword(searchEditText.text.toString())

				getSearchResult()
				return@setOnKeyListener true
			}
			return@setOnKeyListener false

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
						Log.d("retrofitTest", it.body().toString())
						return@observe
					}
					it.body()?.let { searchResponse ->
						setSearchResultItem(searchResponse.searchPoiInfo.pois)
					}

				} catch (e: Exception) {
					e.printStackTrace()
					Toast.makeText(
						this@SearchResultActivity,
						"검색이 제대로 되지 않았습니다.",
						Toast.LENGTH_SHORT
					).show()
				}

			}
		)
	}

	@SuppressLint("ClickableViewAccessibility")
	private fun initSearchBar() = with(binding) {
		searchEditText.setOnTouchListener { v, motionEvent ->
			if (motionEvent.action == MotionEvent.ACTION_DOWN) {
				searchResultButton.isVisible = false
				searchResultRecyclerView.isVisible = false
				recentSearchButton.isVisible = true
				recentSearchRecyclerView.isVisible = true
			}
			return@setOnTouchListener false
		}

	}

	// POI에서 데이터 가져와 submitList
	private fun setSearchResultItem(pois: Pois) = with(binding) {
		dataList = pois.poi.map {
			val fullAddressName = "${it.upperAddrName} ${it.middleAddrName} ${it.lowerAddrName} ${it.detailAddrName}"
			SearchResultItem(
				locationName = it.name ?: "건물명 없음",
				fullAddress = fullAddressName ?: "주소 없음",
				telNumber = it.telNo.toString(),
				upperBizName = it.upperBizName.toString(),
				middleBizName = it.middleBizName.toString(),
				lowerBizName = it.lowerBizName.toString(),
				desc = it.desc.toString(),
				locationLatLngItem = LocationLatLngItem(
					// 좌표값 명시
					latitude = it.noorLat,
					longitude = it.noorLon
				)
			)
		}
		searchResultButton.isVisible = true
		searchResultRecyclerView.isVisible = true
		recentSearchButton.isVisible = false
		recentSearchRecyclerView.isVisible = false

		searchResultAdapter.submitList(dataList)
	}

	private fun initRecentSearchRecyclerView() = with(binding) {
		recentSearchAdapter = RecentSearchAdapter {
			setRemoveButton(it)
		}
		recentSearchRecyclerView.adapter = recentSearchAdapter
		recentSearchRecyclerView.layoutManager = LinearLayoutManager(
			this@SearchResultActivity,
			LinearLayoutManager.VERTICAL,
			true
		).apply {
			stackFromEnd = true
		}
	}

	private fun initSearchResultRecyclerView() = with(binding) {
		searchResultRecyclerView.adapter = searchResultAdapter
		searchResultRecyclerView.layoutManager = LinearLayoutManager(this@SearchResultActivity)
	}

	private fun setRemoveButton(keyword: String) {
		searchViewModel.removeHistory(keyword)
		searchViewModel.searchHistoryLiveData
	}

}