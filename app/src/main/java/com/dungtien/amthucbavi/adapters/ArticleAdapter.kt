package com.dungtien.amthucbavi.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dungtien.amthucbavi.databinding.ItemArticleBinding
import com.dungtien.amthucbavi.models.Article

class ArticleAdapter(
    private val articles: List<Article>
) : RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bind(articles[position])
    }

    override fun getItemCount(): Int = articles.size

    class ArticleViewHolder(private val binding: ItemArticleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(article: Article) {
            binding.tvArticleIcon.text = article.icon
            binding.tvArticleTitle.text = article.title
            binding.tvArticleCategory.text = article.category
            binding.tvArticleSummary.text = article.summary
            binding.tvArticleContent.text = article.content
        }
    }
}
