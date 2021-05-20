package com.priyanshumaurya8868.newsapp.ui.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.priyanshumaurya8868.newsapp.repo.NewsRepo


class NewsViewModelFactory(
   private val app: Application,
    private val newsRepo : NewsRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NewsViewModel(app,newsRepo) as T
    }
}