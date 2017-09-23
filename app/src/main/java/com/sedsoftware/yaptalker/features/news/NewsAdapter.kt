package com.sedsoftware.yaptalker.features.news

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.github.salomonbrys.kodein.LazyKodein
import com.github.salomonbrys.kodein.LazyKodeinAware
import com.github.salomonbrys.kodein.instance
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.YapTalkerApp
import com.sedsoftware.yaptalker.commons.extensions.getLastDigits
import com.sedsoftware.yaptalker.commons.extensions.hideView
import com.sedsoftware.yaptalker.commons.extensions.loadFromUrl
import com.sedsoftware.yaptalker.commons.extensions.showView
import com.sedsoftware.yaptalker.commons.extensions.textFromHtml
import com.sedsoftware.yaptalker.data.remote.video.parseLink
import com.sedsoftware.yaptalker.data.model.NewsItem
import com.sedsoftware.yaptalker.data.remote.video.ThumbnailsManager
import com.sedsoftware.yaptalker.features.imagedisplay.ImageDisplayActivity
import com.sedsoftware.yaptalker.features.settings.SettingsHelper
import com.sedsoftware.yaptalker.features.videodisplay.VideoDisplayActivity
import kotlinx.android.synthetic.main.controller_news_item.view.*
import org.jetbrains.anko.startActivity
import java.util.ArrayList
import java.util.Locale

class NewsAdapter(private val itemClick: (String, String) -> Unit) :
    RecyclerView.Adapter<NewsAdapter.NewsViewHolder>(), LazyKodeinAware {

  override val kodein: LazyKodein
    get() = LazyKodein { YapTalkerApp.kodeinInstance }

  // Kodein injection
  private val thumbnailsLoader: ThumbnailsManager by instance()
  private val settings: SettingsHelper by instance()

  private val normalFontSize by lazy {
    settings.getNormalFontSize()
  }

  private val bigFontSize by lazy {
    settings.getBigFontSize()
  }

  private var news: ArrayList<NewsItem> = ArrayList()
  private var lastPosition = -1

  override fun getItemCount() = news.size

  override fun getItemId(position: Int) = news[position].link.getLastDigits().toLong()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.controller_news_item, parent,
        false)
    return NewsViewHolder(view, itemClick)
  }

  override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
    holder.bindTo(news[position])

    // TODO() Improve animation
    val animation = AnimationUtils.loadAnimation(holder.itemView.context,
        if (position > lastPosition)
          R.anim.recyclerview_up_from_bottom
        else
          R.anim.recyclerview_down_from_top)

    holder.itemView.startAnimation(animation)
    lastPosition = holder.adapterPosition
  }

  override fun onViewDetachedFromWindow(holder: NewsViewHolder?) {
    super.onViewDetachedFromWindow(holder)
    holder?.itemView?.clearAnimation()
  }

  fun addNewsItem(item: NewsItem) {
    val insertPosition = news.size
    news.add(item)
    notifyItemInserted(insertPosition)
  }

  fun clearNews() {
    notifyItemRangeRemoved(0, news.size)
    news.clear()
  }

  inner class NewsViewHolder(itemView: View, private val itemClick: (String, String) -> Unit) :
      RecyclerView.ViewHolder(itemView) {

    private val forumTitleTemplate: String = itemView.context.getString(
        R.string.news_forum_title_template)
    private val commentsTemplate: String = itemView.context.getString(
        R.string.news_comments_template)

    fun bindTo(newsItem: NewsItem) {
      with(itemView) {
        // Font size
        news_author.textSize = normalFontSize
        news_title.textSize = bigFontSize
        news_forum.textSize = normalFontSize
        news_date.textSize = normalFontSize
        news_rating.textSize = normalFontSize
        news_comments_counter.textSize = normalFontSize
        news_content_text.textSize = normalFontSize

        // Content
        news_author.text = newsItem.author
        news_title.text = newsItem.title
        news_forum.text = String.format(Locale.US, forumTitleTemplate, newsItem.forumName)
        news_date.shortDateText = newsItem.date
        news_rating.ratingText = newsItem.rating
        news_comments_counter.text = String.format(Locale.US, commentsTemplate, newsItem.comments)
        news_content_text.textFromHtml(newsItem.cleanedDescription)

        // Remove listener before setting the new one
        news_content_image.setOnClickListener(null)

        when {
          newsItem.images.isNotEmpty() -> {
            var url = newsItem.images.first()
            if (!url.startsWith("http"))
              url = "http:$url"
            news_content_image.showView()
            news_content_image.loadFromUrl(url)
            news_content_image.setOnClickListener {
              context.startActivity<ImageDisplayActivity>("url" to url)
            }
          }
          newsItem.videos.isNotEmpty() -> {
            news_content_image.showView()
            thumbnailsLoader.loadThumbnail(
                parseLink(newsItem.videos.first()), news_content_image)
            news_content_image.setOnClickListener {
              context.startActivity<VideoDisplayActivity>("video" to newsItem.videosRaw.first())
            }
          }
          else -> news_content_image.hideView()
        }

        setOnClickListener { itemClick(newsItem.link, newsItem.forumLink) }
      }
    }
  }
}
