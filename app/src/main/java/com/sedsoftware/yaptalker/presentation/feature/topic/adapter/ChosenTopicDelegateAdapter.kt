package com.sedsoftware.yaptalker.presentation.feature.topic.adapter

import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.text.method.LinkMovementMethod
import android.view.Gravity
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.google.android.flexbox.FlexboxLayout
import com.robertlevonyan.views.chip.Chip
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.domain.device.Settings
import com.sedsoftware.yaptalker.presentation.base.adapter.YapEntityDelegateAdapter
import com.sedsoftware.yaptalker.presentation.extensions.colorFromAttr
import com.sedsoftware.yaptalker.presentation.extensions.currentDensity
import com.sedsoftware.yaptalker.presentation.extensions.inflate
import com.sedsoftware.yaptalker.presentation.extensions.loadAvatarFromUrl
import com.sedsoftware.yaptalker.presentation.extensions.loadFromUrl
import com.sedsoftware.yaptalker.presentation.extensions.string
import com.sedsoftware.yaptalker.presentation.extensions.textFromHtmlWithEmoji
import com.sedsoftware.yaptalker.presentation.extensions.validateUrl
import com.sedsoftware.yaptalker.presentation.model.DisplayedItemModel
import com.sedsoftware.yaptalker.presentation.model.base.PostContentModel.PostHiddenTextModel
import com.sedsoftware.yaptalker.presentation.model.base.PostContentModel.PostQuoteAuthorModel
import com.sedsoftware.yaptalker.presentation.model.base.PostContentModel.PostQuoteModel
import com.sedsoftware.yaptalker.presentation.model.base.PostContentModel.PostScriptModel
import com.sedsoftware.yaptalker.presentation.model.base.PostContentModel.PostTextModel
import com.sedsoftware.yaptalker.presentation.model.base.PostContentModel.PostWarningModel
import com.sedsoftware.yaptalker.presentation.model.base.SinglePostModel
import com.sedsoftware.yaptalker.presentation.model.base.SinglePostParsedModel
import com.sedsoftware.yaptalker.presentation.provider.ThumbnailsProvider
import kotlinx.android.synthetic.main.fragment_chosen_topic_item.view.*
import java.util.ArrayList

