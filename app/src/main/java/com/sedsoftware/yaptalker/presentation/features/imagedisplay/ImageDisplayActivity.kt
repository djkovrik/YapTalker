package com.sedsoftware.yaptalker.presentation.features.imagedisplay

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.Menu
import android.view.MenuItem
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.commons.annotation.LayoutResource
import com.sedsoftware.yaptalker.presentation.base.BaseActivity
import com.sedsoftware.yaptalker.presentation.extensions.loadFromUrl
import com.sedsoftware.yaptalker.presentation.extensions.stringRes
import com.sedsoftware.yaptalker.presentation.extensions.toastError
import com.sedsoftware.yaptalker.presentation.extensions.toastSuccess
import kotlinx.android.synthetic.main.activity_image_display.*
import kotlinx.android.synthetic.main.include_main_appbar.*
import java.util.Locale
import javax.inject.Inject

@LayoutResource(value = R.layout.activity_image_display)
class ImageDisplayActivity : BaseActivity(), ImageDisplayView {

  companion object {
    fun getIntent(ctx: Context, url: String): Intent {
      val intent = Intent(ctx, ImageDisplayActivity::class.java)
      intent.putExtra(IMAGE_URL_KEY, url)
      return intent
    }

    private const val IMAGE_URL_KEY = "IMAGE_URL_KEY"
    private const val STORAGE_WRITE_PERMISSION = 0
  }

  @Inject
  @InjectPresenter
  lateinit var presenter: ImageDisplayPresenter

  @ProvidePresenter
  fun provideImagePresenter() = presenter

  private val imageUrl: String by lazy {
    intent.getStringExtra(IMAGE_URL_KEY)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)

    if (imageUrl.isNotEmpty()) {
      photo_view.loadFromUrl(imageUrl)
    }
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_image_display, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean =
    when (item.itemId) {
      R.id.action_share -> {
        presenter.shareImage(this, imageUrl)
        true
      }
      R.id.action_save -> {
        checkPermissionAndSaveImage()
        true
      }
      else -> super.onOptionsItemSelected(item)
    }

  override fun showErrorMessage(message: String) {
    toastError(message)
  }

  override fun fileSavedMessage(filepath: String) {
    String.format(Locale.getDefault(), stringRes(R.string.msg_file_saved), filepath).apply {
      toastSuccess(this)
    }
  }

  override fun fileNotSavedMessage() {
    toastError(stringRes(R.string.msg_file_not_saved))
  }

  override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<out String>,
    grantResults: IntArray
  ) {

    when (requestCode) {
      STORAGE_WRITE_PERMISSION -> {
        presenter.saveImage(imageUrl)
      }
    }
  }

  private fun checkPermissionAndSaveImage() {
    if (ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
      ) != PackageManager.PERMISSION_GRANTED) {

      ActivityCompat.requestPermissions(
        this,
        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
        STORAGE_WRITE_PERMISSION
      )
    } else {
      presenter.saveImage(imageUrl)
    }
  }
}
