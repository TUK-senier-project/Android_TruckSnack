package com.example.icontest2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.icontest2.databinding.ActivitySellerRegisterBinding
import com.example.icontest2.databinding.ActivitySignUpBinding

class SellerRegisterActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySellerRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySellerRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ToolBar 설정, 제목, 버튼 활성화, 아이콘 클릭 가능 설정
        setSupportActionBar(binding.sellerRegisterToolbar) // 생성시 ()안에 id 변경.
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)



    }
    // 뒤로가기 버튼 클릭 이벤트 처리(사용시 onCreate 밖에 복사)
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