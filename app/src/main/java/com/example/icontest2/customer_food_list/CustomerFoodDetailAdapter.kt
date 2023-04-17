package com.example.icontest2.customer_food_list

import android.app.Dialog
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.icontest2.R
import com.example.icontest2.databinding.CustomerFoodDetailBinding
import com.example.icontest2.databinding.SellerListBinding
import com.example.icontest2.order.OrderRegisterActivity
import java.io.File
import java.io.FileOutputStream

class CustomerFoodDetailAdapter(private val lists: List<CustomerFoodDetailDTO>) : RecyclerView.Adapter<CustomerFoodDetailAdapter.ListViewHolder>(){
    var onItemClickListener: ((CustomerFoodDetailDTO) -> Unit)? = null // 클릭 리스너

    // ViewHolder 생성하는 함수, 최소 생성 횟수만큼만 호출됨 (계속 호출 X)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        Log.d(ContentValues.TAG, "onCreateViewHolder: ")
        val binding = CustomerFoodDetailBinding.inflate(
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
            val mDialogView =
                LayoutInflater.from(holder.itemView.context).inflate(R.layout.selected_food_dialog, null)
            val mBuilder = AlertDialog.Builder(holder.itemView.context)
                .setView(mDialogView)
                //.setTitle("")

            val cancel = mDialogView.findViewById<TextView>(R.id.selected_food_cancel)
            val confirm = mDialogView.findViewById<TextView>(R.id.selected_food_confirm)
            cancel.setOnClickListener {
                val dialog = mBuilder.create()
                dialog.dismiss()
                Toast.makeText(holder.itemView.context, "gd", Toast.LENGTH_SHORT).show()
            }
            confirm.setOnClickListener {
                val intent = Intent(holder.itemView.context, OrderRegisterActivity::class.java). apply {
                    putExtra("foodName", lists[position].foodName)
                    putExtra("foodSeq", lists[position].foodSeq)
                    putExtra("price", lists[position].price)
                }
                holder.itemView.context.startActivity(intent)
            }
            mBuilder.show()

            // holder.itemView.context.startActivity(intent)
        }
    }
    override fun getItemCount(): Int = lists.size

    class ListViewHolder(private val binding: CustomerFoodDetailBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(todo: CustomerFoodDetailDTO, holder: ListViewHolder) {
            // 내부 저장소 경로 가져오기
            val directory = holder.itemView.context.filesDir
            // 파일 경로 생성
            val filePath = File(directory, todo.foodName)
            if(todo.base64Img != null){
                val decodedBytes = Base64.decode(todo.base64Img, Base64.DEFAULT)
                // Bitmap 으로 변환
                val decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                binding.foodDetailImg.setImageBitmap(decodedBitmap)
                // 파일 쓰기
                FileOutputStream(filePath).use { fileOutputStream ->
                    decodedBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
                }
            }
            binding.detailFoodNameTv.text = todo.foodName
            binding.detailPriceTv.text = todo.price.toString()
        }
    }

}