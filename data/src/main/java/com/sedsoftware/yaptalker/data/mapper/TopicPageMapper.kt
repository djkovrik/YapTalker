package com.sedsoftware.yaptalker.data.mapper

import com.sedsoftware.yaptalker.data.mapper.util.PostContentParser
import com.sedsoftware.yaptalker.data.parsed.TopicPageParsed
import com.sedsoftware.yaptalker.data.parsed.TopicTagParsed
import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.entity.base.NavigationPanel
import com.sedsoftware.yaptalker.domain.entity.base.SinglePost
import com.sedsoftware.yaptalker.domain.entity.base.Tag
import com.sedsoftware.yaptalker.domain.entity.base.TopicInfoBlock
import io.reactivex.functions.Function
import java.util.ArrayList
import javax.inject.Inject

class TopicPageMapper @Inject constructor() : Function<TopicPageParsed, List<BaseEntity>> {

    companion object {
        private const val POSTS_PER_PAGE = 25
        private const val SECOND_NAV_PANEL_THRESHOLD = 3
    }

    override fun apply(from: TopicPageParsed): List<BaseEntity> {

        val result: MutableList<BaseEntity> = ArrayList(POSTS_PER_PAGE)

        with(from) {
            result.add(
                TopicInfoBlock(
                    topicTitle = topicTitle,
                    isClosed = isClosed.isNotEmpty(),
                    authKey = authKey,
                    topicRating = topicRating.toInt(),
                    topicRatingPlusAvailable = topicRatingPlusAvailable.isNotEmpty(),
                    topicRatingMinusAvailable = topicRatingMinusAvailable.isNotEmpty(),
                    topicRatingPlusClicked = topicRatingPlusClicked.isNotEmpty(),
                    topicRatingMinusClicked = topicRatingMinusClicked.isNotEmpty(),
                    topicRatingTargetId = topicRatingTargetId.toInt()
                )
            )

            result.add(
                NavigationPanel(
                    currentPage = navigation.currentPage.toInt(),
                    totalPages = navigation.totalPages.toInt()
                )
            )

            posts.forEach { post ->
                result.add(
                    SinglePost(
                        authorNickname = post.authorNickname,
                        authorProfile = post.authorProfile,
                        authorAvatar = post.authorAvatar,
                        authorMessagesCount = post.authorMessagesCount.toInt(),
                        postDate = post.postDate,
                        postRank = post.postRank.toInt(),
                        postRankPlusAvailable = post.postRankPlusAvailable.isNotEmpty(),
                        postRankMinusAvailable = post.postRankMinusAvailable.isNotEmpty(),
                        postRankPlusClicked = post.postRankPlusClicked.isNotEmpty(),
                        postRankMinusClicked = post.postRankMinusClicked.isNotEmpty(),
                        postContentParsed = PostContentParser(post.postContent).getParsedPost(),
                        postId = post.postId.toInt(),
                        hasQuoteButton = post.hasQuoteButton.isNotEmpty(),
                        hasEditButton = post.hasEditButton.isNotEmpty(),
                        tags = post.tags.map { mapTag(it) }
                    )
                )
            }

            if (posts.size >= SECOND_NAV_PANEL_THRESHOLD) {
                result.add(
                    NavigationPanel(
                        currentPage = navigation.currentPage.toInt(),
                        totalPages = navigation.totalPages.toInt()
                    )
                )
            }
        }

        return result
    }

    private fun mapTag(from: TopicTagParsed): Tag =
        Tag(
            name = from.name,
            link = from.link,
            searchParameter = from.name.substringAfter("#")
        )
}
