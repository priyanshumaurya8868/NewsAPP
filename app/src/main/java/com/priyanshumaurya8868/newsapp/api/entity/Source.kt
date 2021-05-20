package com.priyanshumaurya8868.newsapp.api.entity


import com.google.gson.annotations.SerializedName

data class Source(
    @SerializedName("id")
    val id: String?, // la-nacion
    @SerializedName("name")
    val name: String? // La Nacion
)