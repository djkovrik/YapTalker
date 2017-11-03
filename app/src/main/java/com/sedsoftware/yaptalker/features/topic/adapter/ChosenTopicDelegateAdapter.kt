package com.sedsoftware.yaptalker.features.topic.adapter

import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.base.BaseAdapterInjections
import com.sedsoftware.yaptalker.commons.adapter.ViewType
import com.sedsoftware.yaptalker.commons.adapter.ViewTypeDelegateAdapter
import com.sedsoftware.yaptalker.commons.extensions.color
import com.sedsoftware.yaptalker.commons.extensions.getLastDigits
import com.sedsoftware.yaptalker.commons.extensions.hideView
import com.sedsoftware.yaptalker.commons.extensions.inflate
import com.sedsoftware.yaptalker.commons.extensions.loadAvatarFromUrl
import com.sedsoftware.yaptalker.commons.extensions.loadFromUrl
import com.sedsoftware.yaptalker.commons.extensions.showView
import com.sedsoftware.yaptalker.commons.extensions.stringRes
import com.sedsoftware.yaptalker.commons.extensions.textColor
import com.sedsoftware.yaptalker.commons.extensions.textFromHtml
import com.sedsoftware.yaptalker.commons.extensions.textFromHtmlWithEmoji
import com.sedsoftware.yaptalker.commons.extensions.validateURL
import com.sedsoftware.yaptalker.data.parsing.ParsedPost
import com.sedsoftware.yaptalker.data.parsing.PostHiddenText
import com.sedsoftware.yaptalker.data.parsing.PostLink
import com.sedsoftware.yaptalker.data.parsing.PostQuote
import com.sedsoftware.yaptalker.data.parsing.PostQuoteAuthor
import com.sedsoftware.yaptalker.data.parsing.PostScript
import com.sedsoftware.yaptalker.data.parsing.PostText
import com.sedsoftware.yaptalker.data.parsing.PostWarning
import com.sedsoftware.yaptalker.data.parsing.TopicPost
import com.sedsoftware.yaptalker.data.video.getYoutubeVideoId
import com.sedsoftware.yaptalker.data.video.parseLink
import com.sedsoftware.yaptalker.features.imagedisplay.ImageDisplayActivity
import com.sedsoftware.yaptalker.features.videodisplay.VideoDisplayActivity
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_chosen_topic_item.view.*
import org.jetbrains.anko.browse
import org.jetbrains.anko.startActivity

