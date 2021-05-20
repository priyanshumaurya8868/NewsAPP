package com.priyanshumaurya8868.newsapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.priyanshumaurya8868.newsapp.MainActivity
import com.priyanshumaurya8868.newsapp.databinding.FragmentArticleBinding
import com.priyanshumaurya8868.newsapp.ui.viewModel.NewsViewModel

class ArticleFragment : Fragment() {
    private val args: ArticleFragmentArgs by navArgs()
    private lateinit var viewModel: NewsViewModel

    //to use this  1->add id 'androidx.navigation.safeargs' 2 -> dependencies in our gradle's buildScrip
    private var _binding: FragmentArticleBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = (activity as MainActivity).viewModel
        _binding = FragmentArticleBinding.inflate(inflater, container, false)
        return _binding?.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val article = args.article
        _binding?.webView?.apply {
            webViewClient = WebViewClient()
            article.url?.let { loadUrl(it) }
        }
        _binding?.fab?.setOnClickListener {
            viewModel.upsert(article)
            Snackbar.make(view, "Article saved Successfully...!", Snackbar.LENGTH_SHORT).show()
        }

    }
}