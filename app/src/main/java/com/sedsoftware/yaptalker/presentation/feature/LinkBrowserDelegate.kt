package com.sedsoftware.yaptalker.presentation.feature

import android.content.Context
import com.sedsoftware.yaptalker.domain.device.Settings
import com.sedsoftware.yaptalker.presentation.base.enums.navigation.NavigationScreen
import com.sedsoftware.yaptalker.presentation.extensions.extractYoutubeVideoId
import com.sedsoftware.yaptalker.presentation.extensions.validateUrl
import org.jetbrains.anko.browse
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class LinkBrowserDelegate @Inject constructor(
    private val router: Router,
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
}
//
//private fun updateDisplayingItem(item: DisplayedItemModel): Single<DisplayedItemModel> {
//    if (item is SinglePostModel) {
//        if (item.postContentParsed.videosLinks.isNotEmpty()) {
//            return Observable.fromIterable(item.postContentParsed.videosLinks)
//                .flatMapSingle { link ->
//                    when {
//                        link.contains("token") ->
//                            tokenInteractor.getVideoToken(link).map { token ->
//                                val mainId = link.substringAfter("files/").substringBefore("/")
//                                val videoId = link.substringAfterLast("/").substringBefore(".mp4")
//                                "http://www.yapfiles.ru/files/$mainId/$videoId.mp4?token=$token"
//                            }
//                        link.contains(".html") ->
//                            tokenInteractor.getVideoToken(link).map { token ->
//                                val mainId = link.substringAfter("show/").substringBefore("/")
//                                val videoId = link.substringAfterLast("/").substringBefore(".mp4")
//                                "http://www.yapfiles.ru/files/$mainId/$videoId.mp4?token=$token"
//                            }
//                        else -> Single.just("")
//                    }
//                }
//                .toList()
//                .flatMap { list ->
//                    item.postContentParsed.videosLinks = list
//                    Single.just(item)
//                }
//        } else {
//            return Single.just(item)
//        }
//    } else {
//        return Single.just(item)
//    }
//}