package com.sedsoftware.yaptalker.presentation.features.incubator.adapter

import android.support.v4.util.SparseArrayCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.data.settings.SettingsManager
import com.sedsoftware.yaptalker.presentation.base.adapter.YapEntityDelegateAdapter
import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.sedsoftware.yaptalker.presentation.model.YapEntityTypes

class IncubatorAdapter(
    clickListener: IncubatorElementsClickListener,
    thumbnailsLoader: IncubatorThumbnailsLoader,
    settings: SettingsManager
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

  private var items: ArrayList<YapEntity>
  private var delegateAdapters = SparseArrayCompat<YapEntityDelegateAdapter>()

  init {
    delegateAdapters.put(
        YapEntityTypes.INCUBATOR_TOPIC, IncubatorDelegateAdapter(clickListener, thumbnailsLoader, settings))

    items = ArrayList()
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    delegateAdapters.get(getItemViewType(position)).onBindViewHolder(holder, items[position])

    with(holder.itemView) {
      AnimationUtils.loadAnimation(context, R.anim.recyclerview_fade_in).apply {
        startAnimation(this)
      }
    }
  }

  override fun onViewDetachedFromWindow(holder: ViewHolder?) {
    super.onViewDetachedFromWindow(holder)
    holder?.itemView?.clearAnimation()
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
      delegateAdapters.get(viewType).onCreateViewHolder(parent)

  override fun getItemCount(): Int = items.size

  override fun getItemId(position: Int) = position.toLong()

  override fun getItemViewType(position: Int): Int = items[position].getBaseEntityType()

  fun addIncubatorItem(item: YapEntity) {
    val insertPosition = items.size
    items.add(item)
    notifyItemInserted(insertPosition)
  }

  fun clearIncubatorItems() {
    notifyItemRangeRemoved(0, items.size)
    items.clear()
  }
}
