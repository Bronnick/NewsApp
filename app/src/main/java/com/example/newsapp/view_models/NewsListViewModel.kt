package com.example.newsapp.view_models

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.example.NewsApiResponse
import com.example.newsapp.network.classes.Article
import com.example.newsapp.repositories.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsListViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    /*private val _articles = MutableStateFlow<PagingData<Article>?>(null)
    val articles: Flow<PagingData<Article>>
        get() = _articles.filterNotNull()*/

    val articles: Flow<PagingData<Article>> =
        newsRepository.getNews("Apple")
            .cachedIn(viewModelScope)

    init {
        Log.d("myLogs", "init")

    }

    fun getArticles(query: String): Flow<PagingData<Article>> {
        Log.d("myLogs", "articles called")
        return newsRepository.getNews(query)
            .cachedIn(viewModelScope)
    }

    /*fun getArticles(query: String): Flow<PagingData<Article>> {
        Log.d("myLogs", "articles called")
        return newsRepository.getNews(query).cachedIn(viewModelScope)
    }*/
}