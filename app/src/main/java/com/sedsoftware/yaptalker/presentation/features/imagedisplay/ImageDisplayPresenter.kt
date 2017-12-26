package com.sedsoftware.yaptalker.presentation.features.imagedisplay

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import android.support.v4.content.FileProvider
import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.presentation.base.enums.lifecycle.PresenterLifecycle
import com.sedsoftware.yaptalker.presentation.base.BasePresenter
import com.sedsoftware.yaptalker.presentation.extensions.stringRes
import com.sedsoftware.yaptalker.presentation.extensions.validateUrl
import com.squareup.picasso.Picasso
import com.uber.autodispose.kotlin.autoDisposable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.Okio
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject
import javax.inject.Named
import com.squareup.picasso.Target as ImageTarget

@InjectViewState
class ImageDisplayPresenter @Inject constructor(
    @Named("fileClient") private val httpClient: OkHttpClient
) : BasePresenter<ImageDisplayView>() {

  companion object {
    private const val ENCODING_IMAGE_QUALITY = 100
  }

  fun saveImage(url: String) {

    loadImageFromUrl(url.validateUrl())
        .flatMap { response -> saveToDisk(response, url.substringAfterLast("/")) }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .autoDisposable(event(PresenterLifecycle.DESTROY))
        .subscribe({ file ->
          viewState.fileSavedMessage(file.absolutePath)
        }, { _ ->
          viewState.fileNotSavedMessage()
        })
  }

  fun shareImage(context: Context, url: String) {
    if (url.isNotEmpty()) {
      Picasso
          .with(context)
          .load(url.validateUrl())
          .into(ShareTarget(context))
    }
  }

  private fun loadImageFromUrl(url: String): Single<Response> =

      Single.create<Response> { emitter ->
        try {
          val request = Request.Builder().url(url).build()
          val response = httpClient.newCall(request).execute()
          emitter.onSuccess(response)
        } catch (e: IOException) {
          emitter.onError(e)
        }
      }

  private fun saveToDisk(response: Response, filename: String): Single<File> =

      Single.create({ emitter ->

        try {

          val storageDir = Environment.getExternalStoragePublicDirectory(
              "${Environment.DIRECTORY_PICTURES}/YapTalker")

          if (!storageDir.exists() && !storageDir.mkdir()) {
            throw IOException("Can't create file path")
          }

          val file = File(storageDir, filename)
          val sink = Okio.buffer(Okio.sink(file))

          response.body()?.source()?.let { bufferedSource -> sink.writeAll(bufferedSource) }
          sink.close()
          emitter.onSuccess(file)
        } catch (e: IOException) {
          e.printStackTrace()
          emitter.onError(e)
        }
      })

  private fun getBitmapUriSingle(context: Context, bmp: Bitmap): Single<Uri> =

      Single.create { emitter ->

        val bmpUri: Uri?

        try {

          val file = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
              "shared_image_${System.currentTimeMillis()}.png")

          val out = FileOutputStream(file)
          bmp.compress(Bitmap.CompressFormat.PNG, ENCODING_IMAGE_QUALITY, out)
          out.close()

          bmpUri = FileProvider.getUriForFile(context, context.packageName, file)
          emitter.onSuccess(bmpUri)

        } catch (e: IOException) {
          emitter.onError(e)
        }
      }

  private inner class ShareTarget(val context: Context) : ImageTarget {
    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
    }

    override fun onBitmapFailed(errorDrawable: Drawable?) {
    }

    override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom?) {

      getBitmapUriSingle(context, bitmap)
          .observeOn(Schedulers.io())
          .subscribeOn(AndroidSchedulers.mainThread())
          .autoDisposable(event(PresenterLifecycle.DESTROY))
          .subscribe({ uri ->
            // onSuccess
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "image/png"
            intent.putExtra(Intent.EXTRA_STREAM, uri)
            context.startActivity(Intent.createChooser(intent, context.stringRes(R.string.title_share_image)))
          }, { throwable ->
            // onError
            Timber.e("Image sharing error: ${throwable.message}")
          })
    }
  }
}
