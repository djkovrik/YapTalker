package com.sedsoftware.yaptalker.features.videodisplay

import android.annotation.SuppressLint
import android.os.Bundle
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.sedsoftware.yaptalker.R
import kotlinx.android.synthetic.main.activity_video_display.*
import kotlinx.android.synthetic.main.include_main_appbar.*

class VideoDisplayActivity : MvpAppCompatActivity(), VideoDisplayView {

  @InjectPresenter
  lateinit var displayPresenter: VideoDisplayPresenter

  private val videoHtml: String by lazy {
    intent.getStringExtra("video")
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    // TODO() Activity view requires styling
    setContentView(R.layout.activity_video_display)
    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
  }

  override fun onResumeFragments() {
    super.onResumeFragments()
    displayPresenter.loadVideoContent()
  }

  @SuppressLint("SetJavaScriptEnabled")
  override fun initWebView() {
    video_view?.settings?.javaScriptEnabled = true
    video_view?.settings?.setAppCacheEnabled(false)
  }

  override fun displayWebViewContent() {
    if (videoHtml.isNotEmpty()) {
      video_view.loadDataWithBaseURL("http://www.yaplakal.com/", videoHtml,
          "text/html; charset=UTF-8", null, null)
    }
  }

  override fun clearWebView() {
    video_view?.clearHistory()
    video_view?.loadUrl("about:blank")
  }
}