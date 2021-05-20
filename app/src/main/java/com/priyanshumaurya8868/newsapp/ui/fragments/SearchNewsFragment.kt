package com.priyanshumaurya8868.newsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.priyanshumaurya8868.newsapp.MainActivity
import com.priyanshumaurya8868.newsapp.R
import com.priyanshumaurya8868.newsapp.adapter.FeedAdapter
import com.priyanshumaurya8868.newsapp.databinding.FragmentSearchNewsBinding
import com.priyanshumaurya8868.newsapp.ui.viewModel.NewsViewModel
import com.priyanshumaurya8868.newsapp.util.Constants
import com.priyanshumaurya8868.newsapp.util.Constants.SEARCHED_TIME_DELAY
import com.priyanshumaurya8868.newsapp.util.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchNewsFragment : Fragment() {

    private val TAG = "SearchedNewsFrag"
    private lateinit var searchAdapter: FeedAdapter
    lateinit var viewModel: NewsViewModel
    private var _binding : FragmentSearchNewsBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        searchAdapter = FeedAdapter()
        _binding = FragmentSearchNewsBinding.inflate(layoutInflater,container,false)
        //to reduces
        var job : Job? = null
        _binding?.etSearch?.addTextChangedListener { editable->
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCHED_TIME_DELAY)
                editable?.let {
                   if (it.isNotEmpty()){
                       viewModel.searchedNewsResponse = null
                       viewModel.getSearchedNews(it.toString())
                   }
                }
            }
        }
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRV()
        viewModel.searchedNews.observe({ lifecycle }) {
            when (it) {
                is Resource.Success -> {
                    hideProgressBar()
                    searchAdapter.differ.submitList(it.data?.articles?.toList())   // need to convert into List from mutable
                    val totalPages = it.data?.totalResults!!/ Constants.PAGE_SIZE + 2
                    //plus 2 because  of zero based indexing & (.5) neglected  in odd num cases
                    isLastPage = viewModel.searchedNewsPages == totalPages
                    if(isLastPage){
                        _binding?.rv?.setPadding(0,0,0,0,)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    Log.d(TAG, it.msg.toString())
                    Toast.makeText(context,it.msg.toString(), Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        }

        searchAdapter.setOnClickListener {
            val bundle = bundleOf(
                "article" to it
            )
            findNavController().navigate(R.id.action_searchNewsFragment_to_articleFragment,
                bundle
            )
        }


    }

    var isLoading = false
    var isLastPage =false
    var isScrolling = false

    val scrollListener = object : RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndIsNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotBeginning = firstVisibleItemPosition >=0
            val isTotalMoreThanVisible = totalItemCount >= Constants.PAGE_SIZE

            val shouldWePaginate = isNotLoadingAndIsNotLastPage && isNotBeginning && isAtLastItem && isTotalMoreThanVisible
            if (shouldWePaginate){
                viewModel.getSearchedNews("in")
                isScrolling = false
            }

        }
    }


    private fun setUpRV() {
        searchAdapter= FeedAdapter()
        _binding?.rv?.apply {
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(context)
            addOnScrollListener(scrollListener)
        }
    }

    private fun hideProgressBar() {
        isLoading = false
        _binding?.paginationProgressBar?.visibility = View.GONE
    }
    private fun showProgressBar() {
        isLoading = true
        _binding?.paginationProgressBar?.visibility  = View.VISIBLE
    }

}