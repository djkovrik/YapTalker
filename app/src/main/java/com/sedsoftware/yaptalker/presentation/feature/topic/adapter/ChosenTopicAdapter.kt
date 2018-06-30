package com.sedsoftware.yaptalker.presentation.feature.topic.adapter

import android.support.v4.util.SparseArrayCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.ViewGroup
import com.sedsoftware.yaptalker.domain.device.Settings
import com.sedsoftware.yaptalker.presentation.base.adapter.YapEntityDelegateAdapter
import com.sedsoftware.yaptalker.presentation.base.navigation.NavigationPanelClickListener
import com.sedsoftware.yaptalker.presentation.base.navigation.NavigationPanelDelegateAdapter
import com.sedsoftware.yaptalker.presentation.mapper.util.TextTransformer
import com.sedsoftware.yaptalker.presentation.model.DisplayedItemModel
import com.sedsoftware.yaptalker.presentation.model.DisplayedItemType
import com.sedsoftware.yaptalker.presentation.model.base.SinglePostModel
import com.sedsoftware.yaptalker.presentation.thumbnail.ThumbnailsLoader
import java.util.ArrayList
import javax.inject.Inject

class ChosenTopicAdapter @Inject constructor(
  private val textTransformer: TextTransformer,
  elementsClickListener: ChosenTopicElementsClickListener,
  navigationClickListener: NavigationPanelClickListener,
  thumbnailLoader: ThumbnailsLoader,
  settings: Settings
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

  private var items: ArrayList<DisplayedItemModel>
  private var delegateAdapters = SparseArrayCompat<YapEntityDelegateAdapter>()

  init {
    delegateAdapters.put(
      DisplayedItemType.SINGLE_POST, ChosenTopicDelegateAdapter(elementsClickListener, thumbnailLoader, settings)
    )

    delegateAdapters.put(
      DisplayedItemType.NAVIGATION_PANEL,
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

  override fun getItemViewType(position: Int): Int = items[position].getEntityType()

  override fun getItemCount() = items.size

  override fun getItemId(position: Int) = position.toLong()

  fun addPostItem(item: DisplayedItemModel) {
    val insertPosition = items.size
    items.add(item)
    notifyItemInserted(insertPosition)
  }

  fun clearPostsList() {
    notifyItemRangeRemoved(0, items.size)
    items.clear()
  }

  fun updateKarmaUi(targetPostId: Int, shouldIncrease: Boolean) {

    items
      .find { it is SinglePostModel && it.postId == targetPostId }
      .let { post ->
        post as SinglePostModel

        val position = items.indexOf(post)
        val diff = if (shouldIncrease) 1 else -1
        val newRank = post.postRank + diff

        post.postRank = newRank
        post.postRankText = textTransformer.transformRankToFormattedText(newRank)

        if (shouldIncrease) {
          post.postRankPlusClicked = true
        } else {
          post.postRankMinusClicked = true
        }

        notifyItemChanged(position)
      }
  }
}
