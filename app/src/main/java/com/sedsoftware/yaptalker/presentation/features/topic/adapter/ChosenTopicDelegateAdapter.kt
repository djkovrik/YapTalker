package com.sedsoftware.yaptalker.presentation.features.topic.adapter

import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.text.method.LinkMovementMethod
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.data.settings.SettingsManager
import com.sedsoftware.yaptalker.presentation.base.adapter.YapEntityDelegateAdapter
import com.sedsoftware.yaptalker.presentation.extensions.currentDensity
import com.sedsoftware.yaptalker.presentation.extensions.getColorFromAttr
import com.sedsoftware.yaptalker.presentation.extensions.hideView
import com.sedsoftware.yaptalker.presentation.extensions.inflate
import com.sedsoftware.yaptalker.presentation.extensions.loadAvatarFromUrl
import com.sedsoftware.yaptalker.presentation.extensions.loadFromUrl
import com.sedsoftware.yaptalker.presentation.extensions.showView
import com.sedsoftware.yaptalker.presentation.extensions.textFromHtmlWithEmoji
import com.sedsoftware.yaptalker.presentation.extensions.validateUrl
import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.sedsoftware.yaptalker.presentation.model.base.PostContentModel.PostHiddenTextModel
import com.sedsoftware.yaptalker.presentation.model.base.PostContentModel.PostQuoteAuthorModel
import com.sedsoftware.yaptalker.presentation.model.base.PostContentModel.PostQuoteModel
import com.sedsoftware.yaptalker.presentation.model.base.PostContentModel.PostScriptModel
import com.sedsoftware.yaptalker.presentation.model.base.PostContentModel.PostTextModel
import com.sedsoftware.yaptalker.presentation.model.base.PostContentModel.PostWarningModel
import com.sedsoftware.yaptalker.presentation.model.base.SinglePostModel
import com.sedsoftware.yaptalker.presentation.model.base.SinglePostParsedModel
import kotlinx.android.synthetic.main.fragment_chosen_topic_item.view.*

