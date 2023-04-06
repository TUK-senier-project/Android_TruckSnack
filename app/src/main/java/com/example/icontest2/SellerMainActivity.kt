package com.example.icontest2

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import com.example.icontest2.databinding.ActivitySellerMainBinding
import java.io.File
import java.io.FileOutputStream
import android.content.Intent
import android.graphics.Matrix
import android.graphics.PointF
import android.graphics.drawable.BitmapDrawable
import android.provider.MediaStore
import android.view.MotionEvent
import java.lang.Float.min
import java.lang.Math.ceil
import java.util.*
import kotlin.math.sqrt

class SellerMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySellerMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySellerMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        /*
        val imageView1 = binding.imageView1
        val bitmap1 = BitmapFactory.decodeResource(resources, R.drawable.image1) // 이미지 데이터를 받아왔을 때. 여기에 넣어주면됨.(sellerImgS3Url)
        imageView1.setImageBitmap(bitmap1)
        */


    }
}