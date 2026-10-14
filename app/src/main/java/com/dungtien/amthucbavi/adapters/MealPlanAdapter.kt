package com.dungtien.amthucbavi.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dungtien.amthucbavi.databinding.ItemMealPlanBinding
import com.dungtien.amthucbavi.models.MealPlan
import java.text.NumberFormat
import java.util.Locale

class MealPlanAdapter(
    private var plans: List<MealPlan>,
    private val onQuantityChange: (MealPlan, Int) -> Unit,
    private val onDelete: (MealPlan) -> Unit
) : RecyclerView.Adapter<MealPlanAdapter.MealPlanViewHolder>() {

    fun updateData(newPlans: List<MealPlan>) {
        plans = newPlans
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealPlanViewHolder {
        val binding = ItemMealPlanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MealPlanViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MealPlanViewHolder, position: Int) {
        holder.bind(plans[position])
    }

    override fun getItemCount(): Int = plans.size

    inner class MealPlanViewHolder(private val binding: ItemMealPlanBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(plan: MealPlan) {
            binding.tvPlanDishName.text = plan.dishName
            binding.tvPlanTypeDate.text = "${plan.mealType} · ${plan.planDate}"
            val formatter = NumberFormat.getInstance(Locale("vi", "VN"))
            binding.tvPlanPrice.text = "${formatter.format(plan.dishPrice)}đ / phần · ${plan.dishCalories} kcal"
            binding.tvQuantity.text = plan.quantity.toString()

            binding.btnMinus.setOnClickListener { onQuantityChange(plan, plan.quantity - 1) }
            binding.btnPlus.setOnClickListener { onQuantityChange(plan, plan.quantity + 1) }
            binding.btnDeletePlan.setOnClickListener { onDelete(plan) }
        }
    }
}
