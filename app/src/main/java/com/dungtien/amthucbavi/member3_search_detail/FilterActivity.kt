package com.dungtien.amthucbavi.member3_search_detail

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dungtien.amthucbavi.adapters.DishAdapter
import com.dungtien.amthucbavi.database.DatabaseHelper
import com.dungtien.amthucbavi.databinding.ActivityFilterBinding

class FilterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFilterBinding
    private lateinit var adapter: DishAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBackFilter.setOnClickListener { finish() }

        adapter = DishAdapter(emptyList(), onItemClick = { dish ->
            val intent = Intent(this, DishDetailActivity::class.java)
            intent.putExtra("dish_id", dish.id)
            startActivity(intent)
        })

        binding.rvFilterResults.layoutManager = LinearLayoutManager(this)
        binding.rvFilterResults.adapter = adapter

        // Price Options
        val priceOptions = arrayOf("Tất cả mức giá", "200.000đ", "250.000đ")
        binding.spinnerPrice.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, priceOptions)

        // Categories
        val dbHelper = DatabaseHelper.getInstance(this)
        val categories = dbHelper.getCategories()
        val catNames = mutableListOf("Tất cả danh mục")
        catNames.addAll(categories.map { it.name })
        binding.spinnerCategory.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, catNames)

        // Minutes Options
        val minOptions = arrayOf("Không giới hạn", "15 phút", "20 phút", "25 phút")
        binding.spinnerMinutes.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, minOptions)

        // Calories Options
        val calOptions = arrayOf("Không giới hạn", "300 kcal", "350 kcal", "400 kcal")
        binding.spinnerCalories.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, calOptions)

        binding.btnApplyFilter.setOnClickListener {
            val priceVal = when (binding.spinnerPrice.selectedItemPosition) {
                1 -> 200000
                2 -> 250000
                else -> null
            }

            val catPos = binding.spinnerCategory.selectedItemPosition
            val catVal = if (catPos > 0) categories[catPos - 1].id else null

            val minVal = when (binding.spinnerMinutes.selectedItemPosition) {
                1 -> 15
                2 -> 20
                3 -> 25
                else -> null
            }

            val calVal = when (binding.spinnerCalories.selectedItemPosition) {
                1 -> 300
                2 -> 350
                3 -> 400
                else -> null
            }

            val results = dbHelper.filterDishes(price = priceVal, categoryId = catVal, maxMinutes = minVal, maxCalories = calVal)
            adapter.updateData(results)
        }
    }
}
