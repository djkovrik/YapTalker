package com.sedsoftware.yaptalker.data.repository.thumbnail

import io.reactivex.Single

class UnknownThumbnailSource : ThumbnailSource {

    override fun getThumbnailUrl(): Single<String> = Single.just("")
}
