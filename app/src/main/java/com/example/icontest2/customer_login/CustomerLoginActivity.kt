package com.example.icontest2.customer_login

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.icontest2.LoginActivity
import com.example.icontest2.MainActivity
import com.example.icontest2.databinding.ActivityCustomerLoginBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class CustomerLoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCustomerLoginBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val loginButton = binding.loginButton
        val idFindButton = binding.idFindButton

        // 로그인 버튼
        loginButton.setOnClickListener {
            val idEditText = binding.customerLoginId
            val pwEditText = binding.customerLoginPw
            val customer_id = idEditText.text.toString()
            val customer_pw = pwEditText.text.toString()
            val customerlogin = CustomerLoginDTO(customer_id, customer_pw) // 사용하지 않는 파라미터는 어떻게 처리하는지 물어보기
            Log.d(TAG, "$customerlogin")
            Log.d(TAG, "${idEditText::class.java}")
            Log.d(TAG, "${pwEditText::class.java}")

            val retrofit = Retrofit.Builder() // 서버통신
                .baseUrl("http://13.209.18.214:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            // API 서비스 인터페이스 구현체 생성
            val customerLoginAPI = retrofit.create(CustomerLoginAPI::class.java)
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val response = customerLoginAPI.loginCustomer(customerlogin) // api응답 생성 후 수정
                    if (response.isSuccessful) {
                        Log.d(TAG, "서버 성공 로그 체크!!}")
                        Log.d(TAG, "${response.body()}")
                        Log.d(TAG, "$response")
                        var intent = Intent(applicationContext,MainActivity::class.java) // 성공시 화면 전환
                        startActivity(intent)
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
        // 아이디 찾기 버튼
        idFindButton.setOnClickListener {
            val nameEditText = binding.customerFindName
            val phoneEditText = binding.customerFindPhone
            val customer_name = nameEditText.text.toString()
            val customer_phoneNumber = phoneEditText.text.toString()
            val customerFind = CustomerFindDTO(customer_name, customer_phoneNumber)
            // Retrofit 객체 생성
            val retrofit = Retrofit.Builder()
                .baseUrl("http://13.209.18.214:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            // API 서비스 인터페이스 구현체 생성
            val customerFindAPI = retrofit.create(CustomerFindAPI::class.java)
            // API 호출
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val response = customerFindAPI.findCustomer(customerFind) // 성공하면 여기에 아이디가 담겨온다
                    if (response.isSuccessful) {
                        // 성공 처리
                        // val result = response.body() as CustomerFindDTO
                        // 아이디 찾기 결과를 화면에 표시하는 코드 작성
                        runOnUiThread {
                            binding.customerLoginId.setText(response.body())
                        }
                    } else {
                        // 실패 처리
                        // 에러 메시지를 화면에 표시하는 코드 작성
                        runOnUiThread {
                            Toast.makeText(this@CustomerLoginActivity, "다시 입력해주세요.", Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: Exception) {
                    // 예외 처리
                    // 예외 메시지를 화면에 표시하는 코드 작성
                    runOnUiThread {
                        Toast.makeText(this@CustomerLoginActivity, "서버와의 통신 중 오류가 발생했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }


    }
}