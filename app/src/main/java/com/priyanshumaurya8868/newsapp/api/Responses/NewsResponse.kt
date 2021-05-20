package com.priyanshumaurya8868.newsapp.api.Responses


import com.priyanshumaurya8868.newsapp.api.entity.Article
import com.google.gson.annotations.SerializedName

data class NewsResponse(
    @SerializedName("articles")
    val articles: MutableList<Article>?,
    @SerializedName("status")
    val status: String?, // ok
    @SerializedName("totalResults")
    val totalResults: Int? // 19084
)