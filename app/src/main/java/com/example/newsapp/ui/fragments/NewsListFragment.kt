package com.example.newsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentNewsListBinding
import com.example.newsapp.ui.adapters.NewsAdapter
import com.example.newsapp.view_models.NewsListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewsListFragment : Fragment(R.layout.fragment_news_list) {
    private var binding: FragmentNewsListBinding? = null

    private val newsListViewModel: NewsListViewModel by viewModels()
    private var adapter: NewsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewsListBinding.inflate(inflater, container, false)
        Log.d("myLogs", "create view ${binding!!.rvNews}")
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.rvNews?.setOnClickListener {
            Log.d("myLogs", "list clicked")
        }.also { Log.d("mylogs", "click listener set") }

        initView()
        collectUiState()
    }

    private fun initView() {
        adapter = NewsAdapter()
        binding?.rvNews?.adapter = adapter
        Log.d("myLogs", "init view")
    }

    private fun collectUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            Log.d("myLogs", "data submitted")
            newsListViewModel.getArticles("Apple").collectLatest { articles ->
                Log.d("myLogs", articles.toString())
                adapter?.submitData(articles)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null
        binding = null
    }
}