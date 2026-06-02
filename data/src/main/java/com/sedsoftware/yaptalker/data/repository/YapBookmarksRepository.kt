package com.sedsoftware.yaptalker.data.repository

import com.sedsoftware.yaptalker.data.exception.RequestErrorException
import com.sedsoftware.yaptalker.data.mapper.ListToObservablesMapper
import com.sedsoftware.yaptalker.data.network.site.YapApi
import com.sedsoftware.yaptalker.data.network.site.model.PostItem
import com.sedsoftware.yaptalker.data.system.SchedulersProvider
import com.sedsoftware.yaptalker.domain.device.CookieStorage
import com.sedsoftware.yaptalker.domain.device.Settings
import com.sedsoftware.yaptalker.domain.entity.base.BookmarkedTopic
import com.sedsoftware.yaptalker.domain.repository.BookmarksRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.HttpException
import javax.inject.Inject

class YapBookmarksRepository @Inject constructor(
    private val yapApi: YapApi,
    private val cookieStorage: CookieStorage,
    private val settings: Settings,
    private val listMapper: ListToObservablesMapper<BookmarkedTopic>,
    private val schedulers: SchedulersProvider
) : BookmarksRepository {

    companion object {
        private const val FIRST_PAGE_OFFSET = "0"
        private const val TOPIC_LINK_TEMPLATE = "https://www.yaplakal.com/forum%d/st/0/topic%d.html"
        private const val HTTP_FORBIDDEN = 403
    }

    override fun getBookmarks(): Observable<BookmarkedTopic> =
        loadBookmarks()
            .onErrorResumeNext { error ->
                reauthenticateOnForbidden(error)
                    .andThen(loadBookmarks())
            }
            .flatMapObservable(listMapper)
            .subscribeOn(schedulers.io())

    override fun requestBookmarkAdding(topicId: Int, startingPost: Int): Completable =
        addBookmark(topicId)
            .onErrorResumeNext { error ->
                reauthenticateOnForbidden(error)
                    .andThen(addBookmark(topicId))
            }
            .subscribeOn(schedulers.io())

    override fun requestBookmarkDeletion(bookmarkId: Int): Completable =
        deleteBookmark(bookmarkId)
            .onErrorResumeNext { error ->
                reauthenticateOnForbidden(error)
                    .andThen(deleteBookmark(bookmarkId))
            }
            .subscribeOn(schedulers.io())

    private fun loadBookmarks(): Single<List<BookmarkedTopic>> =
        yapApi
            .loadFavoriteTopics(FIRST_PAGE_OFFSET)
            .map { response -> response.feed.orEmpty().mapNotNull(::mapBookmarkedTopic) }

    private fun addBookmark(topicId: Int): Completable =
        yapApi
            .addFavorite(topicId.toString())
            .flatMapCompletable { response -> completeIfSuccessful(response, "Failed to add new bookmark") }

    private fun deleteBookmark(bookmarkId: Int): Completable =
        yapApi
            .removeFavorite(bookmarkId.toString())
            .flatMapCompletable { response -> completeIfSuccessful(response, "Failed to delete bookmark") }

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
            response.code() == HTTP_FORBIDDEN -> Completable.error(HttpException(response))
            else -> Completable.error(RequestErrorException(errorMessage))
        }

    private fun reauthenticateOnForbidden(error: Throwable): Completable =
        when {
            error is HttpException && error.code() == HTTP_FORBIDDEN -> reauthenticate()
            else -> Completable.error(error)
        }

    private fun reauthenticate(): Completable {
        val login = settings.getLogin()
        val password = settings.getPassword()
        if (login.isEmpty() || password.isEmpty()) {
            return Completable.error(RequestErrorException("Unable to restore API session."))
        }

        return yapApi
            .authUser(name = login, password = password)
            .flatMapCompletable { response ->
                val sid = response.user?.sid.orEmpty()
                val authKey = response.user?.authKey.orEmpty()
                val userId = response.user?.id.orEmpty()
                val userName = response.user?.name.orEmpty()
                if (sid.isEmpty() || authKey.isEmpty() || userId == "0" || userName.equals("Guest", ignoreCase = true)) {
                    Completable.error(RequestErrorException("Unable to restore API session."))
                } else {
                    cookieStorage.saveCookie("SID=$sid")
                    Completable.complete()
                }
            }
    }
}
