package com.sedsoftware.yaptalker.presentation.features.topic.adapter

import android.support.v4.util.SparseArrayCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.ViewGroup
import com.sedsoftware.yaptalker.data.SettingsManager
import com.sedsoftware.yaptalker.presentation.base.adapter.YapEntityDelegateAdapter
import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.sedsoftware.yaptalker.presentation.model.YapEntityTypes

class ChosenTopicAdapter(
    profileClick: UserProfileClickListener,
    navigationClick: TopicNavigationClickListener,
    itemClick: ChosenTopicItemClickListener,
    mediaClick: TopicItemMediaClickListener,
    thumbnailLoader: ChosenTopicThumbnailLoader,
    settings: SettingsManager) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

  private var items: ArrayList<YapEntity>
  private var delegateAdapters = SparseArrayCompat<YapEntityDelegateAdapter>()

  init {
    delegateAdapters.put(
        YapEntityTypes.SINGLE_POST_ITEM,
        ChosenTopicDelegateAdapter(itemClick, profileClick, mediaClick, thumbnailLoader, settings))

    delegateAdapters.put(
        YapEntityTypes.NAVIGATION_PANEL_ITEM,
        TopicNavigationDelegateAdapter(navigationClick))

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

  fun addPostItem(item: YapEntity) {
    val insertPosition = items.size
    items.add(item)
    notifyItemInserted(insertPosition)
  }

  fun clearPostsList() {
    notifyItemRangeRemoved(0, items.size)
    items.clear()
  }
}
