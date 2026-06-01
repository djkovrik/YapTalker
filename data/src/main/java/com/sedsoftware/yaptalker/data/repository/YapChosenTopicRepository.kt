package com.sedsoftware.yaptalker.data.repository

import com.sedsoftware.yaptalker.data.exception.RequestErrorException
import com.sedsoftware.yaptalker.data.mapper.EditedPostMapper
import com.sedsoftware.yaptalker.data.mapper.QuotedPostMapper
import com.sedsoftware.yaptalker.data.mapper.ServerResponseMapper
import com.sedsoftware.yaptalker.data.mapper.TopicPageMapper
import com.sedsoftware.yaptalker.data.network.site.YapApi
import com.sedsoftware.yaptalker.data.network.site.YapLoader
import com.sedsoftware.yaptalker.data.system.SchedulersProvider
import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.entity.base.EditedPost
import com.sedsoftware.yaptalker.domain.entity.base.QuotedPost
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
    private val yapApi: YapApi,
    private val dataMapper: TopicPageMapper,
    private val quoteMapper: QuotedPostMapper,
    private val editedPostMapper: EditedPostMapper,
    private val responseMapper: ServerResponseMapper,
    private val schedulers: SchedulersProvider
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

        private const val FILE_PART_NAME = "FILE_UPLOAD[0]"
        private const val UPLOADED_FILE_TYPE = "image/jpeg"
        private const val TEXT_PLAIN_TYPE = "text/plain"

        private const val MESSAGE_SENDING_ERROR_MARKER = "Возникли следующие трудности"
    }

    override fun getChosenTopic(
        forumId: Int,
        topicId: Int,
        startPostNumber: Int
    ): Single<List<BaseEntity>> =
        dataLoader
            .loadTopicPage(forumId, topicId, startPostNumber)
            .map(dataMapper)
            .subscribeOn(schedulers.io())

    override fun requestPostTextAsQuote(
        forumId: Int,
        topicId: Int,
        targetPostId: Int
    ): Single<QuotedPost> =
        dataLoader
            .loadTargetPostQuotedText(forumId, topicId, targetPostId)
            .map(quoteMapper)
            .subscribeOn(schedulers.io())

    override fun requestPostTextForEditing(
        forumId: Int,
        topicId: Int,
        postId: Int,
        startingPost: Int
    ): Single<EditedPost> =
        dataLoader
            .loadTargetPostEditedText(forumId, topicId, postId, startingPost)
            .map(editedPostMapper)
            .subscribeOn(schedulers.io())

    override fun requestKarmaChange(
        isTopic: Boolean,
        targetPostId: Int,
        targetTopicId: Int,
        diff: Int
    ): Single<ServerResponse> =
        requestApiKarmaChange(targetPostId, diff)
            .subscribeOn(schedulers.io())

    override fun requestPostKarmaChange(
        targetPostId: Int,
        targetTopicId: Int,
        diff: Int
    ): Single<ServerResponse> =
        requestApiKarmaChange(targetPostId, diff)
            .subscribeOn(schedulers.io())

    override fun requestTopicKarmaChange(
        targetPostId: Int,
        targetTopicId: Int,
        diff: Int
    ): Single<ServerResponse> =
        requestApiKarmaChange(targetPostId, diff)
            .subscribeOn(schedulers.io())

    override fun requestMessageSending(
        targetForumId: Int,
        targetTopicId: Int,
        page: Int,
        authKey: String,
        message: String,
        filePath: String
    ): Completable {
        val request = if (filePath.isNotEmpty()) {
            yapApi.sendComment(
                topicId = targetTopicId.toString(),
                post = createRequestBody(message),
                replyPostId = null,
                uploadedFile = createMultiPartForFile(FILE_PART_NAME, filePath)
            )
        } else {
            yapApi.sendComment(
                topicId = targetTopicId.toString(),
                post = message,
                replyPostId = null
            )
        }
        return request
            .map(responseMapper)
            .flatMapCompletable { checkMessageSending(it) }
            .subscribeOn(schedulers.io())
    }

    override fun requestEditedMessageSending(
        targetForumId: Int,
        targetTopicId: Int,
        targetPostId: Int,
        page: Int,
        authKey: String,
        message: String,
        file: String
    ): Completable =
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
            .flatMapCompletable { checkMessageSending(it) }
            .subscribeOn(schedulers.io())

    private fun requestApiKarmaChange(targetPostId: Int, diff: Int): Single<ServerResponse> =
        yapApi
            .vote(postId = targetPostId.toString(), value = diff.toString())
            .map { result -> ServerResponse(text = "{\"status\":${result.status ?: 0}}") }

    private fun createRequestBody(text: String): RequestBody =
        RequestBody.create(MediaType.parse(TEXT_PLAIN_TYPE), text)

    private fun createMultiPartForFile(partName: String, path: String): MultipartBody.Part {
        val file = File(path)
        val requestFile = RequestBody.create(MediaType.parse(UPLOADED_FILE_TYPE), file)
        return MultipartBody.Part.createFormData(partName, file.name, requestFile)
    }

    private fun checkMessageSending(response: ServerResponse): Completable =
        if (response.text.contains(MESSAGE_SENDING_ERROR_MARKER)) {
            Completable.error(RequestErrorException("Message sending request failed."))
        } else {
            Completable.complete()
        }
}
