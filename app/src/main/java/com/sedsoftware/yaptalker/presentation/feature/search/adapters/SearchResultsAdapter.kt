package com.sedsoftware.yaptalker.presentation.feature.search.adapters

import android.support.v4.util.SparseArrayCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.ViewGroup
import com.sedsoftware.yaptalker.domain.device.Settings
import com.sedsoftware.yaptalker.presentation.base.adapter.YapEntityDelegateAdapter
import com.sedsoftware.yaptalker.presentation.model.DisplayedItemModel
import com.sedsoftware.yaptalker.presentation.model.DisplayedItemType
import java.util.ArrayList
import javax.inject.Inject

class SearchResultsAdapter @Inject constructor(
  clickListener: SearchResultsItemClickListener,
  settings: Settings
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

  private var items: ArrayList<DisplayedItemModel>
  private var delegateAdapters = SparseArrayCompat<YapEntityDelegateAdapter>()

  init {
    delegateAdapters.put(
      DisplayedItemType.SEARCHED_TOPIC, SearchResultsDelegateAdapter(clickListener, settings)
    )

    items = ArrayList()

    setHasStableIds(true)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    delegateAdapters.get(getItemViewType(position)).onBindViewHolder(holder, items[position])
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
    delegateAdapters.get(viewType).onCreateViewHolder(parent)

  override fun getItemCount(): Int = items.size

  override fun getItemId(position: Int) = position.toLong()

  override fun getItemViewType(position: Int): Int = items[position].getEntityType()

  fun addResultsItem(item: DisplayedItemModel) {
    val insertPosition = items.size
    items.add(item)
    notifyItemInserted(insertPosition)
  }
}
