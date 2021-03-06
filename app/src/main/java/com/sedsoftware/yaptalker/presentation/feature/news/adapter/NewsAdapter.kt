package com.sedsoftware.yaptalker.presentation.feature.news.adapter

import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.domain.device.Settings
import com.sedsoftware.yaptalker.presentation.base.adapter.YapEntityDelegateAdapter
import com.sedsoftware.yaptalker.presentation.model.DisplayedItemType
import com.sedsoftware.yaptalker.presentation.model.base.NewsItemModel
import com.sedsoftware.yaptalker.presentation.provider.ThumbnailsProvider
import java.util.ArrayList
import javax.inject.Inject

class NewsAdapter @Inject constructor(
    clickListener: NewsItemElementsClickListener,
    thumbnailsProvider: ThumbnailsProvider,
    settings: Settings
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: ArrayList<NewsItemModel>
    private var delegateAdapters = SparseArrayCompat<YapEntityDelegateAdapter>()

    init {
        delegateAdapters.put(
            DisplayedItemType.NEWS, NewsDelegateAdapter(clickListener, thumbnailsProvider, settings)
        )

        items = ArrayList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        delegateAdapters.get(viewType)!!.onCreateViewHolder(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        delegateAdapters.get(getItemViewType(position))?.onBindViewHolder(holder, items[position])

        with(holder.itemView) {
            AnimationUtils.loadAnimation(context, R.anim.recyclerview_fade_in).apply {
                startAnimation(this)
            }
        }
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.itemView.clearAnimation()
    }

    override fun getItemCount(): Int = items.size

    override fun getItemId(position: Int) = position.toLong()

    override fun getItemViewType(position: Int): Int = items[position].getEntityType()

    fun addNewsItems(list: List<NewsItemModel>) {
        val insertPosition = items.size
        items.addAll(list)
        notifyItemRangeInserted(insertPosition, items.size)
    }

    fun removeNewsItem(item: NewsItemModel) {
        val position = items.indexOf(item)

        if (position != -1) {
            items.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun clearNews() {
        notifyItemRangeRemoved(0, items.size)
        items.clear()
    }
}
