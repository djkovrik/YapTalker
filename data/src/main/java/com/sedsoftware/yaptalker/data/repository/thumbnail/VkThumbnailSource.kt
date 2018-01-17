package com.sedsoftware.yaptalker.data.repository.thumbnail

import com.sedsoftware.yaptalker.data.BuildConfig
import com.sedsoftware.yaptalker.data.network.thumbnails.VkLoader
import io.reactivex.Single
import java.util.regex.Pattern

class VkThumbnailSource(
  private val vkLoader: VkLoader,
  private val videoLink: String
) : ThumbnailSource {

  companion object {
    private const val VK_ACCESS_TOKEN = BuildConfig.VK_ACCESS_TOKEN
    private const val VK_API_VERSION = "5.58"
  }

  private val videoId: String by lazy {
    getVkVideoId(videoLink)
  }

  override fun getThumbnailUrl(): Single<String> =
    vkLoader
      .loadThumbnail(videos = videoId, access_token = VK_ACCESS_TOKEN, version = VK_API_VERSION)
      .map { vkInfo -> vkInfo.response.items.first().photo_320 }

  private fun getVkVideoId(link: String): String {
    val regex = "(?<=oid=)([-\\d]+).*(?<=id=)([\\d]+)"
    val pattern = Pattern.compile(regex)
    val matcher = pattern.matcher(link)

    return if (matcher.find() && matcher.groupCount() == 2) {
      "${matcher.group(1)}_${matcher.group(2)}"
    } else {
      ""
    }
  }
}
