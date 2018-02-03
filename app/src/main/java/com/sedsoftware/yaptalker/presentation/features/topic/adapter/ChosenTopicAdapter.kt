package com.sedsoftware.yaptalker.presentation.features.topic.adapter

import android.support.v4.util.SparseArrayCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.ViewGroup
import com.sedsoftware.yaptalker.domain.device.Settings
import com.sedsoftware.yaptalker.presentation.base.adapter.YapEntityDelegateAdapter
import com.sedsoftware.yaptalker.presentation.base.navigation.NavigationPanelClickListener
import com.sedsoftware.yaptalker.presentation.base.navigation.NavigationPanelDelegateAdapter
import com.sedsoftware.yaptalker.presentation.base.thumbnail.ThumbnailsLoader
import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.sedsoftware.yaptalker.presentation.model.YapEntityTypes
import java.util.ArrayList
import javax.inject.Inject

class ChosenTopicAdapter @Inject constructor(
  elementsClickListener: ChosenTopicElementsClickListener,
  navigationClickListener: NavigationPanelClickListener,
  thumbnailLoader: ThumbnailsLoader,
  settings: Settings
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

  private var items: ArrayList<YapEntity>
  private var delegateAdapters = SparseArrayCompat<YapEntityDelegateAdapter>()

  init {
    delegateAdapters.put(
      YapEntityTypes.SINGLE_POST_ITEM, ChosenTopicDelegateAdapter(elementsClickListener, thumbnailLoader, settings)
    )

    delegateAdapters.put(
      YapEntityTypes.NAVIGATION_PANEL_ITEM,
      NavigationPanelDelegateAdapter(navigationClickListener)
    )

    items = ArrayList()

    setHasStableIds(true)
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
