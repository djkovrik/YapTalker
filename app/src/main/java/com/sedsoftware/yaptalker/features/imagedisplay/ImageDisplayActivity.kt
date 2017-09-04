package com.sedsoftware.yaptalker.features.imagedisplay

import android.os.Bundle
import android.view.Menu
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.commons.extensions.loadFromUrl
import kotlinx.android.synthetic.main.activity_image_display.*
import kotlinx.android.synthetic.main.activity_main_appbar.*

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
      target_image.loadFromUrl(imageUrl)
    }
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_image_display, menu)
    return true
  }

  override fun hideSystemUi() {

  }

  override fun showSystemUi() {

  }
}