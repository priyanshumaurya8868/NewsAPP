package com.priyanshumaurya8868.newsapp.roomDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.androiddevs.mvvmnewsapp.roomDB.Converter
import com.priyanshumaurya8868.newsapp.api.entity.Article

@Database(
    entities = [Article::class],
version = 1
)
@TypeConverters(Converter::class)
abstract class ArticleDataBase :RoomDatabase(){
    abstract  fun getDao() : Dao //this function will  automatically handled by room db BTS

    companion object{
        @Volatile   //volatile so that other thread can sense it whenever its value change it
        private var instance : ArticleDataBase? = null

        private val  lock = Any()
   //this invoke() will call itself whenever ArticleRoomDataBase() call
        operator fun invoke(context: Context) = instance ?: synchronized(lock){
            instance ?: createDataBase(context).also { instance = it }
        }

        private fun createDataBase(context: Context)= Room.databaseBuilder(
                context.applicationContext,
              ArticleDataBase::class.java,
            "article_db.db"
        ).build()
    }
}