package com.priyanshumaurya8868.newsapp.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.priyanshumaurya8868.newsapp.api.entity.Article
import com.bumptech.glide.Glide
import com.priyanshumaurya8868.newsapp.databinding.ItemArticlePreviewBinding
import com.priyanshumaurya8868.newsapp.util.loadImage
import com.priyanshumaurya8868.newsapp.util.timeStamp


class FeedAdapter : RecyclerView.Adapter<FeedAdapter.NewsVH>() {


    inner class NewsVH(val binding : ItemArticlePreviewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = NewsVH(
        ItemArticlePreviewBinding.inflate(LayoutInflater.from(parent.context))
    )

    //this could be in-build in ListAdapter
    val differ = AsyncListDiffer(this, object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article) = oldItem == newItem
        override fun areContentsTheSame(oldItem: Article, newItem: Article) =
            oldItem.toString() == newItem.toString()
    })

    override fun onBindViewHolder(holder: NewsVH, position: Int) {
        val article = differ.currentList.get(position)
        holder.binding.apply {
            article.urlToImage?.let { ivArticleImage.loadImage(it) }
            tvSource.text =article.description
            tvTitle.text = article.title
            tvDescription.text = article.source?.name
            tvPublishedAt.text = article.publishedAt.toString()

            root.setOnClickListener {

                onItemClickListener?.let { it(article) }
            }
        }
    }

    //this basically a type of variable i.e. type a fun(which can accept Article as a parameter and return nothing)
    private var onItemClickListener: ((Article) -> Unit)? = null

    fun setOnClickListener(listener: (Article) -> Unit) {
        onItemClickListener = listener
    }

    override fun getItemCount() = differ.currentList.size


}