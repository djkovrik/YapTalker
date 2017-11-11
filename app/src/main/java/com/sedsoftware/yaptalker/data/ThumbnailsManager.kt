package com.sedsoftware.yaptalker.data

import android.widget.ImageView
import com.sedsoftware.yaptalker.BuildConfig
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.commons.extensions.loadFromUrl
import com.sedsoftware.yaptalker.data.requests.thumbnails.CoubLoader
import com.sedsoftware.yaptalker.data.requests.thumbnails.RutubeLoader
import com.sedsoftware.yaptalker.data.requests.thumbnails.VkLoader
import com.sedsoftware.yaptalker.data.requests.thumbnails.YapFileLoader
import com.sedsoftware.yaptalker.data.requests.thumbnails.YapVideoLoader
import com.sedsoftware.yaptalker.data.video.VideoTypes
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class ThumbnailsManager(
    private val coubVideo: CoubLoader,
    private val rutubeVideo: RutubeLoader,
    private val yapFile: YapFileLoader,
    private val yapVideo: YapVideoLoader,
    private val vkVideo: VkLoader) {

  companion object {
    private const val VK_ACCESS_TOKEN = BuildConfig.VK_ACCESS_TOKEN
    private const val VK_API_VERSION = "5.58"
    private const val YAP_RESULT_TYPE = "json"
  }

  fun loadThumbnail(video: Pair<Int, String>, imageView: ImageView) {

    when (video.first) {
      VideoTypes.RUTUBE -> {
        rutubeVideo
            .loadThumbnail(video.second, "json")
            .map { (thumbnail_url) -> thumbnail_url }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
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
            .map { (thumbnail_url) -> thumbnail_url }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getImageObserver(imageView))
      }
      VideoTypes.YAP_FILES -> {
        getHashObservable(video.second)
            .flatMap { hash -> getYapPlayerObservable(video.second, hash) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getImageObserver(imageView))
      }
      VideoTypes.VK -> {
        vkVideo
            .loadThumbnail(video.second,
                VK_ACCESS_TOKEN,
                VK_API_VERSION)
            .map { (response) -> response.items.first().photo_320 }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
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

  private fun getHashObservable(id: String): Single<String> =
      yapFile
          .loadHash(id)

  private fun getYapPlayerObservable(id: String, hash: String): Single<String> =
      yapVideo
          .loadThumbnail(id, hash, YAP_RESULT_TYPE)
          .map { (player) -> player.poster }

  private fun getImageObserver(imageView: ImageView) =
      object : SingleObserver<String?> {
        override fun onSubscribe(d: Disposable) {

        }

        override fun onSuccess(url: String) {
          if (url.isNotEmpty()) {
            imageView.loadFromUrl(url)
          }
        }

        override fun onError(e: Throwable) {
          Timber.d("ThumbnailsManager - error with image loading: ${e.message}")
        }
      }

  private fun getDrawableObserver(imageView: ImageView) =
      object : SingleObserver<Int> {
        override fun onSubscribe(d: Disposable) {

        }

        override fun onSuccess(resId: Int) {
          imageView.setImageResource(resId)
        }

        override fun onError(e: Throwable) {
          Timber.d("ThumbnailsManager - error with drawable loading: ${e.message}")
        }
      }
}
