package com.sedsoftware.yaptalker.presentation.features.activetopics.adapter

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.ViewGroup
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.data.settings.SettingsManager
import com.sedsoftware.yaptalker.presentation.base.adapter.YapEntityDelegateAdapter
import com.sedsoftware.yaptalker.presentation.extensions.inflate
import com.sedsoftware.yaptalker.presentation.extensions.loadRatingBackground
import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.sedsoftware.yaptalker.presentation.model.base.ActiveTopicModel
import kotlinx.android.synthetic.main.fragment_active_topics_list_item.view.*

class ActiveTopicsDelegateAdapter(
    private val itemClick: ActiveTopicsElementsClickListener,
    private val settings: SettingsManager) : YapEntityDelegateAdapter {

  private val normalFontSize by lazy {
    settings.getNormalFontSize()
  }

  override fun onCreateViewHolder(parent: ViewGroup): ViewHolder = TopicViewHolder(parent)

  override fun onBindViewHolder(holder: ViewHolder, item: YapEntity) {
    holder as TopicViewHolder
    holder.bindTo(item as ActiveTopicModel)
  }

  inner class TopicViewHolder(parent: ViewGroup) :
      RecyclerView.ViewHolder(parent.inflate(R.layout.fragment_active_topics_list_item)) {

    fun bindTo(topicItem: ActiveTopicModel) {
      with(itemView) {

        active_topic_name.text = topicItem.title
        active_topic_forum.text = topicItem.forumTitle
        active_topic_last_post_date.text = topicItem.lastPostDate
        active_topic_answers.text = topicItem.answers
        active_topic_rating.text = topicItem.ratingText
        active_topic_rating.loadRatingBackground(topicItem.rating)

        active_topic_name.textSize = normalFontSize
        active_topic_forum.textSize = normalFontSize
        active_topic_last_post_date.textSize = normalFontSize
        active_topic_answers.textSize = normalFontSize

        setOnClickListener { itemClick.onTopicClick(topicItem.forumId, topicItem.topicId) }
      }
    }
  }
}
