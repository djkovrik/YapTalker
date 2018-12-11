package com.sedsoftware.yaptalker.presentation.feature.blacklist.adapter

import android.support.v4.util.SparseArrayCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.domain.device.Settings
import com.sedsoftware.yaptalker.presentation.base.adapter.YapEntityDelegateAdapter
import com.sedsoftware.yaptalker.presentation.model.DisplayedItemType
import com.sedsoftware.yaptalker.presentation.model.base.BlacklistedTopicModel
import javax.inject.Inject

class BlacklistAdapter @Inject constructor(
    clickListener: BlacklistElementsClickListener,
    settings: Settings
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: ArrayList<BlacklistedTopicModel>
    private var delegateAdapters = SparseArrayCompat<YapEntityDelegateAdapter>()

    init {
        delegateAdapters.put(
            DisplayedItemType.BLACKLISTED_TOPIC, BlacklistDelegateAdapter(clickListener, settings)
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

    fun setTopics(topics: List<BlacklistedTopicModel>) {
        items.clear()
        items.addAll(topics)
        notifyDataSetChanged()
    }
}
