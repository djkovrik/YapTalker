package com.sedsoftware.yaptalker.presentation.features.bookmarks.adapter

import android.support.v4.util.SparseArrayCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.ViewGroup
import com.sedsoftware.yaptalker.data.settings.SettingsManager
import com.sedsoftware.yaptalker.presentation.base.adapter.YapEntityDelegateAdapter
import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.sedsoftware.yaptalker.presentation.model.YapEntityTypes

class BookmarksAdapter(
    clickListener: BookmarksElementsClickListener,
    settings: SettingsManager
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

  private var items: ArrayList<YapEntity>
  private var delegateAdapters = SparseArrayCompat<YapEntityDelegateAdapter>()

  init {
    delegateAdapters.put(
        YapEntityTypes.BOOKMARKED_TOPIC_ITEM, BookmarksDelegateAdapter(clickListener, settings))
    items = ArrayList()
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
      delegateAdapters.get(viewType).onCreateViewHolder(parent)

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    delegateAdapters.get(getItemViewType(position)).onBindViewHolder(holder, items[position])
  }

  override fun getItemViewType(position: Int): Int = items[position].getBaseEntityType()

  override fun getItemCount() = items.size

  override fun getItemId(position: Int) = position.toLong()

  fun addBookmarkItem(item: YapEntity) {
    val insertPosition = items.size
    items.add(item)
    notifyItemInserted(insertPosition)
  }

  fun clearBookmarksList() {
    notifyItemRangeRemoved(0, items.size)
    items.clear()
  }
}
