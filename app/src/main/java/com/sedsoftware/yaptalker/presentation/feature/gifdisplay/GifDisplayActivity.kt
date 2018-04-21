package com.sedsoftware.yaptalker.presentation.feature.gifdisplay

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.sedsoftware.yaptalker.R
import com.sedsoftware.yaptalker.common.annotation.LayoutResource
import com.sedsoftware.yaptalker.presentation.base.BaseActivity
import com.sedsoftware.yaptalker.presentation.extensions.snackError
import kotlinx.android.synthetic.main.activity_gif_display.*
import kotlinx.android.synthetic.main.include_main_appbar.*
import timber.log.Timber
import javax.inject.Inject

@LayoutResource(value = R.layout.activity_gif_display)
class GifDisplayActivity : BaseActivity(), GifDisplayView {

  companion object {
    fun getIntent(ctx: Context, url: String): Intent {
      val intent = Intent(ctx, GifDisplayActivity::class.java)
      intent.putExtra(GIF_URL_KEY, url)
      return intent
    }

    private const val GIF_URL_KEY = "GIF_URL_KEY"
  }

  @Inject
  @InjectPresenter
  lateinit var presenter: GifDisplayPresenter

  @ProvidePresenter
  fun provideGifPresenter() = presenter

  private val gifHtml: String by lazy {
    val url = intent
      .getStringExtra(GIF_URL_KEY)

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
    presenter.loadGifContent()
  }

  override fun showErrorMessage(message: String) {
    snackError(message)
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
    Timber.d("HTML : $gifHtml")
  }

  override fun clearWebView() {
    gif_view?.clearHistory()
    gif_view?.loadUrl("about:blank")
  }
}
