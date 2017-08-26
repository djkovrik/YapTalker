package com.sedsoftware.yaptalker.features.forum

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.commons.extensions.getLastDigits
import com.sedsoftware.yaptalker.commons.extensions.getShortTime
import com.sedsoftware.yaptalker.commons.extensions.textColor
import com.sedsoftware.yaptalker.data.model.Topic
import kotlinx.android.synthetic.main.controller_chosen_forum_item.view.*
import java.util.Locale

class ChosenForumAdapter : RecyclerView.Adapter<ChosenForumAdapter.ForumViewHolder>() {

  private var topics: ArrayList<Topic> = ArrayList()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForumViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.controller_chosen_forum_item,
        parent, false)
    return ForumViewHolder(view)
  }

  override fun onBindViewHolder(holder: ForumViewHolder, position: Int) {
    holder.bindTo(topics[position])
  }

  override fun getItemCount() = topics.size

  override fun getItemId(position: Int) = topics[position].link.getLastDigits().toLong()

  fun setTopics(list: List<Topic>) {
    topics.clear()
    topics.addAll(list)
    notifyDataSetChanged()
  }

  fun getTopics() = topics

  inner class ForumViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val commentsTemplate = itemView.context.getString(R.string.forum_comments_template)

    fun bindTo(topicItem: Topic) {
      with(topicItem) {
        with(itemView) {
          topic_name.text = title
          topic_last_post_author.text = lastPostAuthor
          topic_last_post_time.text = context.getShortTime(lastPostDate)
          topic_answers.text = String.format(Locale.US, commentsTemplate, answers)
          topic_rating.text = rating

          if (rating.isNotEmpty()) {
            topic_rating.loadRatingBackground(rating.toInt())
          }
        }
      }
    }
  }

  @Suppress("MagicNumber")
  private fun TextView.loadRatingBackground(rating: Int) {

    when (rating) {
    // Platinum
      in 1000..50000 -> {
        textColor = R.color.colorRatingPlatinumText
        setBackgroundResource(R.drawable.topic_rating_platinum)
      }
    // Gold
      in 500..999 -> {
        textColor = R.color.colorRatingGoldText
        setBackgroundResource(R.drawable.topic_rating_gold)
      }
    // Green
      in 50..499 -> {
        textColor = R.color.colorRatingGreenText
        setBackgroundResource(R.drawable.topic_rating_green)
      }
    // Gray
      in -9..49 -> {
        textColor = R.color.colorRatingGreyText
        setBackgroundResource(R.drawable.topic_rating_grey)
      }
    // Red
      in -99..-10 -> {
        textColor = R.color.colorRatingRedText
        setBackgroundResource(R.drawable.topic_rating_red)
      }
    // Dark Red
      else -> {
        textColor = R.color.colorRatingDarkRedText
        setBackgroundResource(R.drawable.topic_rating_dark_red)
      }
    }
  }
}
