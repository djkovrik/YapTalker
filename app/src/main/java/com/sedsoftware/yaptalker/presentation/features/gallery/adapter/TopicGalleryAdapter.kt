package com.sedsoftware.yaptalker.presentation.features.gallery.adapter

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.ViewGroup
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.presentation.extensions.hideView
import com.sedsoftware.yaptalker.presentation.extensions.inflate
import com.sedsoftware.yaptalker.presentation.extensions.loadFromUrl
import com.sedsoftware.yaptalker.presentation.extensions.showView
import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.sedsoftware.yaptalker.presentation.model.base.SinglePostGalleryImageModel
import kotlinx.android.synthetic.main.activity_topic_gallery_item.view.*
import kotlinx.android.synthetic.main.activity_topic_gallery_item_load_more.view.*
import java.util.ArrayList
import javax.inject.Inject

class TopicGalleryAdapter @Inject constructor(
  private val loadMoreCallback: TopicGalleryLoadMoreClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

  private var items: ArrayList<YapEntity> = ArrayList()

  init {
    setHasStableIds(true)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ImageViewHolder(parent)

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder as ImageViewHolder
    holder.bind(items[position] as SinglePostGalleryImageModel)
  }

  override fun getItemId(position: Int) = position.toLong()

  override fun getItemCount(): Int = items.size

  fun addList(images: List<YapEntity>) {
    val insertPosition = items.size
    items.addAll(images)
    notifyItemRangeInserted(insertPosition, items.size)
  }

  inner class ImageViewHolder(parent: ViewGroup) :
    RecyclerView.ViewHolder(parent.inflate(R.layout.activity_topic_gallery_item)) {

    fun bind(image: SinglePostGalleryImageModel) {

      itemView.load_more_button.hideView()
      itemView.gallery_image.loadFromUrl(image.url)

      if (image.showLoadMore) {
        itemView.load_more_button.showView()
        itemView.load_more_button.setOnClickListener {
          loadMoreCallback.onLoadMoreClicked()
          itemView.load_more_label.hideView()
          itemView.load_more_progress.showView()
        }
      }
    }
  }
}
