package com.sedsoftware.yaptalker.presentation.feature.search.adapters

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.ViewGroup
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.domain.device.Settings
import com.sedsoftware.yaptalker.presentation.base.adapter.YapEntityDelegateAdapter
import com.sedsoftware.yaptalker.presentation.extensions.inflate
import com.sedsoftware.yaptalker.presentation.model.DisplayedItemModel
import com.sedsoftware.yaptalker.presentation.model.base.SearchTopicItemModel

class SearchResultsDelegateAdapter(
  private val itemClickListener: SearchResultsItemClickListener,
  private val settings: Settings
) : YapEntityDelegateAdapter {

  private val normalFontSize by lazy {
    settings.getNormalFontSize()
  }

  override fun onCreateViewHolder(parent: ViewGroup): ViewHolder = SearchedTopicViewHolder(parent)

  override fun onBindViewHolder(holder: ViewHolder, item: DisplayedItemModel) {
    if (item is SearchTopicItemModel) {
      holder as SearchedTopicViewHolder
      holder.bindTo(item)
    }
  }

  inner class SearchedTopicViewHolder(parent: ViewGroup) :
    RecyclerView.ViewHolder(parent.inflate(R.layout.fragment_site_search_topic_item)) {

    fun bindTo(topicItem: SearchTopicItemModel) {
      with(itemView) {

        search_topic_name.text = topicItem.title
        search_topic_forum.text = topicItem.forumTitle
        search_topic_last_post_date.text = topicItem.lastPostDate
        search_topic_answers.text = topicItem.answers
        search_topic_rating.text = topicItem.ratingText
        search_topic_rating.loadRatingBackground(topicItem.rating)

        search_topic_name.textSize = normalFontSize
        search_topic_forum.textSize = normalFontSize
        search_topic_last_post_date.textSize = normalFontSize
        search_topic_answers.textSize = normalFontSize

        setOnClickListener {
          val triple = Triple(topicItem.forumId, topicItem.topicId, 0)
          itemClickListener.onSearchResultsItemClick(triple)
        }
      }
    }
  }
}
