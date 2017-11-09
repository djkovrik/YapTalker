package com.sedsoftware.yaptalker.features.forum.adapter

import android.support.v4.util.SparseArrayCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.ViewGroup
import com.sedsoftware.yaptalker.commons.adapter.ContentTypes
import com.sedsoftware.yaptalker.commons.adapter.ViewType
import com.sedsoftware.yaptalker.commons.adapter.ViewTypeDelegateAdapter
import com.sedsoftware.yaptalker.commons.extensions.getLastDigits
import com.sedsoftware.yaptalker.data.parsing.ForumPage
import com.sedsoftware.yaptalker.data.parsing.Topic

class ChosenForumAdapter(itemClick: TopicItemClickListener, navigationClick: ForumNavigationClickListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

  private var items: ArrayList<ViewType>
  private var delegateAdapters = SparseArrayCompat<ViewTypeDelegateAdapter>()

  init {
    delegateAdapters.put(ContentTypes.TOPIC, ChosenForumDelegateAdapter(itemClick))
    delegateAdapters.put(ContentTypes.NAVIGATION_PANEL, ForumNavigationDelegateAdapter(navigationClick))
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

  override fun getItemId(position: Int): Long {
    val item = items[position]

    return if (item is Topic) {
      item.link.getLastDigits().toLong()
    } else {
      position.toLong()
    }
  }

  fun refreshForumPage(page: ForumPage) {
    items.clear()
    items.addAll(page.topics)
    items.add(page.navigation)
    notifyDataSetChanged()
  }
}
