package com.sedsoftware.yaptalker.data.repository

import com.sedsoftware.yaptalker.data.exception.RequestErrorException
import com.sedsoftware.yaptalker.data.mappers.EditedPostMapper
import com.sedsoftware.yaptalker.data.mappers.QuotedPostMapper
import com.sedsoftware.yaptalker.data.mappers.ServerResponseMapper
import com.sedsoftware.yaptalker.data.mappers.TopicPageMapper
import com.sedsoftware.yaptalker.data.network.site.YapLoader
import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.entity.base.ServerResponse
import com.sedsoftware.yaptalker.domain.repository.ChosenTopicRepository
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class YapChosenTopicRepository @Inject constructor(
  private val dataLoader: YapLoader,
  private val dataMapper: TopicPageMapper,
  private val quoteMapper: QuotedPostMapper,
  private val editedPostMapper: EditedPostMapper,
  private val responseMapper: ServerResponseMapper
) : ChosenTopicRepository {

  companion object {
    private const val KARMA_ACT = "ST"
    private const val KARMA_CODE = "vote_post"
    private const val KARMA_TYPE_TOPIC = 1
    private const val KARMA_TYPE_POST = 0

    private const val POST_ACT = "Post"
    private const val POST_CODE = "03"
    private const val POST_MAX_FILE_SIZE = 512000

    private const val POST_EDIT_CODE = "09"

    private const val FILE_PART_NAME = "FILE_UPLOAD"
    private const val UPLOADED_FILE_TYPE = "image/jpeg"

    private const val MESSAGE_SENDING_ERROR_MARKER = "Возникли следующие трудности"
  }

  override fun getChosenTopic(forumId: Int,
                              topicId: Int,
                              startPostNumber: Int): Single<List<BaseEntity>> =
    dataLoader
      .loadTopicPage(forumId, topicId, startPostNumber)
      .map(dataMapper)

  override fun requestPostTextAsQuote(forumId: Int,
                                      topicId: Int,
                                      targetPostId: Int): Single<BaseEntity> =
    dataLoader
      .loadTargetPostQuotedText(forumId, topicId, targetPostId)
      .map(quoteMapper)

  override fun requestPostTextForEditing(forumId: Int,
                                         topicId: Int,
                                         targetPostId: Int,
                                         startingPost: Int): Single<BaseEntity> =
    dataLoader
      .loadTargetPostEditedText(forumId, topicId, targetPostId, startingPost)
      .map(editedPostMapper)

  override fun requestKarmaChange(isTopic: Boolean,
                                  targetPostId: Int,
                                  targetTopicId: Int,
                                  diff: Int): Single<BaseEntity> =
    dataLoader
      .changeKarma(
        act = KARMA_ACT,
        code = KARMA_CODE,
        rank = diff,
        postId = targetPostId,
        topicId = targetTopicId,
        type = if (isTopic) KARMA_TYPE_TOPIC else KARMA_TYPE_POST
      )
      .map(responseMapper)

  override fun requestPostKarmaChange(targetPostId: Int,
                                      targetTopicId: Int,
                                      diff: Int): Single<BaseEntity> =
    dataLoader
      .changeKarma(
        act = KARMA_ACT,
        code = KARMA_CODE,
        rank = diff,
        postId = targetPostId,
        topicId = targetTopicId,
        type = KARMA_TYPE_POST
      )
      .map(responseMapper)

  override fun requestTopicKarmaChange(targetPostId: Int,
                                       targetTopicId: Int,
                                       diff: Int): Single<BaseEntity> =
    dataLoader
      .changeKarma(
        act = KARMA_ACT,
        code = KARMA_CODE,
        rank = diff,
        postId = targetPostId,
        topicId = targetTopicId,
        type = KARMA_TYPE_TOPIC
      )
      .map(responseMapper)

  override fun requestMessageSending(targetForumId: Int,
                                     targetTopicId: Int,
                                     page: Int,
                                     authKey: String,
                                     message: String,
                                     filePath: String): Completable =
    dataLoader
      .postMessage(
        act = POST_ACT,
        code = POST_CODE,
        forum = targetForumId,
        topic = targetTopicId,
        st = page,
        enableemo = "yes",
        enablesig = "yes",
        authKey = authKey,
        postContent = message,
        enabletag = 0,
        maxFileSize = POST_MAX_FILE_SIZE,
        uploadedFile = createMultiPartForFile(FILE_PART_NAME, filePath)
      )
      .map(responseMapper)
      .flatMapCompletable { checkMessageSending(it as ServerResponse) }

  override fun requestEditedMessageSending(targetForumId: Int,
                                           targetTopicId: Int,
                                           targetPostId: Int,
                                           page: Int,
                                           authKey: String,
                                           message: String,
                                           file: String): Completable =
    dataLoader
      .postEditedMessage(
        st = page,
        act = POST_ACT,
        s = "",
        forum = targetForumId,
        enableemo = "yes",
        enablesig = "yes",
        authKey = authKey,
        maxFileSize = POST_MAX_FILE_SIZE,
        code = POST_EDIT_CODE,
        topic = targetTopicId,
        post = targetPostId,
        postContent = message,
        enabletag = 0,
        fileupload = file
      )
      .map(responseMapper)
      .flatMapCompletable { checkMessageSending(it as ServerResponse) }

  private fun createMultiPartForFile(partName: String, path: String): MultipartBody.Part? =
    if (path.isNotEmpty()) {
      val file = File(path)
      val requestFile = RequestBody.create(MediaType.parse(UPLOADED_FILE_TYPE), file)
      MultipartBody.Part.createFormData(partName, file.name, requestFile)
    } else {
      null
    }

  private fun checkMessageSending(response: ServerResponse): Completable =
    if (response.text.contains(MESSAGE_SENDING_ERROR_MARKER)) {
      Completable.error(RequestErrorException("Message sending request failed."))
    } else {
      Completable.complete()
    }
}
