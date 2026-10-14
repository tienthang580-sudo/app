package com.dungtien.amthucbavi.member5_planner_profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dungtien.amthucbavi.adapters.ArticleAdapter
import com.dungtien.amthucbavi.databinding.ActivityArticlesBinding
import com.dungtien.amthucbavi.models.Article

class ArticlesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityArticlesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticlesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBackArticles.setOnClickListener { finish() }

        val articles = listOf(
            Article(
                title = "Cách chọn thịt đà điểu tươi ngon",
                category = "Chọn nguyên liệu",
                summary = "Nhận biết thịt tươi qua màu sắc, độ đàn hồi và mùi tự nhiên.",
                content = "Ưu tiên phần thịt có màu đỏ hồng tự nhiên, thớ mịn, bề mặt khô ráo và có độ đàn hồi khi ấn nhẹ. Thịt tươi không có mùi lạ. Bảo quản lạnh và chế biến sớm để giữ vị ngọt đặc trưng.",
                icon = "🥩"
            ),
            Article(
                title = "Bí quyết chọn thịt bò ngon",
                category = "Chọn nguyên liệu",
                summary = "Chọn thớ thịt săn, màu đỏ tươi và lớp mỡ sáng.",
                content = "Thịt bò ngon có màu đỏ tươi, thớ nhỏ và săn chắc; mỡ có màu vàng nhạt hoặc trắng kem. Dùng khăn sạch thấm khô trước khi nấu, thái ngang thớ để thịt mềm hơn.",
                icon = "🍖"
            ),
            Article(
                title = "Xào thịt mềm, giữ trọn dinh dưỡng",
                category = "Kỹ thuật chế biến",
                summary = "Làm nóng chảo, chia mẻ nhỏ và tránh nấu quá lâu.",
                content = "Làm nóng chảo trước khi cho thịt vào, không xào quá nhiều một lúc để nhiệt không giảm. Thái ngang thớ, ướp vừa đủ và xào nhanh trên lửa lớn giúp thịt giữ độ mềm, rau củ vẫn giòn ngon.",
                icon = "🍳"
            ),
            Article(
                title = "Ướp thịt nướng thơm đậm đà",
                category = "Kỹ thuật chế biến",
                summary = "Cân bằng gia vị và để thịt nghỉ trước khi nướng.",
                content = "Kết hợp tỏi, tiêu, chút dầu ăn cùng gia vị vừa phải; để thịt thấm ít nhất 15–20 phút. Nướng chín đều, không lật liên tục và để thịt nghỉ vài phút trước khi thái để giữ nước ngọt.",
                icon = "🔥"
            )
        )

        binding.rvArticles.layoutManager = LinearLayoutManager(this)
        binding.rvArticles.adapter = ArticleAdapter(articles)
    }
}
