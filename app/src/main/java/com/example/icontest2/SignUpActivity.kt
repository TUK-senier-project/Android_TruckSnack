package com.example.icontest2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.icontest2.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val returnIntent = Intent(this, LoginActivity::class.java)
        /*
        binding.signUpRetryBtn.setOnClickListener {
            binding.signUpIdEdit.setText(null)
            binding.signUpPasswdEdit.setText(null)
            binding.signUpCheckEdit.setText(null)
            binding.signUpNameEdit.setText(null)
            binding.signUpPhoneEdit.setText(null)
        }
        */
        binding.signUpBtn.setOnClickListener {
            returnIntent.putExtra("name", binding.signUpNameEdit.text.toString())
            startActivity(returnIntent)
            finish()
        }


    }
}