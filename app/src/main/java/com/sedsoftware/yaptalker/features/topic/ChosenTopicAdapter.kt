package com.sedsoftware.yaptalker.features.topic

import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.github.salomonbrys.kodein.LazyKodein
import com.github.salomonbrys.kodein.LazyKodeinAware
import com.github.salomonbrys.kodein.instance
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.YapTalkerApp
import com.sedsoftware.yaptalker.commons.extensions.color
import com.sedsoftware.yaptalker.commons.extensions.hideView
import com.sedsoftware.yaptalker.commons.extensions.loadAvatarFromUrl
import com.sedsoftware.yaptalker.commons.extensions.loadFromUrl
import com.sedsoftware.yaptalker.commons.extensions.showView
import com.sedsoftware.yaptalker.commons.extensions.stringRes
import com.sedsoftware.yaptalker.commons.extensions.textColor
import com.sedsoftware.yaptalker.commons.extensions.textFromHtml
import com.sedsoftware.yaptalker.commons.extensions.textFromHtmlWithEmoji
import com.sedsoftware.yaptalker.commons.parseLink
import com.sedsoftware.yaptalker.data.model.ParsedPost
import com.sedsoftware.yaptalker.data.model.PostHiddenText
import com.sedsoftware.yaptalker.data.model.PostLink
import com.sedsoftware.yaptalker.data.model.PostQuote
import com.sedsoftware.yaptalker.data.model.PostQuoteAuthor
import com.sedsoftware.yaptalker.data.model.PostScript
import com.sedsoftware.yaptalker.data.model.PostText
import com.sedsoftware.yaptalker.data.model.TopicPost
import com.sedsoftware.yaptalker.data.remote.ThumbnailsManager
import com.sedsoftware.yaptalker.features.imagedisplay.ImageDisplayActivity
import com.sedsoftware.yaptalker.features.settings.SettingsReader
import com.sedsoftware.yaptalker.features.videodisplay.VideoDisplayActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.toSingle
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.controller_chosen_topic_item.view.*
import org.jetbrains.anko.browse
import org.jetbrains.anko.startActivity
import java.util.Locale

class ChosenTopicAdapter : RecyclerView.Adapter<ChosenTopicAdapter.PostViewHolder>(), LazyKodeinAware {

  companion object {
    private const val INITIAL_NESTING_LEVEL = 0
    private const val MAX_LINK_TITLE_LENGTH = 15
  }

  override val kodein: LazyKodein
    get() = LazyKodein { YapTalkerApp.kodeinInstance }

  // Kodein injections
  private val thumbnailsLoader: ThumbnailsManager by instance()
  private val settings: SettingsReader by instance()

  private val normalFontSize by lazy {
    settings.getNormalFontSize()
  }

  private val smallFontSize by lazy {
    settings.getSmallFontSize()
  }

