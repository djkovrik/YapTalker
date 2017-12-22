package com.sedsoftware.yaptalker.presentation.mappers

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.entity.base.NavigationPanel
import com.sedsoftware.yaptalker.domain.entity.base.PostContent
import com.sedsoftware.yaptalker.domain.entity.base.PostContent.PostHiddenText
import com.sedsoftware.yaptalker.domain.entity.base.PostContent.PostQuote
import com.sedsoftware.yaptalker.domain.entity.base.PostContent.PostQuoteAuthor
import com.sedsoftware.yaptalker.domain.entity.base.PostContent.PostScript
import com.sedsoftware.yaptalker.domain.entity.base.PostContent.PostText
import com.sedsoftware.yaptalker.domain.entity.base.PostContent.PostWarning
import com.sedsoftware.yaptalker.domain.entity.base.SinglePost
import com.sedsoftware.yaptalker.domain.entity.base.SinglePostParsed
import com.sedsoftware.yaptalker.domain.entity.base.TopicInfoBlock
import com.sedsoftware.yaptalker.presentation.mappers.util.TextTransformer
import com.sedsoftware.yaptalker.presentation.model.YapEntity
import com.sedsoftware.yaptalker.presentation.model.base.NavigationPanelModel
import com.sedsoftware.yaptalker.presentation.model.base.PostContentModel
import com.sedsoftware.yaptalker.presentation.model.base.PostContentModel.PostHiddenTextModel
import com.sedsoftware.yaptalker.presentation.model.base.PostContentModel.PostQuoteAuthorModel
import com.sedsoftware.yaptalker.presentation.model.base.PostContentModel.PostQuoteModel
import com.sedsoftware.yaptalker.presentation.model.base.PostContentModel.PostScriptModel
import com.sedsoftware.yaptalker.presentation.model.base.PostContentModel.PostTextModel
import com.sedsoftware.yaptalker.presentation.model.base.PostContentModel.PostWarningModel
import com.sedsoftware.yaptalker.presentation.model.base.SinglePostModel
import com.sedsoftware.yaptalker.presentation.model.base.SinglePostParsedModel
import com.sedsoftware.yaptalker.presentation.model.base.TopicInfoBlockModel
import javax.inject.Inject

/**
 * Mapper class used to transform single topic entities list from the domain layer into YapEntity list
 * in the presentation layer.
 */
class TopicModelMapper @Inject constructor(
    private val textTransformer: TextTransformer) {

  fun transform(items: List<BaseEntity>): List<YapEntity> {

    val result: MutableList<YapEntity> = ArrayList(items.size)

    items.forEach { item ->
      when (item) {
        is TopicInfoBlock -> result.add(TopicInfoBlockModel(
            topicTitle = item.topicTitle,
            isClosed = item.isClosed,
            authKey = item.authKey,
            topicRating = item.topicRating,
            topicRatingPlusAvailable = item.topicRatingPlusAvailable,
            topicRatingMinusAvailable = item.topicRatingMinusAvailable,
            topicRatingPlusClicked = item.topicRatingPlusClicked,
            topicRatingMinusClicked = item.topicRatingMinusClicked,
            topicRatingTargetId = item.topicRatingTargetId
        ))

        is NavigationPanel -> result.add(NavigationPanelModel(
            currentPage = item.currentPage,
            totalPages = item.totalPages,
            navigationLabel = textTransformer.createNavigationLabel(item.currentPage, item.totalPages)
        ))

        is SinglePost -> result.add(SinglePostModel(
            authorNickname = item.authorNickname,
            authorProfile = item.authorProfile,
            authorAvatar = item.authorAvatar,
            authorMessagesCount = item.authorMessagesCount,
            postDate = item.postDate,
            postRank = item.postRank,
            postRankPlusAvailable = item.postRankPlusAvailable,
            postRankMinusAvailable = item.postRankMinusAvailable,
            postRankPlusClicked = item.postRankPlusClicked,
            postRankMinusClicked = item.postRankMinusClicked,
            postContentParsed = transform(item.postContentParsed),
            postId = item.postId
        ))
      }
    }

    return result
  }

  private fun transform(content: PostContent): PostContentModel =
      when (content) {
        is PostText -> PostTextModel(content.text)
        is PostQuote -> PostQuoteModel(content.text)
        is PostQuoteAuthor -> PostQuoteAuthorModel(content.text)
        is PostHiddenText -> PostHiddenTextModel(content.text)
        is PostScript -> PostScriptModel(content.text)
        is PostWarning -> PostWarningModel(content.text)
      }

  private fun transform(post: SinglePostParsed): SinglePostParsedModel =
      SinglePostParsedModel(
          content = post
              .content
              .map { item -> transform(item) }
              .toMutableList(),
          images = post.images,
          videos = post.videos,
          videosRaw = post.videosRaw
      )
}
