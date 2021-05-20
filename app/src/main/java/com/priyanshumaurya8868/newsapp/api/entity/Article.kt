package com.priyanshumaurya8868.newsapp.api.entity


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(
    tableName = "article"
)

data class Article(
    @PrimaryKey(autoGenerate = true)
    val id : Int? =null,
    @SerializedName("author")
    val author: String?, // Redacción LA NACION
    @SerializedName("content")
    val content: String?, // Inventada en 1916 y patentada cuatro años más tarde, la denominada válvula de Nikola Tesla permitía hacer pasar el fluido en una dirección, sin utilizar partes móviles como otros dispositivo de su ép… [+1936 chars]
    @SerializedName("description")
    val description: String?, // A más de 100 años de su invención, el dispositivo de tiene usos impensados
    @SerializedName("publishedAt")
    val publishedAt: String?, // 2021-05-18T19:14:57Z
    @SerializedName("source")
    val source: Source?,
    @SerializedName("title")
    val title: String?, // Descubren un uso impensado para un invento “incomprendido” de Nikola Tesla
    @SerializedName("url")
    val url: String?, // https://www.lanacion.com.ar/ciencia/descubren-un-uso-impensado-para-un-invento-incomprendido-de-nikola-tesla-nid18052021/
    @SerializedName("urlToImage")
    val urlToImage: String? // https://resizer.glanacion.com/resizer/foAZSAT_I-rS9rfugG0wiTq8ctw=/768x0/filters:quality(80)/cloudfront-us-east-1.images.arcpublishing.com/lanacionar/QRKNUJELSRBTPEJABINRKLHJWY.jpg
):Serializable