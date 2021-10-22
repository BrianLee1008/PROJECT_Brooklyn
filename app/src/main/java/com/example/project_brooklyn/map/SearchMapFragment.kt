package com.example.project_brooklyn.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.project_brooklyn.R
import com.example.project_brooklyn.databinding.FragmentSearchMapBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment

class SearchMapFragment : Fragment() , OnMapReadyCallback {

	private lateinit var map : GoogleMap

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
	}

	private fun initGoogleMap(){

		/*Android Fragment에서 Fragment replace를 하기 위해 FragmentManager 객체를 얻으려고 getSupportFragmentManager함수를 호출했으나 찾을 수 없다고 떴다.
		getSupportFragmentManager는 AppCompatActivity를 상속받고 AppCompatActivity는 FragmentActivity을 상속받는데 FragmentActivity 안에
		getSupportFragmentManager() 함수가 존재하기 때문이다.
		그렇기 때문에 Fragment에서 FragmentManager 객체를 얻기 위해선 Fragment에 선언되어있는 getFragmentManager()함수를 호출해야한다.

		결론 : Fragment에선 getSupportFragmentManager 함수 대신 getFragmentManager 함수 호출 - parent말고 child로*/

		val googleMap = childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
		googleMap.getMapAsync(this)

	}

	override fun onMapReady(map: GoogleMap) {
		this.map = map
	}



	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}


}