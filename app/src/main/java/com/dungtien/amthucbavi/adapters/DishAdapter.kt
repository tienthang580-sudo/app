package com.dungtien.amthucbavi.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dungtien.amthucbavi.R
import com.dungtien.amthucbavi.databinding.ItemDishBinding
import com.dungtien.amthucbavi.models.Dish
import java.text.NumberFormat
import java.util.Locale

class DishAdapter(
    private var dishes: List<Dish>,
    private val onItemClick: (Dish) -> Unit,
    private val actionIconRes: Int? = null,
    private val onActionClick: ((Dish) -> Unit)? = null
) : RecyclerView.Adapter<DishAdapter.DishViewHolder>() {

    fun updateData(newDishes: List<Dish>) {
        dishes = newDishes
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DishViewHolder {
        val binding = ItemDishBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DishViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DishViewHolder, position: Int) {
        holder.bind(dishes[position])
    }

    override fun getItemCount(): Int = dishes.size

    inner class DishViewHolder(private val binding: ItemDishBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(dish: Dish) {
            binding.tvDishName.text = dish.name
            val formatter = NumberFormat.getInstance(Locale("vi", "VN"))
            val priceStr = "${formatter.format(dish.price)}đ"
            val subtitle = if (dish.viewedAt != null) {
                "$priceStr · Xem lúc ${dish.viewedAt.split(".")[0]}"
            } else {
                "$priceStr · ${dish.cookingTime} phút · ${dish.calories} kcal"
            }
            binding.tvDishSubtitle.text = subtitle

            Glide.with(binding.imgDish.context)
                .load(dish.imageUrl)
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .into(binding.imgDish)

            binding.root.setOnClickListener { onItemClick(dish) }

            if (actionIconRes != null && onActionClick != null) {
                binding.btnAction.visibility = View.VISIBLE
                binding.btnAction.setImageResource(actionIconRes)
                binding.btnAction.setOnClickListener { onActionClick.invoke(dish) }
            } else {
                binding.btnAction.visibility = View.GONE
            }
        }
    }
}
