package com.priyanshumaurya8868.newsapp.ui.viewModel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.*
import android.os.Build
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.priyanshumaurya8868.newsapp.NewsApplication
import com.priyanshumaurya8868.newsapp.api.Responses.NewsResponse
import com.priyanshumaurya8868.newsapp.api.entity.Article
import com.priyanshumaurya8868.newsapp.repo.NewsRepo
import com.priyanshumaurya8868.newsapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(
    app: Application,
    private val newsRepo : NewsRepo
) : AndroidViewModel(app) {

    val  breakingNews = MutableLiveData<Resource<NewsResponse>?>()
    var breakingNewsPages = 1
    private var breakingNewsResponse : NewsResponse? = null

    val  searchedNews = MutableLiveData<Resource<NewsResponse>?>()
     var searchedNewsPages = 1
     var searchedNewsResponse : NewsResponse? = null

    init {
        getBreakingNews("in")
    }

    fun getBreakingNews(countryCode : String) = viewModelScope.launch {
        safeBreakingNewsCall(countryCode)
        }


    fun  getSearchedNews(query :String)=viewModelScope.launch{
       safeSearchedNewsCall(query)
    }

    private suspend fun safeBreakingNewsCall(countryCode: String){
        breakingNews.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()){
                Log.d("breakingNews","internet connection ok")
                val response = newsRepo.getHeadLines(countryCode, breakingNewsPages)
                breakingNews.postValue(handleBreakingNewsResponse(response))
            }else{
                breakingNews.postValue(Resource.Error(msg="No internet connection...!"))
            }

        }catch (e : Exception){
            breakingNews.postValue(Resource.Error(msg=e.message.toString()))
        }
    }

    private suspend fun safeSearchedNewsCall(query: String){
        breakingNews.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()){
                val response = newsRepo.getSearchedNews(query, searchedNewsPages)
                searchedNews.postValue(handleSearchedNewsResponse(response))
            }else{
                searchedNews.postValue(Resource.Error(msg="No internet connection...!"))
            }

        }catch (e : Exception){
            searchedNews.postValue(Resource.Error(msg=e.message.toString()))
        }
    }

    private fun handleBreakingNewsResponse(resultResponse : Response<NewsResponse>) : Resource<NewsResponse> {
        if (resultResponse.isSuccessful){
            resultResponse.body()?.let {
                breakingNewsPages++
                //for 1st iteration
                if(breakingNewsResponse == null){
                    breakingNewsResponse = it
                }else
                {
                    val  oldArticle = breakingNewsResponse?.articles
                    val newArticle = it.articles
                    newArticle?.let{ oldArticle?.addAll(newArticle) }
                }
                return Resource.Success(breakingNewsResponse?: it)
            }
        }
        return  Resource.Error(msg = resultResponse.message())

    }

    private fun handleSearchedNewsResponse(response : Response<NewsResponse>) : Resource<NewsResponse> {
        if (response.isSuccessful){
            response.body()?.let {
               searchedNewsPages++
                //for 1st iteration
                if(searchedNewsResponse == null){
                    searchedNewsResponse = it
                }else
                {
                    val  oldArticle = searchedNewsResponse?.articles
                    val newArticle = it.articles
                    newArticle?.let{ oldArticle?.addAll(newArticle) }
                }
                return Resource.Success(searchedNewsResponse?:it)
            }
        }
        return  Resource.Error(msg = response.message())

    }

    fun getSavedArticle()= newsRepo.getSavedArticle()

    fun upsert(article : Article)= viewModelScope.launch {
        newsRepo.upsert(article)
    }
    fun deleteArticle(article : Article) = viewModelScope.launch {
        newsRepo.deleteArticle(article)
    }

    private fun hasInternetConnection():Boolean{
        val connectivityManager = getApplication<NewsApplication>().
        getSystemService(Context.CONNECTIVITY_SERVICE)as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capability  = connectivityManager.getNetworkCapabilities(activeNetwork)?:return false
            return when{
                capability.hasTransport(TRANSPORT_WIFI) -> true
                capability.hasTransport(TRANSPORT_CELLULAR)->true
                capability.hasTransport(TRANSPORT_ETHERNET)->true
                else -> false
            }
        }else {
            connectivityManager.activeNetworkInfo?.run {
                return when(type){
                    TRANSPORT_WIFI -> true
                    TRANSPORT_CELLULAR->true
                    TRANSPORT_ETHERNET->true
                    else -> false
                }
            }
        }
        return false

    }


}