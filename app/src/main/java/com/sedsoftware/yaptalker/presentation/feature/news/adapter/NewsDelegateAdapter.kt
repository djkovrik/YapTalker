package com.sedsoftware.yaptalker.presentation.feature.news.adapter

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.domain.device.Settings
import com.sedsoftware.yaptalker.presentation.base.adapter.YapEntityDelegateAdapter
import com.sedsoftware.yaptalker.presentation.extensions.inflate
import com.sedsoftware.yaptalker.presentation.extensions.loadFromUrlAndRoundCorners
import com.sedsoftware.yaptalker.presentation.model.DisplayedItemModel
import com.sedsoftware.yaptalker.presentation.model.base.NewsItemModel
import com.sedsoftware.yaptalker.presentation.thumbnail.ThumbnailsLoader
import kotlinx.android.synthetic.main.fragment_news_item.view.*

class NewsDelegateAdapter(
  private val clickListener: NewsItemElementsClickListener,
  private val thumbnailsLoader: ThumbnailsLoader,
  private val settings: Settings
) : YapEntityDelegateAdapter {

  private val normalFontSize by lazy {
    settings.getNormalFontSize()
  }

  private val bigFontSize by lazy {
    settings.getBigFontSize()
  }

  override fun onCreateViewHolder(parent: ViewGroup): ViewHolder = NewsViewHolder(parent)

  override fun onBindViewHolder(holder: ViewHolder, item: DisplayedItemModel) {
    holder as NewsViewHolder
    holder.bindTo(item as NewsItemModel)
  }

  inner class NewsViewHolder(parent: ViewGroup) :
    RecyclerView.ViewHolder(parent.inflate(R.layout.fragment_news_item)) {

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

      with(itemView) {
        news_author.text = newsItem.author
        news_title.text = newsItem.title
        news_forum.text = newsItem.forumName
        news_date.text = newsItem.date
        news_rating.text = newsItem.rating
        news_comments_counter.text = newsItem.comments
        news_content_text.text = newsItem.cleanedDescription
      }
    }

    private fun setMediaContent(itemView: View, newsItem: NewsItemModel) {

      with(itemView) {

        news_content_image.setOnClickListener(null)
        news_content_image.setImageDrawable(null)
        news_content_image_container.isGone = true
        news_content_image_overlay.isGone = true

        if (newsItem.images.isNotEmpty()) {
          val url = newsItem.images.first()
          news_content_image.loadFromUrlAndRoundCorners(url)
          news_content_image_container.isVisible = true
          news_content_image.setOnClickListener { clickListener.onMediaPreviewClicked(url, "", false) }
        } else if (newsItem.videos.isNotEmpty() && newsItem.videosRaw.isNotEmpty()) {
          val url = newsItem.videos.first()
          val rawVideo = newsItem.videosRaw.first()
          thumbnailsLoader.loadThumbnail(url, news_content_image)
          news_content_image_container.isVisible = true
          news_content_image_overlay.isVisible = true
          news_content_image.setOnClickListener { clickListener.onMediaPreviewClicked(url, rawVideo, true) }
        }

        setOnClickListener { clickListener.onNewsItemClicked(newsItem.forumId, newsItem.topicId) }

        setOnLongClickListener {
          clickListener.onNewsItemLongClicked(newsItem)
          true
        }
      }
    }
  }
}
