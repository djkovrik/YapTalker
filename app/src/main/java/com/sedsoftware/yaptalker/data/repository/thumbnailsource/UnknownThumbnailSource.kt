package com.sedsoftware.yaptalker.data.repository.thumbnailsource

import io.reactivex.Observable

class UnknownThumbnailSource(private val videoLink: String) : ThumbnailSource {

  override fun getThumbnailUrl(): Observable<String> = Observable.just("")
}
