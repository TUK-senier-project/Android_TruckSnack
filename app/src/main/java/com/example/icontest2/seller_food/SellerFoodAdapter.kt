package com.example.icontest2.seller_food

import android.content.ContentValues
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.util.Base64
import com.example.icontest2.databinding.SellerFoodListBinding

class SellerFoodAdapter(private val lists: List<SellerFoodDTO>) : RecyclerView.Adapter<SellerFoodAdapter.ListViewHolder>(){
    var onItemClickListener: ((SellerFoodDTO) -> Unit)? = null // 클릭 리스너
    //private var foodOrderList: MutableList<FoodOrder> = mutableListOf()

    // ViewHolder 생성하는 함수, 최소 생성 횟수만큼만 호출됨 (계속 호출 X)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        Log.d(ContentValues.TAG, "onCreateViewHolder: ")
        val binding = SellerFoodListBinding.inflate(
            LayoutInflater.from(parent.context), // layoutInflater 를 넘기기위해 함수 사용, ViewGroup 는 View 를 상속하고 View 는 이미 Context 를 가지고 있음
            parent, // 부모(리싸이클러뷰 = 뷰그룹)
            false   // 리싸이클러뷰가 attach 하도록 해야함 (우리가 하면 안됨)
        )
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        Log.d(ContentValues.TAG, "onBindViewHolder: $position")
        holder.bind(lists[position], holder)
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(lists[position])
            Log.d("SELLER_FOOD_LIST", "Clicked item: ${lists[position]}")
        }
    }
    override fun getItemCount(): Int = lists.size

    class ListViewHolder(private val binding: SellerFoodListBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(todo: SellerFoodDTO, holder: ListViewHolder) {
            if(todo.base64Img != null){
                val decodedBytes = Base64.decode(todo.base64Img, Base64.DEFAULT)
                // Bitmap 으로 변환
                val decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                binding.sellrFoodImageView.setImageBitmap(decodedBitmap)
                // 파일 쓰기
            }
            //binding.sellrFoodImageView. = todo.base64Image // 여기도물어보기
            binding.sellerFoodNameText.text = todo.foodName
            binding.sellerFoodPriceText.text = todo.price.toString()
        }
    }

}