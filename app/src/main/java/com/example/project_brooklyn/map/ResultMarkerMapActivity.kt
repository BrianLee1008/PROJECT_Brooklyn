package com.example.project_brooklyn.map

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.project_brooklyn.R
import com.example.project_brooklyn.databinding.ActivityResultMarkerMapBinding
import com.example.project_brooklyn.item.retrofitmodel.SearchResultItem
import com.example.project_brooklyn.map.SearchMapFragment.Companion.CURRENT_LOCATION_ZOOM_LEVEL
import com.example.project_brooklyn.map.SearchMapFragment.Companion.MY_FINE_LOCATION_PERMISSION
import com.example.project_brooklyn.viewmodel.POIViewModel
import com.example.project_brooklyn.viewmodel.factory.PoiViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.map_bottom_sheet_view.*


class ResultMarkerMapActivity : AppCompatActivity(), OnMapReadyCallback,
	GoogleMap.OnMyLocationButtonClickListener,
	GoogleMap.OnMyLocationClickListener {

	private lateinit var map: GoogleMap

	private var permissionDenied = false

	private var currentSelectMarker: Marker? = null
	private lateinit var searchResultItem: SearchResultItem

	private lateinit var binding: ActivityResultMarkerMapBinding

	private val viewModel by viewModels<POIViewModel> { PoiViewModelFactory() }

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityResultMarkerMapBinding.inflate(layoutInflater)
		setContentView(binding.root)

		if (ActivityCompat.checkSelfPermission(
				this,
				MY_FINE_LOCATION_PERMISSION
			) != PackageManager.PERMISSION_GRANTED
		) {
			Toast.makeText(this, "위치권한을 먼저 승인시켜주세요", Toast.LENGTH_SHORT).show()
			finish()
		}

		if (::searchResultItem.isInitialized.not()) {
			intent?.let {
				searchResultItem =
					it.getParcelableExtra(INTENT_KEY) ?: throw Exception("데이터가 존재하지 않습니다.")
				initGoogleMap()
			}
		}

		initViews(searchResultItem)

	}


	private fun initGoogleMap() {


		val googleMap =
			supportFragmentManager.findFragmentById(R.id.mapFragmentTwo) as SupportMapFragment
		googleMap.getMapAsync(this)

	}

	override fun onMapReady(map: GoogleMap) {
		this.map = map


		// 줌 레벨 설정
		map.setMinZoomPreference(10f)
		map.setMaxZoomPreference(23f)


		map.setOnMyLocationClickListener(this)
		map.setOnMyLocationButtonClickListener(this)

		currentSelectMarker = setUpMarker(searchResultItem)
		currentSelectMarker?.showInfoWindow()

		if (ActivityCompat.checkSelfPermission(
				this,
				MY_FINE_LOCATION_PERMISSION
			) == PackageManager.PERMISSION_GRANTED
		) {
			map.isMyLocationEnabled = true
		}


	}

	// 현위치 버튼 클릭 리스너
	override fun onMyLocationButtonClick(): Boolean {
		return false
	}

	//내 위치 파란색 점 클릭 리스너
	override fun onMyLocationClick(location: Location) {
		Toast.makeText(this, "Current location:\n$location", Toast.LENGTH_LONG)
			.show()
	}


	// 클릭해서 넘어온 데이터 Observe
	// xo LiveData로 넘어온 값을 받지 못하는 것 같음. 디버깅 끊김 -> 애초에 RecylcerView 초기화부터 문제 있음
	private fun getPoiDataObserver() {
		viewModel.poiResultLiveData.observe(
			this,
			{
			}
		)
	}


	// searchResultItem 기반으로 마커 세팅
	private fun setUpMarker(searchResultItem: SearchResultItem): Marker {

		val positionLatLon = LatLng(
			searchResultItem.locationLatLngItem.latitude.toDouble(),
			searchResultItem.locationLatLngItem.longitude.toDouble()
		)

		map.moveCamera(
			CameraUpdateFactory.newLatLngZoom(
				positionLatLon,
				CURRENT_LOCATION_ZOOM_LEVEL
			)
		)

		val markerOption = MarkerOptions().apply {
			position(positionLatLon)
			title(searchResultItem.locationName)
			snippet(searchResultItem.fullAddress)
		}

		return map.addMarker(markerOption) ?: throw Exception("마커 데이터를 가져오지 못했어요")
	}

	@SuppressLint("SetTextI18n")
	private fun initViews(searchResultItem: SearchResultItem) = with(binding) {
		locationNameTextView.text = "건물(상호)명 : ${searchResultItem.locationName}"
		fullAddressTextView.text = "주소 : ${searchResultItem.fullAddress}"
		categoryDetailInfoTextView.text =
			"${searchResultItem.upperBizName} > ${searchResultItem.middleBizName} > ${searchResultItem.lowerBizName}"
		telNumberTextView.text = searchResultItem.telNumber

	}


	override fun onResume() {
		super.onResume()
		if (permissionDenied) {
			Toast.makeText(this, "위치 권한이 승인되지 않았어요", Toast.LENGTH_SHORT).show()
			permissionDenied = false
		}
	}

	companion object {
		const val INTENT_KEY = "key"
	}

}