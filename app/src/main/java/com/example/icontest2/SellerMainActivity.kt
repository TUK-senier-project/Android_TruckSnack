package com.example.icontest2

import android.app.Activity
import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.icontest2.databinding.ActivitySellerMainBinding
import android.content.Intent
import android.provider.MediaStore
import android.util.Log
import com.example.icontest2.seller_food.SellerFoodActivity
import android.app.AlertDialog
import android.content.ContentValues
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.icontest2.seller_Main.*
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import com.example.icontest2.*
import kotlinx.coroutines.withContext


class SellerMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySellerMainBinding
    private var imageUri: Uri? = null
    private var id: String = ""
    private var customerName: String = ""
    private var sellerId: String = "" // 셀러로변경

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySellerMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        /*
        val imageView1 = binding.imageView1
        val bitmap1 = BitmapFactory.decodeResource(resources, R.drawable.image1) // 이미지 데이터를 받아왔을 때. 여기에 넣어주면됨.(sellerImgS3Url)
        imageView1.setImageBitmap(bitmap1)
        */
        // 데이터 받아오기
        val id = intent.getStringExtra("sellerId")
        if (id != null) {
            sellerId = id
        }
        //id = intent.getStringExtra("id")
        Log.d(TAG, "=========로그인에서부터받기============")
        Log.d(TAG, "$id")
        // 메뉴관리 버튼 누르면 sellerFood로 화면전환
        val sellerFoodBtn = binding.sellerFoodBtn
        sellerFoodBtn.setOnClickListener {
            val intent = Intent(this@SellerMainActivity, SellerFoodActivity::class.java)
            intent.putExtra("sellerId", id)
            startActivity(intent)
            Log.d(TAG, "=========메인에서푸드로전송============")
            Log.d(TAG, "$id")
        }
        val base64String = intent.getStringExtra("base64String").toString()
        Log.d(TAG, "=========로그인에서스트링값부터받기============")
        // Base64 디코딩
        val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
        // Bitmap 으로 변환
        val decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        binding.sellerImageView.setImageBitmap(decodedBitmap)

//        // 메뉴관리 버튼 누르면 sellerFood로 화면전환
//        val sellerFoodBtn = binding.sellerFoodBtn
//        sellerFoodBtn.setOnClickListener {
//            val intent = Intent(this@SellerMainActivity, SellerFoodActivity::class.java)
//            intent.putExtra("sellerId", id)
//            startActivity(intent)
//            Log.d(TAG, "=========메인에서푸드로전송============")
//            Log.d(TAG, "$id")
//        }
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
        // registerForActivityResult 에서 실행될 Callback 함수 정의, 이미 선택한 uri를 찾고 그 uri를 업로드 이미지에 넣어줌
        val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            Log.d("startForResult", "startForResult")
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val selectedImageUri = data?.data
                Log.d("selectedImageUri", "$selectedImageUri")
                // 이미지 경로(selectedImageUri)를 사용해서 서버로 전송하는 코드 작성
                val realPath = getRealPathFromUri(selectedImageUri!!)
                uploadImg(sellerId, realPath)
            } else {
                // 이미지 선택 취소 시 처리할 코드 작성
                Log.d("취소", "취소")
            }
        }
        // 나는 이미지뷰를 클륵하면 변경하는걸로, 내 이미지뷰의 아이디값으로 변경
        binding.sellerImageView.setOnClickListener {
            Log.d("sellerMain", "sellerMain")
            // 갤러리 열기 액티비티 실행
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
            val mimeTypes = arrayOf("image/jpeg", "image/png")
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            startForResult.launch(intent)
        }

        /*
        //val imageRegisterButton = mDialogView.findViewById<Button>(R.id.seller_image_Upload_btn)
        // 이미지뷰 다이얼로그 클릭이벤트
        val imageChange = binding.sellerImageView
        imageChange.setOnClickListener {
            val mDialogView =
                LayoutInflater.from(this).inflate(R.layout.seller_image_upload_dialog, null)
            val mbuilder = AlertDialog.Builder(this).setView(mDialogView)
            val dialog = mbuilder.show()
            val sellerId = mDialogView.findViewById<EditText>(R.id.seller_id_image).text.toString()
            // 다이얼로그에서 ImageView와 Button을 가져옴
            val imagePreview = mDialogView.findViewById<ImageView>(R.id.image_view)
            val imageUploadButton = mDialogView.findViewById<Button>(R.id.seller_image_register_btn)
        }*/
    }//온크리에잇 종료
    // 업로드이미지에 진짜 경로를 넣어주면 서버와 통신하게끔
    private fun uploadImg(sellerId: String, imageUri: String){
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val imageView: ImageView = findViewById(R.id.seller_image_view)
                val file = File(imageUri)
                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                Log.d("requestFile", "$requestFile")
                val body: MultipartBody.Part = MultipartBody.Part.createFormData("images", file.name, requestFile)
                Log.d("body", "$body")
                Log.d("sellerId", sellerId)
                val gson = GsonBuilder()
                    .setLenient()
                    .create()
                val retrofit = Retrofit.Builder()
                    .baseUrl("http://13.124.112.81:8080")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
                val sellerMainAPI = retrofit.create(ImageUploadAPI::class.java)
                val response = sellerMainAPI.uploadImage(sellerId, body)
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
                        //val imageUrl = data.split(": ")[1]
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@SellerMainActivity, "이미지 업로드 성공!", Toast.LENGTH_SHORT).show()
                            imageView.setImageURI(Uri.parse(imageUri))
                        }
                    } else {
                        // 이미지 업로드 실패 처리
                        Toast.makeText(this@SellerMainActivity, "이미지 업로드 실패!", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Log.d(ContentValues.TAG, "예외")
                Log.d(ContentValues.TAG, "$e")
            }
        }
    }

}

