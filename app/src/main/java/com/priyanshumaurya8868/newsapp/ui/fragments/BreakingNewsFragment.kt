package com.priyanshumaurya8868.newsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.priyanshumaurya8868.newsapp.adapter.FeedAdapter
import com.priyanshumaurya8868.newsapp.MainActivity
import com.priyanshumaurya8868.newsapp.R
import com.priyanshumaurya8868.newsapp.ui.viewModel.NewsViewModel
import com.priyanshumaurya8868.newsapp.util.Resource
import com.priyanshumaurya8868.newsapp.databinding.FragmentBreakingNewsBinding
import com.priyanshumaurya8868.newsapp.util.Constants
import com.priyanshumaurya8868.newsapp.util.Constants.PAGE_SIZE


class BreakingNewsFragment : Fragment() {
    private val TAG = "breakingNews"
    private lateinit var  viewModel: NewsViewModel
   private lateinit var  adapter : FeedAdapter
    private var _binding : FragmentBreakingNewsBinding? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = (activity as MainActivity).viewModel
        _binding = FragmentBreakingNewsBinding.inflate(inflater,container,false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRV()
        viewModel.breakingNews.observe({ lifecycle }) { resource->
            when (resource) {
                is Resource.Success -> {
                    hideProgressBar()
                    adapter.differ.submitList(resource.data?.articles?.toList())   // need to convert into List from mutable
                    val totalPages = resource.data?.totalResults!!/PAGE_SIZE + 2
                    //plus 2 because  of zero based indexing & (.5) neglected  in odd num cases
                  isLastPage = viewModel.breakingNewsPages == totalPages
                    if(isLastPage){
                        _binding?.rvBreakingNews?.setPadding(0,0,0,0,)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    Log.d(TAG, resource.msg.toString())
                    Toast.makeText(context,resource.msg.toString(),Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        }
        adapter.setOnClickListener {
            val bundle = bundleOf(
                "article" to it
            )
            findNavController().navigate(R.id.action_breakingNewsFragment_to_articleFragment,
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
                viewModel.getBreakingNews("in")
                isScrolling = false
            }

        }
    }
    private fun setUpRV() {
        adapter= FeedAdapter()
        _binding?.rvBreakingNews?.apply {
            adapter = this@BreakingNewsFragment.adapter
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