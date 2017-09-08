package com.sedsoftware.yaptalker.features.news

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.commons.extensions.getLastDigits
import com.sedsoftware.yaptalker.commons.extensions.getShortTime
import com.sedsoftware.yaptalker.commons.extensions.hideView
import com.sedsoftware.yaptalker.commons.extensions.loadFromUrl
import com.sedsoftware.yaptalker.commons.extensions.showView
import com.sedsoftware.yaptalker.commons.extensions.textFromHtml
import com.sedsoftware.yaptalker.data.model.NewsItem
import com.sedsoftware.yaptalker.features.imagedisplay.ImageDisplayActivity
import kotlinx.android.synthetic.main.controller_news_item.view.*
import org.jetbrains.anko.startActivity
import java.util.ArrayList
import java.util.Locale

class NewsAdapter(
    val itemClick: (String, String) -> Unit) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

//  @Inject
//  lateinit var thumbnailsLoader: ThumbnailsLoader

  private var news: ArrayList<NewsItem> = ArrayList()
  private var lastPosition = -1

  override fun getItemCount() = news.size

  override fun getItemId(position: Int) = news[position].link.getLastDigits().toLong()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.controller_news_item, parent,
        false)
    return NewsViewHolder(view, itemClick)
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

  fun addNewsItem(item: NewsItem) {
    val insertPosition = news.size
    news.add(item)
    notifyItemInserted(insertPosition)
  }

  fun clearNews() {
    notifyItemRangeRemoved(0, news.size)
    news.clear()
  }

  inner class NewsViewHolder(
      itemView: View, val itemClick: (String, String) -> Unit) : RecyclerView.ViewHolder(itemView) {

    private val forumTitleTemplate: String = itemView.context.getString(
        R.string.news_forum_title_template)
    private val karmaTemplate: String = itemView.context.getString(R.string.news_karma_template)
    private val commentsTemplate: String = itemView.context.getString(
        R.string.news_comments_template)

    fun bindTo(newsItem: NewsItem) {
      with(newsItem) {
        with(itemView) {
          news_author.text = author
          news_title.text = title
          news_forum.text = String.format(Locale.US, forumTitleTemplate, forumName)
          news_date.text = context.getShortTime(date)

          if (rating.isNotEmpty()) {
            news_rating.text = String.format(Locale.US, karmaTemplate, rating)
          }

          news_comments_counter.text = String.format(Locale.US, commentsTemplate, comments)
          news_content_text.textFromHtml(cleanedDescription)

          // Remove listener before setting the new one
          news_content_image.setOnClickListener(null)

          when {
            images.isNotEmpty() -> {
              var url = images.first()
              // TODO() Refactor this to handle http as well as https
              if (!url.startsWith("http:"))
                url = "http:$url"
              news_content_image.showView()
              news_content_image.loadFromUrl(url)
              news_content_image.setOnClickListener {
                context.startActivity<ImageDisplayActivity>("url" to url)
              }
            }
//            videos.isNotEmpty() -> {
//              news_content_image.showView()
//              thumbnailsLoader.loadThumbnail(parseLink(videos.first()), news_content_image)
//              news_content_image.setOnClickListener {
//                context.startActivity<VideoDisplayActivity>("video" to videosRaw.first())
//              }
//            }
            else -> news_content_image.hideView()
          }

          setOnClickListener { itemClick(link, forumLink) }
        }
      }
    }
  }
}

