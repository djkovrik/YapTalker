package com.sedsoftware.yaptalker.features.imagedisplay

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.animation.AccelerateInterpolator
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.commons.extensions.hideBeyondScreenEdge
import com.sedsoftware.yaptalker.commons.extensions.loadFromUrl
import com.sedsoftware.yaptalker.commons.extensions.showFromScreenEdge
import kotlinx.android.synthetic.main.activity_image_display.*
import kotlinx.android.synthetic.main.include_main_appbar.*

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
}