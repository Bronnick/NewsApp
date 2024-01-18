package com.example.newsapp.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.newsapp.network.NewsService
import com.example.newsapp.network.classes.Article
import com.example.newsapp.repositories.NETWORK_PAGE_SIZE


private const val NEWS_STARTING_PAGE_INDEX = 1
class NewsPagingSource(
    private val query: String,
    private val newsService: NewsService
) : PagingSource<Int, Article>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val pageIndex = params.key ?: NEWS_STARTING_PAGE_INDEX
        return try {
            val response = newsService.getNews(query = query, page = pageIndex, pageSize = NETWORK_PAGE_SIZE)
            val articles = response.articles

            val prevKey = if(pageIndex == NEWS_STARTING_PAGE_INDEX) null else pageIndex - 1

            val nextKey = if(articles.isEmpty()) {
                null
            } else {
                pageIndex + (params.loadSize / NETWORK_PAGE_SIZE)
            }

            LoadResult.Page(
                data = articles,
                prevKey = prevKey,
                nextKey = nextKey
            ).also {
                Log.d("myLogs", "prevKey: $prevKey, nextKey: $nextKey")
            }
        } catch (exception: Exception) {
            Log.d("myLogs", "paging source loaded with error ${exception.message}")
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        Log.d("myLogs", "get refresh key")
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition((anchorPosition))?.nextKey?.minus(1)
        }
    }
}