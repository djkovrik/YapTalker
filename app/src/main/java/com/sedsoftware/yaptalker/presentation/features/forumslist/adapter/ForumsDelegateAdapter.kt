package com.sedsoftware.yaptalker.presentation.features.forumslist.adapter

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.ViewGroup
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.data.settings.SettingsManager
import com.sedsoftware.yaptalker.presentation.base.adapter.YapEntityDelegateAdapter
import com.sedsoftware.yaptalker.presentation.extensions.inflate
import com.sedsoftware.yaptalker.presentation.extensions.loadFromUrl
import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.sedsoftware.yaptalker.presentation.model.base.ForumModel
import kotlinx.android.synthetic.main.fragment_forums_list_item.view.*

class ForumsDelegateAdapter(
    private val clickListener: ForumsItemClickListener,
    private val settings: SettingsManager) : YapEntityDelegateAdapter {

  private val normalFontSize by lazy {
    settings.getNormalFontSize()
  }

  override fun onCreateViewHolder(parent: ViewGroup): ViewHolder = ForumsViewHolder(parent)

  override fun onBindViewHolder(holder: ViewHolder, item: YapEntity, position: Int) {
    holder as ForumsViewHolder
    holder.bindTo(item as ForumModel)
  }

  inner class ForumsViewHolder(parent: ViewGroup) :
      RecyclerView.ViewHolder(parent.inflate(R.layout.fragment_forums_list_item)) {

    fun bindTo(forumItem: ForumModel) {
      with(itemView) {
        forum_title.text = forumItem.title
        forum_last_topic_title.text = forumItem.lastTopicTitle
        forum_last_topic_author.text = forumItem.lastTopicAuthor
        forum_last_topic_date.text = forumItem.date
        forum_image.loadFromUrl("http://www.yaplakal.com/html/icons/${forumItem.forumId}.gif")

        forum_title.textSize = normalFontSize
        forum_last_topic_title.textSize = normalFontSize
        forum_last_topic_author.textSize = normalFontSize
        forum_last_topic_date.textSize = normalFontSize

        setOnClickListener { clickListener.onForumItemClick(forumItem.forumId) }
      }
    }
  }
}
