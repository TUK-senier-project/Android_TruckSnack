package com.example.icontest2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import com.example.icontest2.databinding.ActivitySignUpBinding
import java.util.regex.Pattern
import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*


class SignUpActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivitySignUpBinding
    private var TAG = "SignUpActivity"
    private lateinit var mMap: GoogleMap
    private lateinit var buttonLocation: Button
    private val REQUEST_LOCATION_PERMISSION = 1



    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val returnIntent = Intent(this, LoginActivity::class.java)
        val locationRegisterIntent = Intent(this, LocationRegisterActivity::class.java)

        // 아이디, 비밀번호, 비밀번호 확인, 이름, 휴대폰번호의 editText id
        val signUpIdEdit = binding.signUpIdEdit // 아이디
        val signUpPasswdEdit = binding.signUpPasswdEdit
        val signUpCheckPasswdEdit = binding.signUpCheckPasswdEdit
        val signUpNameEdit = binding.signUpNameEdit
        val signUpPhoneEdit = binding.signUpPhoneEdit
        val signUptextIdLengthChecker = binding.mainTextInputLayoutID
        val signUptextPwLengthChecker = binding.mainTextInputLayoutPW
        val lengthCheck = binding.mainTextInputLayoutCreateName
        val signUPEdit_ID = binding.EditID // 아이디 만들기
        val signUPEdit_PW = binding.EditPW // 비밀번호 만들기
        val signUpCreate_name = binding.createName // 이름 만들기
        val signUpCreate_phone_number = binding.phoneNumberCreate // 폰번호 숫자만 11자제한
        setEditTextInput(signUpCreate_phone_number, 11)

        buttonLocation = findViewById(R.id.button_location) // 사용자 위치 조회 및 입력
        buttonLocation.setOnClickListener {
            requestLocationPermission()
            showUserLocation()
        }
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val buttonShowLocation = findViewById<Button>(R.id.button_location)
        buttonShowLocation.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
                // 위치 권한이 허용된 경우
                showUserLocation()
            } else {
                // 위치 권한이 허용되지 않은 경우
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
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
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (signUPEdit_ID.length() > 15) {
                        signUptextIdLengthChecker.error = "ID의 글자 수를 초과하였습니다."
                    } else {
                        signUptextIdLengthChecker.error = null
                    }
                    if (signUPEdit_ID.length() < 5) {
                        signUptextIdLengthChecker.error = "ID는 최소 5자 이상입니다."
                    } else {
                        signUptextIdLengthChecker.error = null
                    }
                }
                override fun afterTextChanged(p0: Editable?) {
                }

            })

            signUPEdit_PW.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (signUPEdit_PW.length() > 20) {
                        signUptextPwLengthChecker.error = "PW의 글자 수를 초과하였습니다."
                    } else {
                        signUptextIdLengthChecker.error = null
                    }
                    if (signUPEdit_PW.length() < 8) {
                        signUptextPwLengthChecker.error = "PW는 최소 8자 이상입니다."
                    } else {
                        signUptextIdLengthChecker.error = null
                    }
                }
                override fun afterTextChanged(p0: Editable?) {
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

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            // 사용자의 입력이 끝난 후 처리
            override fun afterTextChanged(s: Editable?) {
                Log.d(TAG, "${s}")
                // 아이디
                if (s == signUpIdEdit.editableText) {
                    Log.d(TAG, "in - id - length")
                    val minLength = 5
                    val maxLength = 15
                    checkWhiteSpace(s, signUpIdEdit) // 공백 문자 확인
                    checkSpecialCharacters(s, signUpIdEdit) // 특수 문자 확인
                    checkLength(s, signUpIdEdit, maxLength, minLength) // 문자열 길이 확인
                }
                // 비밀번호
                if (s == signUpPasswdEdit.editableText) {
                    Log.d(TAG, "in - pw - length")
                    val minLength = 8
                    val maxLength = 20
                    checkWhiteSpace(s, signUpPasswdEdit) // 공백 문자 확인
                    checkSpecialCharacters(s, signUpPasswdEdit) // 특수 문자 확인
                    checkAlphaNumber(signUpPasswdEdit) // 영문 + 숫자 확인
                    checkLength(s, signUpPasswdEdit, maxLength, minLength) // 문자열 길이 확인
                }
                // 비밀번호 체크
                if (s == signUpCheckPasswdEdit.editableText) {
                    Log.d(TAG, "in - pwcheck - length")
                    if (signUpPasswdEdit.text.toString() != signUpCheckPasswdEdit.text.toString()) {
                        signUpCheckPasswdEdit.error = "비밀번호가 일치하지 않습니다."
                    } else {
                        signUpCheckPasswdEdit.error = null
                    }
                }
                // 이름
                if (s == signUpNameEdit.editableText) {
                    Log.d(TAG, "in - name - length")
                    val minLength = 1
                    val maxLength = 10
                    checkWhiteSpace(s, signUpNameEdit) // 공백 문자 확인
                    checkSpecialCharacters(s, signUpNameEdit) // 특수 문자 확인
                    checkKorean(signUpNameEdit) // 한글 확인
                    checkLength(s, signUpNameEdit, maxLength, minLength) // 문자열 길이 확인
                }
                // 휴대폰번호
                if (s == signUpPhoneEdit.editableText) {
                    Log.d(TAG, "in - phone - length")
                    checkWhiteSpace(s, signUpPhoneEdit) // 공백 문자 확인
                    checkSpecialCharacters(s, signUpPhoneEdit) // 특수 문자 확인
                    checkPhoneNumber(signUpPhoneEdit) // 숫자 및 11자 확인
                }
            }
        }

        // 각 항목별 공백, 특수 문자 처리
        signUpIdEdit.addTextChangedListener(textWatcher)
        signUpPasswdEdit.addTextChangedListener(textWatcher)
        signUpCheckPasswdEdit.addTextChangedListener(textWatcher)
        signUpNameEdit.addTextChangedListener(textWatcher)
        signUpPhoneEdit.addTextChangedListener(textWatcher)
        notKorean(signUPEdit_ID) // id 한글 예외처리
        notKorean(signUPEdit_PW) // pw 한글 예외처리
        onlyKorean(signUpCreate_name) // 이름 입력시 한글만
        checkwhite(signUpCreate_name) // 이름 입력시 공백 확인
        textLengthChecker() // 문자길이 체크

        binding.signUpBtn.setOnClickListener {

            returnIntent.putExtra("name", binding.signUpNameEdit.text.toString())
            startActivity(returnIntent)
            finish()
        }

        binding.signUpLocationBtn.setOnClickListener {
            startActivity(locationRegisterIntent)
        }

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

    // 영문 + 숫자 확인 함수
    fun checkAlphaNumber(editText: EditText) {
        Log.d(TAG, " - checkAlphaNumber")

        val alphaNumbericRegex = Regex("[a-zA-Z0-9]+")
        val inputFilter = InputFilter { source, start, end, dest, dstart, dend ->
            val input = dest.subSequence(0, dstart).toString() + source.subSequence(
                start,
                end
            ) + dest.subSequence(dend, dest.length).toString()
            return@InputFilter if (input.matches(alphaNumbericRegex)) null else ""
        }
        editText.filters = arrayOf(inputFilter)

    }

    // 한국어 확인 함수
    fun checkKorean(editText: EditText) {
        Log.d(TAG, " - checkKorean")

        val inputFilter = InputFilter { source, _, _, _, _, _ ->
            val regex = Regex("[ㄱ-ㅎ가-힣]+")
            if (source.toString().matches(regex)) {
                source
            } else {
                ""
            }
        }
        editText.filters = arrayOf(inputFilter)
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
    // 한글예외처리 함수
    fun notKorean(editText: EditText){
        Log.d(TAG, " - checkNotKorean")
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
                val text = s.toString()
                if (text.matches(Regex("[ㄱ-ㅎ가-힣]+"))) {
                    editText.error = "한글은 입력할 수 없습니다."
                } else {
                    editText.error = null
                }
            }
        })
    }
    // 한글만 입력함수
    fun onlyKorean(editText: EditText){
        Log.d(TAG, " - checkNotKorean")
        val pattern = Pattern.compile("[^ㄱ-ㅎ가-힣]*$") // 한글을 제외한 문자열 패턴
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val text = s.toString()
                val matcher = pattern.matcher(text)
                if (matcher.matches()) {
                    editText.error = "한글을 제외한 문자열은 입력할 수 없습니다."
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
                } else {
                    editText.error = null
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
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
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
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            showUserLocation()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        }
    }

}
















