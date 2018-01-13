package com.sedsoftware.yaptalker.data.repository.thumbnail

import io.reactivex.Observable

class UnknownThumbnailSource : ThumbnailSource {

  override fun getThumbnailUrl(): Observable<String> = Observable.just("")
}
