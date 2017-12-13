package com.sedsoftware.yaptalker.presentation.features.news.adapter

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.View
import android.view.ViewGroup
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.data.SettingsManager
import com.sedsoftware.yaptalker.presentation.base.adapter.YapEntityDelegateAdapter
import com.sedsoftware.yaptalker.presentation.commons.extensions.hideView
import com.sedsoftware.yaptalker.presentation.commons.extensions.inflate
import com.sedsoftware.yaptalker.presentation.commons.extensions.loadFromUrl
import com.sedsoftware.yaptalker.presentation.commons.extensions.showView
import com.sedsoftware.yaptalker.presentation.commons.extensions.stringQuantityRes
import com.sedsoftware.yaptalker.presentation.commons.extensions.stringRes
import com.sedsoftware.yaptalker.presentation.commons.extensions.textFromHtml
import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.sedsoftware.yaptalker.presentation.model.base.NewsItemModel
import kotlinx.android.synthetic.main.fragment_news_item.view.news_author
import kotlinx.android.synthetic.main.fragment_news_item.view.news_comments_counter
import kotlinx.android.synthetic.main.fragment_news_item.view.news_content_image
import kotlinx.android.synthetic.main.fragment_news_item.view.news_content_text
import kotlinx.android.synthetic.main.fragment_news_item.view.news_date
import kotlinx.android.synthetic.main.fragment_news_item.view.news_forum
import kotlinx.android.synthetic.main.fragment_news_item.view.news_rating
import kotlinx.android.synthetic.main.fragment_news_item.view.news_title
import java.util.Locale

class NewsDelegateAdapter(
    private val newsClick: NewsItemClickListener,
    private val settings: SettingsManager) : YapEntityDelegateAdapter {

  private val normalFontSize by lazy {
    settings.getNormalFontSize()
  }

  private val bigFontSize by lazy {
    settings.getBigFontSize()
  }

  override fun onCreateViewHolder(parent: ViewGroup): ViewHolder = NewsViewHolder(parent)

  override fun onBindViewHolder(holder: ViewHolder, item: YapEntity) {
    holder as NewsViewHolder
    holder.bindTo(item as NewsItemModel)
  }

  inner class NewsViewHolder(parent: ViewGroup) :
      RecyclerView.ViewHolder(parent.inflate(R.layout.fragment_news_item)) {

    private val forumTitleTemplate: String = parent.context.stringRes(R.string.news_forum_title_template)

    fun bindTo(newsItem: NewsItemModel) {
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

    private fun setViewsContent(itemView: View, newsItem: NewsItemModel) {

      val commentsTemplate: String = itemView.context.stringQuantityRes(
          R.plurals.news_comments_template, newsItem.comments)

      with(itemView) {
        news_author.text = newsItem.author
        news_title.text = newsItem.title
        news_forum.text = String.format(Locale.getDefault(), forumTitleTemplate, newsItem.forumName)
        news_date.shortDateText = newsItem.date
        news_rating.ratingText = newsItem.rating.toString()
        news_comments_counter.text = String.format(Locale.getDefault(), commentsTemplate, newsItem.comments)
        news_content_text.textFromHtml(newsItem.cleanedDescription)
      }
    }

    private fun setMediaContent(itemView: View, newsItem: NewsItemModel) {
      with(itemView) {

        news_content_image.setOnClickListener(null)

        when {
          newsItem.images.isNotEmpty() -> {
            val url = newsItem.images.first()
            news_content_image.showView()
            news_content_image.loadFromUrl(url)
            news_content_image.setOnClickListener {
              // TODO() Navigate to image view activity
            }
          }
          newsItem.videos.isNotEmpty() && newsItem.videosRaw.isNotEmpty() -> {
            val url = newsItem.videos.first()
            val rawHtml = newsItem.videosRaw.first()
            news_content_image.showView()
            // TODO () Load thumbnail
            // thumbnailsLoader.loadThumbnail(video = parseLink(url), imageView = news_content_image)

            // TODO() Navigate to video view activity
//            news_content_image.setOnClickListener {
//              if (url.contains("youtube")) {
//                context.browse("http://www.youtube.com/watch?v=${getYoutubeVideoId(url)}")
//              } else {
//
//              }
//            }
          }
          else -> news_content_image.hideView()
        }

        setOnClickListener { newsClick.onNewsItemClick(newsItem.link, newsItem.forumLink) }
      }
    }
  }
}
