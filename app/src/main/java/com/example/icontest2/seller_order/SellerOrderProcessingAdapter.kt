package com.example.icontest2.seller_order

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.icontest2.R
import com.example.icontest2.customer_food_list.CustomerFoodDetailActivity
import com.example.icontest2.customer_food_list.CustomerFoodListDTOItem
import com.example.icontest2.databinding.SellerListBinding
import com.example.icontest2.databinding.SellerOrderProcessingListBinding
import com.example.icontest2.databinding.SellerOrderWaitingListBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class SellerOrderProcessingAdapter(private val lists: List<SellerOrderManagementDTO>) : RecyclerView.Adapter<SellerOrderProcessingAdapter.ListViewHolder>(){
    var onItemClickListener: ((SellerOrderManagementDTO) -> Unit)? = null // 클릭 리스너

    // ViewHolder 생성하는 함수, 최소 생성 횟수만큼만 호출됨 (계속 호출 X)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        Log.d(ContentValues.TAG, "onCreateViewHolder: ")
        val binding = SellerOrderProcessingListBinding.inflate(
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

    class ListViewHolder(private val binding: SellerOrderProcessingListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(todo: SellerOrderManagementDTO, holder: ListViewHolder) {
            val position = adapterPosition
            val orderLists = SellerOrderManagementActivity.getInstance()?.orderLists
            val seq = orderLists?.get(position)?.seq

            binding.orderProcessingTimeTv .text = holder.itemView.context.getString(R.string.order_waiting_time, String.format("%02d", todo.isCreated[3]), String.format("%02d", todo.isCreated[4]))
            binding.orderProcessingSeq .text = holder.itemView.context.getString(R.string.order_waiting_seq, todo.seq.toString())
            binding.orderProcessingTotalAmount.text = holder.itemView.context.getString(R.string.order_waiting_total_amount, todo.orderTotalPrice.toString())
            binding.orderProcessingCompleteBtn.setOnClickListener {
                if (seq != null){
                    GlobalScope.launch(Dispatchers.IO) {
                        SellerOrderManagementActivity.getInstance()?.getOrderComplete(seq)
                    }
                }
            }
            binding.orderProcessingDetail.setOnClickListener {
                if (seq != null) {
                    GlobalScope.launch(Dispatchers.IO) {
                        SellerOrderManagementActivity.getInstance()?.getOrderDetailList(seq)
                        val foodLists = SellerOrderManagementActivity.getInstance()?.foodLists
                        val orderDetailLists = SellerOrderManagementActivity.getInstance()?.orderDetailLists!!
                        Log.d("aaaaaaa", "$position")
                        Log.d("aaaaaaa", "$orderLists")
                        Log.d("aaaaaaa", "$seq")
                        Log.d("aaaaaaa", "$foodLists")
                        Log.d("aaaaaaa", "$orderDetailLists")
                        val foodTextList = orderDetailLists.mapNotNull { detail ->
                            foodLists?.find { it.foodSeq == detail.foodSeq }?.let { food ->
                                FoodText(
                                    foodSeq = detail.foodSeq,
                                    quantity = detail.quantity,
                                    totalPrice = detail.totalPrice,
                                    foodName = food.foodName
                                )
                            }
                        }
                        Log.d("aaaaaaa", "orderDetailListWithFoodName - $foodTextList")
                        val sb = StringBuilder() // 문자열을 합치기 위한 StringBuilder

                        for ((index, foodText) in foodTextList.withIndex()) { // 리스트의 각 항목에 대해 반복
                            sb.append("${foodText.foodName} ${foodText.quantity}개\n${foodText.totalPrice}원") // 문자열 합치기
                            if (index < foodTextList.size - 1) { // 마지막 항목이 아니라면
                                sb.append("\n") // 개행 추가
                            }
                        }

                        val resultText = sb.toString() // 최종 문자열
                        Log.d("aaaaaaa", "resultText - $resultText")

                        withContext(Dispatchers.Main) {
                            // UI 업데이트
                            if (binding.layoutExpand1.visibility == View.VISIBLE) {
                                binding.layoutExpand1.visibility = View.GONE
                            } else {
                                binding.layoutExpand1.visibility = View.VISIBLE
                                binding.orderProcessingTotalQuantity.text = holder.itemView.context.getString(R.string.order_waiting_total_quantity, foodTextList.sumOf { it.quantity }.toString())
                                binding.orderProcessingFoodList.text = resultText
                            }
                        }
                    }
                }
            }
        }
    }
}