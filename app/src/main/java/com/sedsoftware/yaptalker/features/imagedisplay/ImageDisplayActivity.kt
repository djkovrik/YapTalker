package com.sedsoftware.yaptalker.features.imagedisplay

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.v4.content.FileProvider
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AccelerateInterpolator
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.commons.extensions.hideBeyondScreenEdge
import com.sedsoftware.yaptalker.commons.extensions.loadFromUrl
import com.sedsoftware.yaptalker.commons.extensions.showFromScreenEdge
import com.sedsoftware.yaptalker.commons.extensions.stringRes
import com.squareup.picasso.Picasso
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_image_display.*
import kotlinx.android.synthetic.main.include_main_appbar.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import com.squareup.picasso.Target as ImageTarget

class ImageDisplayActivity : MvpAppCompatActivity(), ImageDisplayView {

  @InjectPresenter
  lateinit var displayPresenter: ImageDisplayPresenter

  private val imageUrl: String by lazy {
    intent.getStringExtra("url")
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_image_display)
    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)

    if (imageUrl.isNotEmpty()) {
      photo_view.loadFromUrl(imageUrl)
      photo_view.setOnPhotoTapListener { _, _, _ -> displayPresenter.toggleFullscreenView() }
    }

    window.decorView.setOnSystemUiVisibilityChangeListener { flags: Int ->
      val visible = flags and View.SYSTEM_UI_FLAG_HIDE_NAVIGATION == 0
      displayPresenter.toggleAppbarVisibility(visible)
    }
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_image_display, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {

    return when (item.itemId) {
      R.id.action_share -> {
        shareImage()
        true
      }
      R.id.action_save -> {
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }

  override fun toggleSystemUiVisibility() {
    val uiOptions = window.decorView.systemUiVisibility
    var newUiOptions = uiOptions

    newUiOptions = newUiOptions xor View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    newUiOptions = newUiOptions xor View.SYSTEM_UI_FLAG_FULLSCREEN
    newUiOptions = newUiOptions xor View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

    window.decorView.systemUiVisibility = newUiOptions
  }

  override fun hideAppbar() {
    appbar?.hideBeyondScreenEdge(-appbar.height.toFloat(), AccelerateInterpolator())
  }

  override fun showAppbar() {
    appbar?.showFromScreenEdge(AccelerateInterpolator())
  }

  private fun shareImage() {
    if (imageUrl.isNotEmpty()) {
      Picasso
          .with(this)
          .load(imageUrl)
          .into(ShareTarget())
    }
  }

  private fun getBitmapUriSingle(bmp: Bitmap): Single<Uri> {

    return Single.create { emitter ->
      val bmpUri: Uri?
      try {

        val file = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            imageUrl.substringAfterLast("/").substringBefore(".") + ".png")

        val out = FileOutputStream(file)
        bmp.compress(Bitmap.CompressFormat.PNG, 100, out)
        out.close()

        bmpUri = FileProvider.getUriForFile(this, applicationContext.packageName, file)

        emitter.onSuccess(bmpUri)
      } catch (e: IOException) {
        emitter.onError(e)
      }
    }
  }

  private inner class ShareTarget : ImageTarget {
    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
    }

    override fun onBitmapFailed(errorDrawable: Drawable?) {
    }

    override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom?) {

      // TODO() Manage subscription
      getBitmapUriSingle(bitmap)
          .observeOn(Schedulers.io())
          .subscribeOn(AndroidSchedulers.mainThread())
          .subscribe({ uri ->
            // onSuccess
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "image/png"
            intent.putExtra(Intent.EXTRA_STREAM, uri)
            startActivity(Intent.createChooser(intent, stringRes(R.string.title_share_image)))
          }, { _ ->
            // onError

          }
          )
    }
  }
}