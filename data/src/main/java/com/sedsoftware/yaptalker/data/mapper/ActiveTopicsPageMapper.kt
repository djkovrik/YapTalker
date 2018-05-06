package com.sedsoftware.yaptalker.data.mapper

import com.sedsoftware.yaptalker.data.extensions.getLastDigits
import com.sedsoftware.yaptalker.data.parsed.ActiveTopicsPageParsed
import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.entity.base.ActiveTopic
import com.sedsoftware.yaptalker.domain.entity.base.NavigationPanel
import io.reactivex.functions.Function
import java.util.ArrayList
import javax.inject.Inject

class ActiveTopicsPageMapper @Inject constructor() : Function<ActiveTopicsPageParsed, List<BaseEntity>> {

  companion object {
    private const val TOPICS_PER_PAGE = 25
  }

  override fun apply(from: ActiveTopicsPageParsed): List<BaseEntity> {

    val result: MutableList<BaseEntity> = ArrayList(TOPICS_PER_PAGE)

    with(from) {
      topics.forEach { topic ->
        result.add(
          ActiveTopic(
            title = topic.title,
            link = topic.link,
            id = topic.link.getLastDigits(),
            isPinned = topic.isPinned.isNotEmpty(),
            isClosed = topic.isClosed.isNotEmpty(),
            forumTitle = topic.forumTitle,
            forumLink = topic.forumLink,
            rating = topic.rating.toInt(),
            answers = topic.answers.toInt(),
            lastPostDate = topic.lastPostDate
          )
        )
      }

      result.add(
        NavigationPanel(
          currentPage = navigation.currentPage.toInt(),
          totalPages = navigation.totalPages.toInt()
        )
      )
    }

    return result
  }
}
