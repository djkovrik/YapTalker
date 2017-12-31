package com.sedsoftware.yaptalker.presentation.features.bookmarks.adapter

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.ViewGroup
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.data.settings.SettingsManager
import com.sedsoftware.yaptalker.presentation.base.adapter.YapEntityDelegateAdapter
import com.sedsoftware.yaptalker.presentation.extensions.inflate
import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.sedsoftware.yaptalker.presentation.model.base.BookmarkedTopicModel
import kotlinx.android.synthetic.main.fragment_bookmarks_item.view.*

class BookmarksDelegateAdapter(
    val clickListener: BookmarksElementsClickListener,
    val settings: SettingsManager) : YapEntityDelegateAdapter {

  private val normalFontSize by lazy {
    settings.getNormalFontSize()
  }

  override fun onCreateViewHolder(parent: ViewGroup): ViewHolder = BookmarkViewHolder(parent)

  override fun onBindViewHolder(holder: ViewHolder, item: YapEntity, position: Int) {
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
