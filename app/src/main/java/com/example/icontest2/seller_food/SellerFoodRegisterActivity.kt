package com.example.icontest2.seller_food

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.icontest2.R
import com.example.icontest2.databinding.ActivitySellerFoodRegisterBinding
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

class SellerFoodRegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySellerFoodRegisterBinding
    private var sellerId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySellerFoodRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 데이터 받아오기
        val id = intent.getStringExtra("sellerId")
        if (id != null) {
            sellerId = id
        }
        Log.d(TAG, "===========푸드에서레지스터로받기========")
        Log.d(TAG, "$id")

        //val sellerFoodImageBtn = binding.sellerFoodImageBtn
        val sellerFoodRegisterBtn2 = binding.sellerFoodRegisterBtn2
        val sellerFoodName = binding.sellerFoodName2
        val sellerFoodPrice = binding.sellerFoodPrice2

        // 이미지를 uri로 변경
        fun getRealPathFromUri(uri: Uri): String {
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = contentResolver.query(uri, projection, null, null, null)
            val columnIndex = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor?.moveToFirst()
            val filePath = cursor?.getString(columnIndex!!)
            cursor?.close()
            return filePath ?: ""
        }
        //registerForActivityResult 에서 실행될 Callback 함수 정의, 이미 선택한 uri를 찾고 그 uri를 업로드 이미지에 넣어줌
//        val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            Log.d("startForResult", "startForResult")
//            if (result.resultCode == Activity.RESULT_OK) {
//                val data = result.data
//                val selectedImageUri = data?.data
//                Log.d("selectedImageUri", "$selectedImageUri")
//                // 이미지 경로(selectedImageUri)를 사용해서 서버로 전송하는 코드 작성
//                val realPath = getRealPathFromUri(selectedImageUri!!)
//                uploadImg(sellerId, realPath)
//            } else {
//                // 이미지 선택 취소 시 처리할 코드 작성
//                Log.d("취소", "취소")
//            }
//        }
        var selectedImageUri: Uri? = null // 이미지 선택 결과를 저장할 변수
        sellerFoodRegisterBtn2.setOnClickListener {
            if (selectedImageUri == null) {
                // 이미지가 선택되지 않은 경우 에러 메시지 출력
                Toast.makeText(this, "이미지를 먼저 선택해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                // 이미지가 선택된 경우, 선택한 이미지의 경로를 얻어와서 서버에 전송
                val realPath = getRealPathFromUri(selectedImageUri!!)
                uploadImg(sellerId, realPath)
            }
        }
        val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            Log.d("startForResult", "startForResult")
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                selectedImageUri = data?.data
                Log.d("selectedImageUri", "$selectedImageUri")
            } else {
                // 이미지 선택 취소 시 처리할 코드 작성
                Toast.makeText(this, "이미지 선택이 취소되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        // 나는 이미지뷰를 클릵하면 변경하는걸로, 내 이미지뷰의 아이디값으로 변경
        binding.sellerFoodImageBtn.setOnClickListener {
            Log.d("sellerMain", "sellerMain")
            // 갤러리 열기 액티비티 실행
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
            val mimeTypes = arrayOf("image/jpeg", "image/png")
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            startForResult.launch(intent)
        }
    }//온크리에잇 종료
    // 업로드이미지에 진짜 경로를 넣어주면 서버와 통신하게끔
    private fun uploadImg(sellerId: String, imageUri: String) {
        val foodNameEditText: EditText = findViewById(R.id.seller_food_name2)
        Log.d(TAG, "Food Name: ${foodNameEditText.text}")
        val priceEditText: EditText = findViewById(R.id.seller_food_price2)
        Log.d(TAG, "Price: ${priceEditText.text}")
        val foodName: String = foodNameEditText.text.toString()
        val price: String = priceEditText.text.toString()
        val file = File(imageUri)
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val body: MultipartBody.Part = MultipartBody.Part.createFormData("images", file.name, requestFile)
        //val imageView: ImageView = findViewById(R.id.seller_food_image_view2)
        GlobalScope.launch(Dispatchers.IO) {
            try {
                Log.d(TAG, "foodName: $foodName, price: $price, imageUri: $imageUri, sellerId: $sellerId")
                val gson = GsonBuilder()
                    .setLenient()
                    .create()
                val retrofit = Retrofit.Builder()
                    .baseUrl("http://13.124.112.81:8080")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
                val sellerFoodAPI = retrofit.create(SellerFoodAPI::class.java)
                val foodNameBody = foodName.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                val priceBody = price.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                val response = sellerFoodAPI.sellerFood(sellerId, foodNameBody, priceBody, body)
                Log.d("response", "$response")
                Log.d(TAG, "통신전")
                Log.d(TAG, "서버 응답1 : $response")
                Log.d(TAG, "서버 응답2 : ${response.contentType()}")
                Log.d(TAG, "서버 응답3 : ${response.source()}") // 여기까지만 가도 서버엔 전달 완료
                val contentType = response.contentType()
                if (contentType?.type == "application" && contentType.subtype == "json") {
                    // 서버에서 반환하는 데이터가 JSON인 경우 처리
                    Log.d(TAG, "여기는 json")
                } else {
                    // 서버에서 반환하는 데이터가 text/plain인 경우 처리
                    val data = response.string() // 데이터가 리스폰스된 데이터값들을 의미
                    Log.d(TAG, "5$data")
                    Log.d(TAG, "여기는 text/plain")
                    if (data.contains("success")) {
                        // 이미지 업로드 성공 처리
                        // imageUrl을 사용하여 해당 이미지를 보여줄 수 있음
                        //val imageUrl = data.split(": ")[1]
                        /*withContext(Dispatchers.Main) {
                            Toast.makeText(this@SellerFoodRegisterActivity, "정보 업로드 성공!", Toast.LENGTH_SHORT).show()
                            imageView.setImageURI(Uri.parse(imageUri))
                        }*/Log.d(TAG, "업로드 실패") // 임시방편임
                    } else {
                        Log.d(TAG, "업로드 성공") // 임시방편임
                        // 이미지 업로드 실패 처리
                        Toast.makeText(this@SellerFoodRegisterActivity, "정보 업로드 성공!", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Log.d(ContentValues.TAG, "예외")
                Log.d(ContentValues.TAG, "$e")
            }
        }
    }
}