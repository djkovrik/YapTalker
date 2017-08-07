package com.sedsoftware.yaptalker.features.news

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.data.NewsItem
import kotlinx.android.synthetic.main.controller_news_item.view.*
import java.util.*

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
    notifyItemRangeRemoved(0, news.size)
    news.clear()
    addNews(list)
  }

  class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val headerTemplate: String = itemView.context.getString(R.string.news_header_info_template)
    val karmaTemplate: String = itemView.context.getString(R.string.news_karma_template)

    fun bindTo(newsItem: NewsItem) {
      with(newsItem) {
        itemView.news_info_text.text = String.format(Locale.US, headerTemplate, topic.author.name, forum)
        itemView.news_date.text = topic.date
        itemView.news_rating.text = String.format(Locale.US, karmaTemplate, topic.uq)
        itemView.news_title.text = topic.title
        itemView.news_content.loadDataWithBaseURL("http://www.yaplakal.com/", summary, "text/html; charset=UTF-8", null, null)
      }
    }
  }
}