package com.sedsoftware.yaptalker.domain.repository

import io.reactivex.Single

/**
 * Interface that represents a Repository for getting video thumbnail.
 */
interface ThumbnailRepository {

  fun getThumbnail(videoLink: String): Single<String>
}
