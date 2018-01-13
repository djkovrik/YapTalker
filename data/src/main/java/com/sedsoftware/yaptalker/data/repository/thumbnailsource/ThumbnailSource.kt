package com.sedsoftware.yaptalker.data.repository.thumbnailsource

import io.reactivex.Observable

interface ThumbnailSource {

  fun getThumbnailUrl(): Observable<String>
}
