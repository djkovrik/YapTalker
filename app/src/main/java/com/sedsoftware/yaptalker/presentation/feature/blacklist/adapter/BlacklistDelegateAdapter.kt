package com.sedsoftware.yaptalker.presentation.feature.blacklist.adapter

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.ViewGroup
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.domain.device.Settings
import com.sedsoftware.yaptalker.presentation.base.adapter.YapEntityDelegateAdapter
import com.sedsoftware.yaptalker.presentation.extensions.inflate
import com.sedsoftware.yaptalker.presentation.model.DisplayedItemModel
import com.sedsoftware.yaptalker.presentation.model.base.BlacklistedTopicModel
import kotlinx.android.synthetic.main.activity_blacklist_item.view.*

class BlacklistDelegateAdapter(
  private val clickListener: BlacklistElementsClickListener,
  private val settings: Settings
) : YapEntityDelegateAdapter {

  private val normalFontSize by lazy {
    settings.getNormalFontSize()
  }

  override fun onCreateViewHolder(parent: ViewGroup): ViewHolder = BlacklistViewHolder(parent)

  override fun onBindViewHolder(holder: ViewHolder, item: DisplayedItemModel) {
    holder as BlacklistViewHolder
    holder.bindTo(item as BlacklistedTopicModel)
  }

  inner class BlacklistViewHolder(parent: ViewGroup) :
    RecyclerView.ViewHolder(parent.inflate(R.layout.activity_blacklist_item)) {

    fun bindTo(topicItem: BlacklistedTopicModel) {
      with(itemView) {
        blacklisted_topic_title.text = topicItem.topicName
        blacklisted_topic_date.text = topicItem.dateAddedLabel
        blacklisted_topic_title.textSize = normalFontSize
        blacklisted_topic_date.textSize = normalFontSize
        blacklist_delete_icon.setOnClickListener { clickListener.onDeleteIconClick(topicItem.topicId) }
      }
    }
  }
}
