package com.androiddevs.mvvmnewsapp.roomDB

import androidx.room.TypeConverter
import com.priyanshumaurya8868.newsapp.api.entity.Source


class Converter {
    @TypeConverter
    fun fromSource(source: Source):String{
        return source.name!!
    }
    @TypeConverter
    fun toSource(name : String): Source {
        return Source(name,name)
    }
}