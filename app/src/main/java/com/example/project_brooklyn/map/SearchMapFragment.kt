package com.example.project_brooklyn.map

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.project_brooklyn.R
import com.example.project_brooklyn.databinding.FragmentSearchMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng

class SearchMapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener,
	GoogleMap.OnMyLocationClickListener {

	private lateinit var map: GoogleMap
	private var permissionDenied = false

	private var _binding: FragmentSearchMapBinding? = null
	val binding: FragmentSearchMapBinding
		get() = _binding!!


	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentSearchMapBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		initGoogleMap()
		searchEditTextListener()
	}

	private fun initGoogleMap() {

		/*Android Fragment에서 Fragment replace를 하기 위해 FragmentManager 객체를 얻으려고 getSupportFragmentManager함수를 호출했으나 찾을 수 없다고 떴다.
		getSupportFragmentManager는 AppCompatActivity를 상속받고 AppCompatActivity는 FragmentActivity을 상속받는데 FragmentActivity 안에
		getSupportFragmentManager() 함수가 존재하기 때문이다.
		그렇기 때문에 Fragment에서 FragmentManager 객체를 얻기 위해선 Fragment에 선언되어있는 getFragmentManager()함수를 호출해야한다.

		결론 : Fragment에선 getSupportFragmentManager 함수 대신 getFragmentManager 함수 호출 - parent말고 child로*/

		val googleMap =
			childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
		googleMap.getMapAsync(this)

	}

	override fun onMapReady(map: GoogleMap) {
		this.map = map

		// 줌 레벨 설정
		map.setMinZoomPreference(15f)
		map.setMaxZoomPreference(23f)

		// 카메라 초기 위치
		val initCamera = CameraUpdateFactory.newLatLngZoom(
			LatLng(37.498095, 127.027610),
			CURRENT_LOCATION_ZOOM_LEVEL
		)
		map.moveCamera(initCamera)


		map.setOnMyLocationClickListener(this)
		map.setOnMyLocationButtonClickListener(this)

		checkPermission()
	}

	// 현위치 버튼 클릭 리스너
	override fun onMyLocationButtonClick(): Boolean {
		return false
	}

	//내 위치 파란색 점 클릭 리스너
	override fun onMyLocationClick(location: Location) {
		Toast.makeText(activity, "Current location:\n$location", Toast.LENGTH_LONG)
			.show()
	}

	// 검색화면으로 이동
	private fun searchEditTextListener() = with(binding) {
		searchTextView.setOnClickListener {
			val intent = Intent(requireContext(), SearchResultActivity::class.java)
			startActivity(intent)
		}
	}


	// abstract 안하고 Location Permission 직접 체크
	private fun checkPermission() {
		if (ContextCompat.checkSelfPermission(
				requireContext(),
				MY_FINE_LOCATION_PERMISSION
			) == PackageManager.PERMISSION_GRANTED
		) {
			map.isMyLocationEnabled = true
		} else {
			requestPermissions(arrayOf(MY_FINE_LOCATION_PERMISSION), LOCATION_REQUEST_CODE)
		}
	}

	override fun onRequestPermissionsResult(
		requestCode: Int,
		permissions: Array<out String>,
		grantResults: IntArray
	) {
		if (requestCode != LOCATION_REQUEST_CODE) {
			super.onRequestPermissionsResult(requestCode, permissions, grantResults)
			return
		}

		if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
			checkPermission()
		} else {
			Toast.makeText(activity, "위치 권한이 승인되지 않았어요", Toast.LENGTH_SHORT).show()
			permissionDenied = true
		}

	}

	override fun onResume() {
		super.onResume()
		if (permissionDenied) {
			Toast.makeText(activity, "위치 권한이 승인되지 않았어요", Toast.LENGTH_SHORT).show()
			permissionDenied = false
		}
	}


	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}

	companion object {
		const val MY_FINE_LOCATION_PERMISSION = android.Manifest.permission.ACCESS_FINE_LOCATION
		const val LOCATION_REQUEST_CODE = 3001
		const val CURRENT_LOCATION_ZOOM_LEVEL = 18f
	}


}