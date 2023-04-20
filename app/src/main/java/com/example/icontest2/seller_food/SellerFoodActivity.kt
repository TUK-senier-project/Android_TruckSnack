package com.example.icontest2.seller_food

import android.app.Activity
import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.icontest2.R
import com.example.icontest2.databinding.ActivitySellerFoodBinding
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.recyclerview.widget.RecyclerView // 리사이클뷰
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SellerFoodActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySellerFoodBinding
    private var sellerId: String = "" // 셀러로변경
    private lateinit var startForResult: ActivityResultLauncher<Intent>

    init {
        instance = this
    }
    companion object{
        private var instance: SellerFoodActivity? = null
        fun getInstance(): SellerFoodActivity?{
            return instance
        }
    }
    private lateinit var lists : List<SellerFoodDTO>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySellerFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // 데이터 받아오기
        val id = intent.getStringExtra("sellerId")
        if (id != null) {
            sellerId = id
        }
        Log.d(TAG, "===========메인에서푸드로받기========")
        Log.d(TAG, "$id")
        // 데이터 전송
        val menuRegisterBtn2 = binding.menuRegisterBtn
        menuRegisterBtn2.setOnClickListener {
            val intent = Intent(this@SellerFoodActivity, SellerFoodRegisterActivity::class.java)
            intent.putExtra("sellerId", id)
            Log.d(TAG,"=====푸드에서레지스터로=====")
            Log.d(TAG,"$id")
            startActivity(intent)
        }
        val menuRegisterBtn = binding.menuRegisterBtn // 메뉴등록 다이얼로그 연결버튼
        val menuCallBtn = binding.menuCallBtn // 등록된 메뉴 불러오는 버튼
        val gson = GsonBuilder()
            .setLenient()
            .create()
        val retrofit = Retrofit.Builder()
            .baseUrl("http://13.124.112.81:8080")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val SellerFoodCallAPI = retrofit.create(SellerFoodCallAPI::class.java)

                val response = SellerFoodCallAPI.sellerFoodCall(sellerId!!)
                Log.d(TAG, "통신전")
                Log.d(TAG, "서버 응답1 : $response")
                Log.d(TAG, "서버 응답2 : ${response.contentType()}")
                Log.d(TAG, "서버 응답3 : ${response.source()}")
                val contentType = response.contentType()
                if (contentType?.type == "application" && contentType.subtype == "json") {
                    // 서버에서 반환하는 데이터가 JSON인 경우 처리
                    val foodList = gson.fromJson(response.string(), SellerFoodDTO::class.java)
                    Log.d(TAG, "4$foodList")
                } else {
                    // 서버에서 반환하는 데이터가 text/plain인 경우 처리
                    val foodDetail = response.string()
                    Log.d(TAG, "5$foodDetail")
                    if (foodDetail == "[]"){ // 실패
                        lists = listOf()
                        Log.d(TAG, "6$foodDetail")
                    } else { // 성공
                        val listType = object : TypeToken<List<SellerFoodDTO>>() {}.type
                        val foodDetailList: List<SellerFoodDTO> = gson.fromJson(foodDetail, listType)
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

    } // 온크리에잇 종료
    private fun initViews() {
        binding.sellerFoodRecylerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.sellerFoodRecylerView.adapter = SellerFoodAdapter(lists)
    }
}
