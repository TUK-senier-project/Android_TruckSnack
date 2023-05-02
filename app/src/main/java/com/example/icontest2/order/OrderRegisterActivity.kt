package com.example.icontest2.order

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.icontest2.R
import com.example.icontest2.customer_food_list.*
import com.example.icontest2.customer_main.CustomerMainActivity
import com.example.icontest2.customer_mypage.order_history.CustomerOrderHistoryDatabase
import com.example.icontest2.customer_mypage.order_history.Order
import com.example.icontest2.databinding.ActivityOrderRegisterBinding
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class OrderRegisterActivity : AppCompatActivity(){
    private lateinit var binding: ActivityOrderRegisterBinding
    private lateinit var foodOrders: MutableList<FoodOrder>
    private lateinit var adapterFoodOrders: MutableList<FoodOrder>
    private lateinit var orderListDTO: List<OrderListDTO>
    private var db: CustomerOrderHistoryDatabase? = null
    init {
        instance = this
    }
    companion object{
        private var instance: OrderRegisterActivity? = null
        fun getInstance(): OrderRegisterActivity?{
            return instance
        }
    }
    val gson = GsonBuilder()
        .setLenient()
        .create()

    val retrofit = Retrofit.Builder()
        .baseUrl("http://13.124.112.81:8080")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // ToolBar 설정, 제목, 버튼 활성화, 아이콘 클릭 가능 설정
        setSupportActionBar(binding.sellerRegisterToolbar) // 생성시 ()안에 id 변경.
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        db = CustomerOrderHistoryDatabase.getInstance(this)
        foodOrders = (intent.getSerializableExtra("foodOrderList") as FoodOrderList).foodOrderList
        val sellerId = intent.getStringExtra("selected_id")
        val customerId = intent.getStringExtra("customer_id")
        Log.d("OrderRegisterAct", "onCreate - customerId - $customerId")
        //Log.d("OrderRegisterActivity", "onCreate - $foodOrderList")
        //
        orderListDTO = foodOrders.groupBy { it.foodName }.map { (foodName, group) ->
            val count = group.size
            val totalPrice = group.sumOf { it.foodPrice }
            OrderListDTO(
                foodName = foodName,
                foodSeq = group.first().foodSeq,
                foodPrice = group.first().foodPrice,
                quantity = count,
                foodTotalPrice =  if (count > 1) totalPrice else group.first().foodPrice
            )
        }
        Log.d("OrderRegisterAct", "onCreate - orderListDTO - $orderListDTO")

        //
        var total = 0
        for (data in foodOrders) {
            total += data.foodPrice
        }
        binding.totalOrderQuantity.text = getString(R.string.food_order_quantity, foodOrders.size.toString())
        binding.totalOrderAmount.text = getString(R.string.food_order_amount, total.toString())

        initViews()
        binding.orderBtn.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {

                order(customerId!!)

            }
        }
    }
    fun addBtn(position: Int) {
        orderListDTO[position].quantity = orderListDTO[position].quantity + 1
        orderListDTO[position].foodTotalPrice = orderListDTO[position].foodTotalPrice + orderListDTO[position].foodPrice
    }
    fun subtractBtn(position: Int) {
        if (orderListDTO[position].quantity >= 1){
            orderListDTO[position].quantity = orderListDTO[position].quantity - 1
            orderListDTO[position].foodTotalPrice = orderListDTO[position].foodTotalPrice - orderListDTO[position].foodPrice
        }
    }
    fun changeOrderList(){
        Log.d("FoodDetailAct", "changeOrderList - $orderListDTO")
        if (orderListDTO.isEmpty()){
            binding.orderCheckLayout.visibility = View.GONE
        } else {
            var totalQuantity = 0
            var totalAmount = 0
            for (data in orderListDTO) {
                totalQuantity += data.quantity
                totalAmount += data.foodTotalPrice
            }
            binding.orderCheckLayout.visibility = View.VISIBLE
            binding.totalOrderQuantity.text = getString(R.string.food_order_quantity, totalQuantity.toString())
            binding.totalOrderAmount.text = getString(R.string.food_order_amount, totalAmount.toString())
        }
    }
    suspend fun order(customerId: String) {
        val orderAPI = retrofit.create(OrderAPI::class.java)
        val orderPaymentList = mutableListOf<OrderDTO>()
        for (order in orderListDTO) {
            val orderPayment = OrderDTO(order.foodSeq, order.quantity)
            orderPaymentList.add(orderPayment)
        }
        Log.d("OrderRegisterAct", "order - orderPaymentList - $orderPaymentList")
        val response = orderAPI.orderRegister(customerId, orderPaymentList)
        val contentType = response.contentType()
        if (contentType?.type == "application" && contentType.subtype == "json") {
            // 서버에서 반환하는 데이터가 JSON인 경우 처리
            //val foodList = gson.fromJson(response.string(), CustomerFoodListDTO::class.java)
            Log.d(ContentValues.TAG, "json형식으로 들어왔습니다.")
        } else {
            // 서버에서 반환하는 데이터가 text/plain인 경우 처리
            val orderList = response.string()
            Log.d(ContentValues.TAG, "onCreate - order - $orderList")
            if (orderList == "[]"){ // 실패
                Log.d(ContentValues.TAG, "onCreate - order - $orderList")
            } else { // 성공
                Log.d(ContentValues.TAG, "onCreate - order - ${orderList}")
                val orderResponse: OrderResponse = gson.fromJson(orderList, OrderResponse::class.java)
                val order = Order(
                    seq = orderResponse.seq,
                    customerId = orderResponse.customerId,
                    sellerId = orderResponse.sellerId,
                    orderTotalPrice = orderResponse.orderTotalPrice,
                    orderState = orderResponse.orderState,
                    isCreated = orderResponse.isCreated,
                    isUpdated = orderResponse.isUpdated,
                    deleted = orderResponse.deleted
                )
                val orderData = db!!.customerOrderHistoryDAO().insertOrder(order)
                Log.d(ContentValues.TAG, "onCreate - order - $orderResponse")
                Log.d(ContentValues.TAG, "onCreate - order - $orderData")
                val orderData2 = db!!.customerOrderHistoryDAO().getAll()
                Log.d(ContentValues.TAG, "onCreate - order - $orderData2")
                runOnUiThread {
                    Toast.makeText(applicationContext, "주문이 접수되었습니다.", Toast.LENGTH_SHORT).show()
                }
                val intent = Intent(this, CustomerMainActivity::class.java).apply {
                    putExtra("customerId", orderResponse.customerId)
                }
                startActivity(intent)
            }
        }
    }
    private fun initViews() {
        binding.foodOrderRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.foodOrderRv.adapter = OrderRegisterAdapter(orderListDTO)
    }
    // 뒤로가기 버튼 클릭 이벤트 처리(사용시 onCreate 밖에 복사)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}