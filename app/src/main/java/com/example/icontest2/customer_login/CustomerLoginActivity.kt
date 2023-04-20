package com.example.icontest2.customer_login

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.icontest2.customer_main.CustomerMainActivity
import com.example.icontest2.R
import com.example.icontest2.customer_register.SignUpActivity
import com.example.icontest2.databinding.ActivityCustomerLoginBinding
import com.google.gson.GsonBuilder
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
        val goToCustomerRegister = binding.toCustomerLogin

        // 로그인 버튼
        loginButton.setOnClickListener {
            /*
            var intent = Intent(applicationContext, CustomerMainActivity::class.java) // 성공시 화면 전환
            startActivity(intent)
            */

            val idEditText = binding.customerLoginId
            val pwEditText = binding.customerLoginPw
            val customerId = idEditText.text.toString()
            val customerPw = pwEditText.text.toString()
            val customerlogin = CustomerLoginDTO(customerId, customerPw) // 사용하지 않는 파라미터는 어떻게 처리하는지 물어보기
            Log.d(TAG, "$customerlogin")
            Log.d(TAG, "${idEditText::class.java}")
            Log.d(TAG, "${pwEditText::class.java}")

            val retrofit = Retrofit.Builder() // 서버통신
                .baseUrl("http://13.124.112.81:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            // API 서비스 인터페이스 구현체 생성
            val customerLoginAPI = retrofit.create(CustomerLoginAPI::class.java)
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val response = customerLoginAPI.loginCustomer(customerlogin) // api응답 생성 후 수정
                    val contentType = response.contentType()
                    val gson = GsonBuilder()
                        .setLenient()
                        .create()
                    if (contentType?.type == "application" && contentType.subtype == "json") {
                        // 서버에서 반환하는 데이터가 JSON인 경우 처리

                        val data = gson.fromJson(response.string(), CustomerLoginResponse::class.java)
                        Log.d(TAG, "4$data")
                    } else {
                        // 서버에서 반환하는 데이터가 text/plain인 경우 처리
                        val data = response.string()
                        Log.d(TAG, "5$data")
                        if (data == "login fail"){ // 로그인 실패
                            Log.d(TAG, "6$data")
                            runOnUiThread {
                                Toast.makeText(applicationContext, "아이디와 비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show()
                            }
                        } else { // 로그인 성공
                            val customerLoginDto = gson.fromJson(data, CustomerLoginResponse::class.java)
                            Log.d(TAG, "7$customerLoginDto")
                            val intent = Intent(applicationContext, CustomerMainActivity::class.java) // 성공시 화면 전환
                            intent.putExtra("customerName", customerLoginDto.customer.name)
                            intent.putExtra("customerId", customerLoginDto.customer.id)
                            intent.putExtra("base64String", customerLoginDto.base64EncodedImage)
                            startActivity(intent)
                            // 성공 처리 토스트 메세지
                            runOnUiThread {
                                Toast.makeText(applicationContext, "로그인 성공", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    /*
                    if (response.isSuccessful) {
                        Log.d(TAG, "서버 성공 로그 체크!!}")
                        Log.d(TAG, "${response.body()}")
                        Log.d(TAG, "$response")
                        val intent = Intent(applicationContext, CustomerMainActivity::class.java) // 성공시 화면 전환
                        intent.putExtra("customerId", "${response.body()}")
                        startActivity(intent)
                        // 성공 처리 토스트 메세지
                        runOnUiThread {
                            Toast.makeText(this@CustomerLoginActivity, "로그인 성공", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        // 실패 처리
                        Log.d(TAG, "서버 실패 로그 체크!!}")
                        Log.d(TAG, "${response.errorBody()}")
                        Log.d(TAG, "$response")
                        runOnUiThread {
                            // binding.customerLoginId.setText(response.body())
                            Toast.makeText(this@CustomerLoginActivity, "아이디와 비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show()
                        }
                    }
                    */
                } catch (e: Exception) {
                    // 예외 처리
                    Log.d(TAG, "서버 예외 로그 체크!!}")
                    Log.d(TAG, "$e")
                }
            }

        }
        // 로그인 화면에서 아이디찾기 버튼 누를 때
        idFindButton.setOnClickListener {
            // 다이얼로그 만들기
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.customer_find_id_dialog, null)
            val mbuilder = AlertDialog.Builder(this)
                .setView(mDialogView)
                //.setTitle("아이디 찾기")
            mbuilder.show()
            val idFindDialogButton = mDialogView.findViewById<Button>(R.id.id_find_Dialog_Button)
            idFindDialogButton.setOnClickListener {
                val nameEditText = mDialogView.findViewById<EditText>(R.id.customer_find_name)
                val phoneEditText = mDialogView.findViewById<EditText>(R.id.customer_find_phone)
                // val nameEditText = binding.customerFindName
                // val phoneEditText = binding.customerFindPhone
                val customerName = nameEditText.text.toString()
                val customerPhoneNumber = phoneEditText.text.toString()
                val customerFind = CustomerFindDTO(customerName, customerPhoneNumber)
            // Retrofit 객체 생성
            val retrofit = Retrofit.Builder()
                .baseUrl("http://13.124.112.81:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            // API 서비스 인터페이스 구현체 생성
            val customerFindAPI = retrofit.create(CustomerFindAPI::class.java)
            // API 호출
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val response = customerFindAPI.findCustomer(customerFind) // 성공하면 여기에 아이디가 담겨온다
                    if (response.isSuccessful) {
                        Log.d(TAG, "$response")
                        // 성공 처리
                        // val result = response.body() as CustomerFindDTO
                        runOnUiThread {
                            // binding.customerLoginId.setText(response.body())
                            mDialogView.findViewById<EditText>(R.id.customer_id_found).setText(response.body())
                        }
                    } else {
                        // 실패 처리
                        Log.d(TAG, "2$response")
                        runOnUiThread {
                            Toast.makeText(this@CustomerLoginActivity, "다시 입력해주세요.", Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: Exception) {
                    // 예외 처리
                    runOnUiThread {
                        Toast.makeText(this@CustomerLoginActivity, "서버와의 통신 중 오류가 발생했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            }
        }
        // 회원가입 하러가기 텍스트버튼
        goToCustomerRegister.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }// 온크리에잇 종료

}