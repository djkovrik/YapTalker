package com.sedsoftware.yaptalker.features.imagedisplay

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
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
import com.sedsoftware.yaptalker.commons.extensions.toastError
import com.sedsoftware.yaptalker.commons.extensions.toastSuccess
import kotlinx.android.synthetic.main.activity_image_display.*
import kotlinx.android.synthetic.main.include_main_appbar.*
import java.util.Locale

class ImageDisplayActivity : MvpAppCompatActivity(), ImageDisplayView {

  @InjectPresenter
  lateinit var displayPresenter: ImageDisplayPresenter

  private val STORAGE_WRITE_PERMISSION = 0

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
        displayPresenter.shareImage(this, imageUrl)
        true
      }
      R.id.action_save -> {
        checkPermissionAndSaveImage()
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
    appbar?.hideBeyondScreenEdge(offset = -appbar.height.toFloat(),
        interpolator = AccelerateInterpolator())
  }

  override fun showAppbar() {
    appbar?.showFromScreenEdge(interpolator = AccelerateInterpolator())
  }

  override fun fileSavedMessage(filepath: String) {
    String.format(Locale.getDefault(), stringRes(R.string.msg_file_saved), filepath).apply {
      toastSuccess(this)
    }
  }

  override fun fileNotSavedMessage() {
    toastError(stringRes(R.string.msg_file_not_saved))
  }

  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
      grantResults: IntArray) {

    when (requestCode) {
      STORAGE_WRITE_PERMISSION -> {
        displayPresenter.saveImage(imageUrl)
      }
    }
  }

  private fun checkPermissionAndSaveImage() {
    if (ContextCompat.checkSelfPermission(this,
        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

      ActivityCompat.requestPermissions(
          this,
          arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
          STORAGE_WRITE_PERMISSION)
    } else {
      fileNotSavedMessage()
    }
  }
}
