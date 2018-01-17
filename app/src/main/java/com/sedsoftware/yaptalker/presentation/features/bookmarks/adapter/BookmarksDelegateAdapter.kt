package com.sedsoftware.yaptalker.presentation.features.bookmarks.adapter

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.ViewGroup
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.domain.device.Settings
import com.sedsoftware.yaptalker.presentation.base.adapter.YapEntityDelegateAdapter
import com.sedsoftware.yaptalker.presentation.extensions.inflate
import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.sedsoftware.yaptalker.presentation.model.base.BookmarkedTopicModel
import kotlinx.android.synthetic.main.fragment_bookmarks_item.view.bookmark_delete_icon
import kotlinx.android.synthetic.main.fragment_bookmarks_item.view.bookmark_title

class BookmarksDelegateAdapter(
  private val clickListener: BookmarksElementsClickListener,
  private val settings: Settings
) : YapEntityDelegateAdapter {

  private val normalFontSize by lazy {
    settings.getNormalFontSize()
  }

  override fun onCreateViewHolder(parent: ViewGroup): ViewHolder = BookmarkViewHolder(parent)

  override fun onBindViewHolder(holder: ViewHolder, item: YapEntity) {
    holder as BookmarkViewHolder
    holder.bindTo(item as BookmarkedTopicModel)
  }

  inner class BookmarkViewHolder(parent: ViewGroup) :
    RecyclerView.ViewHolder(parent.inflate(R.layout.fragment_bookmarks_item)) {

    fun bindTo(topicItem: BookmarkedTopicModel) {
      with(itemView) {
        bookmark_title.text = topicItem.title
        bookmark_title.textSize = normalFontSize
        bookmark_delete_icon.setOnClickListener { clickListener.onDeleteIconClick(topicItem.bookmarkId) }
        setOnClickListener { clickListener.onTopicClick(topicItem.link) }
      }
    }
  }
}
