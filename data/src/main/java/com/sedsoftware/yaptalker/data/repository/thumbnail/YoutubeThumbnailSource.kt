package com.sedsoftware.yaptalker.data.repository.thumbnail

import io.reactivex.Observable

class YoutubeThumbnailSource(private val videoLink: String) : ThumbnailSource {

  private val videoId: String by lazy {
    getYoutubeVideoId(videoLink)
  }

  override fun getThumbnailUrl(): Observable<String> =
      Observable.just("http://img.youtube.com/vi/$videoId/0.jpg")

  private fun getYoutubeVideoId(link: String): String {
    val startPosition = link.lastIndexOf("/")
    val endPosition = link.indexOf("?", startPosition)

    return when (endPosition) {
      -1 -> link.substring(startPosition + 1)
      else -> link.substring(startPosition + 1, endPosition)
    }
  }
}
