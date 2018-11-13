package com.sedsoftware.yaptalker.domain.repository

import io.reactivex.Single

interface VideoTokenRepository {

    fun getVideoToken(mainId: String, videoId: String): Single<String>
}
