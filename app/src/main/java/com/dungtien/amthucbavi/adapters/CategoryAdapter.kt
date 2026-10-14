package com.dungtien.amthucbavi.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dungtien.amthucbavi.R
import com.dungtien.amthucbavi.databinding.ItemCategoryBinding
import com.dungtien.amthucbavi.models.Category

class CategoryAdapter(
    private var categories: List<Category>,
    private val onItemClick: (Category) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    fun updateData(newCategories: List<Category>) {
        categories = newCategories
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categories[position])
    }

    override fun getItemCount(): Int = categories.size

    inner class CategoryViewHolder(private val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cat: Category) {
            binding.tvCatName.text = cat.name
            binding.tvCatDesc.text = cat.description ?: ""
            binding.tvCount.text = "${cat.dishCount} món"

            Glide.with(binding.imgCategory.context)
                .load(cat.imageUrl)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(binding.imgCategory)

            binding.root.setOnClickListener { onItemClick(cat) }
        }
    }
}
