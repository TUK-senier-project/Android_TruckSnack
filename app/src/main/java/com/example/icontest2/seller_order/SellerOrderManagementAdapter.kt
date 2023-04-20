package com.example.icontest2.seller_order

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.icontest2.R
import com.example.icontest2.customer_food_list.CustomerFoodDetailActivity
import com.example.icontest2.customer_food_list.CustomerFoodListDTOItem
import com.example.icontest2.databinding.SellerListBinding
import com.example.icontest2.databinding.SellerOrderWaitingListBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class SellerOrderManagementAdapter(private val lists: List<SellerOrderManagementDTO>) : RecyclerView.Adapter<SellerOrderManagementAdapter.ListViewHolder>(){
    var onItemClickListener: ((SellerOrderManagementDTO) -> Unit)? = null // 클릭 리스너

    // ViewHolder 생성하는 함수, 최소 생성 횟수만큼만 호출됨 (계속 호출 X)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        Log.d(ContentValues.TAG, "onCreateViewHolder: ")
        val binding = SellerOrderWaitingListBinding.inflate(
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
        holder.bind(lists[position], holder)
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(lists[position])
            Log.d("CUSTOMER_FOOD_LIST", "Clicked item: ${lists[position]}")

        }
    }
    override fun getItemCount(): Int = lists.size

    class ListViewHolder(private val binding: SellerOrderWaitingListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(todo: SellerOrderManagementDTO, holder: ListViewHolder) {
            binding.orderWaitingTimeTv.text = holder.itemView.context.getString(R.string.order_waiting_time, todo.isCreated[4].toString(), todo.isCreated[3].toString())
            binding.orderWaitingSeq.text = holder.itemView.context.getString(R.string.order_waiting_seq, todo.seq.toString())
            binding.orderWaitingTotalAmount.text = holder.itemView.context.getString(R.string.order_waiting_total_amount, todo.orderTotalPrice.toString())
            binding.orderWaitingRefuseBtn.setOnClickListener {

            }
            binding.orderWaitingAcceptBtn.setOnClickListener {

            }
            binding.orderWaitingDetail.setOnClickListener {
                val position = adapterPosition
                val orderLists = SellerOrderManagementActivity.getInstance()?.orderLists
                val seq = orderLists?.get(position)?.seq
                val foodLists = SellerOrderManagementActivity.getInstance()?.foodLists
                if (seq != null) {
                    GlobalScope.launch(Dispatchers.IO) {
                        SellerOrderManagementActivity.getInstance()?.getOrderDetailList(seq)
                        val orderDetailLists = SellerOrderManagementActivity.getInstance()?.orderDetailLists!!
                        Log.d("aaaaaaa", "$position")
                        Log.d("aaaaaaa", "$orderLists")
                        Log.d("aaaaaaa", "$seq")
                        Log.d("aaaaaaa", "$foodLists")
                        Log.d("aaaaaaa", "$orderDetailLists")
                    }
                }

            }
        }
    }
}