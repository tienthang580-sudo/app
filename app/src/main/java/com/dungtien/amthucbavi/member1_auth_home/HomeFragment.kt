package com.dungtien.amthucbavi.member1_auth_home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.dungtien.amthucbavi.R
import com.dungtien.amthucbavi.adapters.DishAdapter
import com.dungtien.amthucbavi.database.DatabaseHelper
import com.dungtien.amthucbavi.databinding.FragmentHomeBinding
import com.dungtien.amthucbavi.member2_catalog.CategoryDishesActivity
import com.dungtien.amthucbavi.member3_search_detail.DishDetailActivity
import com.dungtien.amthucbavi.member3_search_detail.SearchActivity
import com.dungtien.amthucbavi.member4_cooking_saved.FavoritesActivity
import com.dungtien.amthucbavi.member4_cooking_saved.ViewingHistoryActivity
import com.dungtien.amthucbavi.member5_planner_profile.ArticlesActivity
import com.dungtien.amthucbavi.services.AuthService

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var dishAdapter: DishAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = AuthService.getInstance(requireContext()).currentUser
        binding.tvGreeting.text = "Xin chào, ${user?.fullName ?: "Quý khách"}"

        dishAdapter = DishAdapter(emptyList(), onItemClick = { dish ->
            val intent = Intent(requireContext(), DishDetailActivity::class.java)
            intent.putExtra("dish_id", dish.id)
            startActivity(intent)
        })

        binding.rvFeaturedDishes.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFeaturedDishes.adapter = dishAdapter

        loadFeaturedDishes()

        binding.btnHeaderSearch.setOnClickListener {
            startActivity(Intent(requireContext(), SearchActivity::class.java))
        }

        binding.btnCallBanner.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:0835272818"))
            startActivity(intent)
        }

        binding.cardDaDieu.setOnClickListener {
            val intent = Intent(requireContext(), CategoryDishesActivity::class.java)
            intent.putExtra("category_id", "da_dieu")
            startActivity(intent)
        }

        binding.cardMonBo.setOnClickListener {
            val intent = Intent(requireContext(), CategoryDishesActivity::class.java)
            intent.putExtra("category_id", "mon_bo")
            startActivity(intent)
        }

        binding.chipFavorites.setOnClickListener {
            startActivity(Intent(requireContext(), FavoritesActivity::class.java))
        }

        binding.chipHistory.setOnClickListener {
            startActivity(Intent(requireContext(), ViewingHistoryActivity::class.java))
        }

        binding.chipArticles.setOnClickListener {
            startActivity(Intent(requireContext(), ArticlesActivity::class.java))
        }
    }

    private fun loadFeaturedDishes() {
        val list = DatabaseHelper.getInstance(requireContext()).getFeaturedDishes()
        dishAdapter.updateData(list)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
