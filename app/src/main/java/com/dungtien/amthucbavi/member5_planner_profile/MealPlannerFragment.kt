package com.dungtien.amthucbavi.member5_planner_profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.dungtien.amthucbavi.adapters.MealPlanAdapter
import com.dungtien.amthucbavi.database.DatabaseHelper
import com.dungtien.amthucbavi.databinding.FragmentMealPlannerBinding
import com.dungtien.amthucbavi.services.AuthService
import java.text.NumberFormat
import java.util.Locale

class MealPlannerFragment : Fragment() {

    private var _binding: FragmentMealPlannerBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: MealPlanAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMealPlannerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MealPlanAdapter(
            plans = emptyList(),
            onQuantityChange = { plan, newQty ->
                DatabaseHelper.getInstance(requireContext()).updateMealQuantity(plan.id, newQty)
                loadMealPlans()
            },
            onDelete = { plan ->
                DatabaseHelper.getInstance(requireContext()).deleteMealPlan(plan.id)
                loadMealPlans()
            }
        )

        binding.rvMealPlans.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMealPlans.adapter = adapter

        loadMealPlans()
    }

    private fun loadMealPlans() {
        val user = AuthService.getInstance(requireContext()).currentUser
        if (user != null) {
            val plans = DatabaseHelper.getInstance(requireContext()).getMealPlans(user.id)
            adapter.updateData(plans)

            val totalPrice = plans.sumOf { it.dishPrice * it.quantity }
            val totalCalories = plans.sumOf { it.dishCalories * it.quantity }

            val formatter = NumberFormat.getInstance(Locale("vi", "VN"))
            binding.tvPlannerSummary.text = "Tổng thực đơn: ${formatter.format(totalPrice)}đ · $totalCalories kcal"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
