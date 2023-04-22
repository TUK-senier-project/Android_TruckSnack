package com.example.icontest2.customer_food_list

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.icontest2.customer_main.SellerListAdapter
import com.example.icontest2.customer_main.SellerListDTO
import com.example.icontest2.databinding.ActivityCustomerFoodListBinding
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.InterfaceAddress

class CustomerFoodListActivity : AppCompatActivity() {

    init {
        instance = this
    }
    companion object{
        private var instance: CustomerFoodListActivity? = null
        fun getInstance(): CustomerFoodListActivity?{
            return instance
        }
    }
    private lateinit var binding : ActivityCustomerFoodListBinding
    private lateinit var lists : List<CustomerFoodListDTOItem>
    val gson = GsonBuilder()
        .setLenient()
        .create()

    val retrofit = Retrofit.Builder()
        .baseUrl("http://13.124.112.81:8080")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    var customerId :String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerFoodListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ToolBar 설정, 제목, 버튼 활성화, 아이콘 클릭 가능 설정
        setSupportActionBar(binding.customerFoodListToolbar) // 생성시 ()안에 id 변경.
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val intent = intent
        if (intent != null) {
            customerId = intent.getStringExtra("customer_id").toString()
        }
        Log.d("FoodListAct", "==========$customerId==========")

        val foodCategory = intent.getIntExtra("foodCategory", -1)

        when (foodCategory) {
            1 -> binding.customerFoodListTitleTv.text = "붕어빵"
            2 -> binding.customerFoodListTitleTv.text = "타코야끼"
            3 -> binding.customerFoodListTitleTv.text = "닭꼬치"
            4 -> binding.customerFoodListTitleTv.text = "떡볶이"
            5 -> binding.customerFoodListTitleTv.text = "스테이크"
            6 -> binding.customerFoodListTitleTv.text = "토스트"
            7 -> binding.customerFoodListTitleTv.text = "햄버거"
            8 -> binding.customerFoodListTitleTv.text = "츄러스"
            9 -> binding.customerFoodListTitleTv.text = "초밥"
            10 -> binding.customerFoodListTitleTv.text = "아이스크림"
            //11 -> binding.customerFoodListTitleTv.text = "스테이크"
            //12 -> binding.customerFoodListTitleTv.text = "스테이크"
            //13 -> binding.customerFoodListTitleTv.text = "스테이크"
            //14 -> binding.customerFoodListTitleTv.text = "스테이크"
            //15 -> binding.customerFoodListTitleTv.text = "스테이크"
        }
        Log.d("===================", "$foodCategory")


        GlobalScope.launch(Dispatchers.IO) {
            try {
                val customerFoodAPI = retrofit.create(CustomerFoodAPI::class.java)

                val response = customerFoodAPI.customerFoodList(foodCategory)
                Log.d(TAG, "통신전")
                Log.d(TAG, "서버 응답1 : $response")
                Log.d(TAG, "서버 응답2 : ${response.contentType()}")
                Log.d(TAG, "서버 응답3 : ${response.source()}")
                val contentType = response.contentType()
                if (contentType?.type == "application" && contentType.subtype == "json") {
                    // 서버에서 반환하는 데이터가 JSON인 경우 처리
                    //val foodList = gson.fromJson(response.string(), CustomerFoodListDTO::class.java)
                    //Log.d(TAG, "4$foodList")
                } else {
                    // 서버에서 반환하는 데이터가 text/plain인 경우 처리
                    val foodList = response.string()
                    Log.d(TAG, "5$foodList")
                    if (foodList == "[]"){ // 실패
                        Log.d(TAG, "6$foodList")
                    } else { // 성공
                        Log.d(TAG, "5${foodList}")
                        val listType = object : TypeToken<List<CustomerFoodListDTOItem>>() {}.type
                        val customerFoodList: List<CustomerFoodListDTOItem> = gson.fromJson(foodList, listType)
                        //Log.d(TAG, "5${customerFoodList[1]}")
                        //Log.d(TAG, "5${customerFoodList[2]}")
                        lists = customerFoodList
                        //val foodData = gson.fromJson(foodList, CustomerFoodListDTO::class.java)
                        //Log.d("ASDASD", "7$foodData")
                        //Log.d("ASDASD", "8${foodData.customerFoodListDTO[0]}")
                        //lists = foodData.customerFoodListDTO
                        //Log.d(TAG, "9$lists")
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
        binding.foodListNumberOfOrders.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                categoryRank("numberOfOrders", foodCategory)
            }
        }
        binding.foodListReorder.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                categoryRank("reorder", foodCategory)
            }
        }
        binding.foodListGrade.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                categoryRank("grade", foodCategory)
            }
        }
    }
    suspend fun categoryRank(type: String, categoryNumber: Int) {
        val customerFoodAPI = retrofit.create(CustomerFoodAPI::class.java)
        val response = customerFoodAPI.getRanking(type, categoryNumber)
        val contentType = response.contentType()
        if (contentType?.type == "application" && contentType.subtype == "json") {
            // 서버에서 반환하는 데이터가 JSON인 경우 처리
            //val foodList = gson.fromJson(response.string(), CustomerFoodListDTO::class.java)
            Log.d(TAG, "json형식으로 들어왔습니다.")
        } else {
            // 서버에서 반환하는 데이터가 text/plain인 경우 처리
            val foodList = response.string()
            Log.d(TAG, "5$foodList")
            if (foodList == "[]"){ // 실패
                Log.d(TAG, "6$foodList")
            } else { // 성공
                Log.d(TAG, "5${foodList}")
                val listType = object : TypeToken<List<CustomerFoodListDTOItem>>() {}.type
                val customerFoodList: List<CustomerFoodListDTOItem> = gson.fromJson(foodList, listType)
                //Log.d(TAG, "5${customerFoodList[1]}")
                //Log.d(TAG, "5${customerFoodList[2]}")
                lists = customerFoodList
                Log.d("categoryRank", "categoryRank - lists - $lists")
                runOnUiThread {
                    initViews()
                }
            }
        }
    }

    private fun initViews() {
        binding.sellerListRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.sellerListRv.adapter = CustomerFoodListAdapter(lists, customerId!!)
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