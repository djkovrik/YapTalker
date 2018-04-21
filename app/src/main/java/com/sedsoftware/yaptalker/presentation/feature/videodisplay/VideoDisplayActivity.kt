package com.sedsoftware.yaptalker.presentation.feature.videodisplay

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.webkit.WebChromeClient
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.common.annotation.LayoutResource
import com.sedsoftware.yaptalker.presentation.base.BaseActivity
import com.sedsoftware.yaptalker.presentation.extensions.snackError
import kotlinx.android.synthetic.main.activity_video_display.*
import javax.inject.Inject

@LayoutResource(value = R.layout.activity_video_display)
class VideoDisplayActivity : BaseActivity(), VideoDisplayView {

  companion object {
    fun getIntent(ctx: Context, html: String): Intent {
      val intent = Intent(ctx, VideoDisplayActivity::class.java)
      intent.putExtra(VIDEO_HTML_KEY, html)
      return intent
    }

    private const val VIDEO_HTML_KEY = "IMAGE_URL_KEY"
    private const val SYSTEM_UI_HIDE_DELAY = 500L
  }

  @Inject
  @InjectPresenter
  lateinit var presenter: VideoDisplayPresenter

  @ProvidePresenter
  fun provideVideoPresenter() = presenter

  private val videoHtml: String by lazy {
    val iframe = intent
      .getStringExtra(VIDEO_HTML_KEY)
      .replace(Regex("width=\"\\d+\" height=\"\\d+\""), "")
      .replace("src=\"//", "src=\"http://")

    """
    <html>
    <head>
    <link href="video.css" type="text/css" rel="stylesheet"/>
    </head>
      <body style='margin:0;padding:0;'>
        $iframe
      </body>
    </html>
    """
  }


  private val handler = Handler()
  private var isSystemUiShown = true

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    window.decorView.setOnSystemUiVisibilityChangeListener({ visibility ->
      isSystemUiShown = if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
        handler.postDelayed(checkSystemUiRunnable, SYSTEM_UI_HIDE_DELAY)
        true
      } else {
        false
      }
    })
  }

  override fun onResume() {
    super.onResume()
    presenter.loadVideoContent()
  }

  override fun onWindowFocusChanged(hasFocus: Boolean) {
    super.onWindowFocusChanged(hasFocus)

    if (hasFocus) {
      checkHideSystemUI()
    }
  }

  override fun showErrorMessage(message: String) {
    snackError(message)
  }

  @SuppressLint("SetJavaScriptEnabled")
  override fun initWebView() {

//    val webChromeClient = VideoWebChromeClient(non_video_layout, video_layout, video_loading, video_view)
//
//    webChromeClient.setOnToggledFullscreen(object : FullscreenCallback {
//      override fun toggledFullscreen(fullscreen: Boolean) {
//        if (fullscreen) {
//          checkHideSystemUI()
//        }
//      }
//    })

    video_view.settings?.setAppCacheEnabled(false)
    video_view.settings?.javaScriptEnabled = true
    video_view.webChromeClient = WebChromeClient()
  }

  override fun displayWebViewContent() {
    video_view.loadDataWithBaseURL(
      "file:///android_asset/", videoHtml,
      "text/html", "utf-8", null
    )
  }

  override fun clearWebView() {
    video_view?.clearHistory()
    video_view?.loadUrl("about:blank")
  }

  private fun checkHideSystemUI() {
    if (isSystemUiShown) {
      handler.postDelayed(checkSystemUiRunnable, SYSTEM_UI_HIDE_DELAY)
    }
  }

  private val checkSystemUiRunnable = Runnable {
    hideSystemUI()
  }

  private fun hideSystemUI() {
    window.decorView.systemUiVisibility =
        (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_FULLSCREEN
            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
  }
}