class ChosenTopicDelegateAdapter(val profileClick: UserProfileClickListener) :
    BaseAdapterInjections(), ViewTypeDelegateAdapter {

  companion object {
    private const val INITIAL_NESTING_LEVEL = 0
  }

  override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
    return PostViewHolder(parent)
  }

  override fun onBindViewHolder(holder: ViewHolder, item: ViewType) {
    holder as PostViewHolder
    holder.bindTo(item as TopicPost)
  }

  inner class PostViewHolder(parent: ViewGroup) :
      RecyclerView.ViewHolder(parent.inflate(R.layout.fragment_chosen_topic_item)) {

    fun bindTo(postItem: TopicPost) {
      getParsedPostSingle(postItem)
          .subscribeOn(Schedulers.computation())
          .observeOn(AndroidSchedulers.mainThread())
          .map { parsedPost -> fillPostText(parsedPost) }
          .map { parsedPost -> fillPostImages(parsedPost) }
          .map { parsedPost -> fillPostVideos(parsedPost) }
          .map { _ -> fillPostHeader(postItem) }
          .subscribe({ _ -> }, { _ -> })
    }

    private fun getParsedPostSingle(item: TopicPost): Single<ParsedPost> =
        Single.just(ParsedPost(item.postContent))

    @Suppress("NestedBlockDepth")
    private fun fillPostText(post: ParsedPost): ParsedPost {

      val textPadding = itemView.context.resources.getDimension(
          R.dimen.post_text_horizontal_padding).toInt()
      var currentNestingLevel = INITIAL_NESTING_LEVEL
      val links = HashSet<PostLink>()
      val warnings = ArrayList<PostWarning>()

      itemView.post_content_text_container.removeAllViews()

      if (post.content.isNotEmpty()) {
        itemView.post_content_text_container.showView()

        post.content.forEach {

          when (it) {
            is PostQuoteAuthor -> {
              currentNestingLevel++
              val quoteAuthor = TextView(itemView.context)
              quoteAuthor.textFromHtml(it.text)
              quoteAuthor.textSize = normalFontSize
              if (currentNestingLevel > INITIAL_NESTING_LEVEL) {
                quoteAuthor.setPadding(textPadding * currentNestingLevel, 0, 0, 0)
              }
              quoteAuthor.setBackgroundColor(
                  itemView.context.color(R.color.colorQuotedTextBackground))
              itemView.post_content_text_container.addView(quoteAuthor)
            }
            is PostQuote -> {
              val quoteText = TextView(itemView.context)
              quoteText.textFromHtmlWithEmoji(it.text)
              quoteText.textSize = normalFontSize
              quoteText.setBackgroundColor(
                  itemView.context.color(R.color.colorQuotedTextBackground))
              quoteText.setPadding(textPadding * currentNestingLevel, 0, 0, 0)
              itemView.post_content_text_container.addView(quoteText)
            }
            is PostText -> {
              currentNestingLevel--
              val postText = TextView(itemView.context)
              postText.textFromHtmlWithEmoji(it.text)
              postText.textSize = normalFontSize
              if (currentNestingLevel > INITIAL_NESTING_LEVEL) {
                postText.setBackgroundColor(
                    itemView.context.color(R.color.colorQuotedTextBackground))
                postText.setPadding(textPadding * currentNestingLevel, 0, 0, 0)
              }
              itemView.post_content_text_container.addView(postText)
            }
            is PostHiddenText -> {
              val hiddenText = TextView(itemView.context)
              hiddenText.textFromHtmlWithEmoji(it.text)
              hiddenText.textSize = smallFontSize
              itemView.post_content_text_container.addView(hiddenText)
            }
            is PostScript -> {
              val postScriptText = TextView(itemView.context)
              postScriptText.setTypeface(postScriptText.typeface, Typeface.ITALIC)
              postScriptText.textFromHtml(it.text)
              postScriptText.textSize = smallFontSize
              postScriptText.textColor = R.color.colorPostScriptText
              itemView.post_content_text_container.addView(postScriptText)
            }
            is PostLink -> {
              val targetUrl = when {
                it.url.startsWith("/go") -> "http://www.yaplakal.com${it.url}"
                else -> it.url
              }

              val targetTitle = when {
                it.title.startsWith("http") -> itemView.context.stringRes(R.string.post_link)
                else -> it.title
              }

              links.add(PostLink(url = targetUrl, title = targetTitle))
            }

            is PostWarning -> {
              warnings.add(it)
            }
          }
        }

        if (links.isNotEmpty()) {
          val link = links.last()
          itemView.post_link_button.setOnClickListener {
            itemView.context.browse(url = link.url, newTask = true)
          }
          itemView.post_link_button.text = link.title
          itemView.post_link_button.showView()
        } else {
          itemView.post_link_button.hideView()
        }

        if (warnings.isNotEmpty()) {
          val warning = warnings.last()
          val warningText = TextView(itemView.context)
          warningText.textFromHtml(warning.text)
          warningText.textSize = smallFontSize
          itemView.post_content_text_container.addView(warningText)
        }
      }

      return post
    }

    private fun fillPostImages(post: ParsedPost): ParsedPost {

      val imagePadding = itemView.context.resources.getDimension(
          R.dimen.post_image_vertical_padding).toInt()

      if (post.images.isNotEmpty()) {
        itemView.post_content_image_container.showView()
        itemView.post_content_image_container.removeAllViews()
        post.images.forEach { url ->
          val image = ImageView(itemView.context)
          image.adjustViewBounds = true
          image.setPadding(0, imagePadding, 0, imagePadding)
          itemView.post_content_image_container.addView(image)
          image.loadFromUrl(url)
          image.setOnClickListener {
            itemView.context.startActivity<ImageDisplayActivity>("url" to url)
          }
        }
      } else {
        itemView.post_content_image_container.hideView()
      }

      return post
    }

    private fun fillPostVideos(post: ParsedPost) {

      val imagePadding = itemView.context.resources.getDimension(
          R.dimen.post_image_vertical_padding).toInt()

      if (post.videos.isNotEmpty() && post.videosRaw.isNotEmpty()) {
        itemView.post_content_video_container.showView()
        itemView.post_content_video_container.removeAllViews()
        post.videos.forEachIndexed { index, url ->
          val rawHtml = post.videosRaw[index]
          val thumbnail = ImageView(itemView.context)
          thumbnail.adjustViewBounds = true
          thumbnail.setPadding(0, imagePadding, 0, imagePadding)
          itemView.post_content_video_container.addView(thumbnail)
          thumbnailsLoader.loadThumbnail(parseLink(url), thumbnail)

          thumbnail.setOnClickListener {
            if (url.contains("youtube")) {
              itemView.context.browse("http://www.youtube.com/watch?v=${getYoutubeVideoId(url)}")
            } else {
              itemView.context.startActivity<VideoDisplayActivity>("videoHtml" to rawHtml)
            }
          }
        }
      } else {
        itemView.post_content_video_container.hideView()
      }
    }

    private fun fillPostHeader(post: TopicPost) {
      with(itemView) {
        post_author.text = post.authorNickname
        post_date.shortDateText = post.postDate
        post_rating.ratingText = post.postRank

        post_author.textSize = normalFontSize
        post_date.textSize = normalFontSize
        post_rating.textSize = normalFontSize

        post_author_avatar.loadAvatarFromUrl(post.authorAvatar.validateURL())
        post_author_avatar.setOnClickListener {
          profileClick.onUserAvatarClick(post.authorProfile.getLastDigits())
        }
      }
    }
  }
}
