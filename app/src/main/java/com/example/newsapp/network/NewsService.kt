package com.example.newsapp.network

import android.util.Log
import com.example.example.NewsApiResponse
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.time.LocalDateTime

val LocalDateTime.simpleDateFormat
    get() = "${this.year}-${this.monthValue}-${this.dayOfMonth}"

interface NewsService {

    @GET("everything")
    suspend fun getNews (
        @Query("q") query: String,
        @Query("from") from: String = LocalDateTime.now().minusDays(2).simpleDateFormat,
        @Query("to") to: String = LocalDateTime.now().simpleDateFormat,
        @Query("sortBy") sortBy: String = "popularity",
        @Query("page") page: Int = 1,
        @Query("pageSize") pageSize: Int = 10
    ): NewsApiResponse

    companion object {
        private const val BASE_URL = "https://newsapi.org/v2/"

        private const val apiKey = "668ceb68185b4fe68c5a0a9ea453324a"

        fun create(): NewsService {
            val client = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val original: Request = chain.request()
                    val originalHttpUrl: HttpUrl = original.url

                    val url = originalHttpUrl.newBuilder()
                        .addQueryParameter("apiKey", apiKey)
                        .build()

                    val request = original.newBuilder()
                        .url(url)
                        .build()

                    chain.proceed(request)
                }
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(NewsService::class.java)
        }
    }
}