package com.sedsoftware.yaptalker.presentation.mapper.util

import android.content.Context
import android.content.res.Resources
import com.sedsoftware.yaptalker.R
import javax.inject.Inject

class VideoTypeDetector @Inject constructor(context: Context) {

    companion object {
        private const val COUB_SELECTOR = "coub.com/embed"
        private const val YOUTUBE_SELECTOR = "youtube.com/embed"
        private const val RUTUBE_SELECTOR = "rutube.ru/video/embed"
        private const val YAPFILES_SELECTOR = "yapfiles.ru/get_player"
        private const val VK_SELECTOR = "vk.com/video_ext.php"
    }

    private val resources: Resources by lazy {
        context.resources
    }

    fun detectVideoType(link: String): String = when {
        link.contains(COUB_SELECTOR) -> resources.getString(R.string.video_coub)
        link.contains(YOUTUBE_SELECTOR) -> resources.getString(R.string.video_youtube)
        link.contains(RUTUBE_SELECTOR) -> resources.getString(R.string.video_rutube)
        link.contains(YAPFILES_SELECTOR) -> resources.getString(R.string.video_yap)
        link.contains(VK_SELECTOR) -> resources.getString(R.string.video_vk)
        else -> resources.getString(R.string.video_other)
    }
}
