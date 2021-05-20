package com.priyanshumaurya8868.newsapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.priyanshumaurya8868.newsapp.repo.NewsRepo
import com.priyanshumaurya8868.newsapp.databinding.ActivityMainBinding
import com.priyanshumaurya8868.newsapp.roomDB.ArticleDataBase
import com.priyanshumaurya8868.newsapp.ui.viewModel.NewsViewModel
import com.priyanshumaurya8868.newsapp.ui.viewModel.NewsViewModelFactory


class MainActivity : AppCompatActivity() {
    lateinit var viewModel: NewsViewModel
    private var _binding: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding?.root)
        viewModel =
            ViewModelProvider(
                this, NewsViewModelFactory(
                    application,
                    NewsRepo(ArticleDataBase(this))
                )
            ).get(
                NewsViewModel::class.java
            )

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        val navController = navHostFragment.navController
        _binding?.bottomNavigationView?.setupWithNavController(navController)
       //make sure  that menu.xml have items with same id(s) as fragments in navGraph

    }
}
