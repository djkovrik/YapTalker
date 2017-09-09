package com.sedsoftware.yaptalker.features.imagedisplay

import android.os.Environment
import com.arellomobile.mvp.InjectViewState
import com.github.salomonbrys.kodein.instance
import com.sedsoftware.yaptalker.features.base.BasePresenter
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.Okio
import timber.log.Timber
import java.io.File
import java.io.IOException

@InjectViewState
class ImageDisplayPresenter : BasePresenter<ImageDisplayView>() {

  // Kodein injection
  private val httpClient: OkHttpClient by instance()

  fun toggleFullscreenView() {
    viewState.toggleSystemUiVisibility()
  }

  fun toggleAppbarVisibility(visible: Boolean) {
    if (visible) {
      viewState.showAppbar()
    } else {
      viewState.hideAppbar()
    }
  }

  fun saveImage(url: String) {

    loadImageFromUrl(url)
        .flatMap { response -> saveToDisk(response, url.substringAfterLast("/")) }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({ file ->
          Timber.d("File saving success: ${file.absolutePath}")
          viewState.updateGallery(file.absolutePath)
        }, { t ->
          Timber.d("File saving failed: ${t.message}")
        })
        .apply { unsubscribeOnDestroy(this) }

  }

  private fun loadImageFromUrl(url: String): Single<Response> {

    return Single.create<Response> { emitter ->
      try {
        val request = Request.Builder().url(url).build()
        val response = httpClient.newCall(request).execute()
        emitter.onSuccess(response)
      } catch (e: IOException) {
        emitter.onError(e)
      }
    }
  }

  private fun saveToDisk(response: Response, filename: String): Single<File> {

    return Single.create({ emitter ->

      try {

        val storageDir = Environment.getExternalStoragePublicDirectory(
            "${Environment.DIRECTORY_PICTURES}/YapTalker")

        if (!storageDir.exists() && !storageDir.mkdir()) {
          throw IOException("Can't create file path")
        }

        val file = File(storageDir, filename)

        val sink = Okio.buffer(Okio.sink(file))

        sink.writeAll(response.body()?.source())
        sink.close()
        emitter.onSuccess(file)
      } catch (e: IOException) {
        e.printStackTrace()
        emitter.onError(e)
      }
    })
  }
}
