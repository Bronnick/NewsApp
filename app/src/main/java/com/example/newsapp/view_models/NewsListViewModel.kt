package com.example.newsapp.view_models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.example.NewsApiResponse
import com.example.newsapp.network.classes.Article
import com.example.newsapp.repositories.DatastoreRepository
import com.example.newsapp.repositories.NewsRepository
import com.example.newsapp.utils.LAST_QUERY_DATASTORE_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsListViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val datastoreRepository: DatastoreRepository
) : ViewModel() {

    /*private val _articles = MutableStateFlow<PagingData<Article>?>(null)
    val articles: Flow<PagingData<Article>>
        get() = _articles.filterNotNull()*/

    var query: String? = null

    var articles: Flow<PagingData<Article>>? = null

    var getInitialDataJob: Job? = null

    init {
        getInitialDataJob = viewModelScope.launch {
            query = datastoreRepository.getParameterByKey(LAST_QUERY_DATASTORE_KEY) as? String ?: "Android"
            Log.d("myLogs", query!!)
            getArticles(query!!)
        }
    }

    fun getArticles(query: String) {
        articles = newsRepository.getNews(query)
            .cachedIn(viewModelScope)
    }

    fun updateUserQuery(query: String) {
        viewModelScope.launch {
            datastoreRepository.updateSettings(LAST_QUERY_DATASTORE_KEY, query)
        }
    }

    /*fun getArticles(query: String): Flow<PagingData<Article>> {
        Log.d("myLogs", "articles called")
        return newsRepository.getNews(query).cachedIn(viewModelScope)
    }*/
}