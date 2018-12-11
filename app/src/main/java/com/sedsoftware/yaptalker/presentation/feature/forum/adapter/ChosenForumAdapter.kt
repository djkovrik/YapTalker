package com.sedsoftware.yaptalker.presentation.feature.forum.adapter

import android.support.v4.util.SparseArrayCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.domain.device.Settings
import com.sedsoftware.yaptalker.presentation.base.adapter.YapEntityDelegateAdapter
import com.sedsoftware.yaptalker.presentation.base.navigation.NavigationPanelClickListener
import com.sedsoftware.yaptalker.presentation.base.navigation.NavigationPanelDelegateAdapter
import com.sedsoftware.yaptalker.presentation.model.DisplayedItemModel
import com.sedsoftware.yaptalker.presentation.model.DisplayedItemType
import com.sedsoftware.yaptalker.presentation.model.base.TopicModel
import java.util.ArrayList
import javax.inject.Inject

class ChosenForumAdapter @Inject constructor(
    itemClickListener: ChosenForumItemClickListener,
    navigationClickListener: NavigationPanelClickListener,
    settings: Settings
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: ArrayList<DisplayedItemModel>
    private var delegateAdapters = SparseArrayCompat<YapEntityDelegateAdapter>()

    init {
        delegateAdapters.put(
            DisplayedItemType.TOPIC,
            ChosenForumDelegateAdapter(itemClickListener, settings)
        )

        delegateAdapters.put(
            DisplayedItemType.NAVIGATION_PANEL,
            NavigationPanelDelegateAdapter(navigationClickListener)
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

    override fun getItemId(position: Int): Long =
        (items[position] as? TopicModel)?.id?.toLong() ?: position.toLong()

    fun addTopicItem(item: DisplayedItemModel) {
        val insertPosition = items.size
        items.add(item)
        notifyItemInserted(insertPosition)
    }

    fun clearTopicsList() {
        notifyItemRangeRemoved(0, items.size)
        items.clear()
    }
}
