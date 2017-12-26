package com.sedsoftware.yaptalker.presentation.features.gifdisplay

import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.presentation.base.BasePresenter
import javax.inject.Inject

@InjectViewState
class GifDisplayPresenter @Inject constructor() : BasePresenter<GifDisplayView>() {

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
