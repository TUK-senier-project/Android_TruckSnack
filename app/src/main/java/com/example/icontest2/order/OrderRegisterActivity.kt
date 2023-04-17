package com.example.icontest2.order

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.icontest2.customer_food_list.CustomerFoodListDTOItem
import com.example.icontest2.databinding.ActivityOrderRegisterBinding
import com.example.icontest2.databinding.ActivitySellerLoginBinding

class OrderRegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val foodName = intent.getStringExtra("foodName")
        val foodSeq = intent.getIntExtra("foodSeq", -1)
        val foodPrice = intent.getIntExtra("foodPrice", -1)
        Log.d("OrderRegisterActivity", "onCreate - $foodName")
        Log.d("OrderRegisterActivity", "onCreate - $foodSeq")
        Log.d("OrderRegisterActivity", "onCreate - $foodPrice")
    }
}