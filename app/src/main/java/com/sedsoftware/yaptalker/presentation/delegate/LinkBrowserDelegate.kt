package com.sedsoftware.yaptalker.presentation.delegate

import android.content.Context
import com.sedsoftware.yaptalker.domain.device.Settings
import com.sedsoftware.yaptalker.domain.interactor.VideoTokenInteractor
import com.sedsoftware.yaptalker.presentation.base.enums.navigation.NavigationScreen
import com.sedsoftware.yaptalker.presentation.extensions.extractYoutubeVideoId
import com.sedsoftware.yaptalker.presentation.extensions.validateUrl
import io.reactivex.Single
import org.jetbrains.anko.browse
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class LinkBrowserDelegate @Inject constructor(
    private val router: Router,
    private val tokenInteractor: VideoTokenInteractor,
    private val settings: Settings,
    context: Context?
) {

    private val weakContext: Context? by weak(context)

    fun browse(url: String, directUrl: String, type: String, html: String, isVideo: Boolean) {
        when {
            isVideo && url.contains("youtube") -> {
                val videoId = url.extractYoutubeVideoId()
                weakContext?.browse("http://www.youtube.com/watch?v=$videoId")
            }

            isVideo && url.contains("coub") && settings.isExternalCoubPlayer() -> {
                weakContext?.browse(url.validateUrl())
            }

            isVideo && settings.isExternalYapPlayer() && type == "YapFiles" -> {
                weakContext?.browse(directUrl.validateUrl())
            }

            isVideo && !url.contains("youtube") -> {
                router.navigateTo(NavigationScreen.VIDEO_DISPLAY_SCREEN, html)
            }

            else -> {
                router.navigateTo(NavigationScreen.IMAGE_DISPLAY_SCREEN, url)
            }
        }
    }

    fun checkVideoLink(directUrl: String): Single<String> =
        if (directUrl.contains("yapfiles.ru") && settings.isExternalYapPlayer()) {
            when {
                directUrl.contains("token") ->
                    tokenInteractor.getVideoToken(directUrl).map { token ->
                        val mainId = directUrl.substringAfter("files/").substringBefore("/")
                        val videoId = directUrl.substringAfterLast("/").substringBefore(".mp4")
                        "http://www.yapfiles.ru/files/$mainId/$videoId.mp4?token=$token"
                    }
                directUrl.contains(".html") ->
                    tokenInteractor.getVideoToken(directUrl).map { token ->
                        val mainId = directUrl.substringAfter("show/").substringBefore("/")
                        val videoId = directUrl.substringAfterLast("/").substringBefore(".mp4")
                        "http://www.yapfiles.ru/files/$mainId/$videoId.mp4?token=$token"
                    }
                else -> Single.just(directUrl)
            }
        } else {
            Single.just(directUrl)
        }
}
