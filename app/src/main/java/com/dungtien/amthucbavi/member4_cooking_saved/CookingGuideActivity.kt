package com.dungtien.amthucbavi.member4_cooking_saved

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dungtien.amthucbavi.R
import com.dungtien.amthucbavi.database.DatabaseHelper
import com.dungtien.amthucbavi.databinding.ActivityCookingGuideBinding

class CookingGuideActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCookingGuideBinding
    private var activeTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCookingGuideBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dishId = intent.getLongExtra("dish_id", -1L)
        if (dishId == -1L) {
            finish()
            return
        }

        val dish = DatabaseHelper.getInstance(this).getDish(dishId)
        if (dish == null) {
            finish()
            return
        }

        binding.tvCookingTitle.text = "Công thức ${dish.name}"
        binding.btnBackCooking.setOnClickListener { finish() }

        val inflater = LayoutInflater.from(this)
        dish.steps.forEachIndexed { index, step ->
            val stepView = inflater.inflate(R.layout.item_article, binding.containerSteps, false)
            val tvIcon = stepView.findViewById<TextView>(R.id.tvArticleIcon)
            val tvTitle = stepView.findViewById<TextView>(R.id.tvArticleTitle)
            val tvCat = stepView.findViewById<TextView>(R.id.tvArticleCategory)
            val tvSummary = stepView.findViewById<TextView>(R.id.tvArticleSummary)
            val tvContent = stepView.findViewById<TextView>(R.id.tvArticleContent)

            tvIcon.text = "⏱️"
            tvTitle.text = "Bước ${index + 1}"
            tvCat.text = if (step.timerMinutes > 0) "Thời gian: ${step.timerMinutes} phút" else "Sơ chế"
            tvSummary.text = step.instruction
            tvContent.text = ""

            if (step.timerMinutes > 0) {
                val btnTimer = Button(this).apply {
                    text = "Bắt đầu đếm ngược (${step.timerMinutes} phút)"
                    setOnClickListener {
                        activeTimer?.cancel()
                        val totalMillis = step.timerMinutes * 60 * 1000L
                        activeTimer = object : CountDownTimer(totalMillis, 1000) {
                            override fun onTick(millisUntilFinished: Long) {
                                val seconds = (millisUntilFinished / 1000) % 60
                                val minutes = (millisUntilFinished / 1000) / 60
                                text = String.format("Còn lại: %02d:%02d", minutes, seconds)
                            }

                            override fun onFinish() {
                                text = "Hết giờ công đoạn ${index + 1}!"
                                Toast.makeText(this@CookingGuideActivity, "Hết giờ công đoạn ${index + 1}!", Toast.LENGTH_LONG).show()
                            }
                        }.start()
                    }
                }
                (stepView as android.view.ViewGroup).addView(btnTimer)
            }

            binding.containerSteps.addView(stepView)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        activeTimer?.cancel()
    }
}
