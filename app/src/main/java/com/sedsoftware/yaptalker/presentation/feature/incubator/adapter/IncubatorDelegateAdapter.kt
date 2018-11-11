package com.sedsoftware.yaptalker.presentation.feature.incubator.adapter

import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.domain.device.Settings
import com.sedsoftware.yaptalker.presentation.base.adapter.YapEntityDelegateAdapter
import com.sedsoftware.yaptalker.presentation.extensions.inflate
import com.sedsoftware.yaptalker.presentation.extensions.loadFromUrlAndRoundCorners
import com.sedsoftware.yaptalker.presentation.model.DisplayedItemModel
import com.sedsoftware.yaptalker.presentation.model.base.IncubatorItemModel
import com.sedsoftware.yaptalker.presentation.provider.ThumbnailsProvider
import kotlinx.android.synthetic.main.fragment_incubator_item.view.incubator_topic_author
import kotlinx.android.synthetic.main.fragment_incubator_item.view.incubator_topic_comments_counter
import kotlinx.android.synthetic.main.fragment_incubator_item.view.incubator_topic_content_image
import kotlinx.android.synthetic.main.fragment_incubator_item.view.incubator_topic_content_image_container
import kotlinx.android.synthetic.main.fragment_incubator_item.view.incubator_topic_content_image_overlay
import kotlinx.android.synthetic.main.fragment_incubator_item.view.incubator_topic_content_text
import kotlinx.android.synthetic.main.fragment_incubator_item.view.incubator_topic_date
import kotlinx.android.synthetic.main.fragment_incubator_item.view.incubator_topic_forum
import kotlinx.android.synthetic.main.fragment_incubator_item.view.incubator_topic_rating
import kotlinx.android.synthetic.main.fragment_incubator_item.view.incubator_topic_title

class IncubatorDelegateAdapter(
    private val clickListener: IncubatorElementsClickListener,
    private val thumbnailsProvider: ThumbnailsProvider,
    private val settings: Settings
) : YapEntityDelegateAdapter {

    private val normalFontSize by lazy {
        settings.getNormalFontSize()
    }

    private val bigFontSize by lazy {
        settings.getBigFontSize()
    }

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder = IncubatorViewHolder(parent)

    override fun onBindViewHolder(holder: ViewHolder, item: DisplayedItemModel) {
        holder as IncubatorViewHolder
        holder.bindTo(item as IncubatorItemModel)
    }

    inner class IncubatorViewHolder(parent: ViewGroup) :
        RecyclerView.ViewHolder(parent.inflate(R.layout.fragment_incubator_item)) {

        fun bindTo(incubatorItem: IncubatorItemModel) {
            setViewsTextSize(itemView)
            setViewsContent(itemView, incubatorItem)
            setMediaContent(itemView, incubatorItem)
        }

        private fun setViewsTextSize(itemView: View) {

            with(itemView) {
                incubator_topic_author.textSize = normalFontSize
                incubator_topic_title.textSize = bigFontSize
                incubator_topic_forum.textSize = normalFontSize
                incubator_topic_date.textSize = normalFontSize
                incubator_topic_rating.textSize = normalFontSize
                incubator_topic_comments_counter.textSize = normalFontSize
                incubator_topic_content_text.textSize = normalFontSize
            }
        }

        private fun setViewsContent(itemView: View, incubatorItem: IncubatorItemModel) {

            with(itemView) {
                incubator_topic_author.text = incubatorItem.author
                incubator_topic_title.text = incubatorItem.title
                incubator_topic_forum.text = incubatorItem.forumName
                incubator_topic_date.text = incubatorItem.date
                incubator_topic_rating.text = incubatorItem.rating
                incubator_topic_comments_counter.text = incubatorItem.comments
                incubator_topic_content_text.text = incubatorItem.cleanedDescription
            }
        }

        private fun setMediaContent(itemView: View, incubatorItem: IncubatorItemModel) {

            with(itemView) {
                val layoutParams = incubator_topic_content_image.layoutParams as ConstraintLayout.LayoutParams
                layoutParams.dimensionRatio = "16:9"

                incubator_topic_content_image.setOnClickListener(null)
                incubator_topic_content_image.setImageDrawable(null)
                incubator_topic_content_image_container.isGone = true
                incubator_topic_content_image_overlay.isGone = true

                if (incubatorItem.images.isNotEmpty()) {
                    val url = incubatorItem.images.first()
                    incubator_topic_content_image_container.isVisible = true
                    val params = incubator_topic_content_image.layoutParams as ConstraintLayout.LayoutParams
                    params.width = ConstraintLayout.LayoutParams.MATCH_PARENT
                    params.height = ConstraintLayout.LayoutParams.WRAP_CONTENT
                    params.dimensionRatio = ""
                    incubator_topic_content_image.layoutParams = params
                    incubator_topic_content_image.loadFromUrlAndRoundCorners(url)

                    incubator_topic_content_image.setOnClickListener {
                        clickListener.onMediaPreviewClicked(url, "", "", false)
                    }
                } else if (incubatorItem.videos.isNotEmpty() && incubatorItem.videosRaw.isNotEmpty()) {
                    val url = incubatorItem.videos.first()
                    val directUrl = incubatorItem.videosLinks.first()

                    val rawVideo = incubatorItem.videosRaw.first()
                    val videoType = incubatorItem.videoTypes.first()
                    incubator_topic_content_image_container.isVisible = true
                    incubator_topic_content_image_overlay.isVisible = true
                    incubator_topic_content_image_overlay.text = videoType
                    val params = incubator_topic_content_image.layoutParams as ConstraintLayout.LayoutParams
                    params.width = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT
                    params.height = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT
                    params.dimensionRatio = "16:9"
                    incubator_topic_content_image.layoutParams = params
                    thumbnailsProvider.loadThumbnail(url, incubator_topic_content_image)

                    incubator_topic_content_image.setOnClickListener {
                        clickListener.onMediaPreviewClicked(url, rawVideo, directUrl, true)
                    }
                }

                setOnClickListener {
                    if (incubatorItem.isYapLink) {
                        clickListener.onIncubatorItemClicked(incubatorItem.forumId, incubatorItem.topicId)
                    }
                }
            }
        }
    }
}