  private var posts: ArrayList<TopicPost> = ArrayList()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.controller_chosen_topic_item,
        parent, false)
    return PostViewHolder(view)
  }

  override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
    holder.bindTo(posts[position])
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

    fun bindTo(postItem: TopicPost) {
      postItem
          .toSingle()
          .observeOn(Schedulers.computation())
          .map { post -> parsePostContent(post) }
          .observeOn(AndroidSchedulers.mainThread())
          .map { parsedPost -> fillPostText(parsedPost) }
          .map { parsedPost -> fillPostMedia(parsedPost) }
          .map { _ -> fillPostHeader(postItem) }
          .subscribeOn(Schedulers.io())
          .subscribe({ _ -> }, { _ -> })
    }

    private fun fillPostHeader(post: TopicPost) {
      with(itemView) {
        post_author.text = post.authorNickname
        post_date.shortDateText = post.postDate
        post_author_avatar.loadAvatarFromUrl("http:${post.authorAvatar}")
        post_rating.ratingText = post.postRank

        post_author.textSize = normalFontSize
        post_date.textSize = normalFontSize
        post_rating.textSize = normalFontSize
      }
    }

    private fun parsePostContent(item: TopicPost): ParsedPost {
      return ParsedPost(item.postContent)
    }

    private fun fillPostText(post: ParsedPost): ParsedPost {

      val textPadding = itemView.context.resources.getDimension(
          R.dimen.post_text_horizontal_padding).toInt()

      with(itemView) {
        var currentNestingLevel = INITIAL_NESTING_LEVEL
        val links = HashSet<PostLink>()

        post_content_text_container.removeAllViews()

        if (post.content.isNotEmpty()) {
          post_content_text_container.showView()

          post.content.forEach {
            when (it) {
              is PostQuoteAuthor -> {
                currentNestingLevel++
                val quoteAuthor = TextView(context)
                quoteAuthor.textFromHtml(it.text)
                quoteAuthor.textSize = normalFontSize
                if (currentNestingLevel > INITIAL_NESTING_LEVEL) {
                  quoteAuthor.setPadding(textPadding * currentNestingLevel, 0, 0, 0)
                }
                quoteAuthor.setBackgroundColor(context.color(R.color.colorQuotedTextBackground))
                post_content_text_container.addView(quoteAuthor)
              }
              is PostQuote -> {
                val quoteText = TextView(context)
                quoteText.textFromHtmlWithEmoji(it.text)
                quoteText.textSize = normalFontSize
                quoteText.setBackgroundColor(context.color(R.color.colorQuotedTextBackground))
                quoteText.setPadding(textPadding * currentNestingLevel, 0, 0, 0)
                post_content_text_container.addView(quoteText)
              }
              is PostText -> {
                currentNestingLevel--
                val postText = TextView(context)
                postText.textFromHtmlWithEmoji(it.text)
                postText.textSize = normalFontSize
                if (currentNestingLevel > INITIAL_NESTING_LEVEL) {
                  postText.setBackgroundColor(context.color(R.color.colorQuotedTextBackground))
                  postText.setPadding(textPadding * currentNestingLevel, 0, 0, 0)
                }
                post_content_text_container.addView(postText)
              }
              is PostHiddenText -> {
                val template = context.stringRes(R.string.post_hidden_text_template)
                val hiddenText = TextView(context)
                hiddenText.textFromHtml(it.text)
                hiddenText.text = String.format(Locale.getDefault(), template, hiddenText.text)
                hiddenText.textSize = smallFontSize
                post_content_text_container.addView(hiddenText)
              }
              is PostScript -> {
                val postScriptText = TextView(context)
                postScriptText.setTypeface(postScriptText.typeface, Typeface.ITALIC)
                postScriptText.textFromHtml(it.text)
                postScriptText.textSize = smallFontSize
                postScriptText.textColor = R.color.colorPostScriptText
                post_content_text_container.addView(postScriptText)
              }
              is PostLink -> {
                val targetUrl = when {
                  it.url.startsWith("/go") -> "http://www.yaplakal.com${it.url}"
                  else -> it.url
                }

                val targetTitle = when {
                  it.title.startsWith("http") ||
                      it.title.length > MAX_LINK_TITLE_LENGTH -> context.stringRes(
                      R.string.post_link)
                  else -> it.title
                }

                links.add(PostLink(url = targetUrl, title = targetTitle))
              }
            }
          }
        }

        if (links.isNotEmpty()) {
          val link = links.last()
          post_link_button.setOnClickListener { context.browse(url = link.url, newTask = true) }
          post_link_button.text = link.title
          post_link_button.showView()
        } else {
          post_link_button.hideView()
        }
      }
      return post
    }

    private fun fillPostMedia(post: ParsedPost) {

      val imagePadding = itemView.context.resources.getDimension(
          R.dimen.post_image_vertical_padding).toInt()

      with(itemView) {
        if (post.images.isNotEmpty()) {
          post_content_image_container.showView()
          post_content_image_container.removeAllViews()
          post.images.forEach {
            val image = ImageView(context)
            image.adjustViewBounds = true
            image.setPadding(0, imagePadding, 0, imagePadding)
            post_content_image_container.addView(image)
            val url = if (it.startsWith("http")) it else "http:$it"
            image.loadFromUrl(url)
            image.setOnClickListener {
              context.startActivity<ImageDisplayActivity>("url" to url)
            }
          }
        } else {
          post_content_image_container.hideView()
        }

        // Videos
        if (post.videos.isNotEmpty() && post.videosRaw.isNotEmpty()) {
          post_content_video_container.showView()
          post_content_video_container.removeAllViews()
          post.videos.forEachIndexed { index, str ->
            val thumbnail = ImageView(context)
            thumbnail.adjustViewBounds = true
            thumbnail.setPadding(0, imagePadding, 0, imagePadding)
            post_content_video_container.addView(thumbnail)
            thumbnailsLoader.loadThumbnail(parseLink(str), thumbnail)
            thumbnail.setOnClickListener {
              context.startActivity<VideoDisplayActivity>("video" to post.videosRaw[index])
            }
          }
        } else {
          post_content_video_container.hideView()
        }
      }
    }
  }
}
