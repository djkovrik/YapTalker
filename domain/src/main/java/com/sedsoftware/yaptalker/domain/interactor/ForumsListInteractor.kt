package com.sedsoftware.yaptalker.domain.interactor

import com.sedsoftware.yaptalker.domain.entity.base.Forum
import com.sedsoftware.yaptalker.domain.repository.ForumsListRepository
import io.reactivex.Observable
import javax.inject.Inject

class ForumsListInteractor @Inject constructor(
    private val forumsListRepository: ForumsListRepository
) {

    fun getForumsList(): Observable<Forum> =
        forumsListRepository
            .getMainForumsList()
}
