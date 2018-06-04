package com.sedsoftware.yaptalker.presentation.feature.gallery.adapter

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.presentation.extensions.inflate
import com.sedsoftware.yaptalker.presentation.extensions.loadFromUrlWithGifSupport
import com.sedsoftware.yaptalker.presentation.model.base.SinglePostGalleryImageModel
import kotlinx.android.synthetic.main.activity_topic_gallery_item.view.gallery_image
import kotlinx.android.synthetic.main.activity_topic_gallery_item_load_more.view.load_more_button
import kotlinx.android.synthetic.main.activity_topic_gallery_item_load_more.view.load_more_label
import kotlinx.android.synthetic.main.activity_topic_gallery_item_load_more.view.load_more_progress
import java.util.ArrayList
import javax.inject.Inject

class TopicGalleryAdapter @Inject constructor(
  private val loadMoreCallback: TopicGalleryLoadMoreClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

  var items: MutableList<SinglePostGalleryImageModel> = ArrayList()
  var isLastPageVisible = false

  init {
    setHasStableIds(true)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ImageViewHolder(parent)

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder as ImageViewHolder
    holder.bind(items[position])
  }

  override fun getItemId(position: Int) = position.toLong()

  override fun getItemCount(): Int = items.size

  fun addList(images: List<SinglePostGalleryImageModel>) {
    if (images.isNotEmpty()) {
      clearLoadingIndicators()
      items.addAll(images)
    }

    showLastLoadingIndicator()
    notifyDataSetChanged()
  }

  private fun clearLoadingIndicators() {
    items = items
      .map { item -> SinglePostGalleryImageModel(url = item.url, showLoadMore = false) }
      .toMutableList()
  }

  private fun showLastLoadingIndicator() {
    clearLoadingIndicators()
    if (items.isNotEmpty()) {
      val newLast = SinglePostGalleryImageModel(url = items.last().url, showLoadMore = true)
      items.removeAt(items.lastIndex)
      items.add(newLast)
    }
  }

  inner class ImageViewHolder(parent: ViewGroup) :
    RecyclerView.ViewHolder(parent.inflate(R.layout.activity_topic_gallery_item)) {

    fun bind(image: SinglePostGalleryImageModel) {
      with(itemView) {
        load_more_button.isInvisible = true
        load_more_label.isInvisible = true
        load_more_progress.isInvisible = true
        gallery_image.loadFromUrlWithGifSupport(image.url)

        if (image.showLoadMore && !isLastPageVisible) {
          load_more_button.isVisible = true
          load_more_label.isVisible = true
          load_more_progress.isInvisible = true
          load_more_button.setOnClickListener {
            loadMoreCallback.onLoadMoreClicked()
            load_more_button.isVisible = true
            load_more_label.isInvisible = true
            load_more_progress.isVisible = true
          }
        }
      }
    }
  }
}
