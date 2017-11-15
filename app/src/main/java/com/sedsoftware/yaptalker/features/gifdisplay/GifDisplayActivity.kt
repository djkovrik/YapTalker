package com.sedsoftware.yaptalker.features.gifdisplay

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import com.arellomobile.mvp.presenter.InjectPresenter
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.base.BaseActivity
import com.sedsoftware.yaptalker.commons.extensions.toastError
import kotlinx.android.synthetic.main.activity_gif_display.*
import kotlinx.android.synthetic.main.include_main_appbar.*

class GifDisplayActivity : BaseActivity(), GifDisplayView {

  @InjectPresenter
  lateinit var displayPresenter: GifDisplayPresenter

  override val layoutId: Int
    get() = R.layout.activity_gif_display

  private val gifHtml: String by lazy {
    val url = intent
        .getStringExtra("url")

    """
    <html>
      <body>
        <img src="http:$url" style="display:block; margin-left: auto; margin-right: auto;">
      </body>
    </html>
    """
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
  }

  override fun onResume() {
    super.onResume()
    displayPresenter.loadGifContent()
  }

  override fun showErrorMessage(message: String) {
    toastError(message)
  }

  @SuppressLint("SetJavaScriptEnabled")
  override fun initWebView() {
    gif_view?.settings?.javaScriptEnabled = true
    gif_view?.webChromeClient = WebChromeClient()
    gif_view?.settings?.loadWithOverviewMode = true
    gif_view?.settings?.useWideViewPort = true
    gif_view?.scrollBarStyle = WebView.SCROLLBARS_OUTSIDE_OVERLAY
    gif_view?.isScrollbarFadingEnabled = false
  }

  override fun displayWebViewContent() {
    gif_view.loadData(gifHtml, "text/html", "utf-8")
  }

  override fun clearWebView() {
    gif_view?.clearHistory()
    gif_view?.loadUrl("about:blank")
  }
}
