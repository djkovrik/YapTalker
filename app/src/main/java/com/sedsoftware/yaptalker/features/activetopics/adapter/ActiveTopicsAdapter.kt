package com.sedsoftware.yaptalker.features.activetopics.adapter

import android.support.v4.util.SparseArrayCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.ViewGroup
import com.sedsoftware.yaptalker.commons.adapter.ContentTypes
import com.sedsoftware.yaptalker.commons.adapter.ViewType
import com.sedsoftware.yaptalker.commons.adapter.ViewTypeDelegateAdapter
import com.sedsoftware.yaptalker.data.parsing.ActiveTopic
import com.sedsoftware.yaptalker.data.parsing.ActiveTopicsNavigationPanel
import com.sedsoftware.yaptalker.data.parsing.ActiveTopicsPage

class ActiveTopicsAdapter(
    itemClick: ActiveTopicsItemClickListener, navigationClick: ActiveTopicsNavigationClickListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

  private var items: ArrayList<ViewType>
  private var delegateAdapters = SparseArrayCompat<ViewTypeDelegateAdapter>()

  init {
    delegateAdapters.put(ContentTypes.ACTIVE_TOPIC, ActiveTopicsDelegateAdapter(itemClick))
    delegateAdapters.put(ContentTypes.NAVIGATION_PANEL, ActiveTopicsNavigationDelegateAdapter(navigationClick))
    items = ArrayList()
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return delegateAdapters.get(viewType).onCreateViewHolder(parent)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    delegateAdapters.get(getItemViewType(position)).onBindViewHolder(holder, items[position])
  }

  override fun getItemViewType(position: Int): Int {
    return items[position].getViewType()
  }

  override fun getItemCount() = items.size

  override fun getItemId(position: Int) = position.toLong()

  fun refreshActiveTopicsPage(page: ActiveTopicsPage) {
    items.clear()
    items.addAll(page.topics)
    items.add(page.navigation)
    notifyDataSetChanged()
  }

  fun getTopics(): List<ActiveTopic> {
    return items
        .filter { it.getViewType() == ContentTypes.ACTIVE_TOPIC }
        .map { it as ActiveTopic }
  }

  fun getNavigationPanel(): List<ActiveTopicsNavigationPanel> {
    return items
        .filter { it.getViewType() == ContentTypes.NAVIGATION_PANEL }
        .map { it as ActiveTopicsNavigationPanel }
  }
}
