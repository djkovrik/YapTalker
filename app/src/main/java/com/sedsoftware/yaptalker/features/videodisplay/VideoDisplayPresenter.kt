package com.sedsoftware.yaptalker.features.videodisplay

import com.arellomobile.mvp.InjectViewState
import com.sedsoftware.yaptalker.features.base.BasePresenter

@InjectViewState
class VideoDisplayPresenter : BasePresenter<VideoDisplayView>() {

  fun loadVideoContent() {
    viewState.displayWebViewContent()
  }

  override fun onFirstViewAttach() {
    super.onFirstViewAttach()
    viewState.initWebView()
  }

  override fun detachView(view: VideoDisplayView?) {
    super.detachView(view)
    viewState.clearWebView()
  }
}