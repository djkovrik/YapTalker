package com.sedsoftware.yaptalker.features.topic

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.commons.extensions.getShortTime
import com.sedsoftware.yaptalker.commons.extensions.loadAvatarFromUrl
import com.sedsoftware.yaptalker.data.model.TopicPost
import kotlinx.android.synthetic.main.controller_chosen_topic_item.view.*

class ChosenTopicAdapter : RecyclerView.Adapter<ChosenTopicAdapter.PostViewHolder>() {

  private var posts: ArrayList<TopicPost> = ArrayList()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.controller_chosen_topic_item,
        parent, false)
    return PostViewHolder(view)
  }

  override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
    holder.bindTo(posts[position])
  }

  override fun onViewDetachedFromWindow(holder: PostViewHolder?) {
    super.onViewDetachedFromWindow(holder)
    // Clear WebView
    holder?.itemView?.post_content?.loadUrl("about:blank")
    holder?.itemView?.post_content?.clearHistory()
  }

  override fun getItemCount() = posts.size

  override fun getItemId(position: Int) = position.toLong()

  fun setPosts(list: List<TopicPost>) {
    posts.clear()
    posts.addAll(list)
    notifyDataSetChanged()
  }

  fun getPosts() = posts

  inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    @SuppressLint("SetJavaScriptEnabled")
    fun bindTo(postItem: TopicPost) {
      with(postItem) {
        with(itemView) {
          post_author.text = authorNickname
          post_rating.text = postRank
          post_date.text = context.getShortTime(postDate)
          post_author_avatar.loadAvatarFromUrl("http:$authorAvatar")
          // TODO() Replace webview with parsing
          post_content.settings.javaScriptEnabled = true
          post_content.loadDataWithBaseURL("http://www.yaplakal.com/", postContent,
              "text/html; charset=UTF-8", null, null)
        }
      }
    }
  }
}