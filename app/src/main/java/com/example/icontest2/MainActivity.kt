package com.example.icontest2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.icontest2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    init {
        instance = this
    }
    companion object{
        private var instance: MainActivity? = null
        fun getInstance(): MainActivity?{
            return instance
        }
    }

    private lateinit var binding : ActivityMainBinding

    private val lists = listOf(
        ListDTO("푸드트럭1", false),
        ListDTO("푸드트럭2", false),
        ListDTO("푸드트럭3", false),
        ListDTO("푸드트럭4", false),
        ListDTO("푸드트럭5", false),
        ListDTO("푸드트럭6", false),
        ListDTO("푸드트럭7", false),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mainHomeBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
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

        // val listBtn = findViewById<Button>(R.id.list_btn)


    }
    private fun moveMenuActivity() {
        val intent = Intent(this, MenuListsActivity::class.java)
        startActivity(intent)
    }

    private fun initViews() {
        binding.todoList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.todoList.adapter = ListAdapter(lists)
    }

    fun move(){
        val intent = Intent(this, MapActivity::class.java)
        startActivity(intent)
    }

    // 마이 페이지 버튼 클릭 시, 해당 UI 다시 작업 시작!
}