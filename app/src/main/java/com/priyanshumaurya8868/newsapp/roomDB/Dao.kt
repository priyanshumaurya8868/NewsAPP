package com.priyanshumaurya8868.newsapp.roomDB

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao
import com.priyanshumaurya8868.newsapp.api.entity.Article

@Dao
interface Dao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun usert(article: Article)

    @Query("SELECT * FROM article")
   fun getArticles() : LiveData<List<Article>>?

    @Delete
    suspend fun deleteArticle(article: Article)

}