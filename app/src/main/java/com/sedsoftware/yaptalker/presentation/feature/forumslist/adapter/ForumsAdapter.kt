package com.sedsoftware.yaptalker.presentation.feature.forumslist.adapter

import android.support.v4.util.SparseArrayCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.ViewGroup
import com.sedsoftware.yaptalker.domain.device.Settings
import com.sedsoftware.yaptalker.presentation.base.adapter.YapEntityDelegateAdapter
import com.sedsoftware.yaptalker.presentation.model.DisplayedItemType
import com.sedsoftware.yaptalker.presentation.model.base.ForumModel
import java.util.ArrayList
import javax.inject.Inject

class ForumsAdapter @Inject constructor(
  itemClickListener: ForumsItemClickListener,
  settings: Settings
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

  private var items: ArrayList<ForumModel>
  private var delegateAdapters = SparseArrayCompat<YapEntityDelegateAdapter>()

  init {
    delegateAdapters.put(DisplayedItemType.FORUM, ForumsDelegateAdapter(itemClickListener, settings))

    items = ArrayList()

    setHasStableIds(true)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
    delegateAdapters.get(viewType).onCreateViewHolder(parent)

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    delegateAdapters.get(getItemViewType(position)).onBindViewHolder(holder, items[position])
  }

  override fun getItemViewType(position: Int): Int = items[position].getEntityType()

  override fun getItemCount() = items.size

  override fun getItemId(position: Int) = position.toLong()

  fun addForumsListItem(item: ForumModel) {
    val insertPosition = items.size
    items.add(item)
    notifyItemInserted(insertPosition)
  }

  fun clearForumsList() {
    notifyItemRangeRemoved(0, items.size)
    items.clear()
  }
}