@Suppress("MagicNumber", "LargeClass")
class ChosenTopicDelegateAdapter(
    private val clickListener: ChosenTopicElementsClickListener,
    private val thumbnailProvider: ThumbnailsProvider,
    private val settings: Settings
) : YapEntityDelegateAdapter {

    companion object {
        private const val INITIAL_NESTING_LEVEL = 0
        private const val OVERLAY_MARGIN = 8
        private const val OVERLAY_TEXT_PADDING = 2
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

    override fun onBindViewHolder(holder: ViewHolder, item: DisplayedItemModel) {
        holder as PostViewHolder
        holder.bindTo(item as SinglePostModel)
    }

    inner class PostViewHolder(parent: ViewGroup) :
        RecyclerView.ViewHolder(parent.inflate(R.layout.fragment_chosen_topic_item)) {

        private val currentAvatarSize = avatarSize * parent.context.currentDensity
        private val textPadding = parent.context.resources.getDimension(R.dimen.post_text_horizontal_padding).toInt()
        private val primaryTextColor = parent.context.colorFromAttr(android.R.attr.textColorPrimary)
        private val secondaryTextColor = parent.context.colorFromAttr(android.R.attr.textColorSecondary)
        private val quoteBackgroundColor = parent.context.colorFromAttr(R.attr.colorQuoteBackground)
        private val warnings = ArrayList<PostWarningModel>()

        fun bindTo(postItem: SinglePostModel) {
            fillPostText(postItem.postContentParsed)
            fillPostImages(postItem.postContentParsed)
            fillPostVideos(postItem.postContentParsed)
            fillControls(postItem)
            fillTags(postItem)
        }

        @Suppress("NestedBlockDepth")
        private fun fillPostText(post: SinglePostParsedModel) {

            var currentNestingLevel = INITIAL_NESTING_LEVEL

            itemView.post_content_text_container.removeAllViews()
            warnings.clear()

            if (post.content.isNotEmpty()) {
                itemView.post_content_text_container.isVisible = true

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
                R.dimen.post_image_vertical_padding
            ).toInt()

            itemView.post_content_image_container.removeAllViews()
            itemView.post_content_image_container.isGone = true

            if (post.images.isNotEmpty()) {
                itemView.post_content_image_container.isVisible = true

                post.images.forEach { url ->

                    // Thumbnail
                    val image = ImageView(itemView.context)
                    image.adjustViewBounds = true
                    image.setPadding(0, imagePadding, 0, imagePadding)

                    // Overlay
                    val overlay = TextView(itemView.context)
                    overlay.setBackgroundResource(R.drawable.bg_primary_solid)
                    overlay.setPadding(
                        OVERLAY_TEXT_PADDING,
                        OVERLAY_TEXT_PADDING,
                        OVERLAY_TEXT_PADDING,
                        OVERLAY_TEXT_PADDING
                    )
                    overlay.gravity = Gravity.START or Gravity.TOP

                    overlay.text =
                            if (url.endsWith(".gif", true)) {
                                itemView.context.string(R.string.video_gif)
                            } else {
                                overlay.isInvisible = true
                                ""
                            }

                    // Container
                    val container = FrameLayout(itemView.context)
                    val containerParams =
                        FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
                    container.layoutParams = containerParams

                    container.addView(image)
                    container.addView(overlay)

                    val verticalMargin = OVERLAY_MARGIN + imagePadding
                    val overlayParams = (overlay.layoutParams as FrameLayout.LayoutParams).apply {
                        width = FrameLayout.LayoutParams.WRAP_CONTENT
                        height = FrameLayout.LayoutParams.WRAP_CONTENT
                        setMargins(OVERLAY_MARGIN, verticalMargin, OVERLAY_MARGIN, verticalMargin)
                    }
                    overlay.layoutParams = overlayParams

                    itemView.post_content_image_container.addView(container)

                    // Load thumbnail
                    image.loadFromUrl(url)
                    image.setOnClickListener { clickListener.onMediaPreviewClicked(url, "", false) }
                }
            }
        }

        private fun fillPostVideos(post: SinglePostParsedModel) {

            val imagePadding = itemView.context.resources.getDimension(
                R.dimen.post_image_vertical_padding
            ).toInt()

            itemView.post_content_video_container.removeAllViews()
            itemView.post_content_video_container.isGone = true

            if (post.videos.isNotEmpty() && post.videosRaw.isNotEmpty()) {
                itemView.post_content_video_container.isVisible = true
                post.videos.forEachIndexed { index, url ->

                    val rawHtml = post.videosRaw[index]

                    // Thumbnail
                    val thumbnail = ImageView(itemView.context)
                    thumbnail.adjustViewBounds = true
                    thumbnail.setPadding(0, imagePadding, 0, imagePadding)

                    // Overlay
                    val overlay = TextView(itemView.context)
                    overlay.text = post.videoTypes[index]
                    overlay.setBackgroundResource(R.drawable.bg_primary_solid)
                    overlay.setPadding(
                        OVERLAY_TEXT_PADDING,
                        OVERLAY_TEXT_PADDING,
                        OVERLAY_TEXT_PADDING,
                        OVERLAY_TEXT_PADDING
                    )
                    overlay.gravity = Gravity.START or Gravity.TOP

                    // Container
                    val container = FrameLayout(itemView.context)
                    val containerParams =
                        FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
                    container.layoutParams = containerParams


                    container.addView(thumbnail)
                    container.addView(overlay)

                    val verticalMargin = OVERLAY_MARGIN + imagePadding
                    val overlayParams = (overlay.layoutParams as FrameLayout.LayoutParams).apply {
                        width = FrameLayout.LayoutParams.WRAP_CONTENT
                        height = FrameLayout.LayoutParams.WRAP_CONTENT
                        setMargins(OVERLAY_MARGIN, verticalMargin, OVERLAY_MARGIN, verticalMargin)
                    }
                    overlay.layoutParams = overlayParams

                    itemView.post_content_video_container.addView(container)

                    // Load thumbnail
                    thumbnailProvider.loadThumbnail(url, thumbnail)

                    thumbnail.setOnClickListener {

                        if (settings.isExternalYapPlayer()) {
                            clickListener.onMediaPreviewClicked(post.videosLinks[index], rawHtml, true)
                        } else {
                            clickListener.onMediaPreviewClicked(url, rawHtml, true)
                        }


                    }
                }
            }
        }

        private fun fillControls(post: SinglePostModel) {
            with(itemView) {
                post_author.text = post.authorNickname
                post_date.text = post.postDate
                post_rating.text = post.postRankText

                post_rating.textSize = normalFontSize
                post_rating_thumb_up.textSize = normalFontSize
                post_rating_thumb_down.textSize = normalFontSize
                post_rating_thumb_up_available.textSize = normalFontSize
                post_rating_thumb_down_available.textSize = normalFontSize

                post_rating.isGone = true
                post_rating_thumb_down.isGone = true
                post_rating_thumb_up.isGone = true
                post_rating_thumb_down_available.isGone = true
                post_rating_thumb_up_available.isGone = true

                when {
                    post.postRankMinusClicked -> {
                        post_rating.isVisible = true
                        post_rating_thumb_down.isVisible = true
                        post_rating_thumb_up_available.isVisible = true
                    }
                    post.postRankPlusClicked -> {
                        post_rating.isVisible = true
                        post_rating_thumb_up.isVisible = true
                        post_rating_thumb_down_available.isVisible = true
                    }
                    post.postRankMinusAvailable && post.postRankPlusAvailable -> {
                        post_rating.isVisible = true
                        post_rating_thumb_up_available.isVisible = true
                        post_rating_thumb_down_available.isVisible = true
                    }
                    post.postRank != 0 -> {
                        post_rating.isVisible = true
                    }
                }

                post_rating_block.setOnClickListener {
                    val isKarmaAvailable = post.postRankPlusAvailable && post.postRankMinusAvailable
                    clickListener.onPostItemClicked(post.postId, isKarmaAvailable)
                }

                post_author.textSize = normalFontSize
                post_date.textSize = normalFontSize
                post_rating.textSize = normalFontSize

                post_author_avatar.layoutParams.width = currentAvatarSize
                post_author_avatar.layoutParams.height = currentAvatarSize
                post_author_avatar.loadAvatarFromUrl(post.authorAvatar.validateUrl())
                post_author_avatar.setOnClickListener { clickListener.onUserAvatarClicked(post.authorProfileId) }

                if (post.hasQuoteButton) {
                    post_button_reply.isVisible = true
                    post_button_reply.setOnClickListener {
                        clickListener.onReplyButtonClicked(post.authorNickname, post.postDateFull, post.postId)
                    }
                } else {
                    post_button_reply.isGone = true
                }

                if (post.hasEditButton) {
                    post_button_edit.isVisible = true
                    post_button_edit.setOnClickListener {
                        clickListener.onEditButtonClicked(post.postId)
                    }
                } else {
                    post_button_edit.isGone = true
                }
            }
        }

        @Suppress("NestedBlockDepth")
        private fun fillTags(post: SinglePostModel) {
            with(itemView) {
                if (post.tags.isNotEmpty()) {
                    post_content_tags_container.isVisible = true

                    post.tags.forEach { tag ->
                        val chip = Chip(context)
                        chip.chipText = tag.name
                        chip.gravity = Gravity.CENTER
                        chip.cornerRadius = 16
                        chip.strokeSize = 2
                        chip.strokeColor = context.colorFromAttr(R.attr.colorAccent)
                        chip.changeBackgroundColor(context.colorFromAttr(R.attr.chipBackground))
                        chip.textColor = context.colorFromAttr(R.attr.colorNavDefaultText)
                        chip.setOnChipClickListener { clickListener.onTopicTagClicked(tag) }

                        post_content_tags_container.addView(chip)

                        val params = (chip.layoutParams as FlexboxLayout.LayoutParams).apply {
                            marginStart = 8
                            marginEnd = 8
                            bottomMargin = 8
                            topMargin = 8
                        }
                        chip.layoutParams = params
                    }
                } else {
                    post_content_tags_container.isGone = true
                }
            }
        }
    }
}
