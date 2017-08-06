package com.sedsoftware.yaptalker.features.news

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.data.NewsItem
import kotlinx.android.synthetic.main.controller_news_item.view.*

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

  private var news: ArrayList<NewsItem> = ArrayList()

  override fun getItemCount() = news.size

  override fun getItemId(position: Int) = news[position].topic.id.toLong()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.controller_news_item, parent,
        false)
    return NewsViewHolder(view)
  }

  override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
    holder.bindTo(news[position])
  }

  fun addNews(list: List<NewsItem>) {
    val insertPosition = news.size
    news.addAll(insertPosition, list)
    notifyItemRangeInserted(insertPosition, news.size)
  }

  fun clearAndAddNews(list: List<NewsItem>) {
    news.clear()
    addNews(list)
  }

  class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bindTo(newsItem: NewsItem) {
      with(newsItem) {
        itemView.news_title.text = topic.title
      }
    }
  }
}