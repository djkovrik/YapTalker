package com.sedsoftware.yaptalker.features.gifdisplay

import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.base.BasePresenter

@InjectViewState
class GifDisplayPresenter : BasePresenter<GifDisplayView>() {

  fun loadGifContent() {
    viewState.displayWebViewContent()
  }

  override fun attachView(view: GifDisplayView?) {
    super.attachView(view)
    viewState.initWebView()
  }

  override fun detachView(view: GifDisplayView?) {
    viewState.clearWebView()
    super.detachView(view)
  }
}
