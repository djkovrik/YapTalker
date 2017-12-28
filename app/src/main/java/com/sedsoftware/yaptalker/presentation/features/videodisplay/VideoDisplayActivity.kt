package com.sedsoftware.yaptalker.presentation.features.videodisplay

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.presentation.base.BaseActivity
import com.sedsoftware.yaptalker.presentation.extensions.hideView
import com.sedsoftware.yaptalker.presentation.extensions.showView
import com.sedsoftware.yaptalker.presentation.extensions.toastError
import kotlinx.android.synthetic.main.activity_video_display.video_view
import kotlinx.android.synthetic.main.include_main_appbar.appbar
import kotlinx.android.synthetic.main.include_main_appbar.toolbar
import javax.inject.Inject

class VideoDisplayActivity : BaseActivity(), VideoDisplayView {

  companion object {
    fun getIntent(ctx: Context, html: String): Intent {
      val intent = Intent(ctx, VideoDisplayActivity::class.java)
      intent.putExtra(VIDEO_HTML_KEY, html)
      return intent
    }

    private const val VIDEO_HTML_KEY = "IMAGE_URL_KEY"
  }

  @Inject
  @InjectPresenter
  lateinit var presenter: VideoDisplayPresenter

  @ProvidePresenter
  fun provideVideoPresenter() = presenter

  override val layoutId: Int
    get() = R.layout.activity_video_display

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

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)

    if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
      hideSystemUI()
    } else {
      showSystemUI()
    }
  }

  override fun onResume() {
    super.onResume()
    presenter.loadVideoContent()
  }

  override fun showErrorMessage(message: String) {
    toastError(message)
  }

  @SuppressLint("SetJavaScriptEnabled")
  override fun initWebView() {
    video_view?.settings?.javaScriptEnabled = true
    video_view?.settings?.setAppCacheEnabled(false)
    video_view?.webChromeClient = WebChromeClient()
    video_view?.settings?.loadWithOverviewMode = true
    video_view?.settings?.useWideViewPort = true
    video_view?.scrollBarStyle = WebView.SCROLLBARS_OUTSIDE_OVERLAY
    video_view?.isScrollbarFadingEnabled = false
  }

  override fun displayWebViewContent() {
    video_view.loadDataWithBaseURL("file:///android_asset/", videoHtml,
        "text/html", "utf-8", null)
  }

  override fun clearWebView() {
    video_view?.clearHistory()
    video_view?.loadUrl("about:blank")
  }

  private fun hideSystemUI() {

    appbar.hideView()

    window.decorView.systemUiVisibility =
        (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_FULLSCREEN
            or View.SYSTEM_UI_FLAG_IMMERSIVE)
  }

  private fun showSystemUI() {

    appbar.showView()

    window.decorView.systemUiVisibility =
        (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
  }
}
