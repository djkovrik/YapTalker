package com.sedsoftware.yaptalker.data.remote

import android.widget.ImageView
import com.sedsoftware.yaptalker.BuildConfig
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.commons.extensions.loadFromDrawable
import com.sedsoftware.yaptalker.commons.extensions.loadFromUrl
import com.sedsoftware.yaptalker.data.model.video.VideoTypes
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class ThumbnailsManager(
    private val coubVideo: CoubLoader,
    private val rutubeVideo: RutubeLoader,
    private val yapVideo: YapVideoLoader,
    private val vkVideo: VkLoader) {

  companion object {
    private const val YAP_PLAYER_HASH = BuildConfig.YAP_PLAYER_HASH
    private const val YAP_RESULT_TYPE = "json"
    private const val VK_ACCESS_TOKEN = BuildConfig.VK_ACCESS_TOKEN
    private const val VK_API_VERSION = "5.58"
  }

  fun loadThumbnail(video: Pair<Int, String>, imageView: ImageView) {

    when (video.first) {
      VideoTypes.RUTUBE -> {
        rutubeVideo
            .loadThumbnail(video.second, "json")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { (thumbnail_url) -> thumbnail_url }
            .subscribe(getImageObserver(imageView))
      }
      VideoTypes.YOUTUBE -> {
        Single
            .just("http://img.youtube.com/vi/${video.second}/0.jpg")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getImageObserver(imageView))
      }
      VideoTypes.COUB -> {
        coubVideo
            .loadThumbnail("http://coub.com/view/${video.second}")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { (thumbnail_url) -> thumbnail_url }
            .subscribe(getImageObserver(imageView))
      }
      VideoTypes.YAP_FILES -> {
        yapVideo
            .loadThumbnail(video.second, YAP_PLAYER_HASH, YAP_RESULT_TYPE)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { (player) -> player.poster }
            .subscribe(getImageObserver(imageView))
      }
      VideoTypes.VK -> {
        vkVideo
            .loadThumbnail(video.second, VK_ACCESS_TOKEN, VK_API_VERSION)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { (response) -> response.items.first().photo_320 }
            .subscribe(getImageObserver(imageView))
      }
      VideoTypes.OTHER -> {
        Single
            .just(R.drawable.ic_othervideo)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getDrawableObserver(imageView))
      }
    }
  }

  private fun getImageObserver(imageView: ImageView) =
      object : SingleObserver<String?> {
        override fun onError(e: Throwable) {
        }

        override fun onSuccess(url: String) {
          if (url.isNotEmpty()) {
            imageView.loadFromUrl(url)
          }
        }

        override fun onSubscribe(d: Disposable) {
        }
      }

  private fun getDrawableObserver(imageView: ImageView) =
      object : SingleObserver<Int> {
        override fun onError(e: Throwable) {
        }

        override fun onSuccess(resId: Int) {
          imageView.loadFromDrawable(resId)
        }

        override fun onSubscribe(d: Disposable) {
        }
      }
}
