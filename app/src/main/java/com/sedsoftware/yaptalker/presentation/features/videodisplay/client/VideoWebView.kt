package com.sedsoftware.yaptalker.presentation.features.videodisplay.client

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView

class VideoWebView : WebView {

  private var videoEnabledWebChromeClient: VideoWebChromeClient? = null
  private var addedJavascriptInterface: Boolean = false

  interface ToggledFullscreenCallback {
    fun toggledFullscreen(fullscreen: Boolean)
  }

  constructor(context: Context) : super(context) {
    addedJavascriptInterface = false
  }

  constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
    addedJavascriptInterface = false
  }

  constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
    addedJavascriptInterface = false
  }

  @SuppressLint("SetJavaScriptEnabled")
  override fun setWebChromeClient(client: WebChromeClient) {
    settings.javaScriptEnabled = true

    if (client is VideoWebChromeClient) {
      this.videoEnabledWebChromeClient = client
    }

    super.setWebChromeClient(client)
  }

  override fun loadData(data: String, mimeType: String, encoding: String) {
    addJavascriptInterface()
    super.loadData(data, mimeType, encoding)
  }

  override fun loadDataWithBaseURL(
    baseUrl: String?,
    data: String?,
    mimeType: String?,
    encoding: String?,
    historyUrl: String?
  ) {
    addJavascriptInterface()
    super.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl)
  }

  override fun loadUrl(url: String) {
    addJavascriptInterface()
    super.loadUrl(url)
  }

  override fun loadUrl(url: String, additionalHttpHeaders: Map<String, String>) {
    addJavascriptInterface()
    super.loadUrl(url, additionalHttpHeaders)
  }

  private fun addJavascriptInterface() {
    if (!addedJavascriptInterface) {

      addJavascriptInterface(object : Any() {
        @JavascriptInterface
        fun notifyVideoEnd() {

          Handler(Looper.getMainLooper()).post {
            if (videoEnabledWebChromeClient != null) {
              videoEnabledWebChromeClient?.onHideCustomView()
            }
          }
        }
      }, "_VideoEnabledWebView")

      addedJavascriptInterface = true
    }
  }
}
