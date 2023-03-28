package com.example.icontest2

import android.Manifest
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.widget.EditText
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.icontest2.databinding.ActivitySellerRegisterBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.OnMapReadyCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class SellerRegisterActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding : ActivitySellerRegisterBinding
    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private lateinit var marker: Marker
    private lateinit var geocoder: Geocoder
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var sellerRegisterLocationEdt: EditText

    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySellerRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // EditText
        val sellerRegisterIdEdt = binding.sellerRegisterIdEdt // 아이디
        val sellerRegisterPasswordEdt = binding.sellerRegisterPasswordEdt // 비밀번호
        val sellerRegisterPasswordCheckEdt = binding.sellerRegisterPasswordCheckEdt //비밀번호 확인
        val sellerRegisterBusinessNameEdt = binding.sellerRegisterBusinessNameEdt // 사업명
        val sellerRegisterContentEdt = binding.sellerRegisterContentEdt // 소개글
        val sellerRegisterCategoryEdt = binding.sellerRegisterCategoryEdt // 카테고리
        val sellerRegisterPhoneNumberEdt = binding.sellerRegisterPhoneNumberEdt // 휴대폰 번호
        sellerRegisterLocationEdt = binding.sellerRegisterLocationEdt // 주소

        var sellerRegisterLocationBtn = binding.sellerRegisterLocationBtn // 주소 버튼
        var sellerRegisterIdCheckTv = binding.sellerRegisterIdCheckTv // 아이디 중복확인 TextView
        var sellerRegisterIdCheckBtn = binding.sellerRegisterIdCheckBtn // 아이디 중복확인 TextView
        val registerBtn = binding.registerBtn



        // ToolBar 설정, 제목, 버튼 활성화, 아이콘 클릭 가능 설정
        setSupportActionBar(binding.sellerRegisterToolbar) // 생성시 ()안에 id 변경.
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        geocoder = Geocoder(this)

        // FusedLocationProviderClient 초기화
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            // 사용자의 입력이 끝난 후 처리
            override fun afterTextChanged(s: Editable?) {
                Log.d(TAG, "${s}")
                // 아이디
                if (s == sellerRegisterIdEdt.editableText) {
                    Log.d(TAG, "in - id")
                    val minLength = 5
                    val maxLength = 15
                    checkLength(s, sellerRegisterIdEdt, maxLength, minLength) // 문자열 길이 확인
                    checkWhiteSpace(s, sellerRegisterIdEdt) // 공백 문자 확인
                    checkSpecialCharacters(s, sellerRegisterIdEdt) // 특수 문자 확인
                    checkAlphaNumber(sellerRegisterIdEdt) // 영문 + 숫자 확인
                }
                // 비밀번호
                if (s == sellerRegisterPasswordEdt.editableText) {
                    Log.d(TAG, "in - pw")
                    val minLength = 8
                    val maxLength = 20
                    checkLength(s, sellerRegisterPasswordEdt, maxLength, minLength) // 문자열 길이 확인
                    checkWhiteSpace(s, sellerRegisterPasswordEdt) // 공백 문자 확인
                    checkSpecialCharacters(s, sellerRegisterPasswordEdt) // 특수 문자 확인
                    checkAlphaNumber(sellerRegisterPasswordEdt) // 영문 + 숫자 확인
                }
                // 비밀번호 체크
                if (s == sellerRegisterPasswordCheckEdt.editableText) {
                    Log.d(TAG, "in - pwcheck")
                    if (sellerRegisterPasswordCheckEdt.text.toString() != sellerRegisterPasswordCheckEdt.text.toString()) {
                        sellerRegisterPasswordCheckEdt.error = "비밀번호가 일치하지 않습니다."
                    } else {
                        sellerRegisterPasswordCheckEdt.error = null
                    }
                }
                // 사업명
                if (s == sellerRegisterBusinessNameEdt.editableText) {
                    Log.d(TAG, "in - name")
                    val minLength = 1
                    val maxLength = 10
                    checkLength(s, sellerRegisterBusinessNameEdt, maxLength, minLength) // 문자열 길이 확인
                    checkWhiteSpace(s, sellerRegisterBusinessNameEdt) // 공백 문자 확인
                    checkSpecialCharacters(s, sellerRegisterBusinessNameEdt) // 특수 문자 확인
                    checkKorean(sellerRegisterBusinessNameEdt) // 한글 확인

                }
                // 소개글
                if (s == sellerRegisterContentEdt.editableText) {
                    Log.d(TAG, "in - content")
                    val minLength = 1
                    val maxLength = 50
                    checkLength(s, sellerRegisterContentEdt, maxLength, minLength) // 문자열 길이 확인
                }
                /*
                // 카테고리
                if (s == sellerRegisterCategoryEdt.editableText) {
                    Log.d(TAG, "in - content")
                    val minLength = 1
                    val maxLength = 50
                    checkLength(s, sellerRegisterCategoryEdt, maxLength, minLength) // 문자열 길이 확인
                }
                */
                // 휴대폰번호
                if (s == sellerRegisterPhoneNumberEdt.editableText) {
                    Log.d(TAG, "in - phone")
                    checkPhoneNumber(sellerRegisterPhoneNumberEdt) // 숫자 및 11자 확인
                    checkWhiteSpace(s, sellerRegisterPhoneNumberEdt) // 공백 문자 확인
                    checkSpecialCharacters(s, sellerRegisterPhoneNumberEdt) // 특수 문자 확인
                }
                // 주소
                if (s == sellerRegisterLocationEdt.editableText){
                    Log.d(TAG, "in - location")
                }
            }
        }

        // 회원가입폼 예외처리
        sellerRegisterIdEdt.addTextChangedListener(textWatcher)
        sellerRegisterPasswordEdt.addTextChangedListener(textWatcher)
        sellerRegisterPasswordCheckEdt.addTextChangedListener(textWatcher)
        sellerRegisterBusinessNameEdt.addTextChangedListener(textWatcher)
        sellerRegisterContentEdt.addTextChangedListener(textWatcher)
        sellerRegisterCategoryEdt.addTextChangedListener(textWatcher)
        sellerRegisterPhoneNumberEdt.addTextChangedListener(textWatcher)
        sellerRegisterLocationEdt.addTextChangedListener(textWatcher)

        /*
        // 아이디 중복확인 버튼 클릭 시
        sellerRegisterIdCheckBtn.setOnClickListener {
            // sellerRegisterIdCheckTv Visible하게 만들어주기
            // 중복확인 통신 후, 성공 시 성공글귀 보여주고, 실패 시 실패글귀 보여주기.(각각 색상에 맞게 설정해줘야함.)

        }
        */

        // 현 위치등록 버튼 클릭 시
        sellerRegisterLocationBtn.setOnClickListener {
            // 현재 위치 정보 조회
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    if (location != null) {
                        val geocoder = Geocoder(this, Locale.getDefault())
                        val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                        val address = addresses?.get(0)
                        if (address != null) {
                            sellerRegisterLocationEdt.setText(address.getAddressLine(0))
                        }
                    }
                }
            } else {
                // 권한이 없는 경우 권한 요청
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_LOCATION_PERMISSION
                )
            }
        }
        registerBtn.setOnClickListener {

            val id = sellerRegisterIdEdt.text.toString()
            val pw = sellerRegisterPasswordEdt.text.toString()
            val name = sellerRegisterBusinessNameEdt.text.toString()
            val content = sellerRegisterContentEdt.text.toString()
            val category = 1
            val deadline = 21
            val phoneNumber = sellerRegisterPhoneNumberEdt.text.toString()
            val location = sellerRegisterLocationEdt.text.toString()

            val seller = SellerDTO(id, pw, name, content, category, deadline, phoneNumber, location)

            val retrofit = Retrofit.Builder()
                .baseUrl("http://13.209.9.240:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val sellerAPI = retrofit.create(SellerAPI::class.java)

            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val response = sellerAPI.registerSeller(seller)
                    if (response != null) {
                        Log.d(TAG, "$response")
                        // 성공 처리
                    } else {
                        // 실패 처리
                    }
                } catch (e: Exception) {
                    // 예외 처리
                }
            }

        }
    }
    // 뒤로가기 버튼 클릭 이벤트 처리(사용시 onCreate 밖에 복사)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                // 위치정보 조회 성공시 실행될 코드
                if (location != null) {
                    val userLatLng = LatLng(location.latitude, location.longitude)
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 15f))
                }
            }
            .addOnFailureListener { exception: Exception ->
                // 위치정보 조회 실패시 실행될 코드
                Log.e(TAG, "Error getting location", exception)
            }



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
                        sellerRegisterLocationEdt.setText(address)
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
                    sellerRegisterLocationEdt.setText("${address.getAddressLine(0)}")
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
    // 공백 문자 확인 함수
    fun checkWhiteSpace(editable: Editable?, editText: EditText) {
        // EditText의 문자열 가져오기
        val text = editable.toString()
        Log.d(TAG, "${text} - checkWhiteSpace")
        // 결과 출력
        if (text.contains(" ")) {
            Log.d(TAG, "${text} - 공백있음")
            editText.error = "공백이 포함되어 있습니다."
        }
    }

    // 특수 문자 확인 함수
    fun checkSpecialCharacters(editable: Editable?, editText: EditText) {
        // EditText의 문자열 가져오기
        val text = editable.toString()
        Log.d(TAG, "${text} - checkSpecialCharacters")

        // 검사할 특수 문자 지정
        val specialCharacters = "!@#$%^&*()_-+=|\\{}[]:;\"'<>,.?/~`"

        // 결과 출력
        if (text.matches(".*[!@#\$%^&*(),.?\":{}|<>\\[\\]~-].*".toRegex())) {
            editText.error = "특수문자는 입력할 수 없습니다."
        }

    }

    // 문자 길이 확인 함수
    fun checkLength(editable: Editable?, editText: EditText, maxLength: Int, minLength: Int) {
        // EditText의 문자열 가져오기
        val text = editable.toString()
        Log.d(TAG, "${text} - checkLength")

        val length = text.length

        if (length < minLength) {
            editText.error = "최소 ${minLength}자 이상 입력하세요"
        } else if (length > maxLength) {
            editText.error = "최대 ${maxLength}자까지 입력 가능합니다"
        }

    }
    fun checkAlphaNumber(editText: EditText) {
        Log.d(TAG, " - checkAlphaNumber")
        val text = editText.text.toString()
        if(text.contains(Regex("[ㄱ-ㅎㅏ-ㅣ가-힣]+"))){
            editText.error = "영문 또는 숫자만 입력 가능합니다."
        }
    }

    /*
    // 영문 + 숫자 확인 함수
    fun checkAlphaNumber(editText: EditText) {
        Log.d(TAG, " - checkAlphaNumber")
        val alphaNumbericRegex = Regex("[a-zA-Z0-9]+")
        val inputFilter = InputFilter { source, start, end, dest, dstart, dend ->
            val input = dest.subSequence(0, dstart).toString() + source.subSequence(
                start,
                end
            ) + dest.subSequence(dend, dest.length).toString()
            return@InputFilter if (input.matches(alphaNumbericRegex)){
                null
            }
             else {
                editText.setText(dest)
                editText.setSelection(dest.length)
                editText.error = "영문 또는 숫자만 입력 가능합니다."
                ""
             }
        }
        editText.filters = arrayOf(inputFilter)
    }
    */
    /*
    fun checkAlphaNumber2(editText: EditText) {
        Log.d(TAG, " - checkKorean")

        val alphaNumbericRegex = Regex("[a-zA-Z0-9]+")
        val inputFilter = InputFilter { source, start, end, dest, dstart, dend ->
            val input = dest.subSequence(0, dstart).toString() + source.subSequence(start, end) + dest.subSequence(dend, dest.length).toString()
            return@InputFilter if (input.matches(alphaNumbericRegex) || !input.matches(Regex("[ㄱ-ㅎㅏ-ㅣ가-힣]+"))) {
                null
            } else {
                editText.error = "영문 또는 숫자만 입력 가능합니다."
                ""
            }
        }
        editText.filters = arrayOf(inputFilter)
    }
    */

    // 한국어 확인 함수
    fun checkKorean(editText: EditText) {
        Log.d(TAG, " - checkKorean")
        val text = editText.text.toString()
        if(text.contains(Regex("[a-zA-Z0-9]+"))){
            editText.error = "한글만 입력 가능합니다."
        }
    }

    // 숫자 + 11글자 확인 함수
    fun checkPhoneNumber(editText: EditText): Boolean {
        Log.d(TAG, " - checkPhoneNumber")

        val regex = Regex("[0-9]+")
        val isNumeric = editText.text.toString().matches(regex)
        val isElevenDigits = editText.text.toString().length == 11
        // 숫자로만 이루어졌는지 확인
        if (!isNumeric) {
            editText.error = "숫자만 입력해주십시오."
        }
        // 11글자인지 확인
        if (!isElevenDigits) {
            editText.error = "11자리를 입력해주십시오."
        }
        return isNumeric && isElevenDigits
    }
}