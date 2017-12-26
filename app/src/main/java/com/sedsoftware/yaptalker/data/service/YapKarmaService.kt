package com.sedsoftware.yaptalker.data.service

import com.sedsoftware.yaptalker.data.network.site.YapLoader
import com.sedsoftware.yaptalker.data.parsed.mappers.ServerResponseMapper
import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.service.KarmaService
import io.reactivex.Observable
import javax.inject.Inject

class YapKarmaService @Inject constructor(
    private val dataLoader: YapLoader,
    private val dataMapper: ServerResponseMapper
) : KarmaService {

  companion object {
    private const val KARMA_ACT = "ST"
    private const val KARMA_CODE = "vote_post"
    private const val KARMA_TYPE_TOPIC = 1
    private const val KARMA_TYPE_POST = 0
  }

  override fun requestTopicKarmaChange(targetPostId: Int, targetTopicId: Int, diff: Int): Observable<BaseEntity> =
      dataLoader
          .changeKarma(
              act = KARMA_ACT,
              code = KARMA_CODE,
              rank = diff,
              postId = targetPostId,
              topicId = targetTopicId,
              type = KARMA_TYPE_TOPIC
          )
          .map { response -> dataMapper.transform(response) }

  override fun requestPostKarmaChange(targetPostId: Int, targetTopicId: Int, diff: Int): Observable<BaseEntity> =
      dataLoader
          .changeKarma(
              act = KARMA_ACT,
              code = KARMA_CODE,
              rank = diff,
              postId = targetPostId,
              topicId = targetTopicId,
              type = KARMA_TYPE_POST
          )
          .map { response -> dataMapper.transform(response) }
}
