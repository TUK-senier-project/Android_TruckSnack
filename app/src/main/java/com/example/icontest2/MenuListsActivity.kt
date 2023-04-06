package com.example.icontest2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.icontest2.customer_main.SellerListDTO
import com.example.icontest2.customer_main.SellerListAdapter
import com.example.icontest2.databinding.ActivityMenuListsBinding

class MenuListsActivity : AppCompatActivity() {
    /*
    private lateinit var binding : ActivityMenuListsBinding

    private val lists = listOf(
        SellerListDTO("푸드트럭 메뉴1", false),
        SellerListDTO("푸드트럭 메뉴2", false),
        SellerListDTO("푸드트럭 메뉴3", false),
        SellerListDTO("푸드트럭 메뉴4", false),
        SellerListDTO("푸드트럭 메뉴5", false),
        SellerListDTO("푸드트럭 메뉴6", false),
        SellerListDTO("푸드트럭 메뉴7", false),
    )

    */
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        /*binding = ActivityMenuListsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        */
    }
    /*
    private fun initViews() {
        binding.todoList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.todoList.adapter = SellerListAdapter(lists)
    }

    fun showDesiredList(view: View){
        val recyclerView = findViewById<RecyclerView>(R.id.todo_list)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = SellerListAdapter(lists)
    }
    */
}