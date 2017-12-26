package com.sedsoftware.yaptalker.data.service

import com.sedsoftware.yaptalker.data.network.site.YapLoader
import com.sedsoftware.yaptalker.data.parsed.mappers.TopicPageMapper
import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.service.SendMessageService
import io.reactivex.Observable
import javax.inject.Inject

class YapSendMessageService @Inject constructor(
    private val dataLoader: YapLoader,
    private val dataMapper: TopicPageMapper
) : SendMessageService {

  companion object {
    private const val POST_ACT = "Post"
    private const val POST_CODE = "03"
    private const val POST_MAX_FILE_SIZE = 512000
  }

  override fun requestMessageSending(
      targetForumId: Int, targetTopicId: Int, page: Int, authKey: String, message: String): Observable<BaseEntity> =
      dataLoader
          .postMessage(
              act = POST_ACT,
              code = POST_CODE,
              forum = targetForumId,
              topic = targetTopicId,
              st = page,
              enableemo = true,
              enablesig = true,
              authKey = authKey,
              postContent = message,
              maxFileSize = POST_MAX_FILE_SIZE,
              enabletag = 1
          )
          .map { response -> dataMapper.transform(response) }
          .flatMap { postList: List<BaseEntity> -> Observable.fromIterable(postList) }
}
