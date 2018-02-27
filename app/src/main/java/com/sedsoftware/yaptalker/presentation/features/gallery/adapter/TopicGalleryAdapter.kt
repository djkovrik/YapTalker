package com.sedsoftware.yaptalker.presentation.features.gallery.adapter

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.ViewGroup
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.presentation.extensions.inflate
import com.sedsoftware.yaptalker.presentation.extensions.loadFromUrl
import kotlinx.android.synthetic.main.activity_topic_gallery_item.view.gallery_image
import java.util.ArrayList
import javax.inject.Inject

class TopicGalleryAdapter @Inject constructor() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

  private var items: ArrayList<String> = ArrayList()

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

  fun setList(images: List<String>) {
    items.clear()
    items.addAll(images)
    notifyDataSetChanged()
  }

  inner class ImageViewHolder(parent: ViewGroup) :
    RecyclerView.ViewHolder(parent.inflate(R.layout.activity_topic_gallery_item)) {

    fun bind(url: String) {
      itemView.gallery_image.loadFromUrl(url)
    }
  }
}
