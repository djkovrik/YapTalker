package com.sedsoftware.yaptalker.features.news

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.YapTalkerApp
import com.sedsoftware.yaptalker.commons.extensions.loadFromUrl
import com.sedsoftware.yaptalker.commons.extensions.textFromHtml
import com.sedsoftware.yaptalker.data.model.NewsItem
import com.sedsoftware.yaptalker.data.model.NewsItemContent
import com.sedsoftware.yaptalker.data.remote.thumbnails.ThumbnailsLoader
import kotlinx.android.synthetic.main.controller_news_item.view.*
import java.util.ArrayList
import java.util.Locale
import javax.inject.Inject

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

  init {
    YapTalkerApp.appComponent.inject(this)
  }

  @Inject
  lateinit var thumbnailsLoader: ThumbnailsLoader

  private var news: ArrayList<NewsItem> = ArrayList()
  private var lastPosition = -1

  override fun getItemCount() = news.size

  override fun getItemId(position: Int) = news[position].topic.id.toLong()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.controller_news_item, parent,
        false)
    return NewsViewHolder(view)
  }

  override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
    holder.bindTo(news[position])

    val animation = AnimationUtils.loadAnimation(holder.itemView.context,
        if (position > lastPosition)
          R.anim.recyclerview_up_from_bottom
        else
          R.anim.recyclerview_down_from_top)

    holder.itemView.startAnimation(animation)
    lastPosition = holder.adapterPosition
  }

  override fun onViewDetachedFromWindow(holder: NewsViewHolder?) {
    super.onViewDetachedFromWindow(holder)
    holder?.itemView?.clearAnimation()
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

  fun getNews() = news

  inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val forumTitleTemplate: String = itemView.context.getString(R.string.news_forum_title_template)
    val karmaTemplate: String = itemView.context.getString(R.string.news_karma_template)
    val dateTemplate: String = itemView.context.getString(R.string.news_date_template)
    val commentsTemplate: String = itemView.context.getString(R.string.news_comments_template)

    fun bindTo(newsItem: NewsItem) {
      with(itemView) {
        news_author.text = newsItem.topic.author.name
        news_title.text = newsItem.topic.title
        news_forum.text = String.format(Locale.US, forumTitleTemplate, newsItem.forum)
        news_date.text = String.format(Locale.US, dateTemplate, newsItem.topic.date)
        news_rating.text = String.format(Locale.US, karmaTemplate, newsItem.topic.uq)
        news_comments_counter.text = String.format(Locale.US, commentsTemplate,
            newsItem.topic.answers)

        NewsItemContent(newsItem.summary).apply {

          news_content_text.textFromHtml(this.text)

          if (this.image.isNotEmpty()) {
            news_content_image.visibility = View.VISIBLE
            news_content_image.loadFromUrl("http:${this.image}")
          } else if (this.video.second.isNotEmpty()) {
            news_content_image.visibility = View.VISIBLE
            thumbnailsLoader.loadThumbnail(this.video, news_content_image)
          } else {
            news_content_image.visibility = View.GONE
          }
        }
      }
    }
  }
}
