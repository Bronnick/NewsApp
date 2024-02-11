package com.example.newsapp.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
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

class NewsAdapter(
    private val onItemClick: (String?) -> Unit,
    private val onOpenInBrowserButtonClick: (String) -> Unit,
    private val onShareButtonClick: (String) -> Unit
) : PagingDataAdapter<Article, ArticleViewHolder>(ArticleDiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            NewsListItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bind(getItem(position) ?: Article(), onItemClick, onOpenInBrowserButtonClick, onShareButtonClick)
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
    private val binding: NewsListItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        article: Article,
        onItemClick: (String?) -> Unit,
        onOpenInBrowserButtonClick: (String) -> Unit,
        onShareButtonClick: (String) -> Unit
    ) {
        binding.articleName.text = article.title
        binding.author.text = "by ${article.author}"
        binding.articlePoster.load(article.urlToImage ?: R.drawable.image_not_found) {
            crossfade(durationMillis = 1000)
            transformations(RoundedCornersTransformation(12.5f))
        }
        article.url?.let { url ->
            binding.buttonOpenBrowser.setOnClickListener {
                onOpenInBrowserButtonClick(url)
            }
            binding.buttonShareOnFacebook.setOnClickListener {
                onShareButtonClick(url)
            }
        }
        binding.root.setOnClickListener {
            onItemClick(article.url)
        }
    }
}