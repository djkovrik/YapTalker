package com.sedsoftware.yaptalker.presentation.features.incubator.adapter

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.View
import android.view.ViewGroup
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.device.settings.SettingsManager
import com.sedsoftware.yaptalker.presentation.base.adapter.YapEntityDelegateAdapter
import com.sedsoftware.yaptalker.presentation.extensions.hideView
import com.sedsoftware.yaptalker.presentation.extensions.inflate
import com.sedsoftware.yaptalker.presentation.extensions.loadFromUrl
import com.sedsoftware.yaptalker.presentation.extensions.showView
import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.sedsoftware.yaptalker.presentation.model.base.IncubatorItemModel
import kotlinx.android.synthetic.main.fragment_incubator_item.view.*

class IncubatorDelegateAdapter(
    private val clickListener: IncubatorElementsClickListener,
    private val thumbnailsLoader: IncubatorThumbnailsLoader,
    private val settings: SettingsManager
) : YapEntityDelegateAdapter {

  private val normalFontSize by lazy {
    settings.getNormalFontSize()
  }

  private val bigFontSize by lazy {
    settings.getBigFontSize()
  }

  override fun onCreateViewHolder(parent: ViewGroup): ViewHolder = IncubatorViewHolder(parent)

  override fun onBindViewHolder(holder: ViewHolder, item: YapEntity) {
    holder as IncubatorViewHolder
    holder.bindTo(item as IncubatorItemModel)
  }

  inner class IncubatorViewHolder(parent: ViewGroup) :
      RecyclerView.ViewHolder(parent.inflate(R.layout.fragment_incubator_item)) {

    fun bindTo(incubatorItem: IncubatorItemModel) {
      setViewsTextSize(itemView)
      setViewsContent(itemView, incubatorItem)
      setMediaContent(itemView, incubatorItem)
    }

    private fun setViewsTextSize(itemView: View) {

      with(itemView) {
        incubator_topic_author.textSize = normalFontSize
        incubator_topic_title.textSize = bigFontSize
        incubator_topic_forum.textSize = normalFontSize
        incubator_topic_date.textSize = normalFontSize
        incubator_topic_rating.textSize = normalFontSize
        incubator_topic_comments_counter.textSize = normalFontSize
        incubator_topic_content_text.textSize = normalFontSize
      }
    }

    private fun setViewsContent(itemView: View, incubatorItem: IncubatorItemModel) {

      with(itemView) {
        incubator_topic_author.text = incubatorItem.author
        incubator_topic_title.text = incubatorItem.title
        incubator_topic_forum.text = incubatorItem.forumName
        incubator_topic_date.text = incubatorItem.date
        incubator_topic_rating.text = incubatorItem.rating
        incubator_topic_comments_counter.text = incubatorItem.comments
        incubator_topic_content_text.text = incubatorItem.cleanedDescription
      }
    }

    private fun setMediaContent(itemView: View, incubatorItem: IncubatorItemModel) {

      with(itemView) {
        incubator_topic_content_image.setOnClickListener(null)
        incubator_topic_content_image.setImageDrawable(null)
        incubator_topic_content_image.hideView()

        if (incubatorItem.images.isNotEmpty()) {
          val url = incubatorItem.images.first()
          incubator_topic_content_image.loadFromUrl(url)
          incubator_topic_content_image.showView()

          incubator_topic_content_image.setOnClickListener {
            clickListener.onMediaPreviewClicked(url, "", false)
          }
        } else if (incubatorItem.videos.isNotEmpty() && incubatorItem.videosRaw.isNotEmpty()) {
          val url = incubatorItem.videos.first()
          val rawVideo = incubatorItem.videosRaw.first()
          thumbnailsLoader.loadThumbnail(url, incubator_topic_content_image)
          incubator_topic_content_image.showView()

          incubator_topic_content_image.setOnClickListener {
            clickListener.onMediaPreviewClicked(url, rawVideo, true)
          }
        }

        setOnClickListener {
          if (incubatorItem.isYapLink) {
            clickListener.onIncubatorItemClick(incubatorItem.forumId, incubatorItem.topicId)
          }
        }
      }
    }
  }
}
