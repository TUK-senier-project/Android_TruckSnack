package com.example.icontest2.customer_food_list

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.icontest2.databinding.SellerListBinding
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream


class CustomerFoodListAdapter(private val lists: List<CustomerFoodListDTOItem>, val customerId: String) : RecyclerView.Adapter<CustomerFoodListAdapter.ListViewHolder>(){
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
        holder.bind(lists[position], holder)
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(lists[position])
            Log.d("CUSTOMER_FOOD_LIST", "Clicked item: ${lists[position]}")
            Log.d("FoodListAdapter", "==========$customerId==========")
            val intent = Intent(holder.itemView.context, CustomerFoodDetailActivity::class.java). apply {
                putExtra("selected_item", lists[position])
                putExtra("customer_id", customerId)
            }
            holder.itemView.context.startActivity(intent)
        }
    }
    override fun getItemCount(): Int = lists.size

    class ListViewHolder(private val binding: SellerListBinding) : RecyclerView.ViewHolder(binding.root) {
        // private val mainActivity = CustomerMainActivity.getInstance()

        fun bind(todo: CustomerFoodListDTOItem, holder: ListViewHolder) {
            // 내부 저장소 경로 가져오기
            val directory = holder.itemView.context.filesDir
            // 파일 경로 생성
            val filePath = File(directory, todo.businessName)
            if(todo.base64Img != null){
                /*Glide.with(holder.itemView.context)
                    .load(todo.base64Img)
                    .into(binding.sellerListImg)*/
                val decodedBytes = Base64.decode(todo.base64Img, Base64.DEFAULT)
                // Bitmap 으로 변환
                val decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                binding.sellerListImg.setImageBitmap(decodedBitmap)
                // 파일 쓰기
                FileOutputStream(filePath).use { fileOutputStream ->
                    decodedBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
                }
            }
            if(todo.sellerImgS3Url != null){
                // 파일 읽기
                val inputStream = FileInputStream(filePath)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                binding.sellerListImg.setImageBitmap(bitmap)
            }
            /*if(todo.sellerImgS3Url != null){
                val decodedBytes = Base64.decode(todo.sellerImgS3Url, Base64.DEFAULT)
                // Bitmap 으로 변환
                val decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                binding.sellerListImg.setImageBitmap(decodedBitmap)
            }*/
            binding.customerMainBusinessNameTv.text = todo.businessName
            binding.customerMainGradeTv.text = todo.grade.toString()
            binding.customerMainDeadlineTv.text = todo.deadline.toString()
        }
    }
}