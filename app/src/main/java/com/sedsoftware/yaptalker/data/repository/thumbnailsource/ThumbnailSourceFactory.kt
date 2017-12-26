package com.sedsoftware.yaptalker.data.repository.thumbnailsource

import com.sedsoftware.yaptalker.data.network.thumbnails.CoubLoader
import com.sedsoftware.yaptalker.data.network.thumbnails.RutubeLoader
import com.sedsoftware.yaptalker.data.network.thumbnails.VkLoader
import com.sedsoftware.yaptalker.data.network.thumbnails.YapFileLoader
import com.sedsoftware.yaptalker.data.network.thumbnails.YapVideoLoader
import javax.inject.Inject

class ThumbnailSourceFactory @Inject constructor(
    private val coubLoader: CoubLoader,
    private val rutubeLoader: RutubeLoader,
    private val yapFileLoader: YapFileLoader,
    private val yapVideoLoader: YapVideoLoader,
    private val vkLoader: VkLoader) {

  companion object {
    private const val COUB_SELECTOR = "coub.com/embed"
    private const val YOUTUBE_SELECTOR = "youtube.com/embed"
    private const val RUTUBE_SELECTOR = "rutube.ru/video/embed"
    private const val YAPFILES_SELECTOR = "yapfiles.ru/get_player"
    private const val VK_SELECTOR = "vk.com/video_ext.php"
  }

  fun createThumbnailSource(link: String): ThumbnailSource =
      when {
        link.contains(COUB_SELECTOR) -> CoubThumbnailSource(coubLoader, link)
        link.contains(YOUTUBE_SELECTOR) -> YoutubeThumbnailSource(link)
        link.contains(RUTUBE_SELECTOR) -> RutubeThumbnailSource(rutubeLoader, link)
        link.contains(YAPFILES_SELECTOR) -> YapThumbnailSource(yapFileLoader, yapVideoLoader, link)
        link.contains(VK_SELECTOR) -> VkThumbnailSource(vkLoader, link)
        else -> UnknownThumbnailSource()
      }
}
