package com.sedsoftware.yaptalker.features.videodisplay

import android.os.Bundle
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.sedsoftware.yaptalker.R
import kotlinx.android.synthetic.main.include_main_appbar.*

class VideoDisplayActivity : MvpAppCompatActivity() {

  @InjectPresenter
  lateinit var displayPresenter: VideoDisplayPresenter

  private val videoHtml: String by lazy {
    intent.getStringExtra("video")
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_video_display)
    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
  }
}