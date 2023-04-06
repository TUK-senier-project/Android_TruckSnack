package com.example.icontest2.seller_login

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.icontest2.*
import com.example.icontest2.customer_login.CustomerLoginActivity
import com.example.icontest2.databinding.ActivitySellerLoginBinding
import com.example.icontest2.seller_register.SellerRegisterActivity
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import okhttp3.ResponseBody



class SellerLoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySellerLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySellerLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 올바른 입력인지 확인
        var idCheck = false // 아이디
        var pwCheck = false // 비밀번호

        val sellerLoginIdEdt = binding.sellerLoginIdEdt
        val sellerLoginPwEdt = binding.sellerLoginPwEdt

        // ToolBar 설정, 제목, 버튼 활성화, 아이콘 클릭 가능 설정
        setSupportActionBar(binding.sellerLoginToolbar) // 생성시 ()안에 id 변경.
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val gson = GsonBuilder()
            .setLenient()
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://13.209.18.214:8080")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            // 사용자의 입력이 끝난 후 처리
            override fun afterTextChanged(s: Editable?) {
                Log.d(TAG, "${s}")
                // 아이디
                if (s == sellerLoginIdEdt.editableText) {
                    Log.d(TAG, "in - id")
                    val minLength = 5
                    val maxLength = 15
                    idCheck = checkLength(s, sellerLoginIdEdt, maxLength, minLength) &&
                            checkWhiteSpace(s, sellerLoginIdEdt) &&
                            checkSpecialCharacters(s, sellerLoginIdEdt) &&
                            checkAlphaNumber(sellerLoginIdEdt)
                }
                // 비밀번호
                if (s == sellerLoginPwEdt.editableText) {
                    Log.d(TAG, "in - pw")
                    val minLength = 8
                    val maxLength = 20

                    pwCheck = checkLength(s, sellerLoginPwEdt, maxLength, minLength) &&
                            checkWhiteSpace(s, sellerLoginPwEdt) &&
                            checkSpecialCharacters(s, sellerLoginPwEdt) &&
                            checkAlphaNumber(sellerLoginPwEdt) &&
                            certainAlphaNumber(sellerLoginPwEdt)
                }
            }
        }
        sellerLoginIdEdt.addTextChangedListener(textWatcher)
        sellerLoginPwEdt.addTextChangedListener(textWatcher)

        // 일반 사용자 로그인으로 이동하기 버튼 클릭 시
        binding.sellerLoginMoveCustomerLoginTv.setOnClickListener {
            val intent = Intent(this, CustomerLoginActivity::class.java)
            startActivity(intent)
        }
        // 회원가입 버튼 클릭 시
        binding.sellerLoginRegisterTv.setOnClickListener {
            val intent = Intent(this, SellerRegisterActivity::class.java)
            startActivity(intent)
        }
        // 아이디 찾기 버튼 클릭 시
        binding.sellerLoginFindIdTv.setOnClickListener {
            // Dialog만들기
            val mDialogView =
                LayoutInflater.from(this).inflate(R.layout.seller_find_id_dialog, null)
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)
                .setTitle("아이디 찾기")
            mBuilder.show()

            val findIdBtn = mDialogView.findViewById<Button>(R.id.seller_find_id_btn)
            findIdBtn.setOnClickListener {
                val businessName =
                    mDialogView.findViewById<EditText>(R.id.seller_find_id_name_edt).text.toString()
                val phoneNumber =
                    mDialogView.findViewById<EditText>(R.id.seller_find_id_phone_number_edt).text.toString()
                val findIdTv = mDialogView.findViewById<TextView>(R.id.find_id_tv)
                var sellerData = SellerIdFindDTO(businessName, phoneNumber)

                val sellerAPI = retrofit.create(SellerAPI::class.java)

                GlobalScope.launch(Dispatchers.IO) {
                    try {
                        val response = sellerAPI.sellerIdFind(sellerData)
                        Log.d(TAG, "통신전")
                        Log.d(TAG, "$sellerData")
                        if (response.isNotEmpty()) {
                            // 요청 성공
                            Log.d(TAG, "성공")
                            if (response == "Seller not found") {
                                runOnUiThread {
                                    Toast.makeText(applicationContext, "올바른 정보가 아닙니다.", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                runOnUiThread {
                                    findIdTv.visibility = View.VISIBLE
                                    findIdTv.text = "아이디 - $response"
                                }
                                Log.d(TAG, "$response")
                            }
                        } else if (response.isEmpty()) {
                            // 요청 실패
                            Log.d(TAG, "실패")
                            Log.d(TAG, "$response")

                        }
                    } catch (e: Exception) {
                        Log.d(TAG, "예외")
                        Log.d(TAG, "$e")
                    }
                }

            }
        }
        // 로그인 버튼 클릭 시
        binding.sellerLoginBtn.setOnClickListener {
            /*
            val intent = Intent(applicationContext, SellerMainActivity::class.java)
            startActivity(intent)
            */

            if (idCheck && pwCheck){
                var id = binding.sellerLoginIdEdt.text.toString()
                var pw = binding.sellerLoginPwEdt.text.toString()
                val sellerData = SellerLoginDTO(id, pw)

                GlobalScope.launch(Dispatchers.IO) {
                    try {
                        val sellerAPI = retrofit.create(SellerAPI::class.java)

                        val response = sellerAPI.sellerLogin(sellerData)
                        Log.d(TAG, "통신전")
                        Log.d(TAG, "서버 응답1 : $response")
                        Log.d(TAG, "서버 응답2 : ${response.contentType()}")
                        Log.d(TAG, "서버 응답3 : ${response.source()}")
                        val contentType = response.contentType()
                        if (contentType?.type == "application" && contentType.subtype == "json") {
                            // 서버에서 반환하는 데이터가 JSON인 경우 처리
                            val gson = GsonBuilder()
                                .setLenient()
                                .create()
                            val data = gson.fromJson(response.string(), SellerLoginResponse::class.java)
                            Log.d(TAG, "4$data")
                        } else {
                            // 서버에서 반환하는 데이터가 text/plain인 경우 처리
                            val data = response.string()
                            Log.d(TAG, "5$data")
                            if (data == "login fail"){ // 로그인 실패
                                Log.d(TAG, "6$data")
                                runOnUiThread {
                                    Toast.makeText(applicationContext, "아이디 혹은 비밀번호가 올바르지 않습니다.", Toast.LENGTH_SHORT).show()
                                }
                            } else { // 로그인 성공
                                val sellerLoginDto = gson.fromJson(data, SellerLoginResponse::class.java)
                                Log.d(TAG, "7$sellerLoginDto")
                                runOnUiThread {
                                    Toast.makeText(applicationContext, "${sellerLoginDto.id} 님 환영합니다.", Toast.LENGTH_SHORT).show()
                                }
                                val intent = Intent(applicationContext, SellerMainActivity::class.java)
                                startActivity(intent)
                            }
                        }

                        // Log.d(TAG, "서버 응답 : ${response.body()}")
                    } catch (e: Exception) {
                        Log.d(TAG, "예외")
                        Log.d(TAG, "$e")
                    }
                }
            } else{
                Toast.makeText(this, "올바른 아이디와 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }
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
}



/*.

override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }



 if (response.isNotEmpty()) {
     // 요청 성공
     runOnUiThread {
         if (response == "login fail"){    // 로그인 실패한 경우
             Log.d(TAG, "String : $response")
             Toast.makeText(applicationContext, "아이디 혹은 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
         } else{     // 로그인 성공한 경우
             Log.d(TAG, "SellerLoginResponse : $response")
             // Toast.makeText(applicationContext, "${responseBody.id} 님 환영합니다.", Toast.LENGTH_SHORT).show()
             val intent = Intent(applicationContext, MainActivity::class.java)
             startActivity(intent)
         }
     }
     Log.d(TAG, "성공")
     // Log.d(TAG, "${response.body()}")
     Log.d(TAG, "$response")
 } else {
     // 요청 실패
     Log.d(TAG, "실패")
     // Log.d(TAG, "${response.body()}")
     Log.d(TAG, "$response")
 }
 */

/*
if(response.isSuccessful) {
    val sellerLoginResponse = response.body()
    Log.d(TAG, "$sellerLoginResponse")
    // 로그인 성공 시 처리
} else {
    // 로그인 실패 시 처리
    Log.d(TAG, "$response")
    Log.d(TAG, "${response.body()}")
}
*/
/*
when(response) {
    is Either.Left -> {
        // handle error
        val message = response.value.text
        Log.d(TAG, "$message")
    }
    is Either.Right -> {
        // handle success
        Log.d(TAG, "${response.value}")
    }
}
*/