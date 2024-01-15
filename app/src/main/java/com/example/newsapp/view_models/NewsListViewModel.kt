package com.example.newsapp.view_models

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.example.NewsApiResponse
import com.example.newsapp.network.classes.Article
import com.example.newsapp.repositories.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsListViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {
    var newsList: NewsApiResponse? = null

    init {
        Log.d("myLogs", "init")
        /*viewModelScope.launch {
            newsList = newsRepository.getNews()
            var i = 0
            //Log.d("myLogs", newsList?.totalResults!!.toString())
            for(item in newsList?.articles!!) {
                Log.d("myLogs", item.author!!)
                i++
                if(i > 20) break
            }
        }*/
    }
}