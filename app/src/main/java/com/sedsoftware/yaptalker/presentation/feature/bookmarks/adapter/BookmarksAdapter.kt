package com.sedsoftware.yaptalker.presentation.feature.bookmarks.adapter

import android.support.v4.util.SparseArrayCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.domain.device.Settings
import com.sedsoftware.yaptalker.presentation.base.adapter.YapEntityDelegateAdapter
import com.sedsoftware.yaptalker.presentation.model.DisplayedItemType
import com.sedsoftware.yaptalker.presentation.model.base.BookmarkedTopicModel
import java.util.ArrayList
import javax.inject.Inject

class BookmarksAdapter @Inject constructor(
    clickListener: BookmarksElementsClickListener,
    settings: Settings
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: ArrayList<BookmarkedTopicModel>
    private var delegateAdapters = SparseArrayCompat<YapEntityDelegateAdapter>()

    init {
        delegateAdapters.put(
            DisplayedItemType.BOOKMARKED_TOPIC, BookmarksDelegateAdapter(clickListener, settings)
        )

        items = ArrayList()

        setHasStableIds(true)
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

    override fun getItemViewType(position: Int): Int =
        items[position].getEntityType()

    override fun getItemCount() =
        items.size

    override fun getItemId(position: Int) =
        position.toLong()

    fun addBookmarkItem(item: BookmarkedTopicModel) {
        val insertPosition = items.size
        items.add(item)
        notifyItemInserted(insertPosition)
    }

    fun clearBookmarksList() {
        notifyItemRangeRemoved(0, items.size)
        items.clear()
    }
}
