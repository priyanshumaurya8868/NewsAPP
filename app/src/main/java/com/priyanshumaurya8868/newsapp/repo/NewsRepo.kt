package com.priyanshumaurya8868.newsapp.repo

import com.priyanshumaurya8868.newsapp.api.RetrofitInstance
import com.priyanshumaurya8868.newsapp.api.entity.Article
import com.priyanshumaurya8868.newsapp.roomDB.ArticleDataBase

class NewsRepo(
    val  db : ArticleDataBase
){
    suspend fun getHeadLines(countryCode : String,pages : Int) =
        RetrofitInstance.api.getHeadLines(countryCode, pages)

    suspend fun getSearchedNews(query : String,pages : Int) =
        RetrofitInstance.api.searchFor(query, pages)

    suspend fun upsert(article : Article) = db.getDao().usert(article)

    suspend fun deleteArticle(article: Article) = db.getDao().deleteArticle(article)

    fun getSavedArticle() = db.getDao().getArticles()

}