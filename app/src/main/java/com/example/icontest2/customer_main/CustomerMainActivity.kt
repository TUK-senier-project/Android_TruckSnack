package com.example.icontest2.customer_main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.icontest2.*
import com.example.icontest2.databinding.ActivityCustomerMainBinding

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
        /*
        binding.mainHomeBtn.setOnClickListener {
            val intent = Intent(this, CustomerMainActivity::class.java)
            startActivity(intent)
        }
        binding.mainSearchBtn.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
        binding.mainMapBtn.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
        }
        binding.mainMyPageBtn.setOnClickListener {
            val intent = Intent(this, MyPageActivity::class.java)
            startActivity(intent)
        }

        binding.mainLocationBtn.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
        }
        */

        val customerId = intent.getStringExtra("customerId")

        binding.customerMainIdTv.text = customerId

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
            mainImgv1.setOnClickListener { moveMenuActivity() }
            mainImgv2.setOnClickListener { moveMenuActivity() }
            mainImgv3.setOnClickListener { moveMenuActivity() }
            mainImgv4.setOnClickListener { moveMenuActivity() }
            mainImgv5.setOnClickListener { moveMenuActivity() }
            mainImgv6.setOnClickListener { moveMenuActivity() }
            mainImgv7.setOnClickListener { moveMenuActivity() }
            mainImgv8.setOnClickListener { moveMenuActivity() }
            mainImgv9.setOnClickListener { moveMenuActivity() }
            mainImgv10.setOnClickListener { moveMenuActivity() }
            mainImgv11.setOnClickListener { moveMenuActivity() }
            mainImgv12.setOnClickListener { moveMenuActivity() }
            mainImgv13.setOnClickListener { moveMenuActivity() }
            mainImgv14.setOnClickListener { moveMenuActivity() }
            mainImgv15.setOnClickListener { moveMenuActivity() }

        }
        initViews()
    }
    private fun moveMenuActivity() {
        val intent = Intent(this, MenuListsActivity::class.java)
        startActivity(intent)
    }

    private fun initViews() {
        binding.todoList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.todoList.adapter = SellerListAdapter(lists)
    }

    fun move(){
        val intent = Intent(this, MapActivity::class.java)
        startActivity(intent)
    }

    // 마이 페이지 버튼 클릭 시, 해당 UI 다시 작업 시작!
}