package com.example.icontest2.seller_login

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import arrow.core.Either
import com.example.icontest2.*
import com.example.icontest2.databinding.ActivitySellerLoginBinding
import com.example.icontest2.seller_register.SellerCheckIdDTO
import com.example.icontest2.seller_register.SellerRegisterActivity
import com.example.icontest2.seller_register.SellerRegisterDTO
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SellerLoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySellerLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySellerLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        // 일반 사용자 로그인으로 이동하기 버튼 클릭 시
        binding.sellerLoginMoveCustomerLoginTv.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
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
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.seller_find_id_dialog, null)
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)
                .setTitle("아이디 찾기")
            mBuilder.show()

            val findIdBtn = mDialogView.findViewById<Button>(R.id.seller_find_id_btn)
            findIdBtn.setOnClickListener {
                val businessName = mDialogView.findViewById<EditText>(R.id.seller_find_id_name_edt).text.toString()
                val phoneNumber = mDialogView.findViewById<EditText>(R.id.seller_find_id_phone_number_edt).text.toString()
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
                            runOnUiThread {
                                findIdTv.visibility = View.VISIBLE
                                findIdTv.text = "아이디 - ${response}"
                            }
                            Log.d(TAG, "$response")
                        } else if(response.isEmpty()) {
                            // 요청 실패
                            Log.d(TAG, "실패")
                            Log.d(TAG, "$response")
                            runOnUiThread {
                                Toast.makeText(applicationContext, "올바른 정보가 아닙니다.", Toast.LENGTH_SHORT).show()
                            }
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
            var id = binding.sellerLoginIdEdt.text.toString()
            var pw = binding.sellerLoginPwEdt.text.toString()
            val sellerData = SellerLoginDTO(id, pw)

            val sellerAPI = retrofit.create(SellerAPI::class.java)

            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val response = sellerAPI.sellerLogin(sellerData)

                    Log.d(TAG, "통신전")
                    Log.d(TAG, "$response")
                    if(response.isSuccessful) {
                        val sellerLoginResponse = response.body()
                        Log.d(TAG, "$sellerLoginResponse")
                        // 로그인 성공 시 처리
                    } else {
                        // 로그인 실패 시 처리
                        Log.d(TAG, "$response")
                        Log.d(TAG, "${response.body()}")
                    }
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
                    /*
                    // 응답 처리
                    if (response.isSuccessful) {
                        // 로그인 성공 시 Seller 객체 받아오기
                        val seller: SellerLoginResponse? = response.body()
                        if (seller != null) {
                            // Seller 객체를 이용한 처리
                            runOnUiThread {
                                Toast.makeText(applicationContext, "${seller.id} 님 환영합니다.", Toast.LENGTH_SHORT).show()
                                val intent = Intent(applicationContext, MainActivity::class.java)
                                startActivity(intent)
                            }
                        }
                    } else {
                        // 로그인 실패 시 "login fail" 문자열 받아오기
                        val errorMsg: String? = response.body().toString()
                        if (errorMsg != null) {
                            // "login fail" 문자열을 이용한 처리
                            runOnUiThread {
                                Toast.makeText(applicationContext, "아이디 혹은 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    */
                    /*
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
                } catch (e: Exception) {
                    Log.d(TAG, "예외")
                    Log.d(TAG, "$e")
                }
            }
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}