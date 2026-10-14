package com.dungtien.amthucbavi.member2_catalog

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.dungtien.amthucbavi.adapters.DishAdapter
import com.dungtien.amthucbavi.database.DatabaseHelper
import com.dungtien.amthucbavi.databinding.FragmentMenuBoardBinding
import com.dungtien.amthucbavi.member3_search_detail.DishDetailActivity
import com.dungtien.amthucbavi.member3_search_detail.FilterActivity

class MenuBoardFragment : Fragment() {

    private var _binding: FragmentMenuBoardBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: DishAdapter

    private var currentSort = "name"
    private var descending = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMenuBoardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = DishAdapter(emptyList(), onItemClick = { dish ->
            val intent = Intent(requireContext(), DishDetailActivity::class.java)
            intent.putExtra("dish_id", dish.id)
            startActivity(intent)
        })

        binding.rvAllDishes.layoutManager = LinearLayoutManager(requireContext())
        binding.rvAllDishes.adapter = adapter

        val sortOptions = arrayOf("Tên món", "Giá tiền", "Thời gian nấu")
        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, sortOptions)
        binding.spinnerSort.adapter = spinnerAdapter

        binding.spinnerSort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                currentSort = when (position) {
                    1 -> "price"
                    2 -> "cooking_time"
                    else -> "name"
                }
                loadDishes()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.btnToggleSortOrder.setOnClickListener {
            descending = !descending
            loadDishes()
        }

        binding.btnMenuFilter.setOnClickListener {
            startActivity(Intent(requireContext(), FilterActivity::class.java))
        }

        loadDishes()
    }

    private fun loadDishes() {
        val list = DatabaseHelper.getInstance(requireContext()).getDishes(sort = currentSort, descending = descending)
        adapter.updateData(list)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
