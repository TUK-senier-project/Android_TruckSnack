package com.example.icontest2.seller_register

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.icontest2.*
import com.example.icontest2.customer_register.SignUpActivity
import com.example.icontest2.databinding.ActivitySellerRegisterBinding
import com.example.icontest2.seller_login.SellerLoginActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.GsonBuilder
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
    private var category: Int = 0
    private var time: Int = 0
    // private var minute: Int = 0

    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySellerRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 올바른 입력인지 확인
        var idCheck = false // 아이디
        var pwCheck = false // 비밀번호
        var pwCheckCheck = false // 비밀번호 재확인
        var nameCheck = false // 사업명
        var contentCheck = false // 소개글
        var phoneCheck = false // 휴대폰 번호
        var locationCheck = false // 주소
        var checkIdCheck = false // 아이디 중복
        var usableId = ""

        // EditText
        val sellerRegisterIdEdt = binding.sellerRegisterIdEdt // 아이디
        val sellerRegisterPasswordEdt = binding.sellerRegisterPasswordEdt // 비밀번호
        val sellerRegisterPasswordCheckEdt = binding.sellerRegisterPasswordCheckEdt //비밀번호 재확인
        val sellerRegisterBusinessNameEdt = binding.sellerRegisterBusinessNameEdt // 사업명
        val sellerRegisterContentEdt = binding.sellerRegisterContentEdt // 소개글
        val sellerRegisterCategorySpinner = binding.sellerRegisterCategorySpinner // 카테고리 스피너
        val sellerRegisterPhoneNumberEdt = binding.sellerRegisterPhoneNumberEdt // 휴대폰 번호
        sellerRegisterLocationEdt = binding.sellerRegisterLocationEdt // 주소

        var sellerRegisterLocationBtn = binding.sellerRegisterLocationBtn // 주소 버튼
        var sellerRegisterIdCheckTv = binding.sellerRegisterIdCheckTv // 아이디 중복확인 TextView
        var sellerRegisterIdCheckBtn = binding.sellerRegisterIdCheckBtn // 아이디 중복확인 TextView
        val registerBtn = binding.registerBtn

        val gson = GsonBuilder()
            .setLenient()
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://13.124.112.81:8080")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        // Spinner 연결
        setUpCategorySpinner()
        setUpTimeSpinner()
        // setUpMinuteSpinner()
        spinnerHandler()

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

                    idCheck = checkLength(s, sellerRegisterIdEdt, maxLength, minLength) &&
                            checkWhiteSpace(s, sellerRegisterIdEdt) &&
                            checkSpecialCharacters(s, sellerRegisterIdEdt) &&
                            checkAlphaNumber(sellerRegisterIdEdt)
                    if(usableId.isNotEmpty() && (usableId != sellerRegisterIdEdt.text.toString())){
                        checkIdCheck = false
                        Toast.makeText(applicationContext, "아이디 중복 확인을 다시 해주십시오.", Toast.LENGTH_SHORT).show()
                    }
                }
                // 비밀번호
                if (s == sellerRegisterPasswordEdt.editableText) {
                    Log.d(TAG, "in - pw")
                    val minLength = 8
                    val maxLength = 20

                    pwCheck = checkLength(s, sellerRegisterPasswordEdt, maxLength, minLength) &&
                            checkWhiteSpace(s, sellerRegisterPasswordEdt) &&
                            checkSpecialCharacters(s, sellerRegisterPasswordEdt) &&
                            checkAlphaNumber(sellerRegisterPasswordEdt) &&
                            certainAlphaNumber(sellerRegisterPasswordEdt)
                }
                // 비밀번호 체크
                if (s == sellerRegisterPasswordCheckEdt.editableText) {
                    Log.d(TAG, "in - pwcheck")
                    if (sellerRegisterPasswordEdt.text.toString() != sellerRegisterPasswordCheckEdt.text.toString()) {
                        sellerRegisterPasswordCheckEdt.error = "비밀번호가 일치하지 않습니다."
                        pwCheckCheck = false
                    } else {
                        sellerRegisterPasswordCheckEdt.error = null
                        pwCheckCheck = true
                    }
                }
                // 사업명
                if (s == sellerRegisterBusinessNameEdt.editableText) {
                    Log.d(TAG, "in - name")
                    val minLength = 1
                    val maxLength = 10

                    nameCheck = checkLength(s, sellerRegisterBusinessNameEdt, maxLength, minLength) &&
                            checkWhiteSpace(s, sellerRegisterBusinessNameEdt) &&
                            checkSpecialCharacters(s, sellerRegisterBusinessNameEdt) &&
                            checkKorean(sellerRegisterBusinessNameEdt)
                }
                // 소개글
                if (s == sellerRegisterContentEdt.editableText) {
                    Log.d(TAG, "in - content")
                    val minLength = 1
                    val maxLength = 50
                    contentCheck = checkLength(s, sellerRegisterContentEdt, maxLength, minLength)
                }
                // 휴대폰번호
                if (s == sellerRegisterPhoneNumberEdt.editableText) {
                    Log.d(TAG, "in - phone")

                    phoneCheck = checkPhoneNumber(sellerRegisterPhoneNumberEdt) &&
                            checkWhiteSpace(s, sellerRegisterPhoneNumberEdt) &&
                            checkSpecialCharacters(s, sellerRegisterPhoneNumberEdt)
                }
                // 주소
                if (s == sellerRegisterLocationEdt.editableText){
                    Log.d(TAG, "in - location")
                    locationCheck = checkKoreanNumber(sellerRegisterLocationEdt)
                }
            }
        }

        // 회원가입폼 예외처리
        sellerRegisterIdEdt.addTextChangedListener(textWatcher)
        sellerRegisterPasswordEdt.addTextChangedListener(textWatcher)
        sellerRegisterPasswordCheckEdt.addTextChangedListener(textWatcher)
        sellerRegisterBusinessNameEdt.addTextChangedListener(textWatcher)
        sellerRegisterContentEdt.addTextChangedListener(textWatcher)
        //sellerRegisterCategoryEdt.addTextChangedListener(textWatcher)
        sellerRegisterPhoneNumberEdt.addTextChangedListener(textWatcher)
        sellerRegisterLocationEdt.addTextChangedListener(textWatcher)



        // 아이디 중복확인 버튼 클릭 시
        sellerRegisterIdCheckBtn.setOnClickListener {
            // sellerRegisterIdCheckTv Visible하게 만들어주기
            // 중복확인 통신 후, 성공 시 성공글 보여주고, 실패 시 실패글 보여주기.(각각 색상에 맞게 설정해줘야함.)
            // 서버에서 아이디가 존재할 시, this_id_already 출력
            if (idCheck){
                val id = sellerRegisterIdEdt.text.toString()
                val checkId = SellerCheckIdDTO(id)

                val sellerAPI = retrofit.create(SellerAPI::class.java)

                GlobalScope.launch(Dispatchers.IO) {
                    try {
                        val response = sellerAPI.registerSellerIdCheck(checkId)
                        Log.d(TAG, "통신전")
                        Log.d(TAG, "$checkId")
                        if (response.isNotEmpty()) {
                            // 요청 성공
                            Log.d(TAG, "성공")
                            // this_id_already가 데이터로 들어올 시, 아이디 존재
                            runOnUiThread {
                                if (response == "this_id_already"){
                                    sellerRegisterIdCheckTv.visibility = View.VISIBLE
                                    sellerRegisterIdCheckTv.setText("이미 존재하는 아이디입니다.")
                                    sellerRegisterIdCheckTv.setTextColor(Color.RED)
                                    checkIdCheck = false
                                } else{
                                    sellerRegisterIdCheckTv.visibility = View.VISIBLE
                                    sellerRegisterIdCheckTv.setText("사용 가능한 아이디입니다.")
                                    sellerRegisterIdCheckTv.setTextColor(Color.BLUE)
                                    usableId = "$response"
                                    checkIdCheck = true
                                }
                            }
                            // this_id_already 이외의 데이터가 들어올 시, 아이디 사용가능.
                            Log.d(TAG, "$response")
                        } else {
                            // 요청 실패
                            Log.d(TAG, "실패")
                            // Log.d(TAG, "${response.errorBody()}")
                            Log.d(TAG, "$response")
                        }
                    } catch (e: Exception) {
                        Log.d(TAG, "예외")
                        Log.d(TAG, "$e")
                    }
                }
            }else{
                Toast.makeText(this, "올바른 아이디를 입력해주세요.", Toast.LENGTH_SHORT).show()
                checkIdCheck = false
            }
        }


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
        // 회원가입 버튼 클릭 시
        registerBtn.setOnClickListener {
            if (idCheck && !checkIdCheck && pwCheck && pwCheckCheck && nameCheck && contentCheck && phoneCheck && locationCheck){
                Toast.makeText(this, "아이디 중복 확인을 해주십시오.", Toast.LENGTH_SHORT).show()
            } else if (idCheck && checkIdCheck && pwCheck && pwCheckCheck && nameCheck && contentCheck && phoneCheck && locationCheck){

                val id = sellerRegisterIdEdt.text.toString()
                val pw = sellerRegisterPasswordEdt.text.toString()
                val name = sellerRegisterBusinessNameEdt.text.toString()
                val content = sellerRegisterContentEdt.text.toString()
                val phoneNumber = sellerRegisterPhoneNumberEdt.text.toString()
                val location = sellerRegisterLocationEdt.text.toString()
                val deadline = time
                val seller = SellerRegisterDTO(id, pw, name, content, category, deadline, phoneNumber, location)

                val sellerAPI = retrofit.create(SellerAPI::class.java)

                GlobalScope.launch(Dispatchers.IO) {
                    try {
                        val response = sellerAPI.registerSeller(seller)
                        Log.d(TAG, "통신전")
                        if (response.isSuccessful) {
                            // 요청 성공
                            Log.d(TAG, "성공")
                            Log.d(TAG, "${response.body()}")
                            Log.d(TAG, "$response")
                            val intent = Intent(applicationContext, SellerLoginActivity::class.java)
                            startActivity(intent)
                        } else {
                            // 요청 실패
                            Log.d(TAG, "실패")
                            Log.d(TAG, "${response.errorBody()}")
                            Log.d(TAG, "$response")
                            //Toast.makeText(applicationContext, "회원가입에 실패하셨습니다.", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Log.d(TAG, "예외")
                        Log.d(TAG, "$e")
                    }
                }
            } else{
                Toast.makeText(this, "정보를 모두 입력해 주세요.", Toast.LENGTH_SHORT).show()
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
    fun checkWhiteSpace(editable: Editable?, editText: EditText): Boolean {
        // EditText의 문자열 가져오기
        val text = editable.toString()
        Log.d(TAG, "${text} - checkWhiteSpace")
        // 결과 출력
        return if (text.contains(" ")) {
            Log.d(TAG, "${text} - 공백있음")
            editText.error = "공백이 포함되어 있습니다."
            false
        } else true
    }

    // 특수 문자 확인 함수
    fun checkSpecialCharacters(editable: Editable?, editText: EditText): Boolean {
        // EditText의 문자열 가져오기
        val text = editable.toString()
        Log.d(TAG, "${text} - checkSpecialCharacters")

        // 결과 출력
        return if (text.matches(".*[!@#\$%^&*(),.?\":{}|<>\\[\\]~-].*".toRegex())) {
            editText.error = "특수문자는 입력할 수 없습니다."
            false
        } else true

    }

    // 문자 길이 확인 함수
    fun checkLength(editable: Editable?, editText: EditText, maxLength: Int, minLength: Int): Boolean {
        // EditText의 문자열 가져오기
        val text = editable.toString()
        Log.d(TAG, "${text} - checkLength")

        val length = text.length

        return if (length < minLength) {
            editText.error = "최소 ${minLength}자 이상 입력하세요"
            false
        } else if (length > maxLength) {
            editText.error = "최대 ${maxLength}자까지 입력 가능합니다"
            false
        } else true
    }
    // 영+숫 확인 함수
    fun checkAlphaNumber(editText: EditText): Boolean {
        Log.d(TAG, " - checkAlphaNumber")
        val text = editText.text.toString()
        return if(text.contains(Regex("[ㄱ-ㅎㅏ-ㅣ가-힣]+"))){
            editText.error = "영문 또는 숫자만 입력 가능합니다."
            false
        } else true
    }

    // 영+숫 조합인지 확인
    fun certainAlphaNumber(editText: EditText): Boolean {
        Log.d(TAG, " - certainAlphaNumber")
        val text = editText.text.toString()
        return if(text.contains(Regex("[0-9]+")) && text.contains(Regex("[a-zA-Z]+"))){
            true
        } else {
            editText.error = "반드시 영문+숫자 조합만 가능합니다."
            false
        }
    }

    // 한국어 확인 함수
    fun checkKorean(editText: EditText): Boolean{
        Log.d(TAG, " - checkKorean")
        val text = editText.text.toString()
        return if(text.contains(Regex("[a-zA-Z0-9]+"))){
            editText.error = "한글만 입력 가능합니다."
            false
        } else true
    }
    // 한글 + 숫자 확인 함수(영어 입력 X)
    fun checkKoreanNumber(editText: EditText): Boolean{
        Log.d(TAG, " - checkKorean")
        val text = editText.text.toString()
        return if(text.contains(Regex("[a-zA-Z]+"))){
            editText.error = "한글과 숫자만 입력 가능합니다."
            false
        } else true
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
            return false
        }
        // 11글자인지 확인
        if (!isElevenDigits) {
            editText.error = "11자리를 입력해주십시오."
            return false
        }
        return isNumeric && isElevenDigits
    }
    // 카테고리 목록(Spinner)
    private fun setUpCategorySpinner(){
        val category = resources.getStringArray(R.array.category)
        println("=========================================================================")
        println("${category::class.simpleName}")
        val categoryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, category)
        binding.sellerRegisterCategorySpinner.adapter = categoryAdapter
    }
    // Deadline 시간(시) 목록(Spinner)
    private fun setUpTimeSpinner(){
        val time = Array<String>(24) { i -> (i).toString() }
        val timeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, time)
        binding.sellerRegisterTimeSpinner.adapter = timeAdapter
    }
    /*
    // Deadline 시간(분) 목록(Spinner)
    private fun setUpMinuteSpinner(){
        val minute = Array<String>(60) { i -> (i).toString() }
        val minuteAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, minute)
        binding.sellerRegisterMinuteSpinner.adapter = minuteAdapter
    }
    */
    // 카테고리, Deadline 입력 시 핸들러
    private fun spinnerHandler(){
        binding.sellerRegisterCategorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Log.d(TAG, "$position")
                category = position
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        binding.sellerRegisterTimeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Log.d(TAG, "$position")
                time = position
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        /*
        binding.sellerRegisterMinuteSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Log.d(TAG, "$position")
                minute = position
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        */
    }


}