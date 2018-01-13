package com.sedsoftware.yaptalker.data.repository.thumbnail

import io.reactivex.Observable

interface ThumbnailSource {

  fun getThumbnailUrl(): Observable<String>
}
