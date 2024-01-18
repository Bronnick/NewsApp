package com.example.newsapp.repositories

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.example.NewsApiResponse
import com.example.newsapp.network.NewsService
import com.example.newsapp.network.classes.Article
import com.example.newsapp.paging.NewsPagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

const val NETWORK_PAGE_SIZE = 10

class NewsRepository @Inject constructor(
    private val newsService: NewsService
) {

    fun getNews(query: String): Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = NETWORK_PAGE_SIZE),
            pagingSourceFactory = { NewsPagingSource(query, newsService) }
        ).flow
    }

    /*suspend fun getNews(): NewsApiResponse {
        return newsService.getNews(query = "apple", page = 1, pageSize = 30)
    }*/
}