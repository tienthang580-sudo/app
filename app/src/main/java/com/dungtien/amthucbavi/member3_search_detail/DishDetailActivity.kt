package com.dungtien.amthucbavi.member3_search_detail

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dungtien.amthucbavi.R
import com.dungtien.amthucbavi.database.DatabaseHelper
import com.dungtien.amthucbavi.databinding.ActivityDishDetailBinding
import com.dungtien.amthucbavi.member4_cooking_saved.CookingGuideActivity
import com.dungtien.amthucbavi.models.Dish
import com.dungtien.amthucbavi.services.AuthService
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DishDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDishDetailBinding
    private var currentDish: Dish? = null
    private var isFav = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDishDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dishId = intent.getLongExtra("dish_id", -1L)
        if (dishId == -1L) {
            finish()
            return
        }

        val dbHelper = DatabaseHelper.getInstance(this)
        val dish = dbHelper.getDish(dishId)
        if (dish == null) {
            finish()
            return
        }
        currentDish = dish

        val currentUser = AuthService.getInstance(this).currentUser
        if (currentUser != null) {
            dbHelper.addHistory(currentUser.id, dish.id)
            isFav = dbHelper.isFavorite(currentUser.id, dish.id)
            updateFavoriteIcon()
        }

        binding.btnBackDetail.setOnClickListener { finish() }

        binding.tvDetailName.text = dish.name
        binding.tvDetailDescription.text = dish.description ?: ""

        val formatter = NumberFormat.getInstance(Locale("vi", "VN"))
        binding.chipPrice.text = "Giá: ${formatter.format(dish.price)}đ"
        binding.chipTime.text = "Thời gian: ${dish.cookingTime} phút"
        binding.chipCalories.text = "Năng lượng: ${dish.calories} kcal"
        binding.chipServings.text = "Khẩu phần: ${dish.servings} người"
        binding.chipDifficulty.text = "Độ khó: ${dish.difficulty}"

        binding.tvIngredientsList.text = dish.ingredients.joinToString("\n") { "• $it" }

        Glide.with(this)
            .load(dish.imageUrl)
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(binding.imgDishDetail)

        binding.btnFavorite.setOnClickListener {
            if (currentUser != null) {
                isFav = dbHelper.toggleFavorite(currentUser.id, dish.id)
                updateFavoriteIcon()
                val msg = if (isFav) "Đã thêm vào danh sách yêu thích" else "Đã xóa khỏi danh sách yêu thích"
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnCookingGuide.setOnClickListener {
            val intent = Intent(this, CookingGuideActivity::class.java)
            intent.putExtra("dish_id", dish.id)
            startActivity(intent)
        }

        binding.btnAddToPlanner.setOnClickListener {
            if (currentUser != null) {
                showAddToMealPlanDialog(currentUser.id, dish.id)
            }
        }
    }

    private fun updateFavoriteIcon() {
        val icon = if (isFav) android.R.drawable.btn_star_big_on else android.R.drawable.btn_star_big_off
        binding.btnFavorite.setImageResource(icon)
    }

    private fun showAddToMealPlanDialog(userId: Long, dishId: Long) {
        val mealTypes = arrayOf("Bữa trưa", "Bữa tối", "Tiệc Villa")
        AlertDialog.Builder(this)
            .setTitle("Chọn bữa ăn")
            .setItems(mealTypes) { _, which ->
                val selectedType = mealTypes[which]
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val todayStr = sdf.format(Date())

                DatabaseHelper.getInstance(this).addMealPlan(userId, dishId, selectedType, todayStr)
                Toast.makeText(this, "Đã thêm vào kế hoạch bữa ăn ($selectedType)", Toast.LENGTH_SHORT).show()
            }
            .show()
    }
}
