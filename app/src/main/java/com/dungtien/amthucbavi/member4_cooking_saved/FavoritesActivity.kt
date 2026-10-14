package com.dungtien.amthucbavi.member4_cooking_saved

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dungtien.amthucbavi.R
import com.dungtien.amthucbavi.adapters.DishAdapter
import com.dungtien.amthucbavi.database.DatabaseHelper
import com.dungtien.amthucbavi.databinding.ActivityFavoritesBinding
import com.dungtien.amthucbavi.member3_search_detail.DishDetailActivity
import com.dungtien.amthucbavi.services.AuthService

class FavoritesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoritesBinding
    private lateinit var adapter: DishAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBackFav.setOnClickListener { finish() }

        val currentUser = AuthService.getInstance(this).currentUser

        adapter = DishAdapter(
            dishes = emptyList(),
            onItemClick = { dish ->
                val intent = Intent(this, DishDetailActivity::class.java)
                intent.putExtra("dish_id", dish.id)
                startActivity(intent)
            },
            actionIconRes = android.R.drawable.ic_menu_delete,
            onActionClick = { dish ->
                if (currentUser != null) {
                    DatabaseHelper.getInstance(this).toggleFavorite(currentUser.id, dish.id)
                    loadFavorites()
                }
            }
        )

        binding.rvFavorites.layoutManager = LinearLayoutManager(this)
        binding.rvFavorites.adapter = adapter

        loadFavorites()
    }

    private fun loadFavorites() {
        val user = AuthService.getInstance(this).currentUser
        if (user != null) {
            val favs = DatabaseHelper.getInstance(this).getFavorites(user.id)
            adapter.updateData(favs)
        }
    }
}
