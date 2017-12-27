package com.sedsoftware.yaptalker.presentation.features.forum.adapter

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.ViewGroup
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.data.settings.SettingsManager
import com.sedsoftware.yaptalker.presentation.base.adapter.YapEntityDelegateAdapter
import com.sedsoftware.yaptalker.presentation.extensions.inflate
import com.sedsoftware.yaptalker.presentation.extensions.loadRatingBackground
import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.sedsoftware.yaptalker.presentation.model.base.TopicModel
import kotlinx.android.synthetic.main.fragment_chosen_forum_item.view.topic_answers
import kotlinx.android.synthetic.main.fragment_chosen_forum_item.view.topic_last_post_author
import kotlinx.android.synthetic.main.fragment_chosen_forum_item.view.topic_last_post_date
import kotlinx.android.synthetic.main.fragment_chosen_forum_item.view.topic_name
import kotlinx.android.synthetic.main.fragment_chosen_forum_item.view.topic_rating

class ChosenForumDelegateAdapter(
    val itemClick: ChosenForumElementsClickListener,
    val settings: SettingsManager) : YapEntityDelegateAdapter {

  private val normalFontSize by lazy {
    settings.getNormalFontSize()
  }

  override fun onCreateViewHolder(parent: ViewGroup): ViewHolder = TopicViewHolder(parent)

  override fun onBindViewHolder(holder: ViewHolder, item: YapEntity) {
    holder as TopicViewHolder
    holder.bindTo(item as TopicModel)
  }

  inner class TopicViewHolder(parent: ViewGroup) :
      RecyclerView.ViewHolder(parent.inflate(R.layout.fragment_chosen_forum_item)) {

    fun bindTo(topicItem: TopicModel) {
      with(itemView) {
        topic_name.text = topicItem.title
        topic_last_post_author.text = topicItem.lastPostAuthor
        topic_last_post_date.text = topicItem.lastPostDate
        topic_answers.text = topicItem.answers
        topic_rating.text = topicItem.ratingText
        topic_rating.loadRatingBackground(topicItem.rating)

        topic_name.textSize = normalFontSize
        topic_last_post_author.textSize = normalFontSize
        topic_last_post_date.textSize = normalFontSize
        topic_answers.textSize = normalFontSize

        setOnClickListener { itemClick.onTopicClick(topicItem.id) }
      }
    }
  }
}
