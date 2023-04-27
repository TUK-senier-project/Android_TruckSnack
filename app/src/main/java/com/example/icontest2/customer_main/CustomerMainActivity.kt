package com.example.icontest2.customer_main

import android.app.Activity
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.icontest2.*
import com.example.icontest2.customer_food_list.CustomerFoodListActivity
import com.example.icontest2.databinding.ActivityCustomerMainBinding
import com.example.icontest2.order.OrderRegisterActivity
import com.example.icontest2.seller_login.SellerLoginResponse
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

class CustomerMainActivity : AppCompatActivity() {
    init {
        instance = this
    }
    companion object{
        private var instance: CustomerMainActivity? = null
        fun getInstance(): CustomerMainActivity?{
            return instance
        }
    }
    private lateinit var binding : ActivityCustomerMainBinding
    private var customerName: String = ""
    private var customerId: String = ""
    private var base64String: String = ""

    private val lists = listOf(
        SellerListDTO("붕어빵먹자 1호트럭", "#붕어빵 #팥 #슈크림 #피자", 4.5, 21, ""),
        SellerListDTO("전통붕어빵집", "#붕어빵 #팥 #슈크림 #피자", 4.7, 23, ""),
        SellerListDTO("너와나의 붕어빵", "#붕어빵 #팥 #슈크림 #피자", 5.0, 22, ""),
        SellerListDTO("붕어빵먹자 2호트럭", "#붕어빵 #팥 #슈크림 #피자", 3.8, 19, ""),
        SellerListDTO("붕어빵먹자 3호트럭", "#붕어빵 #팥 #슈크림 #피자", 4.0, 20, ""),
        SellerListDTO("붕어빵먹자 4호트럭", "#붕어빵 #팥 #슈크림 #피자", 3.0, 21, ""),
        SellerListDTO("붕어빵먹자 5호트럭", "#붕어빵 #팥 #슈크림 #피자", 2.8, 21, ""),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        base64String = intent.getStringExtra("base64String").toString()
        customerId = intent.getStringExtra("customerId").toString()
        customerName = intent.getStringExtra("customerName").toString()
        binding.customerMainNameTv.text = customerName
        //binding.locationTv.text = intent.getStringExtra("location").toString()

        Log.d("AAAA", "onCreate - $base64String")
        if (base64String != null){
            // Base64 디코딩
            val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
            // Bitmap 으로 변환
            val decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
            binding.customerMainProfile.setImageBitmap(decodedBitmap)
        }

        fun getRealPathFromUri(uri: Uri): String {
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = contentResolver.query(uri, projection, null, null, null)
            val columnIndex = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor?.moveToFirst()
            val filePath = cursor?.getString(columnIndex!!)
            cursor?.close()
            return filePath ?: ""
        }

        // registerForActivityResult 에서 실행될 Callback 함수 정의
        val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            Log.d("startForResult", "startForResult")
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val selectedImageUri = data?.data
                Log.d("selectedImageUri", "$selectedImageUri")
                // 이미지 경로(selectedImageUri)를 사용해서 서버로 전송하는 코드 작성
                val realPath = getRealPathFromUri(selectedImageUri!!)
                uploadImg(realPath)
            } else {
                // 이미지 선택 취소 시 처리할 코드 작성
                Log.d("취소", "취소")
            }
        }

        binding.customerMainProfile.setOnClickListener {
            Log.d("customerMainProfile", "customerMainProfile")
            // 갤러리 열기 액티비티 실행
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            //intent.type = "image/*"
            intent.type = "image/jpeg, image/png"
            val mimeTypes = arrayOf("image/jpeg", "image/png")
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            startForResult.launch(intent)
        }

        binding.parentTv.setOnClickListener {
            if (binding.layoutExpand1.visibility == View.VISIBLE) {
                binding.layoutExpand1.visibility = View.GONE
                binding.parentTv.setImageResource(R.drawable.baseline_expand_more_24)
            } else {
                binding.layoutExpand1.visibility = View.VISIBLE
                binding.parentTv.setImageResource(R.drawable.baseline_expand_less_24)
            }
        }

        // 메뉴 정보를 여기서 넘겨주거나, 메뉴 액티비티에서 정보 찾기.
        // 어떤 메뉴를 클릭했는지의 정보는 여기서 주고, 메뉴 정보를 찾는 것은 메뉴 액티비티에서.

        with(binding) {
            mainImgv1.setOnClickListener { moveMenuActivity(1) }
            mainImgv2.setOnClickListener { moveMenuActivity(2) }
            mainImgv3.setOnClickListener { moveMenuActivity(3) }
            mainImgv4.setOnClickListener { moveMenuActivity(4) }
            mainImgv5.setOnClickListener { moveMenuActivity(5) }
            mainImgv6.setOnClickListener { moveMenuActivity(6) }
            mainImgv7.setOnClickListener { moveMenuActivity(7) }
            mainImgv8.setOnClickListener { moveMenuActivity(8) }
            mainImgv9.setOnClickListener { moveMenuActivity(9) }
            mainImgv10.setOnClickListener { moveMenuActivity(10) }
            mainImgv11.setOnClickListener { moveMenuActivity(11) }
            mainImgv12.setOnClickListener { moveMenuActivity(12) }
            mainImgv13.setOnClickListener { moveMenuActivity(13) }
            mainImgv14.setOnClickListener { moveMenuActivity(14) }
            mainImgv15.setOnClickListener { moveMenuActivity(15) }
        }
        initViews()

    }
    private fun uploadImg(imageUri: String){
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val file = File(imageUri)
                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                Log.d("requestFile", "$requestFile")
                val body: MultipartBody.Part = MultipartBody.Part.createFormData("images", file.name, requestFile)
                Log.d("body", "$body")
                Log.d("customerId", customerId)
                val gson = GsonBuilder()
                    .setLenient()
                    .create()
                val retrofit = Retrofit.Builder()
                    .baseUrl("http://13.124.112.81:8080")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
                val customerMainAPI = retrofit.create(CustomerMainAPI::class.java)
                val response = customerMainAPI.uploadImage(customerId, body)
                Log.d("response", "$response")
                Log.d(TAG, "통신전")
                Log.d(TAG, "서버 응답1 : $response")
                Log.d(TAG, "서버 응답2 : ${response.contentType()}")
                Log.d(TAG, "서버 응답3 : ${response.source()}")
                val contentType = response.contentType()
                if (contentType?.type == "application" && contentType.subtype == "json") {
                    // 서버에서 반환하는 데이터가 JSON인 경우 처리
                    Log.d(TAG, "여기는 json")

                } else {
                    // 서버에서 반환하는 데이터가 text/plain인 경우 처리
                    val data = response.string()
                    Log.d(TAG, "5$data")
                    Log.d(TAG, "여기는 text/plain")
                    if (data.contains("success")) {
                        // 이미지 업로드 성공 처리
                        // imageUrl을 사용하여 해당 이미지를 보여줄 수 있음
                        val imageUrl = data.split(": ")[1]
                        Log.d(TAG, imageUrl)
                        runOnUiThread {
                            setImage(imageUri)
                        }
                    } else {
                        // 이미지 업로드 실패 처리
                        runOnUiThread {
                            Toast.makeText(applicationContext, "jpg파일 혹은 png파일만 등록 가능합니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } catch (e: Exception) {
                Log.d(ContentValues.TAG, "예외")
                Log.d(ContentValues.TAG, "$e")
            }
        }
    }
    private fun setImage(imageUrl: String) {
        if (imageUrl != null){
            Glide.with(this)
                .load(imageUrl)
                .into(binding.customerMainProfile)
        }
    }
    private fun moveMenuActivity(num : Int) {
        val intent = Intent(this, CustomerFoodListActivity::class.java).apply {
            putExtra("foodCategory", num)
            putExtra("customer_id", customerId)
        }
        startActivity(intent)
    }

    private fun initViews() {
        binding.sellerListRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.sellerListRv.adapter = SellerListAdapter(lists)
    }
}