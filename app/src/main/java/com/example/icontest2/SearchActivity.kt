package com.example.icontest2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.icontest2.databinding.ActivitySearchBinding
import java.lang.reflect.Member

class SearchActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val list = ArrayList<SearchListDTO>()
        list.add(SearchListDTO("검색어1"))
        list.add(SearchListDTO("검색어2"))
        list.add(SearchListDTO("검색어3"))
        list.add(SearchListDTO("검색어4"))
        list.add(SearchListDTO("검색어5"))

        val adapter = SearchListAdapter(list)
        val layoutManager = LinearLayoutManager(this)
        binding.searchList.layoutManager = layoutManager
        binding.searchList.adapter = adapter
    }

}