package com.example.newsapp.repositories

import com.example.example.NewsApiResponse
import com.example.newsapp.network.NewsService
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val newsService: NewsService
) {
    suspend fun getNews(): NewsApiResponse {
        return newsService.getNews("apple", "2024-01-12", "2024-01-12", "popularity")
    }
}