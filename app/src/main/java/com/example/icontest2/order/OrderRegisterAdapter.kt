package com.example.icontest2.order

import android.graphics.BitmapFactory
import android.provider.Settings.System.getString
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.icontest2.R
import com.example.icontest2.customer_food_list.CustomerFoodDetailActivity
import com.example.icontest2.databinding.OrderListBinding
import java.io.File
import java.io.FileInputStream

class OrderRegisterAdapter(private val lists: List<OrderListDTO>) : RecyclerView.Adapter<OrderRegisterAdapter.ListViewHolder>(){
    var onItemClickListener: ((OrderListDTO) -> Unit)? = null // 클릭 리스너

    // ViewHolder 생성하는 함수, 최소 생성 횟수만큼만 호출됨 (계속 호출 X)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        Log.d("OrderRegisterAdapter", "onCreateViewHolder: ")
        val binding = OrderListBinding.inflate(
            LayoutInflater.from(parent.context), // layoutInflater 를 넘기기위해 함수 사용, ViewGroup 는 View 를 상속하고 View 는 이미 Context 를 가지고 있음
            parent, // 부모(리싸이클러뷰 = 뷰그룹)
            false   // 리싸이클러뷰가 attach 하도록 해야함 (우리가 하면 안됨)
        )
        return ListViewHolder(binding)
    }
    // 만들어진 ViewHolder에 데이터를 바인딩하는 함수
    // position = 리스트 상에서 몇번째인지 의미
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        Log.d("OrderRegisterAdapter", "onBindViewHolder: $position")
        holder.bind(lists[position], holder)
        holder.itemView.tag = position
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(lists[position])
            Log.d("CUSTOMER_FOOD_LIST", "Clicked item: ${lists[position]}")

        }
    }

    override fun getItemCount(): Int = lists.size

    class ListViewHolder(private val binding: OrderListBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(todo: OrderListDTO, holder: ListViewHolder) {
            // 내부 저장소 경로 가져오기
            val directory = holder.itemView.context.filesDir
            // 파일 경로 생성
            val filePath = File(directory, todo.foodName)
            val inputStream = FileInputStream(filePath)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            if (bitmap != null){
                binding.orderListImg.setImageBitmap(bitmap)
            }
            binding.orderListCount.text = todo.quantity.toString()
            binding.orderListName.text = todo.foodName
            binding.orderListPrice.text = holder.itemView.context.getString(R.string.food_order_amount, todo.foodTotalPrice.toString())

            binding.orderListAdd.setOnClickListener {
                val position = holder.adapterPosition
                Log.d("Adapter", "======$position======")
                position.let { OrderRegisterActivity.getInstance()?.addBtn(it) }
                binding.orderListCount.text = todo.quantity.toString()
                binding.orderListPrice.text = holder.itemView.context.getString(R.string.food_order_amount, todo.foodTotalPrice.toString())
                (holder.itemView.context as OrderRegisterActivity).changeOrderList()
            }
            binding.orderListSubtract.setOnClickListener {
                val position = holder.adapterPosition
                Log.d("Adapter", "======$position======")
                position.let { OrderRegisterActivity.getInstance()?.subtractBtn(it) }
                binding.orderListCount.text = todo.quantity.toString()
                binding.orderListPrice.text = holder.itemView.context.getString(R.string.food_order_amount, todo.foodTotalPrice.toString())
                (holder.itemView.context as OrderRegisterActivity).changeOrderList()
            }
        }
    }
}