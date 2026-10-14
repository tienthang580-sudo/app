package com.dungtien.amthucbavi.member3_search_detail

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dungtien.amthucbavi.adapters.DishAdapter
import com.dungtien.amthucbavi.database.DatabaseHelper
import com.dungtien.amthucbavi.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var adapter: DishAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBackSearch.setOnClickListener { finish() }

        adapter = DishAdapter(emptyList(), onItemClick = { dish ->
            val intent = Intent(this, DishDetailActivity::class.java)
            intent.putExtra("dish_id", dish.id)
            startActivity(intent)
        })

        binding.rvSearchResults.layoutManager = LinearLayoutManager(this)
        binding.rvSearchResults.adapter = adapter

        binding.etSearchKeyword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val kw = s?.toString() ?: ""
                val results = DatabaseHelper.getInstance(this@SearchActivity).searchDishes(kw)
                adapter.updateData(results)
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // Initial empty search
        val initialList = DatabaseHelper.getInstance(this).searchDishes("")
        adapter.updateData(initialList)
    }
}
