package com.example.newsapp.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
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
    private var collectUiStateCancellationJob: Job? = null

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
        adapter = NewsAdapter (
            onItemClick = { url ->
                findNavController().navigate(
                    R.id.action_newsListFragment_to_articleWebViewFragment,
                    Bundle().apply {
                        putString("url", url)
                    }
                )
            },
            onOpenInBrowserButtonClick = { url ->
                openArticleInBrowser(url)
            }
        )
        binding?.rvNews?.adapter = adapter

        binding?.buttonStartSearch?.setOnClickListener {
            val query = binding?.etQuery?.text.toString()
            newsListViewModel.getArticles(query)
            collectUiState()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            newsListViewModel.getInitialDataJob?.join()
            binding?.etQuery?.setText(newsListViewModel.query)
        }

        binding?.etQuery?.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                newsListViewModel.updateUserQuery(text.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {}

        })
    }

    private fun collectUiState() {
        collectUiStateJob?.let { collectUiStateJob ->
            collectUiStateCancellationJob = viewLifecycleOwner.lifecycleScope.launch {
                collectUiStateJob.cancelAndJoin()
            }
        }

        collectUiStateJob = viewLifecycleOwner.lifecycleScope.launch {
            collectUiStateCancellationJob?.join()
            newsListViewModel.getInitialDataJob?.join()
            newsListViewModel.articles?.collectLatest { articles ->
                adapter?.submitData(articles)
            }
        }
    }

    private fun openArticleInBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null
        binding = null
    }
}