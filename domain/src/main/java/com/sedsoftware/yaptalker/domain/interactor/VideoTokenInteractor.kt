package com.sedsoftware.yaptalker.domain.interactor

import com.sedsoftware.yaptalker.domain.repository.VideoTokenRepository
import io.reactivex.Single
import javax.inject.Inject

class VideoTokenInteractor @Inject constructor(
    private val repository: VideoTokenRepository
) {

    fun getVideoToken(url: String): Single<String> {

        val mainId = url.substringAfter("show/").substringBefore("/")
        val videoId = url.substringAfterLast("/").substringBefore(".mp4")

        return repository.getVideoToken(mainId, videoId)
    }
}
