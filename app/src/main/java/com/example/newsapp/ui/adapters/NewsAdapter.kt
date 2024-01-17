package com.example.newsapp.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.newsapp.R
import com.example.newsapp.databinding.NewsListItemBinding
import com.example.newsapp.network.classes.Article
import java.io.File

class NewsAdapter : PagingDataAdapter<Article, ArticleViewHolder>(ArticleDiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        Log.d("myLogs", "view holder created")
        return ArticleViewHolder(
            NewsListItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        Log.d("myLogs", "view holder binded")
        holder.bind(getItem(position) ?: Article())
    }
}

class ArticleDiffCallback : DiffUtil.ItemCallback<Article>() {

    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem.url == newItem.url
    }

    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem == newItem
    }
}

class ArticleViewHolder(
    val binding: NewsListItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(article: Article) {
        binding.articleName.text = article.title
        binding.author.text = article.author
        binding.articlePoster.load(article.urlToImage ?: R.drawable.image_not_found) {
            crossfade(durationMillis = 1000)
            transformations(RoundedCornersTransformation(12.5f))
        }
    }
}