package com.sedsoftware.yaptalker.data.repository

import com.sedsoftware.yaptalker.data.network.site.YapLoader
import com.sedsoftware.yaptalker.data.parsed.mappers.TopicPageMapper
import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.repository.ChosenTopicRepository
import io.reactivex.Observable
import javax.inject.Inject

class YapChosenTopicRepository @Inject constructor(
    private val dataLoader: YapLoader,
    private val dataMapper: TopicPageMapper
) : ChosenTopicRepository {

  override fun getChosenTopic(forumId: Int, topicId: Int, startPostNumber: Int): Observable<List<BaseEntity>> =
      dataLoader
          .loadTopicPage(forumId, topicId, startPostNumber)
          .map { parsedTopicPage -> dataMapper.transform(parsedTopicPage) }
}
