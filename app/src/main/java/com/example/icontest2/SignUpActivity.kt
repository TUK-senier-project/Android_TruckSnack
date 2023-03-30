package com.example.icontest2

import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.icontest2.databinding.ActivitySignUpBinding
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class SignUpActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var mMap: GoogleMap
    private lateinit var buttonLocation: Button
    private val REQUEST_LOCATION_PERMISSION = 1
    private var TAG = "SignUpActivity"

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 아이디, 비밀번호, 비밀번호 확인, 이름, 휴대폰번호의 editText id
        val signUpBtn = binding.signUpBtn
        val signUptextIdLengthChecker = binding.mainTextInputLayoutID
        val signUptextPwLengthChecker = binding.mainTextInputLayoutPW
        val lengthCheck = binding.mainTextInputLayoutCreateName
        val signUPEdit_ID = binding.EditID // 아이디 만들기
        val signUPEdit_PW = binding.EditPW // 비밀번호 만들기
        val signUpCreate_name = binding.createName // 이름 만들기
        val signUpCreate_phone_number = binding.phoneNumberCreate // 폰번호 숫자만 11자제한
        val editText = binding.editText // 위치 조회값
        setEditTextInput(signUpCreate_phone_number, 11)

        //signUpBtn.setOnClickListener {
        //    var intent = Intent(this, LoginActivity::class.java)
        //    startActivity(intent)
        //}

        buttonLocation = findViewById(R.id.button_location) // 사용자 위치 조회 및 입력
        buttonLocation.setOnClickListener {
            requestLocationPermission()
            showUserLocation()
        }
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val buttonShowLocation = findViewById<Button>(R.id.button_location)
        buttonShowLocation.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
                // 위치 권한이 허용된 경우
                showUserLocation()
            } else {
                // 위치 권한이 허용되지 않은 경우
                ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_LOCATION_PERMISSION)
            }
        }

        // 아이디, 패스워드 초과 경고 메세지
        fun textLengthChecker() {
            signUptextIdLengthChecker.isCounterEnabled = true
            signUptextPwLengthChecker.isCounterEnabled = true
            lengthCheck.isCounterEnabled = true

            signUptextIdLengthChecker.counterMaxLength = 15 // ID 최대 길이
            signUptextPwLengthChecker.counterMaxLength = 20 // PW 최대 길이
            lengthCheck.counterMaxLength = 10

            signUptextIdLengthChecker.isErrorEnabled = true
            signUptextPwLengthChecker.isErrorEnabled = true
            lengthCheck.isErrorEnabled = true

            signUPEdit_ID.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(p0: Editable?) {
                    if (signUPEdit_ID.text.contains(" ")) {
                        signUptextIdLengthChecker.error = "공백이 포함될 수 없습니다."
                    } else {
                        if (signUPEdit_ID.length() > 15) {
                            signUptextIdLengthChecker.error = "ID의 글자 수를 초과하였습니다."
                        } else if (signUPEdit_ID.length() < 5) {
                            signUptextIdLengthChecker.error = "ID는 최소 5자 이상입니다."
                        } else {
                            signUptextIdLengthChecker.error = null
                        }
                    }
                }
            })


            signUPEdit_PW.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }
                override fun afterTextChanged(p0: Editable?) {
                    val pw = p0.toString()
                    if (pw.contains(" ")) {
                        signUptextPwLengthChecker.error = "공백을 포함할 수 없습니다."
                    } else if (pw.length > 20) {
                        signUptextPwLengthChecker.error = "PW의 글자 수를 초과하였습니다."
                    } else if (pw.length < 8) {
                        signUptextPwLengthChecker.error = "PW는 최소 8자 이상입니다."
                    } else {
                        signUptextPwLengthChecker.error = null
                    }
                }
            })
            signUpCreate_name.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (signUpCreate_name.length() > 10) {
                        lengthCheck.error = "이름의 글자 수를 초과 하였습니다."
                    } else {
                        lengthCheck.error = null
                    }
                }
                override fun afterTextChanged(p0: Editable?) {
                }
            })
        }

        notKorean(signUPEdit_ID) // id 한글 예외처리
        notKorean(signUPEdit_PW) // pw 한글 예외처리
        onlyKorean(signUpCreate_name) // 이름 입력시 한글만
        checkwhite(signUpCreate_name) // 이름 입력시 공백 확인
        textLengthChecker() // 문자길이 체크
        checkKoreanNumber(editText) // 한글 + 숫자 확인 함수(영어 입력 X)

        // '생성' 버튼 클릭 시 API 호출
        Log.d(TAG, "버튼 클릭 전 로그 체크!!}")

        signUpBtn.setOnClickListener {
            Log.d(TAG, "버튼 클릭 후 로그 체크!!}")
            val id = signUPEdit_ID.text.toString()
            val password = signUPEdit_PW.text.toString()
            val name = signUpCreate_name.text.toString()
            val phoneNumber = signUpCreate_phone_number.text.toString()
            val location = editText.text.toString()
            val customer = CustomerRegisterDTO(id, name, password, phoneNumber, location)
            Log.d(TAG, "$customer")
            Log.d(TAG, "${id::class.java}")
            Log.d(TAG, "${password::class.java}")
            Log.d(TAG, "${name::class.java}")
            Log.d(TAG, "${phoneNumber::class.java}")
            Log.d(TAG, "${location::class.java}")
            val retrofit = Retrofit.Builder() // 서버통신
                .baseUrl("http://13.209.9.240:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            // API 서비스 인터페이스 구현체 생성
            val customerAPI = retrofit.create(CustomerRegisterAPI::class.java)
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val response = customerAPI.registerCustomer(customer)
                    Log.d(TAG, "서버 통신 전 로그 체크!!}")
                    if (response.isSuccessful) {
                        Log.d(TAG, "서버 성공 로그 체크!!}")
                        Log.d(TAG, "${response.body()}")
                        Log.d(TAG, "$response")
                        // 성공 처리
                    } else {
                        // 실패 처리
                        Log.d(TAG, "서버 실패 로그 체크!!}")
                        Log.d(TAG, "${response.errorBody()}")
                        Log.d(TAG, "$response")
                    }
                } catch (e: Exception) {
                    // 예외 처리
                    Log.d(TAG, "서버 예외 로그 체크!!}")
                    Log.d(TAG, "$e")
                }
            }
        }

    }

    // 한글예외처리 함수
    fun notKorean(editText: EditText) {
        Log.d(TAG, " - checkNotKorean")
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val text = s.toString()
                if (text.matches(Regex("[^a-zA-Z0-9 ]+")) || text.matches(Regex(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*"))) {
                    editText.error = "영어와 숫자만 입력 가능합니다."
                } else {
                    editText.error = null
                }
            }
        })
    }
    // 한글 + 숫자 확인 함수(영어 입력 X)
    fun checkKoreanNumber(editText: EditText) {
        Log.d(TAG, " - checkKorean")
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val text = editText.text.toString()
                if(text.contains(Regex("[a-zA-Z]+"))){
                    editText.error = "한글과 숫자만 입력 가능합니다."
                }
            }
        })
    }

    // 한글만 입력함수
    fun onlyKorean(editText: EditText) {
        Log.d(TAG, " - checkOnlyKorean")
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val text = s.toString()
                if (!text.matches(Regex("[ㄱ-ㅎㅏ-ㅣ가-힣 ]*"))) {
                    editText.error = "한글만 입력 가능합니다."
                } else {
                    editText.error = null
                }
            }
        })
    }

    // 공백체크함수
    fun checkwhite(editText: EditText){
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                // EditText의 문자열 가져오기
                val text = s.toString()
                Log.d(TAG, "${text} - checkWhiteSpace")
                // 결과 출력
                if (text.contains(" ")) {
                    Log.d(TAG, "${text} - 공백있음")
                    editText.error = "공백이 포함되어 있습니다."
                }
            }
        })

    }
    // 폰번호 입력시 11자제한, 숫자만 입력
    fun setEditTextInput(editText: EditText, maxLength: Int) {
        val inputFilter = arrayOf<InputFilter>(InputFilter { source, _, _, _, _, _ ->
            if (source.toString().matches(Regex("[0-9]+"))) {
                null // 숫자일 경우, null 리턴
            } else {
                "" // 숫자가 아닐 경우, 빈 문자열("") 리턴
            }
        })
        editText.filters = inputFilter
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val input = s.toString()
                if (input.length > maxLength) {
                    editText.error = "입력할 수 있는 숫자는 ${maxLength}자리 이하입니다."
                } else if (input.length < 11) {
                    editText.error = "11자리까지 숫자만 입력해주세요."
                } else {
                    editText.error = null // 에러 없음
                }
                val text = s.toString()
                Log.d(TAG, "${text} - checkWhiteSpace")
                // 결과 출력
                if (text.contains(" ")) {
                    Log.d(TAG, "${text} - 공백있음")
                    editText.error = "공백이 포함되어 있습니다."
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    // 사용자 위치 조회, 위치 입력
    private fun showUserLocation() {
        val editText = findViewById<EditText>(R.id.editText)
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.isMyLocationEnabled = true

            // 사용자 위치 가져오기
            val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val geocoder = Geocoder(this, Locale.KOREAN)
                    val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                    if (addresses != null) {
                        if (addresses.isNotEmpty()) {
                            val address = addresses[0]
                            val addressStr = address.getAddressLine(0)
                            editText.setText(addressStr)
                        }
                    }


                    val latLng = LatLng(location.latitude, location.longitude)
                    mMap.addMarker(MarkerOptions().position(latLng).title("User Location"))
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                }
            }
        }
    }
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showUserLocation()
            }
        }
    }
    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            showUserLocation()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        }
    }

}