package com.priyanshumaurya8868.newsapp.api

import com.priyanshumaurya8868.newsapp.api.Responses.NewsResponse
import com.priyanshumaurya8868.newsapp.util.Constants.API_KEY
import com.priyanshumaurya8868.newsapp.util.Constants.PAGE_SIZE
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("/v2/top-headlines")
    suspend fun getHeadLines(
        @Query("country") countryCode: String = "in",
        @Query("page") pages: Int = 1,
        @Query("apiKey") apiKey: String = API_KEY,
    ): Response<NewsResponse>

    @GET("/v2/everything")
    suspend fun searchFor(
        @Query("q") query: String,
        @Query("page") pages: Int = 1,
        @Query("apiKey") apiKey: String = API_KEY,
        @Query("sortBy") sortBy : String = "popularity",
    @Query("pageSize") pageSize : Int = PAGE_SIZE

    ): Response<NewsResponse>


}