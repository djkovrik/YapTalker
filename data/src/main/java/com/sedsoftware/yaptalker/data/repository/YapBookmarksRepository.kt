package com.sedsoftware.yaptalker.data.repository

import com.sedsoftware.yaptalker.data.exception.RequestErrorException
import com.sedsoftware.yaptalker.data.mapper.BookmarksMapper
import com.sedsoftware.yaptalker.data.mapper.ListToObservablesMapper
import com.sedsoftware.yaptalker.data.mapper.ServerResponseMapper
import com.sedsoftware.yaptalker.data.network.site.YapLoader
import com.sedsoftware.yaptalker.data.system.SchedulersProvider
import com.sedsoftware.yaptalker.domain.entity.base.BookmarkedTopic
import com.sedsoftware.yaptalker.domain.repository.BookmarksRepository
import io.reactivex.Completable
import io.reactivex.Observable
import javax.inject.Inject

class YapBookmarksRepository @Inject constructor(
    private val dataLoader: YapLoader,
    private val dataMapper: BookmarksMapper,
    private val responseMapper: ServerResponseMapper,
    private val listMapper: ListToObservablesMapper<BookmarkedTopic>,
    private val schedulers: SchedulersProvider
) : BookmarksRepository {

    companion object {
        private const val BOOKMARKS_ACT = "UserCP"
        private const val BOOKMARKS_CODE_LOAD = "10"
        private const val BOOKMARKS_CODE_ADD = "11"
        private const val BOOKMARKS_CODE_REMOVE = "12"
        private const val BOOKMARKS_TYPE = 1
        private const val BOOKMARK_SUCCESS_MARKER = "Закладка добавлена"
    }

    override fun getBookmarks(): Observable<BookmarkedTopic> =
        dataLoader
            .loadBookmarks(
                act = BOOKMARKS_ACT,
                code = BOOKMARKS_CODE_LOAD
            )
            .map(dataMapper)
            .flatMap(listMapper)
            .subscribeOn(schedulers.io())

    override fun requestBookmarkAdding(topicId: Int, startingPost: Int): Completable =
        dataLoader
            .addToBookmarks(
                act = BOOKMARKS_ACT,
                code = BOOKMARKS_CODE_ADD,
                item = topicId,
                startPostNumber = startingPost,
                type = BOOKMARKS_TYPE
            )
            .map(responseMapper)
            .flatMapCompletable { response ->
                if (response.text.contains(BOOKMARK_SUCCESS_MARKER)) {
                    Completable.complete()
                } else {
                    Completable.error(RequestErrorException("Failed to add new bookmark"))
                }
            }
            .subscribeOn(schedulers.io())

    override fun requestBookmarkDeletion(bookmarkId: Int): Completable =
        dataLoader
            .removeFromBookmarks(
                act = BOOKMARKS_ACT,
                code = BOOKMARKS_CODE_REMOVE,
                id = bookmarkId
            )
            .ignoreElement()
            .subscribeOn(schedulers.io())
}
