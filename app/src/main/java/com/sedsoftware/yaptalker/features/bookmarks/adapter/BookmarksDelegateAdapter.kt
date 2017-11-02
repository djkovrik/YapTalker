package com.sedsoftware.yaptalker.features.bookmarks.adapter

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.ViewGroup
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.base.BaseAdapterInjections
import com.sedsoftware.yaptalker.commons.adapter.ViewType
import com.sedsoftware.yaptalker.commons.adapter.ViewTypeDelegateAdapter
import com.sedsoftware.yaptalker.commons.extensions.inflate
import com.sedsoftware.yaptalker.data.parsing.BookmarkedTopic
import kotlinx.android.synthetic.main.fragment_bookmarks_item.view.*

class BookmarksDelegateAdapter(
    val itemClick: BookmarksItemClickListener,
    val deleteClick: BookmarksDeleteClickListener) :
    BaseAdapterInjections(), ViewTypeDelegateAdapter {

  override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
    return BookmarkViewHolder(parent)
  }

  override fun onBindViewHolder(holder: ViewHolder, item: ViewType) {
    holder as BookmarkViewHolder
    holder.bindTo(item as BookmarkedTopic)
  }

  inner class BookmarkViewHolder(parent: ViewGroup) :
      RecyclerView.ViewHolder(parent.inflate(R.layout.fragment_bookmarks_item)) {

    fun bindTo(topicItem: BookmarkedTopic) {
      with(itemView) {
        bookmark_title.text = topicItem.title
        bookmark_delete_icon.setOnClickListener { deleteClick.onDeleteIconClick(topicItem.bookmarkId) }
        setOnClickListener { itemClick.onTopicClick(topicItem.link) }
      }
    }
  }
}
