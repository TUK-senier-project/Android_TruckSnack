package com.example.icontest2.seller_login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.icontest2.databinding.ActivitySellerFirstLoginBinding

class SellerFirstLoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySellerFirstLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySellerFirstLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}