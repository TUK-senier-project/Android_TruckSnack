package com.example.icontest2.customer_food_list

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.BitmapFactory
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.icontest2.R
import com.example.icontest2.databinding.ActivityCustomerFoodDetailBinding
import com.example.icontest2.navigation.NavigationActivity
import com.example.icontest2.order.OrderRegisterActivity
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileInputStream

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
    private lateinit var lists : List<CustomerFoodDetailDTO>
    var foodOrderList: MutableList<FoodOrder> = mutableListOf()
    var customerId: String? = null
    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerFoodDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        if (intent != null) {
            customerId = intent.getStringExtra("customer_id").toString()
        }
        Log.d("FoodDetailAct", "==========$customerId==========")

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
        if (selectedList?.sellerImgS3Url != null){
            // 내부 저장소 경로 가져오기
            val directory = applicationContext.filesDir
            // 파일 경로 생성
            val filePath = File(directory, selectedList.businessName)
            val inputStream = FileInputStream(filePath)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            binding.customerFoodDetailImg.setImageBitmap(bitmap)
        }
        val sellerId = selectedList?.id

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val customerFoodAPI = retrofit.create(CustomerFoodAPI::class.java)

                val response = customerFoodAPI.customerFoodDetail(sellerId!!)
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
                        lists = listOf()
                        Log.d(TAG, "6$foodDetail")
                    } else { // 성공
                        val listType = object : TypeToken<List<CustomerFoodDetailDTO>>() {}.type
                        val foodDetailList: List<CustomerFoodDetailDTO> = gson.fromJson(foodDetail, listType)
                        lists = foodDetailList
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
        binding.customerFoodDetailLocationImg.setOnClickListener {
            val intent = Intent(this, NavigationActivity::class.java)
            startActivity(intent)
        }
        binding.foodDetailOrder.setOnClickListener {
            val intent = Intent(this, OrderRegisterActivity::class.java).apply {
                putExtra("foodOrderList", FoodOrderList(foodOrderList))
                putExtra("selected_id", sellerId)
                putExtra("customer_id", customerId)
            }
            startActivity(intent)
        }
    }
    private fun initViews() {
        binding.customerFoodDetailRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.customerFoodDetailRv.adapter = CustomerFoodDetailAdapter(lists)
    }
    fun changeOrderList () {
        Log.d("FoodDetailAct", "changeOrderList - $foodOrderList")
        if (foodOrderList.size == 0){
            binding.orderCheckLayout.visibility = View.GONE
        } else {
            var total = 0
            for (data in foodOrderList) {
                total += data.foodPrice
            }
            binding.orderCheckLayout.visibility = View.VISIBLE
            binding.totalOrderQuantity.text = getString(R.string.food_order_quantity, foodOrderList.size.toString())
            binding.totalOrderAmount.text = getString(R.string.food_order_amount, total.toString())
        }
    }

}