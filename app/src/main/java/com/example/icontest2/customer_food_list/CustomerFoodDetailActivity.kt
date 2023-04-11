package com.example.icontest2.customer_food_list

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.icontest2.databinding.ActivityCustomerFoodDetailBinding
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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
    private lateinit var lists : List<CustomerFoodDetailDTOItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerFoodDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val gson = GsonBuilder()
            .setLenient()
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://13.124.112.81:8080")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val selectedList = intent.getParcelableExtra<CustomerFoodListDTOItem>("selected_item")
        Log.d(TAG, "onCreate - selectedList - $selectedList")
        binding.customerFoodDetailBusinessNameTv.text = selectedList?.businessName
        binding.customerFoodDetailGradeTv.text = selectedList?.grade.toString()
        binding.customerFoodDetailDeadlineTv.text = selectedList?.deadline.toString()
        val sellerId = selectedList!!.id

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val customerFoodAPI = retrofit.create(CustomerFoodAPI::class.java)

                val response = customerFoodAPI.customerFoodDetail(sellerId)
                Log.d(TAG, "통신전")
                Log.d(TAG, "서버 응답1 : $response")
                Log.d(TAG, "서버 응답2 : ${response.contentType()}")
                Log.d(TAG, "서버 응답3 : ${response.source()}")
                val contentType = response.contentType()
                if (contentType?.type == "application" && contentType.subtype == "json") {
                    // 서버에서 반환하는 데이터가 JSON인 경우 처리
                    val foodList = gson.fromJson(response.string(), CustomerFoodDetailDTO::class.java)
                    Log.d(TAG, "4$foodList")
                } else {
                    // 서버에서 반환하는 데이터가 text/plain인 경우 처리
                    val foodDetail = response.string()
                    Log.d(TAG, "5$foodDetail")
                    if (foodDetail == "[]"){ // 실패
                        Log.d(TAG, "6$foodDetail")
                    } else { // 성공
                        val foodData = gson.fromJson(foodDetail, CustomerFoodDetailDTO::class.java)
                        Log.d(TAG, "7$foodData")
                        Log.d(TAG, "8${foodData[0]}")
                        lists = foodData.toList()
                        Log.d(TAG, "9$lists")

                        runOnUiThread {
                            initViews()
                        }

                    }
                }
            } catch (e: Exception) {
                Log.d(TAG, "예외")
                Log.d(TAG, "$e")
            }
        }
    }
    private fun initViews() {
        binding.customerFoodDetailRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.customerFoodDetailRv.adapter = CustomerFoodDetailAdapter(lists)
    }
}