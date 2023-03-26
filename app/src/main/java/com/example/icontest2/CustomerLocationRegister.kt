package com.example.icontest2

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException
import java.util.*

class CustomerLocationRegister : AppCompatActivity(),OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var userLocation: LatLng
    private lateinit var addressEditText: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_location_register)

        addressEditText = findViewById(R.id.address_text_view)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val locationButton = findViewById<Button>(R.id.location_button)
        locationButton.setOnClickListener {
            mMap.clear()
            val location = mMap.myLocation
            if (location != null) {
                userLocation = LatLng(location.latitude, location.longitude) // userLocation 초기화
                mMap.addMarker(MarkerOptions().position(userLocation).title("User Location"))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15f))

                val geocoder = Geocoder(this)
                try {
                    val addresses =
                        geocoder.getFromLocation(userLocation.latitude, userLocation.longitude, 1)
                    if (addresses != null) {
                        if (addresses.isNotEmpty() && addresses[0].getAddressLine(0) != null) {
                            val address = addresses[0].getAddressLine(0)
                            addressEditText.setText(address)
                        } else {
                            addressEditText.setText("주소 정보를 가져올 수 없습니다.")
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onBackPressed() {
        val userLocation = mMap.myLocation
        if (userLocation != null) {
            val geocoder = Geocoder(this, Locale.getDefault())
            val addresses = geocoder.getFromLocation(userLocation.latitude, userLocation.longitude, 1)
            if (addresses != null) {
                if (addresses.isNotEmpty()) {
                    val address = addresses[0].getAddressLine(0)
                    val intent = Intent()
                    intent.putExtra("address", address)
                    intent.putExtra("latitude", userLocation.latitude) // userLocation 정보 추가
                    intent.putExtra("longitude", userLocation.longitude)
                    setResult(Activity.RESULT_OK, intent)
                }
            }
        }
        super.onBackPressed()
    }

    override fun onMapReady(p0: GoogleMap) {

    }


}

