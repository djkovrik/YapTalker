package com.sedsoftware.yaptalker.features.forumslist.adapter

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.ViewGroup
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.base.BaseAdapterInjections
import com.sedsoftware.yaptalker.commons.adapter.ViewType
import com.sedsoftware.yaptalker.commons.adapter.ViewTypeDelegateAdapter
import com.sedsoftware.yaptalker.commons.extensions.inflate
import com.sedsoftware.yaptalker.commons.extensions.loadFromUrl
import com.sedsoftware.yaptalker.data.parsing.ForumItem
import kotlinx.android.synthetic.main.fragment_forums_list_item.view.*

class ForumsDelegateAdapter(val clickListener: ForumsItemClickListener) :
    BaseAdapterInjections(), ViewTypeDelegateAdapter {

  override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
    return ForumsViewHolder(parent)
  }

  override fun onBindViewHolder(holder: ViewHolder, item: ViewType) {
    holder as ForumsViewHolder
    holder.bindTo(item as ForumItem)
  }

  inner class ForumsViewHolder(parent: ViewGroup) :
      RecyclerView.ViewHolder(parent.inflate(R.layout.fragment_forums_list_item)) {

    fun bindTo(forumItem: ForumItem) {
      with(forumItem) {
        with(itemView) {
          forum_title.text = title
          forum_last_topic_title.text = lastTopicTitle
          forum_last_topic_author.text = lastTopicAuthor
          forum_last_topic_date.shortDateText = date
          forum_image.loadFromUrl("http://www.yaplakal.com/html/icons/$forumId.gif")

          forum_title.textSize = normalFontSize
          forum_last_topic_title.textSize = normalFontSize
          forum_last_topic_author.textSize = normalFontSize
          forum_last_topic_date.textSize = normalFontSize

          setOnClickListener { clickListener.onForumItemClick(forumId) }
        }
      }
    }
  }
}
