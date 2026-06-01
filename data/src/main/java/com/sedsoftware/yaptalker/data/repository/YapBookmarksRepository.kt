package com.sedsoftware.yaptalker.data.repository

import com.sedsoftware.yaptalker.data.exception.RequestErrorException
import com.sedsoftware.yaptalker.data.mapper.ListToObservablesMapper
import com.sedsoftware.yaptalker.data.network.site.YapApi
import com.sedsoftware.yaptalker.data.network.site.model.PostItem
import com.sedsoftware.yaptalker.data.system.SchedulersProvider
import com.sedsoftware.yaptalker.domain.entity.base.BookmarkedTopic
import com.sedsoftware.yaptalker.domain.repository.BookmarksRepository
import io.reactivex.Completable
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class YapBookmarksRepository @Inject constructor(
    private val yapApi: YapApi,
    private val listMapper: ListToObservablesMapper<BookmarkedTopic>,
    private val schedulers: SchedulersProvider
) : BookmarksRepository {

    companion object {
        private const val FIRST_PAGE_OFFSET = "0"
        private const val TOPIC_LINK_TEMPLATE = "https://www.yaplakal.com/forum%d/st/0/topic%d.html"
    }

    override fun getBookmarks(): Observable<BookmarkedTopic> =
        yapApi
            .loadFavoriteTopics(FIRST_PAGE_OFFSET)
            .map { response -> response.feed.orEmpty().mapNotNull(::mapBookmarkedTopic) }
            .flatMapObservable(listMapper)
            .subscribeOn(schedulers.io())

    override fun requestBookmarkAdding(topicId: Int, startingPost: Int): Completable =
        yapApi
            .addFavorite(topicId.toString())
            .flatMapCompletable { response -> completeIfSuccessful(response, "Failed to add new bookmark") }
            .subscribeOn(schedulers.io())

    override fun requestBookmarkDeletion(bookmarkId: Int): Completable =
        yapApi
            .removeFavorite(bookmarkId.toString())
            .flatMapCompletable { response -> completeIfSuccessful(response, "Failed to delete bookmark") }
            .subscribeOn(schedulers.io())

    private fun mapBookmarkedTopic(item: PostItem): BookmarkedTopic? {
        val topicId = item.id?.toIntOrNull() ?: return null
        val forumId = item.catId?.toIntOrNull() ?: return null

        return BookmarkedTopic(
            bookmarkId = topicId,
            title = item.title.orEmpty(),
            link = TOPIC_LINK_TEMPLATE.format(forumId, topicId)
        )
    }

    private fun completeIfSuccessful(response: Response<ResponseBody>, errorMessage: String): Completable =
        when {
            response.isSuccessful -> Completable.complete()
            else -> Completable.error(RequestErrorException(errorMessage))
        }
}
