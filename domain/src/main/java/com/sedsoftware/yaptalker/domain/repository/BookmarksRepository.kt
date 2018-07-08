package com.sedsoftware.yaptalker.domain.repository

import com.sedsoftware.yaptalker.domain.entity.base.BookmarkedTopic
import io.reactivex.Completable
import io.reactivex.Observable

interface BookmarksRepository {

    fun getBookmarks(): Observable<BookmarkedTopic>

    fun requestBookmarkAdding(topicId: Int, startingPost: Int): Completable

    fun requestBookmarkDeletion(bookmarkId: Int): Completable
}
