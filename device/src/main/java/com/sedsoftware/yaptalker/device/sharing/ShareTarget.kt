package com.sedsoftware.yaptalker.device.sharing

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import android.support.v4.content.FileProvider
import com.sedsoftware.yaptalker.device.R
import com.squareup.picasso.Picasso
import io.reactivex.Single
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject
import com.squareup.picasso.Target as ImageTarget

class ShareTarget @Inject constructor(val context: Context) : ImageTarget {

  companion object {
    private const val ENCODING_IMAGE_QUALITY = 100
  }

  override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
  }

  override fun onBitmapFailed(errorDrawable: Drawable?) {
  }

  @SuppressLint("RxLeakedSubscription")
  override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom?) {

    getBitmapUriSingle(context, bitmap)
      .subscribe({ uri ->
        // onSuccess
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        context.startActivity(
          Intent.createChooser(
            intent,
            context.getString(R.string.title_share_image)
          ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
      }, { throwable ->
        // onError
        Timber.e("Image sharing error: ${throwable.message}")
      })
  }

  private fun getBitmapUriSingle(context: Context, bmp: Bitmap): Single<Uri> =

    Single.create { emitter ->

      val bmpUri: Uri?

      try {

        val file = File(
          context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
          "shared_image_${System.currentTimeMillis()}.png"
        )

        val out = FileOutputStream(file)
        bmp.compress(Bitmap.CompressFormat.PNG, ENCODING_IMAGE_QUALITY, out)
        out.close()

        bmpUri = FileProvider.getUriForFile(context, context.packageName, file)
        emitter.onSuccess(bmpUri)

      } catch (e: IOException) {
        emitter.onError(e)
      }
    }
}
