package com.sedsoftware.yaptalker.features.news.adapter

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.View
import android.view.ViewGroup
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.base.BaseAdapterInjections
import com.sedsoftware.yaptalker.commons.adapter.ViewType
import com.sedsoftware.yaptalker.commons.adapter.ViewTypeDelegateAdapter
import com.sedsoftware.yaptalker.commons.extensions.hideView
import com.sedsoftware.yaptalker.commons.extensions.inflate
import com.sedsoftware.yaptalker.commons.extensions.loadFromUrl
import com.sedsoftware.yaptalker.commons.extensions.showView
import com.sedsoftware.yaptalker.commons.extensions.stringQuantityRes
import com.sedsoftware.yaptalker.commons.extensions.stringRes
import com.sedsoftware.yaptalker.commons.extensions.textFromHtml
import com.sedsoftware.yaptalker.data.parsing.NewsItem
import com.sedsoftware.yaptalker.data.video.getYoutubeVideoId
import com.sedsoftware.yaptalker.data.video.parseLink
import com.sedsoftware.yaptalker.features.imagedisplay.ImageDisplayActivity
import com.sedsoftware.yaptalker.features.videodisplay.VideoDisplayActivity
import kotlinx.android.synthetic.main.fragment_news_item.view.*
import org.jetbrains.anko.browse
import org.jetbrains.anko.startActivity
import java.util.Locale

class NewsDelegateAdapter(val newsClick: NewsItemClickListener) :
    BaseAdapterInjections(), ViewTypeDelegateAdapter {

  override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
    return NewsViewHolder(parent)
  }

  override fun onBindViewHolder(holder: ViewHolder, item: ViewType) {
    holder as NewsViewHolder
    holder.bindTo(item as NewsItem)
  }

  inner class NewsViewHolder(parent: ViewGroup) :
      RecyclerView.ViewHolder(parent.inflate(R.layout.fragment_news_item)) {

    private val forumTitleTemplate: String = parent.context.stringRes(R.string.news_forum_title_template)

    fun bindTo(newsItem: NewsItem) {
      setViewsTextSize(itemView)
      setViewsContent(itemView, newsItem)
      setMediaContent(itemView, newsItem)
    }

    private fun setViewsTextSize(itemView: View) {
      with(itemView) {
        news_author.textSize = normalFontSize
        news_title.textSize = bigFontSize
        news_forum.textSize = normalFontSize
        news_date.textSize = normalFontSize
        news_rating.textSize = normalFontSize
        news_comments_counter.textSize = normalFontSize
        news_content_text.textSize = normalFontSize
      }
    }

    private fun setViewsContent(itemView: View, newsItem: NewsItem) {

      val commentsTemplate: String = itemView.context.stringQuantityRes(
          R.plurals.news_comments_template, newsItem.comments.toInt())

      with(itemView) {
        news_author.text = newsItem.author
        news_title.text = newsItem.title
        news_forum.text = String.format(Locale.getDefault(), forumTitleTemplate, newsItem.forumName)
        news_date.shortDateText = newsItem.date
        news_rating.ratingText = newsItem.rating
        news_comments_counter.text = String.format(Locale.getDefault(), commentsTemplate, newsItem.comments)
        news_content_text.textFromHtml(newsItem.cleanedDescription)
      }
    }

    private fun setMediaContent(itemView: View, newsItem: NewsItem) {
      with(itemView) {

        news_content_image.setOnClickListener(null)

        when {
          newsItem.images.isNotEmpty() -> {
            val url = newsItem.images.first()
            news_content_image.showView()
            news_content_image.loadFromUrl(url)
            news_content_image.setOnClickListener {
              context.startActivity<ImageDisplayActivity>("url" to url)
            }
          }
          newsItem.videos.isNotEmpty() && newsItem.videosRaw.isNotEmpty() -> {
            val url = newsItem.videos.first()
            val rawHtml = newsItem.videosRaw.first()
            news_content_image.showView()
            thumbnailsLoader.loadThumbnail(video = parseLink(url), imageView = news_content_image)

            news_content_image.setOnClickListener {
              if (url.contains("youtube")) {
                context.browse("http://www.youtube.com/watch?v=${getYoutubeVideoId(url)}")
              } else {
                context.startActivity<VideoDisplayActivity>("videoHtml" to rawHtml)
              }
            }
          }
          else -> news_content_image.hideView()
        }

        setOnClickListener { newsClick.onNewsItemClick(newsItem.link, newsItem.forumLink) }
      }
    }
  }
}
