package com.sedsoftware.yaptalker.domain.repository

import com.sedsoftware.yaptalker.domain.entity.base.Forum
import io.reactivex.Observable

interface ForumsListRepository {

    fun getMainForumsList(): Observable<Forum>
}
