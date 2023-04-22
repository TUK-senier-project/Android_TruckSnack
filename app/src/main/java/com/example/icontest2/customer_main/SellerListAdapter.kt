package com.example.icontest2.customer_main

import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.icontest2.databinding.SellerListBinding

class SellerListAdapter(private val lists: List<SellerListDTO>) : RecyclerView.Adapter<SellerListAdapter.ListViewHolder>() {

    // ViewHolder 생성하는 함수, 최소 생성 횟수만큼만 호출됨 (계속 호출 X)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        Log.d(TAG, "onCreateViewHolder: ")
        val binding = SellerListBinding.inflate(
            LayoutInflater.from(parent.context), // layoutInflater 를 넘기기위해 함수 사용, ViewGroup 는 View 를 상속하고 View 는 이미 Context 를 가지고 있음
            parent, // 부모(리싸이클러뷰 = 뷰그룹)
            false   // 리싸이클러뷰가 attach 하도록 해야함 (우리가 하면 안됨)
        )
        return ListViewHolder(binding)
    }

    // 만들어진 ViewHolder에 데이터를 바인딩하는 함수
    // position = 리스트 상에서 몇번째인지 의미
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder: $position")
        holder.bind(lists[position])
    }

    override fun getItemCount(): Int = lists.size

    class ListViewHolder(private val binding: SellerListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(todo: SellerListDTO) {
            binding.customerMainBusinessNameTv.text = todo.businessName
            binding.customerMainContentTv.text = todo.content
            binding.customerMainGradeTv.text = todo.grade.toString()
            binding.customerMainDeadlineTv.text = todo.deadline.toString()
        }
    }
}