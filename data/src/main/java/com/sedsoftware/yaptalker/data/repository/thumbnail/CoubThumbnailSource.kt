package com.sedsoftware.yaptalker.data.repository.thumbnail

import com.sedsoftware.yaptalker.data.network.thumbnails.CoubLoader
import io.reactivex.Single

class CoubThumbnailSource(
    private val coubLoader: CoubLoader,
    private val videoLink: String
) : ThumbnailSource {

    private val videoId: String by lazy {
        videoLink.substringAfterLast("/")
    }

    override fun getThumbnailUrl(): Single<String> =
        coubLoader
            .loadThumbnail("http://coub.com/view/$videoId")
            .map { it.thumbnail_url }
}
