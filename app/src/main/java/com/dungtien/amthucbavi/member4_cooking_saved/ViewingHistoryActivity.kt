package com.dungtien.amthucbavi.member4_cooking_saved

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dungtien.amthucbavi.adapters.DishAdapter
import com.dungtien.amthucbavi.database.DatabaseHelper
import com.dungtien.amthucbavi.databinding.ActivityViewingHistoryBinding
import com.dungtien.amthucbavi.member3_search_detail.DishDetailActivity
import com.dungtien.amthucbavi.services.AuthService

class ViewingHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewingHistoryBinding
    private lateinit var adapter: DishAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewingHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBackHistory.setOnClickListener { finish() }

        val currentUser = AuthService.getInstance(this).currentUser

        adapter = DishAdapter(emptyList(), onItemClick = { dish ->
            val intent = Intent(this, DishDetailActivity::class.java)
            intent.putExtra("dish_id", dish.id)
            startActivity(intent)
        })

        binding.rvHistory.layoutManager = LinearLayoutManager(this)
        binding.rvHistory.adapter = adapter

        binding.btnClearHistory.setOnClickListener {
            if (currentUser != null) {
                AlertDialog.Builder(this)
                    .setTitle("Xóa lịch sử xem?")
                    .setMessage("Thao tác này sẽ xóa toàn bộ lịch sử của bạn.")
                    .setPositiveButton("Xóa") { _, _ ->
                        DatabaseHelper.getInstance(this).clearHistory(currentUser.id)
                        loadHistory()
                    }
                    .setNegativeButton("Hủy", null)
                    .show()
            }
        }

        loadHistory()
    }

    private fun loadHistory() {
        val user = AuthService.getInstance(this).currentUser
        if (user != null) {
            val list = DatabaseHelper.getInstance(this).getHistory(user.id)
            adapter.updateData(list)
        }
    }
}
