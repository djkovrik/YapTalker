package com.sedsoftware.yaptalker.presentation.feature.forum.adapter

import android.support.v4.util.SparseArrayCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.ViewGroup
import com.sedsoftware.yaptalker.domain.device.Settings
import com.sedsoftware.yaptalker.presentation.base.adapter.YapEntityDelegateAdapter
import com.sedsoftware.yaptalker.presentation.base.navigation.NavigationPanelClickListener
import com.sedsoftware.yaptalker.presentation.base.navigation.NavigationPanelDelegateAdapter
import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.sedsoftware.yaptalker.presentation.model.YapEntityTypes
import com.sedsoftware.yaptalker.presentation.model.base.TopicModel
import java.util.ArrayList
import javax.inject.Inject

class ChosenForumAdapter @Inject constructor(
  itemClickListener: ChosenForumItemClickListener,
  navigationClickListener: NavigationPanelClickListener,
  settings: Settings
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

  private var items: ArrayList<YapEntity>
  private var delegateAdapters = SparseArrayCompat<YapEntityDelegateAdapter>()

  init {
    delegateAdapters.put(
      YapEntityTypes.TOPIC_ITEM,
      ChosenForumDelegateAdapter(itemClickListener, settings)
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

  override fun getItemId(position: Int): Long =
    (items[position] as? TopicModel)?.id?.toLong() ?: position.toLong()

  fun addTopicItem(item: YapEntity) {
    val insertPosition = items.size
    items.add(item)
    notifyItemInserted(insertPosition)
  }

  fun clearTopicsList() {
    notifyItemRangeRemoved(0, items.size)
    items.clear()
  }
}