class ChosenTopicDelegateAdapter(
    val clickListener: ChosenTopicElementsClickListener,
    val thumbnailLoader: ChosenTopicThumbnailLoader,
    val settings: SettingsManager) : YapEntityDelegateAdapter {

  companion object {
    private const val INITIAL_NESTING_LEVEL = 0
  }

  private val normalFontSize by lazy {
    settings.getNormalFontSize()
  }

  private val smallFontSize by lazy {
    settings.getSmallFontSize()
  }

  private val avatarSize by lazy {
    settings.getAvatarSize()
  }

  override fun onCreateViewHolder(parent: ViewGroup): ViewHolder = PostViewHolder(parent)

  override fun onBindViewHolder(holder: ViewHolder, item: YapEntity, position: Int) {
    holder as PostViewHolder
    holder.bindTo(item as SinglePostModel, position)
  }

  inner class PostViewHolder(parent: ViewGroup) :
      RecyclerView.ViewHolder(parent.inflate(R.layout.fragment_chosen_topic_item)) {

    private val currentAvatarSize = avatarSize * parent.context.currentDensity
    private val textPadding = parent.context.resources.getDimension(R.dimen.post_text_horizontal_padding).toInt()
    private val primaryTextColor = parent.context.getColorFromAttr(android.R.attr.textColorPrimary)
    private val secondaryTextColor = parent.context.getColorFromAttr(android.R.attr.textColorSecondary)
    private val quoteBackgroundColor = parent.context.getColorFromAttr(R.attr.colorQuoteBackground)
    private val warnings = ArrayList<PostWarningModel>()

    fun bindTo(postItem: SinglePostModel, position: Int) {
      fillPostText(postItem.postContentParsed)
      fillPostImages(postItem.postContentParsed)
      fillPostVideos(postItem.postContentParsed)
      fillPostHeader(postItem, position)
    }

    @Suppress("NestedBlockDepth")
    private fun fillPostText(post: SinglePostParsedModel) {

      var currentNestingLevel = INITIAL_NESTING_LEVEL

      itemView.post_content_text_container.removeAllViews()

      if (post.content.isNotEmpty()) {
        itemView.post_content_text_container.showView()

        post.content.forEach { contentItem ->

          when (contentItem) {
            is PostQuoteAuthorModel -> {
              currentNestingLevel++
              val quoteAuthor = TextView(itemView.context)
              quoteAuthor.textSize = normalFontSize
              quoteAuthor.setTextColor(primaryTextColor)
              quoteAuthor.text = contentItem.text
              if (currentNestingLevel > INITIAL_NESTING_LEVEL) {
                quoteAuthor.setPadding(textPadding * currentNestingLevel, 0, 0, 0)
              }
              quoteAuthor.setBackgroundColor(quoteBackgroundColor)
              itemView.post_content_text_container.addView(quoteAuthor)
            }
            is PostQuoteModel -> {
              val quoteText = TextView(itemView.context)
              quoteText.textSize = normalFontSize
              quoteText.setTextColor(primaryTextColor)
              quoteText.textFromHtmlWithEmoji(contentItem.text)
              quoteText.setBackgroundColor(quoteBackgroundColor)
              quoteText.setPadding(textPadding * currentNestingLevel, 0, 0, 0)
              itemView.post_content_text_container.addView(quoteText)
            }
            is PostTextModel -> {
              currentNestingLevel--
              val postText = TextView(itemView.context)
              postText.movementMethod = LinkMovementMethod.getInstance()
              postText.textSize = normalFontSize
              postText.setTextColor(primaryTextColor)
              postText.textFromHtmlWithEmoji(contentItem.text)
              if (currentNestingLevel > INITIAL_NESTING_LEVEL) {
                postText.setBackgroundColor(quoteBackgroundColor)
                postText.setPadding(textPadding * currentNestingLevel, 0, 0, 0)
              }
              itemView.post_content_text_container.addView(postText)
            }
            is PostHiddenTextModel -> {
              val hiddenText = TextView(itemView.context)
              hiddenText.textSize = smallFontSize
              hiddenText.setTextColor(primaryTextColor)
              hiddenText.textFromHtmlWithEmoji(contentItem.text)
              itemView.post_content_text_container.addView(hiddenText)
            }
            is PostScriptModel -> {
              val postScriptText = TextView(itemView.context)
              postScriptText.setTypeface(postScriptText.typeface, Typeface.ITALIC)
              postScriptText.textSize = smallFontSize
              postScriptText.setTextColor(secondaryTextColor)
              postScriptText.text = contentItem.text
              itemView.post_content_text_container.addView(postScriptText)
            }
            is PostWarningModel -> {
              warnings.add(contentItem)
            }
          }
        }

        if (warnings.isNotEmpty()) {
          val warning = warnings.last()
          val warningText = TextView(itemView.context)
          warningText.textSize = smallFontSize
          warningText.setTextColor(secondaryTextColor)
          warningText.text = warning.text
          itemView.post_content_text_container.addView(warningText)
        }
      }
    }

    private fun fillPostImages(post: SinglePostParsedModel) {

      val imagePadding = itemView.context.resources.getDimension(
          R.dimen.post_image_vertical_padding).toInt()

      itemView.post_content_image_container.removeAllViews()
      itemView.post_content_image_container.hideView()

      if (post.images.isNotEmpty()) {
        itemView.post_content_image_container.showView()
        post.images.forEach { url ->
          val image = ImageView(itemView.context)
          image.adjustViewBounds = true
          image.setPadding(0, imagePadding, 0, imagePadding)
          itemView.post_content_image_container.addView(image)
          image.loadFromUrl(url)
          image.setOnClickListener { clickListener.onMediaPreviewClicked(url, "", false) }
        }
      }
    }

    private fun fillPostVideos(post: SinglePostParsedModel) {

      val imagePadding = itemView.context.resources.getDimension(
          R.dimen.post_image_vertical_padding).toInt()

      itemView.post_content_video_container.removeAllViews()
      itemView.post_content_video_container.hideView()

      if (post.videos.isNotEmpty() && post.videosRaw.isNotEmpty()) {
        itemView.post_content_video_container.showView()
        post.videos.forEachIndexed { index, url ->
          val rawHtml = post.videosRaw[index]
          val thumbnail = ImageView(itemView.context)
          thumbnail.adjustViewBounds = true
          thumbnail.setPadding(0, imagePadding, 0, imagePadding)
          itemView.post_content_video_container.addView(thumbnail)
          thumbnailLoader.loadThumbnail(url, thumbnail)
          thumbnail.setOnClickListener { clickListener.onMediaPreviewClicked(url, rawHtml, true) }
        }
      }
    }

    private fun fillPostHeader(post: SinglePostModel, position: Int) {
      with(itemView) {
        post_author.text = post.authorNickname
        post_date.text = post.postDate
        post_rating.text = post.postRankText

        post_rating.textSize = normalFontSize
        post_rating_thumb_up.textSize = normalFontSize
        post_rating_thumb_down.textSize = normalFontSize
        post_rating_thumb_up_available.textSize = normalFontSize
        post_rating_thumb_down_available.textSize = normalFontSize

        post_rating.hideView()
        post_rating_thumb_down.hideView()
        post_rating_thumb_up.hideView()
        post_rating_thumb_down_available.hideView()
        post_rating_thumb_up_available.hideView()

        when {
          post.postRankMinusClicked -> {
            post_rating.showView()
            post_rating_thumb_down.showView()
            post_rating_thumb_up_available.showView()
          }
          post.postRankPlusClicked -> {
            post_rating.showView()
            post_rating_thumb_up.showView()
            post_rating_thumb_down_available.showView()
          }
          post.postRankMinusAvailable && post.postRankPlusAvailable -> {
            post_rating.showView()
            post_rating_thumb_up_available.showView()
            post_rating_thumb_down_available.showView()
          }
          post.postRank != 0 -> {
            post_rating.showView()
          }
        }

        post_rating_block.setOnClickListener {
          val isKarmaAvailable = post.postRankPlusAvailable && post.postRankMinusAvailable
          clickListener.onPostItemClicked(post.postId, position, isKarmaAvailable)
        }

        post_author.textSize = normalFontSize
        post_date.textSize = normalFontSize
        post_rating.textSize = normalFontSize

        post_author_avatar.layoutParams.width = currentAvatarSize
        post_author_avatar.layoutParams.height = currentAvatarSize

        post_author_avatar.loadAvatarFromUrl(post.authorAvatar.validateUrl())

        post_author_avatar.setOnClickListener {
          clickListener.onUserAvatarClick(post.authorProfileId)
        }
      }

    }
  }
}
