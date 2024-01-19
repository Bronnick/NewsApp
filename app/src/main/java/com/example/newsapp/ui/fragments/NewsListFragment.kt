package com.example.newsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentNewsListBinding
import com.example.newsapp.ui.adapters.NewsAdapter
import com.example.newsapp.view_models.NewsListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

const val RECYCLER_VIEW_CURRENT_LAST_POSITION_KEY = "last_position_key"

@AndroidEntryPoint
class NewsListFragment : Fragment(R.layout.fragment_news_list) {
    private var binding: FragmentNewsListBinding? = null

    private val newsListViewModel: NewsListViewModel by viewModels()
    private var adapter: NewsAdapter? = null

    private var collectUiStateJob: Job? = null
    private var cancellationJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewsListBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        collectUiState()
    }

    private fun initView() {
        adapter = NewsAdapter { url ->
            findNavController().navigate(
                R.id.action_newsListFragment_to_articleWebViewFragment,
                Bundle().apply {
                    putString("url", url)
                }
            )
        }
        binding?.rvNews?.adapter = adapter

        binding?.buttonStartSearch?.setOnClickListener {
            val query = binding?.etQuery?.text.toString()
            newsListViewModel.getArticles(query)
            collectUiState()
        }
    }

    private fun collectUiState() {
        collectUiStateJob?.let { collectUiStateJob ->
            cancellationJob = viewLifecycleOwner.lifecycleScope.launch {
                collectUiStateJob.cancelAndJoin()
            }
        }

        collectUiStateJob = viewLifecycleOwner.lifecycleScope.launch {
            cancellationJob?.join()
            newsListViewModel.articles.collectLatest { articles ->
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