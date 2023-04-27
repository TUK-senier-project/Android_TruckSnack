package com.example.icontest2.navigation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.icontest2.databinding.ActivityNavigationBinding

// 처음에 가게의 주소를 받으면, 그거를 바로 네비게이션에 넣어 놓고, 버튼에 네비게이션 시작이란걸 알려주고, 버튼을 누르면 바로 네비게이션이 시작하게끔 만들기.
class NavigationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNavigationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.navigationView.api.routeReplayEnabled(true)
    }
}
