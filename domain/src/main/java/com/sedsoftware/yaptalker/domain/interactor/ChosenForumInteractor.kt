package com.sedsoftware.yaptalker.domain.interactor

import com.sedsoftware.yaptalker.domain.entity.BaseEntity
import com.sedsoftware.yaptalker.domain.repository.ChosenForumRepository
import io.reactivex.Observable
import javax.inject.Inject

class ChosenForumInteractor @Inject constructor(
    private val chosenForumRepository: ChosenForumRepository
) {

    fun getChosenForum(forumId: Int, startPage: Int, sortingMode: String): Observable<List<BaseEntity>> =
        chosenForumRepository
            .getChosenForum(forumId, startPage, sortingMode)
}
