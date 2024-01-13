package com.example.newsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class NewsListActivity : AppCompatActivity() {
    val apiKey = "668ceb68185b4fe68c5a0a9ea453324a"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}