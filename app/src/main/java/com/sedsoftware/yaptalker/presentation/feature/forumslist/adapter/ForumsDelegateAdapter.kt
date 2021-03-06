package com.sedsoftware.yaptalker.presentation.feature.forumslist.adapter

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import android.view.ViewGroup
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.domain.device.Settings
import com.sedsoftware.yaptalker.presentation.base.adapter.YapEntityDelegateAdapter
import com.sedsoftware.yaptalker.presentation.extensions.inflate
import com.sedsoftware.yaptalker.presentation.extensions.loadFromUrl
import com.sedsoftware.yaptalker.presentation.model.DisplayedItemModel
import com.sedsoftware.yaptalker.presentation.model.base.ForumModel
import kotlinx.android.synthetic.main.fragment_forums_list_item.view.forum_image
import kotlinx.android.synthetic.main.fragment_forums_list_item.view.forum_last_topic_author
import kotlinx.android.synthetic.main.fragment_forums_list_item.view.forum_last_topic_date
import kotlinx.android.synthetic.main.fragment_forums_list_item.view.forum_last_topic_title
import kotlinx.android.synthetic.main.fragment_forums_list_item.view.forum_title

class ForumsDelegateAdapter(
    private val itemClickListener: ForumsItemClickListener,
    private val settings: Settings
) : YapEntityDelegateAdapter {

    private val normalFontSize by lazy {
        settings.getNormalFontSize()
    }

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder = ForumsViewHolder(parent)

    override fun onBindViewHolder(holder: ViewHolder, item: DisplayedItemModel) {
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
                forum_image.loadFromUrl("https://yaplakal.com/html/icons/${forumItem.forumId}.gif")

                forum_title.textSize = normalFontSize
                forum_last_topic_title.textSize = normalFontSize
                forum_last_topic_author.textSize = normalFontSize
                forum_last_topic_date.textSize = normalFontSize

                setOnClickListener { itemClickListener.onForumItemClick(forumItem.forumId, forumItem.title) }
            }
        }
    }
}
