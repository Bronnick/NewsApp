package com.example.example

import com.example.newsapp.network.classes.Article
import com.google.gson.annotations.SerializedName


data class NewsApiResponse (

  @SerializedName("status"       ) var status       : String?             = null,
  @SerializedName("totalResults" ) var totalResults : Int?                = null,
  @SerializedName("articles"     ) var articles     : ArrayList<Article> = arrayListOf()

)