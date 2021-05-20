package com.priyanshumaurya8868.newsapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.priyanshumaurya8868.newsapp.MainActivity
import com.priyanshumaurya8868.newsapp.adapter.FeedAdapter
import com.priyanshumaurya8868.newsapp.databinding.FragmentSavedNewsBinding
import com.priyanshumaurya8868.newsapp.ui.viewModel.NewsViewModel

class SavedNewsFragment : Fragment() {
    private lateinit var adapter : FeedAdapter
    lateinit var viewModel: NewsViewModel
    private var _binding : FragmentSavedNewsBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSavedNewsBinding.inflate(layoutInflater,container,false)
        return _binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRV()
       viewModel.getSavedArticle()?.observe({lifecycle}){
           adapter.differ.submitList(it)
       }
        val  itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
               val article = adapter.differ.currentList[ viewHolder.adapterPosition]
                viewModel.deleteArticle(article)
                Snackbar.make(view,"Article deleted successfully...!",Snackbar.LENGTH_LONG).apply {
                    setAction("Undo"){
                        viewModel.upsert(article)
                    }
                }.show()
            }

        }
        ItemTouchHelper(itemTouchHelperCallback)
            .attachToRecyclerView(_binding?.rv)
    }
    private fun setUpRV() {
        adapter= FeedAdapter()
        _binding?.rv?.apply {
            adapter = this@SavedNewsFragment.adapter
            layoutManager = LinearLayoutManager(context)
        }
    }



}