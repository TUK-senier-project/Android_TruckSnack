package com.example.icontest2.customer_food_list

import android.content.ContentValues
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.icontest2.databinding.SellerListBinding


class CustomerFoodListAdapter(private val lists: List<CustomerFoodListDTOItem>) : RecyclerView.Adapter<CustomerFoodListAdapter.ListViewHolder>(){
    var onItemClickListener: ((CustomerFoodListDTOItem) -> Unit)? = null // 클릭 리스너

    // ViewHolder 생성하는 함수, 최소 생성 횟수만큼만 호출됨 (계속 호출 X)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        Log.d(ContentValues.TAG, "onCreateViewHolder: ")
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
        Log.d(ContentValues.TAG, "onBindViewHolder: $position")
        holder.bind(lists[position])
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(lists[position])
            Log.d("CUSTOMER_FOOD_LIST", "Clicked item: ${lists[position]}")
            /*val intent = Intent(holder.itemView.context, YourNextActivity::class.java)
            intent.putExtra("selected_item", lists[position])
            holder.itemView.context.startActivity(intent)*/
        }
    }
    override fun getItemCount(): Int = lists.size

    class ListViewHolder(private val binding: SellerListBinding) : RecyclerView.ViewHolder(binding.root) {
        // private val mainActivity = CustomerMainActivity.getInstance()

        /*
        init {
            binding.customerMainLikeBtn.setOnClickListener {
                // 찜기능
            }
        }
        */

        fun bind(todo: CustomerFoodListDTOItem) {
            binding.customerMainBusinessNameTv.text = todo.businessName
            binding.customerMainGradeTv.text = todo.grade.toString()
            binding.customerMainDeadlineTv.text = todo.deadline.toString()
        }
    }
}