package com.dungtien.amthucbavi.member2_catalog

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dungtien.amthucbavi.adapters.DishAdapter
import com.dungtien.amthucbavi.database.DatabaseHelper
import com.dungtien.amthucbavi.databinding.ActivityCategoryDishesBinding
import com.dungtien.amthucbavi.member3_search_detail.DishDetailActivity

class CategoryDishesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoryDishesBinding
    private lateinit var adapter: DishAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryDishesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val catId = intent.getStringExtra("category_id") ?: "da_dieu"
        val categories = DatabaseHelper.getInstance(this).getCategories()
        val currentCat = categories.find { it.id == catId }

        binding.tvCategoryTitle.text = currentCat?.name ?: "Danh mục"
        binding.btnBackCategory.setOnClickListener { finish() }

        adapter = DishAdapter(emptyList(), onItemClick = { dish ->
            val intent = Intent(this, DishDetailActivity::class.java)
            intent.putExtra("dish_id", dish.id)
            startActivity(intent)
        })

        binding.rvCategoryDishes.layoutManager = LinearLayoutManager(this)
        binding.rvCategoryDishes.adapter = adapter

        val dishes = DatabaseHelper.getInstance(this).getDishes(categoryId = catId)
        adapter.updateData(dishes)
    }
}
