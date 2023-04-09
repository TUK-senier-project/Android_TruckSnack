package com.example.icontest2.customer_food_list

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.icontest2.databinding.ActivityCustomerFoodDetailBinding

class CustomerFoodDetailActivity : AppCompatActivity() {
    init {
        instance = this
    }
    companion object{
        private var instance: CustomerFoodDetailActivity? = null
        fun getInstance(): CustomerFoodDetailActivity?{
            return instance
        }
    }

    private lateinit var binding : ActivityCustomerFoodDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerFoodDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}