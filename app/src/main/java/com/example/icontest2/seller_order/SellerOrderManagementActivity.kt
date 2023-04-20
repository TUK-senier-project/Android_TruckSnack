package com.example.icontest2.seller_order

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.icontest2.customer_food_list.*
import com.example.icontest2.databinding.ActivitySellerOrderManagementBinding
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class SellerOrderManagementActivity : AppCompatActivity() {
    init {
        instance = this
    }
    companion object{
        private var instance: SellerOrderManagementActivity? = null
        fun getInstance(): SellerOrderManagementActivity?{
            return instance
        }
    }

    private lateinit var binding: ActivitySellerOrderManagementBinding
    var foodLists : List<CustomerFoodDetailDTO> = listOf()
    var orderLists : List<SellerOrderManagementDTO> = listOf()
    var orderDetailLists : List<SellerOrderDetailResponse> = listOf()
    private val gson = GsonBuilder()
        .setLenient()
        .create()
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://13.124.112.81:8080")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySellerOrderManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sellerId = intent.getStringExtra("seller_id")

        // ToolBar 설정, 제목, 버튼 활성화, 아이콘 클릭 가능 설정
        setSupportActionBar(binding.sellerOrderManagementToolbar) // 생성시 ()안에 id 변경.
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        if (sellerId != null) {
            GlobalScope.launch(Dispatchers.IO) {
                getOrderList(sellerId)
                getFoodList(sellerId)

            }
        }else {
            Toast.makeText(this, "다시 로그인하여 주십시오.", Toast.LENGTH_SHORT).show()
        }

    }
    suspend fun getOrderDetailList(seq: Int){
        try {
            val managementAPI = retrofit.create(SellerOrderManagementAPI::class.java)
            val response = managementAPI.sellerOrderDetailList(SellerOrderDetailDTO(seq))
            Log.d(ContentValues.TAG, "통신전")
            Log.d(ContentValues.TAG, "서버 응답1 : $response")
            Log.d(ContentValues.TAG, "서버 응답2 : ${response.contentType()}")
            Log.d(ContentValues.TAG, "서버 응답3 : ${response.source()}")
            val contentType = response.contentType()
            Log.d("getOrderList", "getOrderList - contentType - $contentType")
            if (contentType?.type == "application" && contentType.subtype == "json") {
                // 서버에서 반환하는 데이터가 JSON인 경우 처리
                //val orderList = gson.fromJson(response.string(), CustomerFoodDetailDTO::class.java)
                //Log.d(ContentValues.TAG, "4$orderList")
                Log.d("SOMAct", "getOrderList - Json입니다!!!")
            } else {
                // 서버에서 반환하는 데이터가 text/plain인 경우 처리
                val orderDetailList = response.string()
                Log.d(ContentValues.TAG, "getOrderList - orderList - $orderDetailList")
                if (orderDetailList == "[]"){ // 실패
                    Log.d(ContentValues.TAG, "getOrderList - orderLists - 실패 - $orderLists")
                } else { // 성공
                    val listType = object : TypeToken<List<SellerOrderDetailResponse>>() {}.type
                    val detailLists: List<SellerOrderDetailResponse> = gson.fromJson(orderDetailList, listType)
                    orderDetailLists = detailLists
                    Log.d("getOrderDetailList","getOrderDetailList- orderDetailList - $orderDetailLists")
                }
            }
        } catch (e: Exception) {
            Log.d(ContentValues.TAG, "예외")
            Log.d(ContentValues.TAG, "$e")
        }
    }

    private suspend fun getOrderList(sellerId: String){
        try {
            val managementAPI = retrofit.create(SellerOrderManagementAPI::class.java)
            val response = managementAPI.sellerOrderList(SellerIdDTO(sellerId))
            Log.d(ContentValues.TAG, "통신전")
            Log.d(ContentValues.TAG, "서버 응답1 : $response")
            Log.d(ContentValues.TAG, "서버 응답2 : ${response.contentType()}")
            Log.d(ContentValues.TAG, "서버 응답3 : ${response.source()}")
            val contentType = response.contentType()
            Log.d("getOrderList", "getOrderList - contentType - $contentType")
            if (contentType?.type == "application" && contentType.subtype == "json") {
                // 서버에서 반환하는 데이터가 JSON인 경우 처리
                //val orderList = gson.fromJson(response.string(), CustomerFoodDetailDTO::class.java)
                //Log.d(ContentValues.TAG, "4$orderList")
                Log.d("SOMAct", "getOrderList - Json입니다!!!")
            } else {
                // 서버에서 반환하는 데이터가 text/plain인 경우 처리
                val orderList = response.string()
                Log.d(ContentValues.TAG, "getOrderList - orderList - $orderList")
                if (orderList == "[]"){ // 실패
                    orderLists = listOf()
                    Log.d(ContentValues.TAG, "getOrderList - orderLists - 실패 - $orderLists")
                } else { // 성공
                    val listType = object : TypeToken<List<SellerOrderManagementDTO>>() {}.type
                    val orderDetailList: List<SellerOrderManagementDTO> = gson.fromJson(orderList, listType)
                    orderLists = orderDetailList
                    runOnUiThread {
                        initViews()
                    }
                }
            }
        } catch (e: Exception) {
            Log.d(ContentValues.TAG, "예외")
            Log.d(ContentValues.TAG, "$e")
        }
    }

    private suspend fun getFoodList(sellerId: String) {
        val customerFoodAPI = retrofit.create(CustomerFoodAPI::class.java)
        val response = customerFoodAPI.customerFoodDetail(sellerId)
        Log.d(ContentValues.TAG, "통신전")
        Log.d(ContentValues.TAG, "서버 응답1 : $response")
        Log.d(ContentValues.TAG, "서버 응답2 : ${response.contentType()}")
        Log.d(ContentValues.TAG, "서버 응답3 : ${response.source()}")
        val contentType = response.contentType()
        if (contentType?.type == "application" && contentType.subtype == "json") {
            // 서버에서 반환하는 데이터가 JSON인 경우 처리
            val foodList = gson.fromJson(response.string(), CustomerFoodDetailDTO::class.java)
            Log.d(ContentValues.TAG, "4$foodList")
        } else {
            // 서버에서 반환하는 데이터가 text/plain인 경우 처리
            val foodDetail = response.string()
            Log.d(ContentValues.TAG, "5$foodDetail")
            if (foodDetail == "[]"){ // 실패
                foodLists = listOf()
                Log.d(ContentValues.TAG, "6$foodDetail")
            } else { // 성공
                val listType = object : TypeToken<List<CustomerFoodDetailDTO>>() {}.type
                val foodDetailList: List<CustomerFoodDetailDTO> = gson.fromJson(foodDetail, listType)
                foodLists = foodDetailList
            }
        }
    }
    private fun initViews() {
        binding.sellerOrderManagementRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.sellerOrderManagementRv.adapter = SellerOrderManagementAdapter(orderLists)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}