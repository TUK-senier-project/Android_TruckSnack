package com.example.icontest2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.icontest2.databinding.ActivitySellerMainBinding

class SellerMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySellerMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySellerMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.sellerMainIdTv.text = intent.getStringExtra("sellerId")
        binding.sellerMainBusinessNameTv.text = intent.getStringExtra("sellerBusinessName")
    }
    override fun onBackPressed() {
    }
}