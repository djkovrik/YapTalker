package com.sedsoftware.yaptalker.features.topic.adapter

import android.support.v4.util.SparseArrayCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.ViewGroup
import com.sedsoftware.yaptalker.commons.adapter.ContentTypes
import com.sedsoftware.yaptalker.commons.adapter.ViewType
import com.sedsoftware.yaptalker.commons.adapter.ViewTypeDelegateAdapter
import com.sedsoftware.yaptalker.data.model.TopicNavigationPanel
import com.sedsoftware.yaptalker.data.model.TopicPage
import com.sedsoftware.yaptalker.data.model.TopicPost

class ChosenTopicAdapter(profileClick: UserProfileClickListener, navigationClick: TopicNavigationClickListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

  private var items: ArrayList<ViewType>
  private var delegateAdapters = SparseArrayCompat<ViewTypeDelegateAdapter>()

  init {
    delegateAdapters.put(ContentTypes.POST, ChosenTopicDelegateAdapter(profileClick))
    delegateAdapters.put(ContentTypes.NAVIGATION_PANEL, TopicNavigationDelegateAdapter(navigationClick))
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

  fun refreshTopicPage(page: TopicPage) {
    items.clear()
    items.add(page.navigation)
    items.addAll(page.posts)
    items.add(page.navigation)
    notifyDataSetChanged()
  }

  fun getPosts(): List<TopicPost> {
    return items
        .filter { it.getViewType() == ContentTypes.POST }
        .map { it as TopicPost }
  }

  fun getNavigationPanel(): List<TopicNavigationPanel> {
    return items
        .filter { it.getViewType() == ContentTypes.NAVIGATION_PANEL }
        .map { it as TopicNavigationPanel }
  }
}
