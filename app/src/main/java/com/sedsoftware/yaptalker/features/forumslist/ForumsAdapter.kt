package com.sedsoftware.yaptalker.features.forumslist

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.salomonbrys.kodein.LazyKodein
import com.github.salomonbrys.kodein.LazyKodeinAware
import com.github.salomonbrys.kodein.instance
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.YapTalkerApp
import com.sedsoftware.yaptalker.commons.extensions.loadFromUrl
import com.sedsoftware.yaptalker.data.model.ForumItem
import com.sedsoftware.yaptalker.features.settings.SettingsHelper
import kotlinx.android.synthetic.main.controller_forums_list_item.view.*

class ForumsAdapter(
    private val itemClick: (Int) -> Unit) : RecyclerView.Adapter<ForumsAdapter.ForumsViewHolder>(), LazyKodeinAware {

  override val kodein: LazyKodein
    get() = LazyKodein { YapTalkerApp.kodeinInstance }

  // Kodein injection
  private val settings: SettingsHelper by instance()

  private val normalFontSize by lazy {
    settings.getNormalFontSize()
  }

  private var forumsList: ArrayList<ForumItem> = ArrayList()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForumsViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.controller_forums_list_item, parent, false)
    return ForumsViewHolder(view, itemClick)
  }

  override fun onBindViewHolder(holder: ForumsViewHolder, position: Int) {
    holder.bindTo(forumsList[position])
  }

  override fun getItemCount() = forumsList.size

  override fun getItemId(position: Int) = forumsList[position].forumId.toLong()

  fun getForums() = forumsList

  fun addForumsListItem(item: ForumItem) {
    val insertPosition = forumsList.size
    forumsList.add(item)
    notifyItemInserted(insertPosition)
  }

  fun addForumsList(list: List<ForumItem>) {
    forumsList.clear()
    forumsList.addAll(list)
    notifyDataSetChanged()
  }

  fun clearForumsList() {
    notifyItemRangeRemoved(0, forumsList.size)
    forumsList.clear()
  }

  inner class ForumsViewHolder(
      itemView: View, private val itemClick: (Int) -> Unit) : RecyclerView.ViewHolder(itemView) {

    fun bindTo(forumItem: ForumItem) {
      with(forumItem) {
        with(itemView) {
          forum_title.text = title
          forum_last_topic_title.text = lastTopicTitle
          forum_last_topic_author.text = lastTopicAuthor
          forum_last_topic_date.shortDateText = date
          forum_image.loadFromUrl("http://www.yaplakal.com/html/icons/$forumId.gif")

          forum_title.textSize = normalFontSize
          forum_last_topic_author.textSize = normalFontSize

          setOnClickListener { itemClick(forumId) }
        }
      }
    }
  }
}
