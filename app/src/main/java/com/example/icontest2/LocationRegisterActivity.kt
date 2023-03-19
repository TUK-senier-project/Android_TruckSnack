package com.example.icontest2

import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.icontest2.databinding.ActivityLocationRegisterBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class LocationRegisterActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding : ActivityLocationRegisterBinding

    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private lateinit var marker: Marker
    private lateinit var geocoder: Geocoder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        geocoder = Geocoder(this)
    }

    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0

        // 서울 시청을 초기 위치로 설정
        val seoulCityHall = LatLng(37.5662952, 126.9779451)
        // googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(seoulCityHall, 15f))

        // 마커 추가
        googleMap.addMarker(
            MarkerOptions()
                .position(seoulCityHall)
                .draggable(true)
        )
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(seoulCityHall, 15f))

        // 마커 드래그 이벤트 처리
        googleMap.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
            override fun onMarkerDragStart(marker: Marker) {}

            override fun onMarkerDrag(marker: Marker) {}

            override fun onMarkerDragEnd(marker: Marker) {
                val position = marker.position
                val addressList = geocoder.getFromLocation(position.latitude, position.longitude, 1)
                if (addressList != null) {
                    if (addressList.isNotEmpty()) {
                        val address = addressList[0].getAddressLine(0)
                        // 주소 출력
                        Log.d("Address", address)
                    }
                }
            }
        })

        googleMap.setOnCameraIdleListener {
            // 지도 중앙 위치 가져오기
            val center = googleMap.cameraPosition.target

            // 마커 위치 설정
            val markerOptions = MarkerOptions().position(center)
            googleMap.clear()
            googleMap.addMarker(markerOptions)

            // 지오코딩으로 주소 찾기
            val geocoder = Geocoder(this)
            val addressList = geocoder.getFromLocation(center.latitude, center.longitude, 1)
            if (addressList != null) {
                if (addressList.isNotEmpty()) {
                    val address = addressList[0]
                    Log.d("MainActivity", "주소: ${address.getAddressLine(0)}")
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}