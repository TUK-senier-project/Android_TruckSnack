package com.example.icontest2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.icontest2.customer_login.CustomerLoginActivity
import com.example.icontest2.customer_register.SignUpActivity
import com.example.icontest2.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra("name")
        if(name != null){
            Toast.makeText(this, "${name} 님 회원가입을 축하드립니다.", Toast.LENGTH_SHORT).show()
        }

        binding.loginToSignUpBtn.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
        binding.customerLoginBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            // 고객 DB정보 넘겨주고 로그인 처리하기
        }
        binding.sellerLoginBtn.setOnClickListener {
            val intent = Intent(this, SellerRegisterActivity::class.java)
            startActivity(intent)
            // 사장 DB정보 넘겨주고 로그인 처리하기
        }
        binding.sampleLoginBtn.setOnClickListener {
            //val intent = Intent(this, CustomerLogin::class.java)
            val intent = Intent(this, CustomerLoginActivity::class.java)
            startActivity(intent)
            // 손님 로그인 화면
        }
    }
}